// IdoMichael-201157138
// DanaErlich-200400950
package ex5.models;

import javax.media.opengl.GL;

/**
 * A simple axes dummy 
 *
 */
public class Empty implements IRenderable {	
	
	private boolean isLightSpheres = true;
	
	public void render(GL gl, boolean isAxis) {
		//TODO Define your OpenGL scene here.
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
