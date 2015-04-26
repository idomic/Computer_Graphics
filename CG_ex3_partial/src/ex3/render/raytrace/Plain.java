package ex3.render.raytrace;

import java.util.Map;

import math.Point3D;
import math.Ray;
import math.Vec;

/**
 * A simple plane representation.
 */
public class Plain extends Surface {

	/** The plane is represented by a position and a normal. */
	protected Point3D position;
	protected Vec normal;
	protected double plainCoefficient;

	@Override
	public double Intersect(Ray ray) {
		Point3D rayOrigin = ray.p;
		Vec rayDirection = ray.v;

		// According to slide 83 in the modeling presentation:
		// T = -(n*P0+d)/n*v
		double t = -(Vec.dotProd(this.normal, rayOrigin.convertToVec()) + plainCoefficient)
				/ Vec.dotProd(this.normal, rayDirection);

		// Get the intersection point.
		Point3D intersectionPoint = Point3D.pointAtEndOfVec(rayOrigin, t,
				rayDirection);

		// Get the distance from the ray origin to the intersection point and if
		// it is a non negative number then it is in the correct direction.
		double distance = Point3D.vecFromSub2Points(intersectionPoint,
				rayOrigin).length();

		// Check that the ray is in the direction of the front side of the
		// plain. Both ray.v and the normal are normalize so we get the
		// cos(angle).
		if (Vec.dotProd(rayDirection, this.normal) >= 0) {
			return Double.POSITIVE_INFINITY;
		}
		return distance;
	}

	@Override
	public Vec normal(Point3D intersection) {
		return normal;
	}

	@Override
	public void init(Map<String, String> attributes) {
		if (attributes.containsKey("position")) {
			this.position = new Point3D(attributes.get("position"));
		}
		if (attributes.containsKey("normal")) {
			this.normal = new Vec(attributes.get("normal"));
			this.normal.normalize();
		}

		super.init(attributes);

		// Computes the plain coefficient.
		plainCoefficient = (Vec.dotProd(this.normal,
				this.position.convertToVec()));
	}
}
