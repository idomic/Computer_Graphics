package ex3.render.raytrace;

import java.util.Map;

import math.Point3D;
import math.Ray;
import math.Vec;


/**
 * Represents the scene's camera.
 * 
 */
public class Camera implements IInitable{

//TODO add members and whatever you need 

	public void init(Map<String, String> attributes) {

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
