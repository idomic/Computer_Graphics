package ex3.render.raytrace;

import java.util.Map;

import math.Point3D;
import math.Ray;
import math.Vec;

public class disc extends Surface {

	protected Point3D center = null;
	protected double radius;
	protected Vec normal = null;
	private double plainCoefficient;

	public disc() {
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
		if (attributes.containsKey("center")) {
			center = new Point3D(attributes.get("center"));
		}
		if (attributes.containsKey("radius")) {
			radius = Double.parseDouble(attributes.get("radius"));
		}
		if (attributes.containsKey("normal")) {
			normal = new Vec(attributes.get("normal"));
			this.normal.normalize();
		}

		// Computes the plain coefficient (the plain that the disc is part of).
		plainCoefficient = -(Vec.dotProd(normal, center.convertToVec()));
	}

	@Override
	public double Intersect(Ray ray) {
		double inf = Double.MAX_VALUE;
		Point3D pos = ray.p;
		Vec rayDirection = ray.v;

		// According to slide 83 in the modeling presentation:
		// T = -(n*P0+d)/n*v
		double t = -(Vec.dotProd(normal, pos.convertToVec()) + plainCoefficient)
				/ Vec.dotProd(this.normal, rayDirection);

		// Get the intersection point.
		Point3D intersectionPoint = Point3D.pointAtEndOfVec(pos, t,
				rayDirection);

		// Check that the point is in the disc and not just in the plain.
		// If the distance from the center is larger than the radius.
		if (Point3D.vecFromSub2Points(intersectionPoint, this.center).length() > this.radius) {
			return inf;
		}

		// Get the distance from the ray origin to the intersection point and if
		// it is a non negative number then it is in the correct direction.
		double distance = Point3D.vecFromSub2Points(intersectionPoint,
				pos).length();

		// Check that the ray is in the direction of the front side of the disc
		// .Both ray.v and the normal are normalize so we get the cos(angle).
		if (Vec.dotProd(rayDirection, normal) >= 0) {
			return inf;
		}
		return distance;

	}

	@Override
	public Vec normal(Point3D p) {
		return normal;
	}

}
