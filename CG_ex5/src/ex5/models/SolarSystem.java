// IdoMichael-201157138
// DanaErlich-200400950
package ex5.models;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;


public class SolarSystem implements IRenderable {	

	private boolean isLightSpheres = true;
	private boolean AxisOn = true;
	
	//Define colors
	private static final float[] SunColor = {1.0F, 0.347F, 0F};
	private static final float[] MercuryColor = {0.75294F, 0.75294F, 0.75294F};
	private static final float[] VenusColor = {0.9873F ,0.664706F, 0.164706F};
	private static final float[] EarthColor =  {0.0F, 0.4789F, 1.0F};
	private static final float[] MarsColor = {1.0F, 0.45555F, 0F};
	private static final float[] JupiterColor = {0.623529F ,0.333333F, 0.222222F};
	private static final float[] SaturnColor = {0.44555F, 0.44555F, 0.44555F};
	private static final float[] SaturnRingColor = {0.54555F, 0.54555F, 0.54555F};
	private static final float[] UranusColor = {0.0F, 0.51755F, 0.55563F};
	private static final float[] NeptuneColor = {0.0F, 0.85864F, 0.96554F};
	private static final float[] PlutoColor = {0.87666F, 0.36555F, 0.27777F};
	private static final float[] MoonColor = {0.93777F, 0.93777F, 0.93777F};

	
	public void render(GL gl,boolean isAxis ) {
		AxisOn = isAxis;

		GLU glu = new GLU();
		GLUquadric quad = glu.gluNewQuadric();
		
		lighting(gl, glu, quad); //Add light sources
		gl.glScaled(0.2D, 0.2D, 0.2D);//scale
		drawSun(gl, glu, quad);//add sun
		drawPlanets(gl, glu, quad);//add planets
		glu.gluDeleteQuadric(quad);
	}

	private void lighting(GL gl, GLU glu, GLUquadric quad) {
		
		gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 3.0F);

