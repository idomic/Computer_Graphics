package ex3.render.raytrace;

import java.util.Map;

import math.Point3D;
import math.Ray;
import math.Vec;

public class sphere extends Surface {

	protected Point3D center = null;
	protected double radius;

	public sphere() { 
		super.diffuse = new Vec(0.7, 0.7, 0.7);
		super.specular = new Vec(1, 1, 1);
		super.ambient = new Vec(0.1, 0.1, 0.1);
		super.emission = new Vec(0, 0, 0);
		super.shininess = 100;
		super.reflectance = 0;
		}
	
	@Override
	public void init(Map<String, String> attributes) {
		super.init(attributes);
		if (attributes.containsKey("center")){
			center = new Point3D(attributes.get("center"));
		}
		if (attributes.containsKey("radius")){
			radius = Double.parseDouble(attributes.get("radius"));
		}				
	}

	@Override
	/**
	 * Check intersection with the sphere and ray,
	 * return the closest point of intersection (dist) to the nearest intersection point.
	 * We worked according to the Geometric method.
	 * @param ray - the ray that intersects the sphere
	 */
	public double Intersect(Ray ray) {
		double inf = Double.MAX_VALUE;
		Point3D p = ray.p;
		
		// V is the vector which intersects the sphere + vCenter is a vector to the sphere center.
		Vec v = ray.v;
		Vec vCenter = new Vec(center, p);
		double dRadius =  Math.pow(radius,2);
		
		// Proj (Tm) is the projection of vCenter on v.
		double proj = Vec.dotProd(vCenter, v);
		if (proj < 0) {
			return inf;
		}
		
		// The double distance from the ray to the sphere center.
		// If bigger than radius, no intersection.
		double dDist = vCenter.lengthSquared() - Math.pow(proj, 2);
		if (dDist > dRadius) {
			return inf;
		}
		
		// The distance from radius to intersection points.
		// If there is one intersection point.
		double th = Math.sqrt(dRadius - dDist);
		if (Math.sqrt(dDist) == radius) {
			return proj;
		} else {
			
			// Get the minimal distance intersection point.
			if (proj - th < proj + th) {
				return (proj - th);
			} else {
				return (proj + th);
			}
		}
	}

	@Override
	/**
	 * Normal to the sphere surface at intersection point.
	 */
	public Vec normal(Point3D p) {
		Vec ans = new Vec(p,center);
		ans.normalize();
		return ans;
	}
}
