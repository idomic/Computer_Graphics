package ex5;

import java.awt.Point;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import com.sun.opengl.util.FPSAnimator;

import ex5.models.IRenderable;

/**
 * An OpenGL model viewer 
 *
 */
public class Viewer implements GLEventListener {

	private double zoom = 0.0; //How much to zoom in? >0 mean magnify, <0 means shrink
	private Point mouseFrom, mouseTo; //From where to where was the mouse dragged between the last redraws?
	private boolean isWireframe = false; //Should we display wireframe or not?
	private boolean isAxes = true; //Should we display axes or not?
	private IRenderable model; //Model to display
	private FPSAnimator ani; //This object is responsible to redraw the model with a constant FPS
	private GLAutoDrawable m_drawable = null; //We store the drawable OpenGL object to refresh the scene
	private boolean isModelCamera = false; //Whether the camera is relative to the model, rather than the world (ex6)
	private boolean isModelInitialized = false; //Whether model.init() was called.
	

	@Override
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		if (!isModelInitialized) {
			model.init(gl);
			isModelInitialized = true;
		}
		//TODO: uncomment the following line to clear the window before drawing
		//gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		setupCamera(gl);
		if (isAxes)
			renderAxes(gl);
		
		//TODO: set wireframe mode

		model.render(gl);
	}

	private void setupCamera(GL gl) {
		if (!isModelCamera) { //Camera is in an absolute location
			//TODO: place the camera. You should use mouseFrom, mouseTo, canvas width and
			//      height (reshape function), zoom etc. This should actually implement the trackball
			//		and zoom. You might want to store the rotation matrix in an array for next time.
			//		Relevant functions: glGetDoublev, glMultMatrixd
			//      Example: gl.glGetDoublev(GL.GL_MODELVIEW_MATRIX, rotationMatrix, 0);
			
			
			//By this point, we should have already changed the point of view, now set these to null
			//so we don't change it again on the next redraw.
			mouseFrom = null;
			mouseTo = null;
		} else { //Camera is relative to the model
			gl.glLoadIdentity();
			model.setCamera(gl);
		}
	}
	
	@Override
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
			boolean deviceChanged) {
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		drawable.setGL(new javax.media.opengl.DebugGL(gl));

		//TODO: light model, normal normalization, depth test, back face culling, ...
		
		//gl.glCullFace(GL.GL_BACK);    // Set Culling Face To Back Face
        //gl.glEnable(GL.GL_CULL_FACE); // Enable back face culling

		// Initialize display callback timer
		ani = new FPSAnimator(30, true);
		ani.add(drawable);
		
		m_drawable = drawable;
		
		if (model.isAnimated()) //Start animation (for ex6)
			startAnimation();
		else
			stopAnimation();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		//TODO: Remember the width and height of the canvas for the trackball.
		//TODO: Apply zoom using the projection matrix
		//TODO: Set the projection to perspective.
	}

	/**
	 * Stores the mouse coordinates for trackball rotation.
	 * 
	 * @param from
	 *            2D canvas point of drag beginning
	 * @param to
	 *            2D canvas point of drag ending
	 */
	public void storeTrackball(Point from, Point to) {
		//The following lines store the rotation for use when next displaying the model.
		//After you redraw the model, you should set these variables back to null.
		if (!isModelCamera) {
			if (null == mouseFrom)
				mouseFrom = from;
			mouseTo = to;
			m_drawable.repaint();
		}
	}

	/**
	 * Zoom in or out of object. s<0 - zoom out. s>0 zoom in.
	 * 
	 * @param s
	 *            Scalar
	 */
	public void zoom(double s) {
		if (!isModelCamera) {
			zoom += s*0.1;
			m_drawable.repaint();
		}
	}

	/**
	 * Toggle rendering method. Either wireframes (lines) or fully shaded
	 */
	public void toggleRenderMode() {
		isWireframe = !isWireframe;
		m_drawable.repaint();
	}
	
	/**
	 * Toggle whether little spheres are shown at the location of the light sources.
	 */
	public void toggleLightSpheres() {
		model.control(IRenderable.TOGGLE_LIGHT_SPHERES, null);
		m_drawable.repaint();
	}

	/**
	 * Toggle whether axes are shown.
	 */
	public void toggleAxes() {
		isAxes = !isAxes;
		m_drawable.repaint();
	}
	
	public void toggleModelCamera() {
		isModelCamera =! isModelCamera;
		m_drawable.repaint();
	}
	
	/**
	 * Start redrawing the scene with 60 FPS
	 */
	public void startAnimation() {
		if (!ani.isAnimating())
			ani.start();
	}
	
	/**
	 * Stop redrawing the scene with 60 FPS
	 */
	public void stopAnimation() {
		if (ani.isAnimating())
			ani.stop();
	}
	
	private void renderAxes(GL gl) {
		gl.glLineWidth(2);
		boolean flag = gl.glIsEnabled(GL.GL_LIGHTING);
		gl.glDisable(GL.GL_LIGHTING);
		gl.glBegin(GL.GL_LINES);
		gl.glColor3d(1, 0, 0);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(10, 0, 0);
		
		gl.glColor3d(0, 1, 0);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(0, 10, 0);
		
		gl.glColor3d(0, 0, 1);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(0, 0, 10);
		
		gl.glEnd();
		if(flag)
			gl.glEnable(GL.GL_LIGHTING);
	}

	public void setModel(IRenderable model) {
		this.model = model;
		isModelInitialized = false;
	}
	
}
