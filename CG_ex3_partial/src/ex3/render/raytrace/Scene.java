package ex3.render.raytrace;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import math.Point3D;
import math.Ray;
import math.Vec;

/**
 * A Scene class containing all the scene objects including camera, lights and
 * surfaces. Some suggestions for code are in comment If you uncomment these
 * lines you'll need to implement some new types like Surface
 * 
 * You can change all methods here this is only a suggestion! This is your
 * world, add members methods as you wish
 */
public class Scene implements IInitable {

	protected List<Surface> surfaces = null;
	protected List<Light> lights = null;
	protected Camera camera = null;
	protected Vec backCol = null;
	protected String backTex = null;
	protected double recLevel;
	protected Vec ambient = null;
	protected double superSamp;
	protected int acceleration;

	public Scene() {
		surfaces = new LinkedList<Surface>();
		lights = new LinkedList<Light>();
		camera = new Camera();
		backCol = new Vec(0, 0, 0);
		recLevel = 10;
		ambient = new Vec(0, 0, 0);
		superSamp = 1;
		acceleration = 0;
	}

	/**
	 * Reset the scene parameters from XML
	 */
	public void init(Map<String, String> attributes) {
		if (attributes.containsKey("background-col")) {
			backCol = new Vec(attributes.get("background-col"));
		}
		if (attributes.containsKey("background-tex")) {
			backTex = attributes.get("background-tex");
		}
		if (attributes.containsKey("max-recursion-level")) {
			recLevel = Double
					.parseDouble(attributes.get("max-recursion-level"));
		}
		if (attributes.containsKey("ambient-light")) {
			ambient = new Vec(attributes.get("ambient-light"));
		}
		if (attributes.containsKey("super-samp-width")) {
			superSamp = Double.parseDouble(attributes.get("super-samp-width"));
		}
		if (attributes.containsKey("use-acceleration")) {
			acceleration = Integer.parseInt(attributes.get("use-acceleration"));
		}
	}

	/**
	 * Send ray return the nearest intersection. Return null if no intersection
	 * 
	 * @param ray
	 * @return
	 */
	public MinIntersection findIntersection(Ray ray) {
		double min = Double.MAX_VALUE;
		Surface min_surface = null;

		// For each surface check for nearest intersection.
		for (Surface surface : surfaces) {
			double curDist = surface.Intersect(ray);
			if ((curDist < min) && (curDist > Ray.eps)) {
				min_surface = surface;
				min = curDist;
			}
		}
		// In case of no intersection
		if (min == Double.MAX_VALUE) {
			return null;
		}
		Point3D intersection = new Point3D(ray.p, Vec.scale(min, ray.v));

		// In case intersection was found
		return new MinIntersection(intersection, min_surface, min);
	}

	public Vec calcColor(Ray ray, int curLevel, MinIntersection intersection) {
		if(recLevel >= curLevel) {
			return new Vec(0,0,0);
		}
		if(intersection == null) {
			return this.backCol;
		}
		Point3D intersectionPoint = intersection.intersectionPoint;
		Surface surface = intersection.minSurface;
		
		// Ambient and Emission calculations
		Vec Ie = new Vec(surface.emission);
		Vec kaIa = Vec.scale(surface.ambient, this.ambient);
		Vec color = Vec.add(Ie, kaIa);
		
		// Diffuse & Specular calculations
		for (int i = 0; i < lights.size(); i++) {
			Light light = lights.get(i);
			Vec Kd = surface.diffuse;
			double NdotLi = Vec.dotProd(surface.normal(intersectionPoint),light.getDir(intersectionPoint));
			Vec diffuse = Vec.scale(NdotLi, Kd);
			Vec Ks = surface.specular;
			Vec Ri = light.getDir(intersectionPoint).reflect(surface.normal(intersectionPoint));
			double VdotRi = Vec.dotProd(ray.v, Ri);
			double VdotRipowN = Math.pow(VdotRi, surface.shininess);
			Vec specular = Vec.scale(VdotRipowN, Ks);
			color.add(diffuse);
			color.add(specular);
		}
		return color;
	}

	/**
	 * Add objects to the scene by name
	 * 
	 * @param name
	 *            Object's name
	 * @param attributes
	 *            Object's attributes
	 */
	public void addObjectByName(String name, Map<String, String> attributes) {
		Scanner scan = null;
		Surface surface = null;
		Light light = null;

		if ("sphere".equals(name))
			surface = new sphere();

		if ("disc".equals(name)) {
			surface = new disc();
		}
		
		if ("trimesh".equals(name)) {
			for (String key : attributes.keySet()) {
				if (key.startsWith("tri")) {
					// The triangle 3 points are given in 3 coordinates each.
					double triP0x, triP0y, triP0z;
					double triP1x, triP1y, triP1z;
					double triP2x, triP2y, triP2z;
					Point3D p1, p2, p3;
					Triangle triangle;

					scan = new Scanner(attributes.get(key));
					triP0x = scan.nextDouble();
					triP0y = scan.nextDouble();
					triP0z = scan.nextDouble();
					p1 = new Point3D(triP0x, triP0y, triP0z);

					triP1x = scan.nextDouble();
					triP1y = scan.nextDouble();
					triP1z = scan.nextDouble();
					p2 = new Point3D(triP1x, triP1y, triP1z);

					triP2x = scan.nextDouble();
					triP2y = scan.nextDouble();
					triP2z = scan.nextDouble();
					p3 = new Point3D(triP2x, triP2y, triP2z);

					triangle = new Triangle(p1, p2, p3);
					triangle.init(attributes);
					this.surfaces.add(triangle);
				}
			}
		}
			
		// Lights objects
		if ("omni-light".equals(name))
			light = new omniLight();
		if ("dir-light".equals(name))
			light = new dirLight();
		if ("spot-light".equals(name))
			light = new spotLight();

		// Adds a surface to the list of surfaces
		if (surface != null) {
			surface.init(attributes);
			surfaces.add(surface);
		}

		// Adds a light to the list of lights
		if (light != null) {
			light.init(attributes);
			lights.add(light);
		}

	}

	public void setCameraAttributes(Map<String, String> attributes) {
		this.camera.init(attributes);
	}
}
