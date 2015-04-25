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
		Point3D pos = ray.p;
		Vec direction = ray.v;
		double inf = Double.MAX_VALUE;

		// The "t" from the plane - ray intersection from class.
		double distance;

		// Check that the ray direction is in the face of the triangle.
		// (direction and normal are already normalized).
		if (Vec.dotProd(direction, normal) >= 0) {
			return inf;
		}

		// Get the intersection distance.
		distance = Point3D.vecFromSub2Points(p1, pos)
				.dotProd(normal) / ray.v.dotProd(normal);

		Point3D intersection = Point3D.pointAtEndOfVec(pos,
				distance, direction);

		Vec posTointersection = Point3D.vecFromSub2Points(
				intersection, p0);

		// Gets the projection of the new vector on according to the plain
		// vectors
		// if the projections are between 0 and the original length of the
		// vectors it means that the point is on the Rectangular Plane.
		double projectV1 = Vec.dotProd(posTointersection,
				vecfirstSide);
		double projectV2 = Vec.dotProd(posTointersection,
				vecsecondSide);
		if (projectV1 >= 0 && projectV2 >= 0 && projectV1 <= firstSideL
				&& projectV2 <= secondSideL) {
			return distance;
		} else {
			return inf;
		}

	}

	@Override
	public Vec normal(Point3D intersection) {
		return normal;
	}

	/*
	 * Generate rectangle from the 3 points.
	 * 
	 * @param p1 - The rectangle position.
	 * 
	 * @param p2
	 * 
	 * @param p3
	 */
	private void generate3Points(Point3D p0, Point3D p1, Point3D p2) {
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
		
		// Define the rectangle and the sides lengths.
		vecfirstSide = Point3D.vecFromSub2Points(this.p1, this.p0);
		firstSideL = vecfirstSide.length();
		vecfirstSide.normalize();
		vecsecondSide = Point3D.vecFromSub2Points(this.p2, this.p0);
		secondSideL = vecsecondSide.length();
		vecsecondSide.normalize();

		// Check if a legal rectangle - vectors are orthogonal
		if (vecfirstSide.dotProd(vecsecondSide) != 0) {
			System.err.println("Not a legal rectangle!");
		}

		// Generate a fourth point.
		p3 = Point3D.pointAtEndOfVec(this.p1, secondSideL,
				vecsecondSide);

		// Calculate the normal to the plain of the rectangle.
		normal = Vec.crossProd(vecfirstSide, vecsecondSide);
		normal.normalize();
	}
}
