package math;

import java.util.Scanner;

public class Point3D{
	public double x, y, z;


	public Point3D(String v) {
		Scanner s = new Scanner(v);
		x = s.nextDouble();
		y = s.nextDouble();
		z = s.nextDouble();
		s.close();
	}

	public Point3D(Point3D p, Vec v) {
		this.x = p.x + v.x;
		this.y = p.y + v.y;
		this.z = p.z + v.z;
	}
	
	public Point3D(Point3D p, Point3D p0) {
		this.x = p.x + p0.x;
		this.y = p.y + p0.y;
		this.z = p.z + p0.z;
	}
	
	public Point3D(Point3D p0) {
		x = p0.x;
		y = p0.y;
		z = p0.z;
	}
	
	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static Vec vecFromSub2Points(Point3D p, Point3D p0) {
		// V = p - p0. V points from p0 to p.
		return (new Vec(p.x - p0.x, p.y - p0.y, p.z	- p0.z));
	}
	
	public static Point3D pointAtEndOfVec(Point3D p0, double d, Vec v) {
		Point3D p = new Point3D(p0);
		p.x += d * (v.x);
		p.y += d * (v.y);
		p.z += d * (v.z);
		return p;
	}
	
	public void vectorAdd(Vec a) {
		this.x += a.x;
		this.y += a.y;
		this.z += a.z;
	}


}


