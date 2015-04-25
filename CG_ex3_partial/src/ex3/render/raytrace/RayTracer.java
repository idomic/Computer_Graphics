package ex3.render.raytrace;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//import com.sun.medialib.mlib.Image;

import math.Ray;
import math.Vec;
//import sun.reflect.generics.repository.ConstructorRepository;
import ex3.parser.Element;
import ex3.parser.SceneDescriptor;
import ex3.render.IRenderer;

public class RayTracer implements IRenderer {
	
	protected Scene scene;
	protected int width;
	protected int height;
	protected File path;
	protected BufferedImage image;
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
		scene = new Scene();
		this.path = path;
		this.height = height;
		this.width = width;
		scene.init(sceneDesc.getSceneAttributes());
		if(this.scene.backTex != null) {
			image = null;
			File imageFile = new File(this.path.getParent() + File.separator + this.scene.backTex);
			try {
				image = ImageIO.read(imageFile);
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		for (Element e : sceneDesc.getObjects()) {
			scene.addObjectByName(e.getName(), e.getAttributes());
		}
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
		for (int i = 0; i < canvas.getWidth(); i++) {
			canvas.setRGB(i, line, castRay(i,line).getRGB());
		}
	}

	protected Color castRay(int x, int y) {
		
		// Constructing the ray through the center of a pixel.
		Ray ray = scene.camera.constructRayThroughPixel(x, y, height, width);
		
		// Find intersection with the scene
		MinIntersection intersection = scene.findIntersection(ray, false);
		
		// If no intersection try background image or background color.
		if(intersection == null) {
			if(image == null) {
				return scene.backCol.Vec2Color();
			} else { 
				return new Color(image.getRGB(x, y));
			}
		}
		
		if (scene.superSamp > 1)
		{
			
			Vec color = new Vec();
			for (int i = 0; i < scene.superSamp; i++) {
				for (int j = 0; j < scene.superSamp; j++)
				{
					Ray newRay = this.scene.camera.constructRayThroughPixel(x + i / this.scene.superSamp, 
							y + j / this.scene.superSamp, height, width);
					MinIntersection newIntersection = this.scene.findIntersection(newRay, false);
					Vec subcolor = this.scene.calcColor(newRay, 0, newIntersection);
					color.add(subcolor);
				}
			}
			color.scale(1.0D / Math.pow(this.scene.superSamp, 2.0D));
			return color.Vec2Color();
		}

		return scene.calcColor(ray, 0, intersection).Vec2Color();
	}

}
