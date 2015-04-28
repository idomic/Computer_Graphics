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
	
	@Override
	public double Intersect(Ray ray) {
		Point3D rayOrigin = ray.p;
		Vec rayDirection = ray.v;

		// Getting intersection point.
		double t = -(Vec.dotProd(this.normal, rayOrigin.convertToVec()) + plainCoefficient)
				/ Vec.dotProd(this.normal, rayDirection);
		Point3D intersectionPoint = Point3D.pointAtEndOfVec(rayOrigin, t,
				rayDirection);
		double distance = Point3D.vecFromSub2Points(intersectionPoint,
				rayOrigin).length();

		// Check direction and if both are normalize.
		if (Vec.dotProd(rayDirection, this.normal) >= 0) {
			return Double.POSITIVE_INFINITY;
		}
		return distance;
	}

	@Override
	public Vec normal(Point3D intersection) {
		return normal;
	}

}
