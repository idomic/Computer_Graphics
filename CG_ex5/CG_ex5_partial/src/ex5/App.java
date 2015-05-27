package ex5;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLJPanel;
import javax.swing.JFrame;

import ex5.models.Empty;
import ex5.models.IRenderable;


public class App {
	
	static IRenderable[] models = {new Empty()}; //TODO: Set additional models here
	static Point prevMouse;
	static int currentModel;
	static Frame frame;
	
	/**
	 * Create frame, canvas and viewer, and load the first model.
	 * 
	 * @param args
	 *            No arguments
	 */
	public static void main(String[] args) {

		frame = new JFrame();
		
		// Create viewer and initialize with first model
		final Viewer viewer = new Viewer();
		viewer.setModel(nextModel());
		
		final GLJPanel canvas = new GLJPanel();
		
		frame.setSize(500, 500);
		frame.setLayout(new BorderLayout());		
		frame.add(canvas, BorderLayout.CENTER);
		
		// Add event listeners
		canvas.addGLEventListener(viewer);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(1);
				super.windowClosing(e);
			}
		});
		canvas.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {

				// Toggle wireframe mode
				if (e.getKeyChar() == 'p')
					viewer.toggleRenderMode();

				// Toggle axes
				if (e.getKeyChar() == 'a')
					viewer.toggleAxes();

				// Toggle light spheres
				if (e.getKeyChar() == 'l')
					viewer.toggleLightSpheres();

				// Show next model
				if (e.getKeyChar() == 'm')
					viewer.setModel(nextModel());

				// Set camera to follow model (ex6)
				//ex6: uncomment the following 2 lines to enable placing the camera ralative to the model
				//if (e.getKeyChar() == 'c')
				//	viewer.toggleModelCamera();
				
				canvas.repaint();
				super.keyTyped(e);
			}
		});
		canvas.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				// Let mouse drag affect trackball view
				viewer.storeTrackball(prevMouse, e.getPoint());
				prevMouse = e.getPoint();
			}
		});
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				prevMouse = e.getPoint();
				viewer.startAnimation();
				super.mousePressed(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				viewer.stopAnimation();
				super.mouseReleased(e);
			}
		});
		canvas.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				// Let mouse wheel affect zoom 
				double rot = e.getWheelRotation();
				viewer.zoom(rot); //zoom out for negative rot, zoom in for positive rot.
				canvas.repaint();
			}
		});
		
		// Show frame
		
		canvas.setFocusable(true);
		canvas.requestFocus();
		frame.setVisible(true);
		canvas.repaint();
	}
	
	/**
	 * Return the next model in the array
	 * 
	 * @return Renderable model
	 */
	private static IRenderable nextModel() {
		IRenderable model = models[currentModel++];
		frame.setTitle("Exercise 5/6 - " + model.toString());
		currentModel = currentModel%models.length;
		
		return model;
	}
}
