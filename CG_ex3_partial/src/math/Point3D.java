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

	//TODO add methods/more constructors here

}

