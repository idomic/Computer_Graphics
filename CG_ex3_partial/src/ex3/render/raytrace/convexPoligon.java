package ex3.render.raytrace;

import java.util.Map;

import math.Point3D;
import math.Ray;
import math.Vec;

public class convexPoligon extends Surface {
	
	protected Point3D p0;
	protected Point3D p1;
	protected Point3D p2;
	protected Point3D p3;
	private Vec normal;

	/* Vectors of the rectangle sides direction */
	private Vec vecSide1;
	private Vec vecSide2;

	/* The rectangle sides lengths. */
	private double side1Length;
	private double side2Length;
	
	public convexPoligon(Point3D p0, Point3D p1, Point3D p2, Point3D p3) {
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		vecSide1 = Point3D.vecFromSub2Points(this.p1, this.p0);
		side1Length = vecSide1.length();
		vecSide1.normalize();
		vecSide2 = Point3D.vecFromSub2Points(this.p2, this.p0);
		side2Length = vecSide2.length();
		vecSide2.normalize();

		// Checks whether the 3 points creates a legal rectangle (The vector
		// between them are orthogonal).
		/*if (vecSide1.dotProd(vecSide2) != 0) {
			System.err.println("The given points are not of a rectangle");
		}*/

		// Calculate the last (fourth) point of the rectangle.
		this.p3 = Point3D.pointAtEndOfVec(this.p1, this.side2Length,
				this.vecSide2);

		// Calculate the normal to the plain on which the rectangle is on.
		this.normal = Vec.crossProd(this.vecSide1, this.vecSide2);
		this.normal.normalize();
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
		if (attributes.containsKey("p3")) {
			p3 = new Point3D(attributes.get("p3"));
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

		// Gets the projection of the new vector on according to the plain
				// vectors
				// if the projections are between 0 and the original length of the
				// vectors it means that the point is on the Rectangular Plane.
				double projectV1 = Vec.dotProd(intersectionDirection,
						this.vecSide1);
				double projectV2 = Vec.dotProd(intersectionDirection,
						this.vecSide2);
				if (projectV1 >= 0 && projectV2 >= 0 && projectV1 <= this.side1Length
						&& projectV2 <= this.side2Length) {
					return intersectionDistance;
				} else {
					return inf;
				}
				}

	@Override
	public Vec normal(Point3D p) {
		return normal;
	}


}
