package ex3.render.raytrace;

import java.util.Map;

import math.Point3D;
import math.Vec;

public class spotLight extends omniLight {

	protected Vec direction = null;
	
	// The attenuation variables.
		protected double kc = 1;
		protected double kl = 0;
		protected double kq = 0;
	
	public spotLight() {
		super.color = new Vec(1,1,1);
	}
	
	@Override
	public Vec getColor(Point3D point) {
		
		// lightToPoint is "L" from the class slides.
		Vec lightToPoint = Point3D.vecFromSub2Points(pos, point);
		
		// distance is "d" from class slides.
		double distance = lightToPoint.length();
		lightToPoint.normalize();

		double LD = -(Vec.dotProd(lightToPoint, direction));
		
		// Check if the point is behind us will return a (0,0,0) color.
		if (LD <= 0) {
			return new Vec(0, 0, 0);
		}

		double lightCoefficient = LD
				/ (kc + kl * distance + kq * distance * distance);

		return Vec.scale(lightCoefficient, color);
	}
	
	@Override
	public void init(Map<String, String> attributes) {
		super.init(attributes);
		if (attributes.containsKey("direction")){
			direction = new Vec(attributes.get("direction"));
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
		super.attenuation = new Point3D(kc, kl, kq);
		direction.normalize();
	}
}
