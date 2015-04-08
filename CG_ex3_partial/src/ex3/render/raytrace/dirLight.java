package ex3.render.raytrace;

import java.util.Map;

import math.Point3D;
import math.Vec;

public class dirLight extends Light {

	protected Point3D direction = null;

	public dirLight() {
		super.color = new Vec(1, 1, 1);
	}

	@Override
	public void init(Map<String, String> attributes) {
		super.init(attributes);
		if (attributes.containsKey("direction")) {
			direction = new Point3D(attributes.get("direction"));
		}
	}
}
