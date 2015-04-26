package ex3.render.raytrace;

import java.util.Map;

import math.Point3D;
import math.Ray;
import math.Vec;

public class Triangle extends Surface {

	protected Point3D p0;
	protected Point3D p1;
	protected Point3D p2;
	private Vec normal;

	public Triangle(Point3D p0, Point3D p1, Point3D p2) {
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
		
		
		// Calculates the normal to the plain of the triangle.
		normal = Vec.crossProd(
				Point3D.vecFromSub2Points(this.p2, this.p1),
				Point3D.vecFromSub2Points(this.p0, this.p1));
		normal.normalize();
	}
	
	@Override
	public void init(Map<String, String> attributes) {
		
		// Initialize the material attributes.
		super.init(attributes);
		if (attributes.containsKey("p0")) {
			p0 = new Point3D(attributes.get("p0"));
		}
		if (attributes.containsKey("p1")) {
			p1 = new Point3D(attributes.get("p1"));
		}
		if (attributes.containsKey("p2")) {
			p2 = new Point3D(attributes.get("p2"));
		}
	}

	@Override
	public double Intersect(Ray ray) {
		double inf = Double.MAX_VALUE;
		Point3D pos = ray.p;
		Vec direction = ray.v;
		Point3D intersectionPos;
		Vec intersectionDirection;

		// The "t" from the plane - ray intersection from class.
		double intersectionDistance;

		// Check that the ray direction is in the face of the triangle.
		// (direction and normal are already normalized).
		if (Vec.dotProd(direction, this.normal) >= 0) {
			return inf;
		}

		// Get the intersection distance.
		intersectionDistance = Point3D.vecFromSub2Points(this.p1, pos)
				.dotProd(this.normal) / ray.v.dotProd(this.normal);

		intersectionPos = Point3D.pointAtEndOfVec(pos,
				intersectionDistance, direction);
		intersectionDirection = Point3D.vecFromSub2Points(intersectionPos,
				pos);

		// Check if the intersection point is in the triangle.
		Vec vec1 = Point3D.vecFromSub2Points(this.p1, pos);
		Vec vec2 = Point3D.vecFromSub2Points(this.p2, pos);
		Vec vec3 = Point3D.vecFromSub2Points(this.p0, pos);

		Vec norm1 = Vec.crossProd(vec2, vec1);
		Vec norm2 = Vec.crossProd(vec3, vec2);
		Vec norm3 = Vec.crossProd(vec1, vec3);

		// Before normalizing check that the norms are not a result of
		// perpendicular vectors dot product.
		if ((norm1.length() == 0) || (norm2.length() == 0)
				|| (norm3.length() == 0)) {
			return inf;
			}
		// Normalize the norms vectors.
		norm1.normalize();
		norm2.normalize();
		norm3.normalize();

		// Check that the intersection point is in the triangle.
		if ((Vec.dotProd(intersectionDirection, norm1) < 0)
				|| (Vec.dotProd(intersectionDirection, norm2) < 0)
				|| (Vec.dotProd(intersectionDirection, norm3) < 0)) {
			return inf;
			}
		if (Vec.dotProd(ray.v, normal) > 0) {
			return inf;
		}

		return intersectionDistance;
	}

	@Override
	public Vec normal(Point3D p) {
		return normal;
	}

}
