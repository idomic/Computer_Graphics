package ex3.render.raytrace;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import math.Ray;
import math.Vec;
import ex3.parser.Element;
import ex3.parser.SceneDescriptor;
import ex3.render.IRenderer;

public class RayTracer implements IRenderer {

	// scene
	protected Scene scene;
	// screen width
	protected int width;
	// screen height
	protected int height;
	// background image file path
	protected File path;
	// background image
	protected BufferedImage image;
	// Height of background image
	protected int imgHeight;
	// Width of background image
	protected int imgWidth;

	/**
	 * Inits the renderer with scene description and sets the target canvas to
	 * size (width X height). After init renderLine may be called
	 * 
	 * @param sceneDesc
	 *            Description data structure of the scene
	 * @param width
	 *            Width of the canvas
	 * @param height
	 *            Height of the canvas
	 * @param path
	 *            File path to the location of the scene. Should be used as a
	 *            basis to load external resources (e.g. background image)
	 */
	@Override
	public void init(SceneDescriptor sceneDesc, int width, int height, File path) {

		// initiate scene
		scene = new Scene();
		this.path = path;
		this.height = height;
		this.width = width;
		scene.init(sceneDesc.getSceneAttributes());

		// initiate background image
		if (this.scene.backTex != null) {
			image = null;
			File imageFile = new File(this.path.getParent() + File.separator
					+ this.scene.backTex);
			try {
				image = ImageIO.read(imageFile);
				imgHeight = image.getHeight();
				imgWidth = image.getWidth();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// initiate scene objects.
		for (Element e : sceneDesc.getObjects()) {
			scene.addObjectByName(e.getName(), e.getAttributes());
		}

		// initiate camera.
		scene.setCameraAttributes(sceneDesc.getCameraAttributes());

	}

	/**
	 * Renders the given line to the given canvas. Canvas is of the exact size
	 * given to init. This method must be called only after init.
	 * 
	 * @param canvas
	 *            BufferedImage containing the partial image
	 * @param line
	 *            The line of the image that should be rendered.
	 */
	@Override
	public void renderLine(BufferedImage canvas, int line) {
		for (int i = 0; i < width; i++) {
			canvas.setRGB(i, line, castRay(i, line).getRGB());
		}
	}

	/**
	 * this method cast the ray through a given pixel constructs the
	 * intersection and gets the color, the it sets the pixel color.
	 * 
	 * @param x
	 * @param y
	 * @return Color to pixel
	 */
	protected Color castRay(int x, int y) {
		
		// construct the ratio to screen.
		double scaleHeightFactor = 1.0;
		double scaleWidthFactor = 1.0;
		if (image != null) {
			scaleHeightFactor = (double) imgHeight / height;
			scaleWidthFactor = (double) imgWidth / width;
		}

		// Construct the ray through the pixel.
		Ray ray = scene.camera.constructRayThroughPixel(x, y, height, width);

		// Find intersection.
		MinIntersection intersection = scene.findIntersection(ray);

		// In case of no intersection.
		if (intersection == null) {
			if (image == null) {
				return scene.backCol.Vec2Color();
			} else {
				return new Color(image.getRGB((int) (x * scaleWidthFactor),
						(int) (y * scaleHeightFactor)));
			}
		}

		// implemented superSamp
		if (scene.superSamp > 1) {
			Vec sumColors = new Vec();
			
			// Scan grid of subpixels
			for (int i = 0; i < scene.superSamp; i++) {
				for (int j = 0; j < scene.superSamp; j++) {
					double newSubPixelX = x + i / scene.superSamp;
					double newSubPixelY = y + j / scene.superSamp;
					Ray rayThroughSubpixel = scene.camera
							.constructRayThroughPixel(newSubPixelX,
									newSubPixelY, height, width);
					MinIntersection newIntersection = this.scene
							.findIntersection(rayThroughSubpixel);
					sumColors.add(this.scene.calcColor(rayThroughSubpixel, 0,
							newIntersection));
				}
			}
			
			// Divide sum of colors by number of subpixels
			sumColors.scale(1.0 / (scene.superSamp * scene.superSamp));
			return sumColors.Vec2Color();
		}
		return scene.calcColor(ray, 0, intersection).Vec2Color();
	}

}
