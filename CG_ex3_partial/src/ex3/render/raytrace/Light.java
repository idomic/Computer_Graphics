package ex3.render.raytrace;

import java.util.Map;

import math.Point3D;
import math.Vec;

/**
 * Represent a point light
 * 
 * Add methods as you wish, this can be a super class for other lights (think which)
 */
public abstract class Light implements IInitable {

	// color of light
	protected Vec color;
	


	/**
	 * Constructor
	 */
	public Light() {
		color = new Vec(1,1,1);
	}

	@Override
	public void init(Map<String, String> attributes) {
		if (attributes.containsKey("color")){
			color = new Vec(attributes.get("color"));
		}
	}
	
	public abstract Vec getColor(Point3D point);

	/**
	 * A method to get the lights direction.
	 * @param p
	 * @return
	 */
	public abstract Vec getDir(Point3D p);
	
	/**
	 * Return the shadow value of that point that is caused by that light
	 * source.
	 * 
	 * @param point
	 * @param hitDistance
	 * @return shadow value
	 */
	public abstract double getShadow(Point3D point, double hitDistance);
	

	
}
