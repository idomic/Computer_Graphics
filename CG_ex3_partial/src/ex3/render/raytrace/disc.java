package ex3.render.raytrace;

import java.util.Map;

import math.Point3D;
import math.Ray;
import math.Vec;

public class disc extends Surface {

	protected Point3D center = null;
	protected double radius;
	protected Point3D normal = null;
	public disc() { 
		super.diffuse = new Vec(0.7, 0.7, 0.7);
		super.specular = new Vec(1, 1, 1);
		super.ambient = new Vec(0.1, 0.1, 0.1);
		super.emission = new Vec(0, 0, 0);
		super.shininess = 100;
		super.reflectance = 0;
		}
	
	@Override
	public void init(Map<String, String> attributes) {
		super.init(attributes);
		if (attributes.containsKey("center")){
			center = new Point3D(attributes.get("center"));
		}
		if (attributes.containsKey("radius")){
			radius =Double.parseDouble(attributes.get("radius"));
		}			
		if (attributes.containsKey("normal")){
			normal = new Point3D(attributes.get("normal"));
		}	
	}

	@Override
	public double Intersect(Ray ray) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Vec normal(Point3D p) {
		// TODO Auto-generated method stub
		return null;
	}

}
