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

		// Computes the plain coefficient.
		plainCoefficient = -(Vec.dotProd(normal, center.convertToVec()));
	}

	@Override
	public double Intersect(Ray ray) {
		double inf = Double.MAX_VALUE;
		Point3D pos = ray.p;
		Vec rayDirection = ray.v;

		// Getting the intersection.
		double t = -(Vec.dotProd(normal, pos.convertToVec()) + plainCoefficient)
				/ Vec.dotProd(this.normal, rayDirection);
		Point3D intersectionPoint = Point3D.pointAtEndOfVec(pos, t,
				rayDirection);

		// If the distance from the center is larger than the radius no intersection.
		if (Point3D.vecFromSub2Points(intersectionPoint, this.center).length() > this.radius) {
			return inf;
		}

		// if a non negative number then the correct direction.
		double distance = Point3D.vecFromSub2Points(intersectionPoint,
				pos).length();

		// Check correct dir, Both are normalized.
		if (Vec.dotProd(rayDirection, normal) < 0) {
			return inf;
		}
		return distance;

	}

	@Override
	public Vec normal(Point3D p) {
		return normal;
	}

}
