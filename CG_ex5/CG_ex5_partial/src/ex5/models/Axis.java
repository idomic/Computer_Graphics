package ex5.models;

import javax.media.opengl.GL;

public class Axis {
	public static void render(GL gl, float length, boolean draw){
		if (draw) {
			gl.glLineWidth(2.0F);
			boolean flag = gl.glIsEnabled(GL.GL_LIGHTING);
			gl.glDisable(GL.GL_LIGHTING);
			gl.glBegin(GL.GL_LINES);
			gl.glColor3d(1.0D, 0.0D, 0.0D);
			gl.glVertex3d(0.0D, 0.0D, 0.0D);
			gl.glVertex3d(length, 0.0D, 0.0D);

			gl.glColor3d(0.0D, 1.0D, 0.0D);
			gl.glVertex3d(0.0D, 0.0D, 0.0D);
			gl.glVertex3d(0.0D, length, 0.0D);

			gl.glColor3d(0.0D, 0.0D, 1.0D);
			gl.glVertex3d(0.0D, 0.0D, 0.0D);
			gl.glVertex3d(0.0D, 0.0D, length);

			gl.glEnd();
			if (flag) {
				gl.glEnable(GL.GL_LIGHTING);
			}
		}
	}
}
