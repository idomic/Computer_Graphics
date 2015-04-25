package ex3.render.raytrace;

import java.util.LinkedList;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import math.Point3D;
import math.Ray;
import math.Vec;

public class convexPoligon extends Surface {
	protected LinkedList<Point3D> pts;
	protected int n;
	protected Vec normal;

	 public convexPoligon()
	  {
	  }
	 
	public convexPoligon(LinkedList<Point3D> pts)
			throws IllegalArgumentException {
		pts = new LinkedList();
		this.n = pts.size();
		for (Point3D p : pts)
			this.pts.add(p);
		if (!isPlanar())
			throw new IllegalArgumentException("not planar polygon");
		if (!isConvex())
			throw new IllegalArgumentException("not convex polygon");
		normal(pts.getFirst());
	}

	public double Intersect(Ray ray, boolean backside) {
		Double inf = Double.MAX_VALUE;
		Vec faceNormal = new Vec();
		if (backside)
			faceNormal = Vec.negate(this.normal);
		else {
			faceNormal = this.normal;
		}
		Point3D intersectionPoint = findRayPlaneIntersection((Point3D) pts.getFirst(), faceNormal, ray);
		if (intersectionPoint == null) {
			return inf;
		}
		double totalLength = Point3D.vecFromSub2Points(intersectionPoint, ray.p).length();
		if (totalLength < Ray.eps) {
			return inf;
		}
		if (rayIntersectionPointInPolygon(intersectionPoint, ray, backside)) {
			return totalLength;
		}
		return inf;
	}

	public Vec normalAt(Point3D intersection, Ray ray) {
		return this.normal;
	}

	public void init(Map<String, String> attributes)
			throws IllegalArgumentException {
		pts = new LinkedList();
		SortedSet<String> keys = new TreeSet<String>(attributes.keySet());
		for (String key : keys) {
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

	protected boolean isConvex() {
		for (int pInd = 1; pInd < this.n - 1; pInd++) {
			Vec edge1 = Point3D.vecFromSub2Points((Point3D) this.pts.get(pInd + 1),
					(Point3D) this.pts.get(pInd));
			Vec edge2 = Point3D.vecFromSub2Points((Point3D) this.pts.get(pInd - 1),
					(Point3D) this.pts.get(pInd));
			Vec v3 = Vec.crossProd(edge1, edge2);

			if (Vec.dotProd(Vec.crossProd(v3, edge1), edge2) < 0.0D) {
				return false;
			}
		}
		Vec edge1 = Point3D.vecFromSub2Points((Point3D) this.pts.get(1),
				(Point3D) this.pts.get(0));
		Vec edge2 = Point3D.vecFromSub2Points((Point3D) this.pts.get(this.n - 1),
				(Point3D) this.pts.get(0));
		Vec v3 = Vec.crossProd(edge1, edge2);

		if (Vec.dotProd(Vec.crossProd(v3, edge1), edge2) < 0.0D)
			return false;
		return true;
	}

	protected boolean isPlanar() {
		Vec edge1 = Point3D.vecFromSub2Points((Point3D) this.pts.get(1),
				(Point3D) this.pts.get(0));
		Vec edge2 = Point3D.vecFromSub2Points((Point3D) this.pts.get(this.n - 1),
				(Point3D) this.pts.get(0));

		for (int pInd = 2; pInd < this.n - 1; pInd++) {
			Vec v3 = Point3D.vecFromSub2Points((Point3D) this.pts.get(pInd),
					(Point3D) this.pts.get(0));
			/*	if (!Vec.areCoPlanar(edge1, edge2, v3))
				return false; */
		}
		return true;
	}

	public Vec normal(Point3D p) {
		Vec vec1 = Point3D.vecFromSub2Points((Point3D) this.pts.get(1),
				(Point3D) this.pts.get(0));
		Vec vec2 = Point3D.vecFromSub2Points((Point3D) this.pts.get(this.n - 1),
				(Point3D) this.pts.get(0));
		normal = Vec.crossProd(vec1, vec2);
		normal.normalize();
		return normal;
	}

	protected boolean rayIntersectionPointInPolygon(Point3D p, Ray ray,
			boolean backFace) {
		Vec PintPray = Point3D.vecFromSub2Points(p, ray.p);
		for (int pInd = 1; pInd < this.n; pInd++) {
			Vec sub1 = Point3D.vecFromSub2Points((Point3D) this.pts.get(pInd - 1), ray.p);
			Vec sub2 = Point3D.vecFromSub2Points((Point3D) this.pts.get(pInd), ray.p);
			Vec cross = Vec.crossProd(sub2, sub1);

			double dot = Vec.dotProd(cross, PintPray);
			if (((dot < 0.0D ? 1 : 0) & (backFace ? 0 : 1)) != 0) {
				return false;
			}
			if ((dot > 0.0D & backFace)) {
				return false;
			}
		}

		Vec sub1 = Point3D.vecFromSub2Points((Point3D) this.pts.get(this.n - 1), ray.p);
		Vec sub2 = Point3D.vecFromSub2Points((Point3D) this.pts.get(0), ray.p);
		Vec cross = Vec.crossProd(sub2, sub1);
		double dot = Vec.dotProd(cross, PintPray);
		if (((dot < 0.0D ? 1 : 0) & (backFace ? 0 : 1)) != 0) {
			return false;
		}
		if ((dot > 0.0D & backFace)) {
			return false;
		}

		return true;
	}
	public Point3D findRayPlaneIntersection(Point3D planePoint, Vec planeNormal, Ray ray)
	{
		double dotVecNormal = Vec.dotProd(ray.v, planeNormal);
		if (dotVecNormal >= 0.0D)
		{
			return null;
		}

		Vec planeRayVec = Point3D.vecFromSub2Points(planePoint, ray.p);
		double planeRayVecDotWithNormal = Vec.dotProd(planeRayVec, planeNormal);

		if (planeRayVecDotWithNormal >= 0.0D)
		{
			return null;
		}

		Point3D intersectionPoint = new Point3D(ray.p);
		intersectionPoint.mac(planeRayVecDotWithNormal / dotVecNormal, ray.v);
		return intersectionPoint;
	}
}
