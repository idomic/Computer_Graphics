package ex3.render.raytrace;

import math.Point3D;

public class MinIntersection {

	// Intersection point between ray and surface.
	protected Point3D intersectionPoint;
	
	// The minimal surface that the ray intersect.
	protected Surface minSurface;
	
	// Minimal distance from the ray to the surface.
	protected double dist;
	
	// The constructor itself.
	public MinIntersection(Point3D p,Surface s, double d) {
		intersectionPoint = p;
		minSurface = s;
		dist = d;
	}

}
