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
 * surfaces. Some suggestions for code are in comment
 * If you uncomment these lines you'll need to implement some new types like Surface
 * 
 * You can change all methods here this is only a suggestion! This is your world, 
 * add members methods as you wish
 */
public class Scene implements IInitable {

	protected List<Surface> surfaces = null;
	protected List<Light> lights = null;
	protected Camera camera = null;
	protected Vec backCol = null;
	protected String backTex = null;
	protected double recLevel ;
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
	 *  Reset the scene parameters from XML
	 */
	public void init(Map<String, String> attributes) {
		if (attributes.containsKey("background-col")) {
			backCol = new Vec(attributes.get("background-col"));
		}
		if (attributes.containsKey("background-tex")) {
			backTex = attributes.get("background-tex");
		}
		if (attributes.containsKey("max-recursion-level")) {
			recLevel = Double.parseDouble(attributes.get("max-recursion-level"));
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
	public Point3D findIntersection(Ray ray) {
		double min = Integer.MAX_VALUE;
		Color p = new Color
		for (Surface surface : surfaces) {
			getClass().
			if() {
				
			}
			if(){
				
			}
		}
		//TODO find ray intersection with scene, change the output type, add whatever you need
	}

	public Vec calcColor(Ray ray, int level) {
		//TODO implement ray tracing recursion here, add whatever you need
		return null;
	}

	/**
	 * Add objects to the scene by name
	 * 
	 * @param name Object's name
	 * @param attributes Object's attributes
	 */
	public void addObjectByName(String name, Map<String, String> attributes) {
		//TODO this adds all objects to scene except the camera
		//here is some code example for adding a surface or a light. 
		//you can change everything and if you don't want this method, delete it
		
		Surface surface = null;
		Light light = null;
	
//		if ("sphere".equals(name))
//			surface = new Sphere();
		
		// Lights objects
		if ("omni-light".equals(name))
			light = new omniLight();
		if ("dir-light".equals(name))
			light = new dirLight();
		if ("spot-light".equals(name))
			light = new spotLight();

		//adds a surface to the list of surfaces
		/*if (surface != null) {
			surface.init(attributes);
			surfaces.add(surface);
		} */
		
		// adds a light to the list of lights
		if (light != null) {
			light.init(attributes);
			lights.add(light);
		}

	}

	public void setCameraAttributes(Map<String, String> attributes) {
		this.camera.init(attributes);
	}
}
