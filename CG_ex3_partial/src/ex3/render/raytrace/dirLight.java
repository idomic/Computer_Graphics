package ex3.render.raytrace;

import java.util.Map;

import math.Point3D;
import math.Vec;

/**
 * 
 * Describes directional light
 * @author Dana
 *
 */
public class dirLight extends Light {

	// Direction of light
	protected Vec direction = null;

	/**
	 * Constructor
	 */
	public dirLight() {
		super.color = new Vec(1, 1, 1);
	}

	@Override
	public void init(Map<String, String> attributes) {
		super.init(attributes);
		if (attributes.containsKey("direction")) {
			direction = new Vec(attributes.get("direction"));
		}
	}
	
	@Override
	public Vec getColor(Point3D point) {
		return color;
	}

	@Override
	public Vec getDir(Point3D p) {
		return Vec.negate(direction);
	}
	
	@Override
	public double getShadow(Point3D point, double paramDouble) {
		// A directional light has no shadow.
		return 1;
	}
}
