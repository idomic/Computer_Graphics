package ex3.render.raytrace;

import java.util.Map;

import math.Point3D;
import math.Ray;
import math.Vec;

public class Rectangle extends Surface {

	/** The four points represent the rectangle. */
	protected Point3D p0;
	protected Point3D p1;
	protected Point3D p2;
	protected Point3D p3;

	/* The normal to the plain on which the rectangle is on. */
	private Vec normal;

	/* Vectors of the rectangle sides direction */
	private Vec vecfirstSide;
	private Vec vecsecondSide;

	/* The rectangle sides lengths. */
	private double firstSideL;
	private double secondSideL;

	/**
	 * The constructor.
	 */
	public Rectangle() {
	}

	@Override
	public void init(Map<String, String> attributes) {
		
		// Initialize the material attributes
		super.init(attributes);
		if (attributes.containsKey("p0") && attributes.containsKey("p1")
				&& attributes.containsKey("p2")) {

			generate3Points(new Point3D(attributes.get("p0")),
					new Point3D(attributes.get("p1")),
					new Point3D(attributes.get("p2")));

		} else {
			System.err.println("Cannot represent a rectangle - need 3 points");
			System.exit(1);
		}
	}

	@Override
	public double Intersect(Ray ray, boolean backside) {
		Point3D rayOrigin = ray.p;
		Vec rayDirection = ray.v;
		Point3D intersectionPoint;

		// The "t" from the plane - ray intersection from class.
		double intersectionDistance;

		// Check that the ray direction is in the face of the triangle.
		// (direction and normal are already normalized).
		if (Vec.dotProd(rayDirection, this.normal) >= 0) {
			return Double.POSITIVE_INFINITY;
		}

		// Get the intersection distance.
		intersectionDistance = Point3D.vecFromSub2Points(this.p1, rayOrigin)
				.dotProd(this.normal) / ray.v.dotProd(this.normal);

		intersectionPoint = Point3D.pointAtEndOfVec(rayOrigin,
				intersectionDistance, rayDirection);

		Vec rectangoriginToIntersection = Point3D.vecFromSub2Points(
				intersectionPoint, this.p0);

		// Gets the projection of the new vector on according to the plain
		// vectors
		// if the projections are between 0 and the original length of the
		// vectors it means that the point is on the Rectangular Plane.
		double projectV1 = Vec.dotProd(rectangoriginToIntersection,
				this.vecfirstSide);
		double projectV2 = Vec.dotProd(rectangoriginToIntersection,
				this.vecsecondSide);
		if (projectV1 >= 0 && projectV2 >= 0 && projectV1 <= this.firstSideL
				&& projectV2 <= this.secondSideL) {
			return intersectionDistance;
		} else {
			return Double.POSITIVE_INFINITY;
		}

	}

	@Override
	public Vec normal(Point3D intersection) {
		return this.normal;
	}

	/*
	 * Create from the 3 given point a rectangle.
	 * 
	 * @param p1 - The rectangle origin.
	 * 
	 * @param p2
	 * 
	 * @param p3
	 */
	private void generate3Points(Point3D p0, Point3D p1, Point3D p2) {
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
		// Create the 2 vectors define the rectangle and the sides lengths.
		this.vecfirstSide = Point3D.vecFromSub2Points(this.p1, this.p0);
		this.firstSideL = vecfirstSide.length();
		this.vecfirstSide.normalize();
		this.vecsecondSide = Point3D.vecFromSub2Points(this.p2, this.p0);
		this.secondSideL = vecsecondSide.length();
		this.vecsecondSide.normalize();

		// Checks whether the 3 points creates a legal rectangle (The vector
		// between them are orthogonal).
		if (vecfirstSide.dotProd(vecsecondSide) != 0) {
			System.err.println("The given points are not of a rectangle");
		}

		// Calculate the last (fourth) point of the rectangle.
		this.p3 = Point3D.pointAtEndOfVec(this.p1, this.secondSideL,
				this.vecsecondSide);

		// Calculate the normal to the plain on which the rectangle is on.
		this.normal = Vec.crossProd(this.vecfirstSide, this.vecsecondSide);
		this.normal.normalize();
	}
}
