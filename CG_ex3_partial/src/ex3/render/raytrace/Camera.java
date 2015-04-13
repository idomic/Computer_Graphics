package ex3.render.raytrace;

import java.util.Map;
import math.Point3D;
import math.Ray;
import math.Vec;

/**
 * Represents the scene's camera.
 * 
 */
public class Camera implements IInitable {

	protected Point3D eye = null;
	protected Vec direction = null;
	protected Point3D lookAt = null;
	protected Vec upDirection = null;
	protected Vec rightDirection = null;
	protected double screenDist;
	protected double screenWidth;

	public Camera() {
		screenDist = 0;
		screenWidth = 2;
	}

	public void init(Map<String, String> attributes) {
		if (attributes.containsKey("eye")) {
			eye = new Point3D(attributes.get("eye"));
		}
//		if (attributes.containsKey("direction")) {
//			direction = new Vec(attributes.get("direction"));
//		}
		if (attributes.containsKey("look-at")) {
			lookAt = new Point3D((String)attributes.get("look-at"));
			direction = Point3D.vecFromSub2Points(lookAt, eye);
			direction.normalize();
		} else if (attributes.containsKey("direction")) {
			direction = new Vec(attributes.get("direction"));
			lookAt = Point3D.pointAtEndOfVec(eye, 1, direction);
			direction.normalize();
		}
//		if (attributes.containsKey("look-at")) {
//			lookAt = new Point3D(attributes.get("look-at"));
//		}
		
		// Set the given up direction.
		if (attributes.containsKey("up-direction")) {
			upDirection = new Vec((String)attributes.get("up-direction"));

			if (direction != null) {
				rightDirection = Vec.crossProd(direction,upDirection);
				rightDirection.normalize();
				upDirection = Vec.crossProd(rightDirection,direction);
				upDirection.normalize();
			}
		}

//		if (attributes.containsKey("up-direction")) {
//			upDirection = new Vec(attributes.get("up-direction"));
//		}
		if (attributes.containsKey("screen-dist")) {
			Double.parseDouble(attributes.get("screen-dist"));
		}
		if (attributes.containsKey("screen-width")) {
			Double.parseDouble(attributes.get("screen-width"));
		}
	}

	/**
	 * Transforms image xy coordinates to view pane xyz coordinates. Returns the
	 * ray that goes through it.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */

	public Ray constructRayThroughPixel(double x, double y, double height,
			double width) {
		Point3D centerPoint = Point3D.pointAtEndOfVec(eye, screenDist, direction);

		double ratio = screenWidth / width;

		// Calculate the offset from the given (x,y).
		double upOffset = ((height / 2) - y) * ratio;
		double rightOffset = (x - (width / 2)) * ratio;

		// Calculate the point value in the scene. p = centerpoint +
		// rightOffset*rightdirection + upOffset*upDirection.
		Point3D pixel = new Point3D(centerPoint);
		pixel.vectorAdd(Vec.scale(rightOffset, rightDirection));
		pixel.vectorAdd(Vec.scale(upOffset, upDirection));

		// Calculate the vector from the camera eye that goes throw the pixel.
		Vec directionToPixel = Point3D.vecFromSub2Points(pixel,eye);
		directionToPixel.normalize();

		return new Ray(eye, directionToPixel);

		
//		rightDirection = Vec.crossProd(upDirection, direction);
//		
//		if (direction == null && lookAt == null) {
//			System.err.println("insuficient data for camera");
//		} else if (direction == null) {
//			direction = new Vec(eye, lookAt);
//		}
//		return new Ray(eye, direction);
	}
}
