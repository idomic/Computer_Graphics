package ex3.render.raytrace;

import java.util.Map;

import math.Ray;
import math.Point3D;
import math.Vec;

public class spotLight extends Light {

	//Source point of light
	protected Point3D pos = null;
	// Light attenuation
	protected Point3D attenuation = null;
	// direction of spot light
	protected Vec direction = null;
	
	// The attenuation variables.
		protected double kc = 1;
		protected double kl = 0;
		protected double kq = 0;
	
	public spotLight() {
		super.color = new Vec(1,1,1);
	}
	
	@Override
	public void init(Map<String, String> attributes) {
		super.init(attributes);
		if (attributes.containsKey("pos")) {
			pos = new Point3D(attributes.get("pos"));
		}
		if (attributes.containsKey("dir")){
			direction = new Vec(attributes.get("dir"));
			direction.normalize();
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
	public Vec getDir(Point3D p) {
		Vec vecToLight = Point3D.vecFromSub2Points(pos, p);
		vecToLight.normalize();
		return vecToLight;
	}

	@Override
	public double getShadow(Point3D p, double hitDistance) {
		double distanceToLight;
		Vec vecToLight = Point3D.vecFromSub2Points(pos, p);
		distanceToLight = vecToLight.length();

		// If the hit distance is closer than the light than we have a shadow.
		if ((hitDistance > Ray.eps)
				&& (hitDistance < distanceToLight - Ray.eps)) {
			return 0;
		}
		return 1;
	}
}