		//White light source:
		float[] whiteLightPosition = { 0.0F, 3.0F, 0.0F, 0.0F };
		float[] whiteLightColor = { 1.0F, 1.0F, 1.0F };
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, whiteLightColor, 0); //Diffuse element of light
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, whiteLightColor, 0); //Specular element of light
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, whiteLightPosition, 0);
		gl.glEnable(GL.GL_LIGHT0);


		//Red light source
		float[] redLightPosition = { 0.0F, -3.0F, 0.0F, 0.0F };
		float[] redLightColor = { 1.0F, 0.0F, 0.0F };
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, redLightColor, 0);
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, redLightColor, 0);
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, redLightPosition, 0);
		gl.glEnable(GL.GL_LIGHT1);

		//Display light sources as spheres:
		if (this.isLightSpheres) {
			boolean lightFlag = gl.glIsEnabled(GL.GL_LIGHTING);
			gl.glDisable(GL.GL_LIGHTING);

			//sphere parameters:
			double radius = 0.1D;
			int slices = 20;
			int stacks = 20;
			
			//White light source sphere
			gl.glPushMatrix();
			gl.glTranslated(whiteLightPosition[0], whiteLightPosition[1], whiteLightPosition[2]);
			gl.glColor4fv(whiteLightColor, 0);
			glu.gluSphere(quad, radius, slices, stacks);
			gl.glPopMatrix();	 

			//Red light source sphere
			gl.glPushMatrix();
			gl.glTranslated(redLightPosition[0], redLightPosition[1], redLightPosition[2]);
			gl.glColor4fv(redLightColor, 0);
			glu.gluSphere(quad, radius, slices, stacks);
			gl.glPopMatrix();

			if (lightFlag) {
				gl.glEnable(GL.GL_LIGHTING);
			}
		}
		
	}


	private void drawPlanets(GL gl, GLU glu, GLUquadric quad) {
		drawPlanetX(gl, glu, quad, 2.0F, 7.0F, 1.5F, 0.1F, MercuryColor, 40);
		drawPlanetX(gl, glu, quad, 2.0F, 3.39F, 2.0F, 0.15F, VenusColor, 80);
		drawPlanetX(gl, glu, quad, 23.45F, 0F, 2.5F, 0.2F, EarthColor, 120);
		drawPlanetX(gl, glu, quad, 24.0F, 1.85F, 3.0F, 0.12F, MarsColor, 160);
		drawPlanetX(gl, glu, quad, 3.1F, 1.3F, 4.0F, 0.28F, JupiterColor, 200);
		drawPlanetX(gl, glu, quad, 26.7F, 2.49F, 4.5F, 0.26F, SaturnColor, 240);
		drawPlanetX(gl, glu, quad, 97.9F, 0.77F, 5.5F, 0.24F, UranusColor,280);
		drawPlanetX(gl, glu, quad, 28.8F, 1.77F, 6.5F, 0.22F, NeptuneColor,320);
		drawPlanetX(gl, glu, quad, 57.5F, 17.2F, 6.5F, 0.1F, PlutoColor, 360);
	}


	private void drawPlanetX(GL gl, GLU glu, GLUquadric quad, float axialTilt, 
			float orbitInclination, float orbitRadius, float planetRadius, float[] color, double initPos) {
		//draw orbit 
		drawOrbit(gl, orbitInclination, orbitRadius, color);
		gl.glPushMatrix();
		
		// set planet material properties
		setMaterialProperties(gl, color);
		gl.glPushMatrix();
		
		//rotate according to planet orbit inclination
		gl.glRotated(orbitInclination, 0.0D, 0.0D, 1.0D);
		
		//shift planet to some initial position on its orbit.
		gl.glRotated(initPos, 0.0D, 1.0D, 0.0D);
		gl.glTranslated(orbitRadius, 0.0D, 0.0D);

		//set axial tilt
		gl.glRotated(-90.0D, 1.0D, 0.0D, 0.0D); //map y to -z
		gl.glRotated(axialTilt, 0.0D, 1.0D, 0.0D);

		//create planet
		double radius = planetRadius;
		int slices = 50;
		int stacks = 50;
		glu.gluSphere(quad, radius, slices, stacks);

		//render axis
		Axis.render(gl, planetRadius * 1.5F, AxisOn);

		//draw moon for planet earth
		if (color == EarthColor) {
			gl.glPushMatrix();
			//set moon material properties
			setMaterialProperties(gl, MoonColor);
			gl.glPushMatrix();
			gl.glTranslated(0.3D, 0.0D, 0.0D);
			//create moon
			glu.gluSphere(quad, 0.03D, 50, 50);
			//render axis
			Axis.render(gl, 0.06F, AxisOn);
			gl.glPopMatrix();
			gl.glPopMatrix();
		}

		//draw Saturn's ring
		if (color == SaturnColor) {
			gl.glPushMatrix();
			//set material properties
			setMaterialProperties(gl, SaturnRingColor);
			gl.glPushMatrix();
			//create ring
			glu.gluDisk(quad, 0.5D, 0.5D, 50, 50);	    
			gl.glRotated(180.0D, 0.0D, 1.0D, 0.0D);
			//back ring
			glu.gluDisk(quad, 0.5D, 0.5D, 50, 50);	
			gl.glPopMatrix();
			gl.glPopMatrix();	
		}

		gl.glPopMatrix();
		gl.glPopMatrix();
	}


	private void setMaterialProperties(GL gl, float[] color) {
		//set planet color
		gl.glColor3fv(color, 0);
		//set diffuse property
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, color, 0);
		//set specular property
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, color, 0);
		//set emission
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_EMISSION, color, 0);
	}


	private void drawOrbit(GL gl, float orbitInclination, float orbitRadius, float[] color) {
		gl.glPushMatrix();
		//add orbit inclination
		gl.glRotated(orbitInclination, 0.0D, 0.0D, 1.0D);
		
		boolean lightFlag = gl.glIsEnabled(GL.GL_LIGHTING);
		gl.glDisable(GL.GL_LIGHTING);

		// color orbit
		gl.glColor3f(color[0], color[1], color[2]);

		//draw the orbit using line loop
		gl.glBegin(GL.GL_LINE_LOOP);
		for (float alpha = 0; alpha < 2.0 * Math.PI; alpha += 0.1) {
			double x = orbitRadius * Math.sin(alpha);
			double z = orbitRadius * Math.cos(alpha);
			gl.glVertex3d(x, 0.0, z);
		}
		gl.glEnd();

		if (lightFlag) {
			gl.glEnable(GL.GL_LIGHTING);
		}
		gl.glPopMatrix();
	}


	private void drawSun(GL gl, GLU glu, GLUquadric quad) {
		gl.glPushMatrix();
		//set material properties
		setMaterialProperties(gl, SunColor);
		gl.glPushMatrix();

		//rotate 90 degrees in the x axis
		gl.glRotated(-90.0D, 1.0D, 0.0D, 0.0D);

		//create sun
		double radius = 1.0D;
		int slices = 50;
		int stacks = 50;
		glu.gluSphere(quad, radius, slices, stacks);
		gl.glPopMatrix();
		gl.glPopMatrix();
	}


	@Override
	public void init(GL gl) {
		//TODO If you need to initialize any OpenGL parameters, here is the place.
	}

	@Override
	public String toString() {
		return "Solar System"; //TODO your scene's name goes here
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
