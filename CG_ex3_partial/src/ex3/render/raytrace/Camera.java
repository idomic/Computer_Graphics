package ex3.render.raytrace;

import java.util.Map;
import java.util.Scanner;

import math.Point3D;
import math.Ray;
import math.Vec;


/**
 * Represents the scene's camera.
 * 
 */
public class Camera implements IInitable{

	private Point3D eye = null;
	private Vec direction = null;
	private Point3D lookAt = null;
	private Vec upDirection = null;
	private double screenDist = (Double)null;
	private double screenWidth =  (Double)null;
	
	public Camera() {
		screenDist = 0;
		screenWidth = 2;
	}

	public void init(Map<String, String> attributes) {
		String temp;
		if (attributes.containsKey("eye")){
			eye = new Point3D(attributes.get("eye"));
		}
		if (attributes.containsKey("direction")){
			direction = new Vec(attributes.get("direction"));
		}
		if (attributes.containsKey("look-at")){
			lookAt = new Point3D(attributes.get("look-at"));
		}
		if (attributes.containsKey("up-direction")){
			upDirection = new Vec(attributes.get("up-direction"));
		}
		if (attributes.containsKey("screen-dist")){
			temp = attributes.get("screen-dist");
			if (temp.equals("")) {
				screenDist = 0;
			} else {
				Scanner s = new Scanner(temp);
				screenDist = s.nextDouble();
				s.close();
			}
		}
		if (attributes.containsKey("screen-width")){
			temp = attributes.get("screen-width");
			if (temp.equals("")) {
				screenWidth = 0;
			} else {
				Scanner s = new Scanner(temp);
				screenWidth = s.nextDouble();
				s.close();
			}
		}
	}
	
	/**
	 * Transforms image xy coordinates to view pane xyz coordinates. Returns the
	 * ray that goes through it.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	
	
	public Ray constructRayThroughPixel(double x, double y, double height, double width) {		
		//TODO implement it
		return new Ray();
	}

	
	
	
	
	
	
	
}
