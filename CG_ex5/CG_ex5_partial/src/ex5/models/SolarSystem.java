package ex5.models;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

/**
 * A simple axes dummy 
 *
 */
public class SolarSystem implements IRenderable {	
	
	private boolean isLightSpheres = true;
	
	public void render(GL gl) {
		//TODO Define your OpenGL scene here.
	}
	
	 private void drawSun(GL gl, GLU glu, GLUquadric quad)
	  {
	    float[] clr = { 1.0F, 0.0F, 1.0F, 1.0F };
	    gl.glPushMatrix();
	    //setMaterialGenericPlanet(gl, clr);
	    
	    gl.glPushMatrix();
	    gl.glRotated(-90.0D, 1.0D, 0.0D, 0.0D);
	    glu.gluSphere(quad, 1.0D, 48, 48);
	    gl.glPopMatrix();
	    gl.glPopMatrix();
	  }
	 
	@Override
	public void init(GL gl) {
		//TODO If you need to initialize any OpenGL parameters, here is the place.
	}

	@Override
	public String toString() {
		return "Empty"; //TODO your scene's name goes here
	}

	

	//If your scene requires more control (like keyboard events), you can define it here.
	@Override
	public void control(int type, Object params) {
		switch (type) {
		case IRenderable.TOGGLE_LIGHT_SPHERES:
		{
			isLightSpheres = ! isLightSpheres;
			break;
		}
		default:
			System.out.println("Control type not supported: " + toString() + ", " + type);
		}
	}
	
	@Override
	public boolean isAnimated() {
		//This will be needed only in ex6
		return false;
	}

	@Override
	public void setCamera(GL gl) {
		//This will be needed only in ex6
	}
}
