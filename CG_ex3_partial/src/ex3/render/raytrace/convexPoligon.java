package ex3.render.raytrace;

import java.util.LinkedList;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import math.Point3D;
import math.Ray;
import math.Vec;

/**
 * A convex poligon class which creates a convex object. it runs through a list
 * of points and generates an object.
 * 
 * @author Idomic
 *
 */
public class convexPoligon extends Surface {
	// Points of polygon
	protected LinkedList<Point3D> pts;
	// Number of points
	protected int n;
	// Normal to surface
	protected Vec normal;

	/**
	 * Constructor
	 */
	public convexPoligon() {
	}

	/**
	 * Constructor - constructs a convex polygon from a list of points 
	 * 
	 * @param pts - list of points
	 * @throws IllegalArgumentException - When points create a non planar polygon
	 */
	public convexPoligon(LinkedList<Point3D> pts)
			throws IllegalArgumentException {
		pts = new LinkedList<Point3D>();
		this.n = pts.size();

		// add the points if the polygon is illegal throw exception
		for (Point3D p : pts)
			this.pts.add(p);
		if (!isPlanar())
			throw new IllegalArgumentException("Not a planar polygon");
		if (!isConvex())
			throw new IllegalArgumentException("Not a convex polygon");
		normal(pts.getFirst());
	}

	public double Intersect(Ray ray) {
		Double inf = Double.MAX_VALUE;
		Point3D intersectionPoint = findRayPlaneIntersection(
				(Point3D) pts.getFirst(), normal, ray);
		if (intersectionPoint == null) {
			return inf;
		}
		double totalLength = Point3D
				.vecFromSub2Points(intersectionPoint, ray.p).length();
		if (totalLength < Ray.eps) {
			return inf;
		}
		if (IntersectfromPoligon(intersectionPoint, ray)) {
			return totalLength;
		}
		return inf;
	}

	public Vec normalAt(Point3D intersection, Ray ray) {
		return this.normal;
	}

	public void init(Map<String, String> attributes)
			throws IllegalArgumentException {
		pts = new LinkedList<Point3D>();
		SortedSet<String> poliPoints = new TreeSet<String>(attributes.keySet());

		// Initialize all the points of the poligon.
		for (String key : poliPoints) {
			if ((key.startsWith("p") & key.length() == 2)) {
				this.pts.add(new Point3D((String) attributes.get(key)));
			}
		}
		this.n = this.pts.size();
		if (!isPlanar())
			throw new IllegalArgumentException("Error! Not planar polygon");
		if (!isConvex()) {
			throw new IllegalArgumentException("Error! Not convex polygon");
		}
		normal(pts.getFirst());
		super.init(attributes);
	}

	/**
	 * Checks if a polygon is a convex polygon
	 * 
	 * @return boolean
	 */
	protected boolean isConvex() {

		// run on all points and create edges
		for (int point = 1; point < this.n - 1; point++) {
			Vec edge1 = Point3D.vecFromSub2Points(
					(Point3D) this.pts.get(point + 1),
					(Point3D) this.pts.get(point));
			Vec edge2 = Point3D.vecFromSub2Points(
					(Point3D) this.pts.get(point - 1),
					(Point3D) this.pts.get(point));
			Vec v3 = Vec.crossProd(edge1, edge2);

			if (Vec.dotProd(Vec.crossProd(v3, edge1), edge2) < 0) {
				return false;
			}
		}

		// for each edge check if orthogonal.
		Vec edge1 = Point3D.vecFromSub2Points((Point3D) this.pts.get(1),
				(Point3D) this.pts.get(0));
		Vec edge2 = Point3D.vecFromSub2Points(
				(Point3D) this.pts.get(this.n - 1), (Point3D) this.pts.get(0));
		Vec v3 = Vec.crossProd(edge1, edge2);

		if (Vec.dotProd(Vec.crossProd(v3, edge1), edge2) < 0)
			return false;
		return true;
	}

	/**
	 * Checks if a polygon is planar
	 * 
	 * @return boolean
	 */
	protected boolean isPlanar() {

		// Create poligon edges
		Vec edge1 = Point3D.vecFromSub2Points((Point3D) this.pts.get(1),
				(Point3D) this.pts.get(0));
		Vec edge2 = Point3D.vecFromSub2Points(
				(Point3D) this.pts.get(this.n - 1), (Point3D) this.pts.get(0));

		// for each check if the vectors are orthogonal.
		for (int point = 2; point < this.n - 1; point++) {
			Vec v3 = Point3D.vecFromSub2Points((Point3D) this.pts.get(point),
					(Point3D) this.pts.get(0));
			if (!Vec.areCoPlanar(edge1, edge2, v3))
				return false;
		}
		return true;
	}

	public Vec normal(Point3D p) {
		// create normal and normalize.
		Vec vec1 = Point3D.vecFromSub2Points((Point3D) this.pts.get(1),
				(Point3D) this.pts.get(0));
		Vec vec2 = Point3D.vecFromSub2Points(
				(Point3D) this.pts.get(this.n - 1), (Point3D) this.pts.get(0));
		normal = Vec.crossProd(vec1, vec2);
		normal.normalize();
		return normal;
	}

	/**
	 * Checks if there is an intersection with polygon
	 * 
	 * @param p - point
	 * @param ray - ray through polygon
	 * @return - true if intersects and false otherwise
	 */
	protected boolean IntersectfromPoligon(Point3D p, Ray ray) {
		Vec vec1 = Point3D.vecFromSub2Points(p, ray.p);
		
		// run for each point and check for intersection. only through the shape
		for (int point = 1; point < this.n; point++) {
			Vec sub1 = Point3D.vecFromSub2Points(
					(Point3D) this.pts.get(point - 1), ray.p);
			Vec sub2 = Point3D.vecFromSub2Points((Point3D) this.pts.get(point),
					ray.p);
			Vec cross = Vec.crossProd(sub2, sub1);

			double dot = Vec.dotProd(cross, vec1);
			if (dot < 0){
				return false;
			}
		}

		Vec sub1 = Point3D.vecFromSub2Points(
				(Point3D) this.pts.get(this.n - 1), ray.p);
		Vec sub2 = Point3D.vecFromSub2Points((Point3D) this.pts.get(0), ray.p);
		Vec cross = Vec.crossProd(sub2, sub1);
		double dot = Vec.dotProd(cross, vec1);
		if (dot < 0) {
			return false;
		}
		return true;
	}

	/**
	 * Find intersection point with plane
	 * 
	 * @param planePoint - point on plane
	 * @param planeNormal - normal to plane
	 * @param ray - ray through plane
	 * @return - intersection point
	 */
	public Point3D findRayPlaneIntersection(Point3D planePoint,
			Vec planeNormal, Ray ray) {
		
		// run for each point and check for intersection. only through the plane itself.
		double dotVecNormal = Vec.dotProd(ray.v, planeNormal);
		if (dotVecNormal >= 0) {
			return null;
		}

		Vec planeRayVec = Point3D.vecFromSub2Points(planePoint, ray.p);
		double planeRayVecDotWithNormal = Vec.dotProd(planeRayVec, planeNormal);

		if (planeRayVecDotWithNormal >= 0) {
			return null;
		}

		Point3D intersectionPoint = new Point3D(ray.p);
		intersectionPoint.mac(planeRayVecDotWithNormal / dotVecNormal, ray.v);
		return intersectionPoint;
	}
}
