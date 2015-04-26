package ex3.render.raytrace;

import java.util.Map;
import math.Point3D;
import math.Ray;
import math.Vec;

/**
 * Represents the scene's camera. contains it's location and coordinates.
 * 
 * @author Dana
 *
 */
public class Camera implements IInitable {

	// Center of Camera
	protected Point3D eye = null;
	// Direction camera is pointing
	protected Vec direction = null;
	// Point direction canera is pointing
	protected Point3D lookAt = null;
	// Horizontal axis
	protected Vec upDirection = null;
	// Vertical axis
	protected Vec rightDirection = null;
	// Distance of eye from screen
	protected double screenDist;
	// Width of screen
	protected double screenWidth;

	/**
	 * Constructor
	 */
	public Camera() {
		screenDist = 0;
		screenWidth = 2;
	}

	public void init(Map<String, String> attributes) {

		// initialize from attributes
		if (attributes.containsKey("eye")) {
			eye = new Point3D(attributes.get("eye"));
		}
		if (attributes.containsKey("look-at")) {
			lookAt = new Point3D((String) attributes.get("look-at"));
			direction = Point3D.vecFromSub2Points(lookAt, eye);
			direction.normalize();
		} else if (attributes.containsKey("direction")) {
			direction = new Vec(attributes.get("direction"));
			lookAt = Point3D.pointAtEndOfVec(eye, 1, direction);
			direction.normalize();
		}

		// Set the up direction
		if (attributes.containsKey("up-direction")) {
			upDirection = new Vec((String) attributes.get("up-direction"));

			if (direction != null) {
				rightDirection = Vec.crossProd(direction, upDirection);
				rightDirection.normalize();
				upDirection = Vec.crossProd(rightDirection, direction);
				upDirection.normalize();
			}
		}
		// Set the screen distance
		if (attributes.containsKey("screen-dist")) {
			screenDist = Double.parseDouble(attributes.get("screen-dist"));
		}
		// Set the screen width
		if (attributes.containsKey("screen-width")) {
			screenWidth = Double.parseDouble(attributes.get("screen-width"));
		}
	}

	/**
	 * Transforms image coordinates to view pane coordinates. Returns the ray
	 * which goes through the middle.
	 * 
	 * @param x - x value of pixel
	 * @param y - y value of pixel
	 * @return Ray - that from camera through the pixel (x,y) on the canvas
	 */
	public Ray constructRayThroughPixel(double x, double y, double height,
			double width) {
		Point3D centerPoint = Point3D.pointAtEndOfVec(eye, screenDist,
				direction);

		double ratio = screenWidth / width;

		// Calculate the offset from the given (x,y)
		double upOffset = ((height / 2) - y) * ratio;
		double rightOffset = (x - (width / 2)) * ratio;

		// Calculate the point value in the scene
		Point3D pixel = new Point3D(centerPoint);
		pixel.vectorAdd(Vec.scale(rightOffset, rightDirection));
		pixel.vectorAdd(Vec.scale(upOffset, upDirection));

		// Calculate the vector from the (Position) eye that goes throw the pixel
		Vec directionToPixel = Point3D.vecFromSub2Points(pixel, eye);
		directionToPixel.normalize();
		return new Ray(eye, directionToPixel);
	}
}
