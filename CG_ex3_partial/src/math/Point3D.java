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

}


