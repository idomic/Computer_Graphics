// IdoMichael-201157138
// DanaErlich-200400950
package ex5.models;

import javax.media.opengl.GL;

public class Cube implements IRenderable {	
	
	private boolean isLightSpheres = true;
	
	
	
	public void render(GL gl, boolean isAxis) {
		
		boolean lightingFlag = gl.glIsEnabled(GL.GL_LIGHTING);
	    gl.glDisable(GL.GL_LIGHTING);

		gl.glBegin(GL.GL_QUADS);// Start Drawing The Cube (GL_QUADS)
		
		// Quad parallel xy plane at z=1
		gl.glColor3d(0.0D, 0.0D, 1.0D); //blue 
		gl.glVertex3d(-0.5D, -0.5D, 0.5D); // Bottom Left Of The Quad 
		gl.glColor3d(1.0D, 0.0D, 1.0D); //magenta
		gl.glVertex3d(0.5D, -0.5D, 0.5D); // Bottom Right Of The Quad 
		gl.glColor3d(1.0D, 1.0D, 1.0D); //white
		gl.glVertex3d(0.5D, 0.5D, 0.5D); // Top Right Of The Quad 
		gl.glColor3d(0.0D, 1.0D, 1.0D); //cyan
		gl.glVertex3d(-0.5D, 0.5D, 0.5D); // Top Left Of The Quad 

		// Quad parallel yz plane at x=-1
		gl.glColor3d(0.0D, 0.0D, 0.0D); //black
		gl.glVertex3d(-0.5D, -0.5D, -0.5D); // Bottom Left Of The Quad
		gl.glColor3d(0.0D, 0.0D, 1.0D); //blue
		gl.glVertex3d(-0.5D, -0.5D, 0.5D); // Bottom Right Of The Quad 
		gl.glColor3d(0.0D, 1.0D, 1.0D); //cyan
		gl.glVertex3d(-0.5D, 0.5D, 0.5D); // Top Right Of The Quad
		gl.glColor3d(0.0D, 1.0D, 0.0D); //green
		gl.glVertex3d(-0.5D, 0.5D, -0.5D); // Top Left Of The Quad 

		// Quad parallel yz plane at x=1
		gl.glColor3d(1.0D, 0.0D, 1.0D); //magenta
		gl.glVertex3d(0.5D, -0.5D, 0.5D); // Bottom Left Of The Quad
		gl.glColor3d(1.0D, 0.0D, 0.0D); //red
		gl.glVertex3d(0.5D, -0.5D, -0.5D); // Bottom Right Of The Quad
		gl.glColor3d(1.0D, 1.0D, 0.0D); //yellow
		gl.glVertex3d(0.5D, 0.5D, -0.5D); // Top Right Of The Quad 
		gl.glColor3d(1.0D, 1.0D, 1.0D); //white
		gl.glVertex3d(0.5D, 0.5D, 0.5D); // Top Left Of The Quad
		
		// Quad parallel xy plane at z=-1
		gl.glColor3d(1.0D, 0.0D, 0.0D); //red
		gl.glVertex3d(0.5D, -0.5D, -0.5D); // Bottom Left Of The Quad
		gl.glColor3d(0.0D, 0.0D, 0.0D); //black
		gl.glVertex3d(-0.5D, -0.5D, -0.5D); // Bottom Right Of The Quad
		gl.glColor3d(0.0D, 1.0D, 0.0D); //green
		gl.glVertex3d(-0.5D, 0.5D, -0.5D); // Top Right Of The Quad
		gl.glColor3d(1.0D, 1.0D, 0.0D); //yellow
		gl.glVertex3d(0.5D, 0.5D, -0.5D); // Top Left Of The Quad 
		
		// Quad parallel xz plane at y=1
		gl.glColor3d(0.0D, 1.0D, 1.0D); //cyan
		gl.glVertex3d(-0.5D, 0.5D, 0.5D); // Bottom Left Of The Quad
		gl.glColor3d(1.0D, 1.0D, 1.0D); //white
		gl.glVertex3d(0.5D, 0.5D, 0.5D); // Bottom Right Of The Quad
		gl.glColor3d(1.0D, 1.0D, 0.0D); //yellow
		gl.glVertex3d(0.5D, 0.5D, -0.5D); // Top Right Of The Quad
		gl.glColor3d(0.0D, 1.0D, 0.0D); //green
		gl.glVertex3d(-0.5D, 0.5D, -0.5D); // Top Left Of The Quad 

		// Quad parallel xz plane at y=-1
		gl.glColor3d(0.0D, 0.0D, 0.0D); //black
		gl.glVertex3d(-0.5D, -0.5D, -0.5D); // Bottom Left Of The Quad
		gl.glColor3d(1.0D, 0.0D, 0.0D); //red
		gl.glVertex3d(0.5D, -0.5D, -0.5D); // Bottom Right Of The Quad
		gl.glColor3d(1.0D, 0.0D, 1.0D); //magenta
		gl.glVertex3d(0.5D, -0.5D, 0.5D); // Top Right Of The Quad
		gl.glColor3d(0.0D, 0.0D, 1.0D); //blue
		gl.glVertex3d(-0.5D, -0.5D, 0.5D); // Top Left Of The Quad

		gl.glEnd();
		if (lightingFlag) {
			gl.glEnable(2896);
		}
	}

	@Override
	public void init(GL gl) {

	}

	@Override
	public String toString() {
		return "Cube"; 
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
