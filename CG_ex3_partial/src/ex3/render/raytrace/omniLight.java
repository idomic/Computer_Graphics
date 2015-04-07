package ex3.render.raytrace;

import java.util.Map;

import math.Point3D;
import math.Vec;

public class omniLight extends Light {

	private Point3D pos = null;
	private Point3D attenuation = null;

	public omniLight() {
		super.color = new Vec(1,1,1);
		setAttenuation(new Point3D("1 0 0"));

	}
	
	@Override
	public void init(Map<String, String> attributes) {
		super.init(attributes);
		if (attributes.containsKey("pos")){
			setPos(new Point3D(attributes.get("pos")));
		}
		if (attributes.containsKey("attenuation")){
			setPos(new Point3D(attributes.get("attenuation")));
		}
		
	}

	public Point3D getPos() {
		return pos;
	}

	public void setPos(Point3D pos) {
		this.pos = pos;
	}

	public Point3D getAttenuation() {
		return attenuation;
	}

	public void setAttenuation(Point3D attenuation) {
		this.attenuation = attenuation;
	}

}
