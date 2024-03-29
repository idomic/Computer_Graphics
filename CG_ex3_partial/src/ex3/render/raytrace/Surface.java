package ex3.render.raytrace;

import java.util.Map;

import math.Point3D;
import math.Ray;
import math.Vec;

/**
 * Represent a point light
 * 
 * Add methods as you wish, this can be a super class for other lights (think
 * which)
 */
public abstract class Surface implements IInitable {

	// Diffuse coeff
	protected Vec diffuse = null;
	// Specular coeff
	protected Vec specular = null;
	// ambient coeff
	protected Vec ambient = null;
	// Emission of surface
	protected Vec emission = null;
	// Shininess coeff
	protected double shininess;
	// Reflectance coeff
	protected double reflectance;

	/**
	 * Constructor
	 */
	public Surface() {
		// Initialize members with default values
		diffuse = new Vec(0.7, 0.7, 0.7);
		specular = new Vec(1, 1, 1);
		ambient = new Vec(0.1, 0.1, 0.1);
		emission = new Vec(0, 0, 0);
		shininess = 100;
		reflectance = 0;
	}

	@Override
	public void init(Map<String, String> attributes) {
		if (attributes.containsKey("mtl-diffuse")) {
			diffuse = new Vec(attributes.get("mtl-diffuse"));
		}
		if (attributes.containsKey("mtl-specular")) {
			specular = new Vec(attributes.get("mtl-specular"));
		}
		if (attributes.containsKey("mtl-ambient")) {
			ambient = new Vec(attributes.get("mtl-ambient"));
		}
		if (attributes.containsKey("mtl-emission")) {
			emission = new Vec(attributes.get("mtl-emission"));
		}
		if (attributes.containsKey("mtl-shininess")) {
			shininess = Double.parseDouble((attributes.get("mtl-shininess")));
		}
		if (attributes.containsKey("mtl-reflectance")) {
			reflectance = Double.parseDouble((attributes.get("mtl-reflectance")));
		}
	}
	
	/**
	 * This method calculates the nearest intersection point ray - object.
	 * If no intersection return Integer.MAX_VALUE.
	 * @param ray
	 * @return the ray's distance to the object
	 */
	public abstract double Intersect(Ray ray);
	
	public abstract Vec normal(Point3D p);
}
