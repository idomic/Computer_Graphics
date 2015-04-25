package ex3.render.raytrace;

import java.util.Map;

import math.Point3D;
import math.Ray;
import math.Vec;

public class omniLight extends Light {

	protected Point3D pos = null;
	protected Point3D attenuation = null;

	// The attenuation variables.
	protected double kc = 1;
	protected double kl = 0;
	protected double kq = 0;

	public omniLight() {
		pos = new Point3D(0, 0, 0);
		super.color = new Vec(1, 1, 1);
	}

	@Override
	public void init(Map<String, String> attributes) {
		super.init(attributes);
		if (attributes.containsKey("pos")) {
			pos = new Point3D(attributes.get("pos"));
		}
		if (attributes.containsKey("kc")) {
			kc = new Double(attributes.get("kc")).doubleValue();
		}
		if (attributes.containsKey("kl")) {
			kl = new Double(attributes.get("kl")).doubleValue();
		}
		if (attributes.containsKey("kq")) {
			kq = new Double(attributes.get("kq")).doubleValue();
		}
		attenuation = new Point3D(kc, kl, kq);
	}
	
	@Override // Problem with color!!!
	public Vec getColor(Point3D point) {
		
		// lightToPoint is "L" from the class slides.
		Vec lightToPoint = Point3D.vecFromSub2Points(this.pos, point);
		
		// distance is "d" from class slides.
		double distance = lightToPoint.length();

		double lightCoefficient = 1 / (kc + kl * distance + kq
				* distance * distance);
		
		return Vec.scale(lightCoefficient, this.color);
	}

	public Vec getDir(Point3D p) {
		Vec ans = Point3D.vecFromSub2Points(this.pos, p);
		ans.normalize();
		return ans;
	}

	@Override
	public double getShadow(Point3D p, double hitDistance) {
		double distanceToLight;
		Vec vecToLight = Point3D.vecFromSub2Points(this.pos, p);
		distanceToLight = vecToLight.length();

		// If the hit distance is closer than the light than we have a shadow.
		if ((hitDistance > Ray.eps)
				&& (hitDistance < distanceToLight - Ray.eps)) {
			return 0;
		}

		return 1;
	}
}
