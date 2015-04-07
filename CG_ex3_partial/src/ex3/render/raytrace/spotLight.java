package ex3.render.raytrace;

import java.util.Map;

import math.Point3D;
import math.Vec;

public class spotLight extends omniLight {

	private Point3D direction = null;
	
	public spotLight() {
		super.color = new Vec(1,1,1);
		super.setAttenuation(new Point3D("1 0 0"));
	}
	
	@Override
	public void init(Map<String, String> attributes) {
		super.init(attributes);
		if (attributes.containsKey("direction")){
			setDirection(new Point3D(attributes.get("direction")));
		}
	}

	public Point3D getDirection() {
		return direction;
	}

	public void setDirection(Point3D direction) {
		this.direction = direction;
	}

}
