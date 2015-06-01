package ex5.models;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;


public class SolarSystem implements IRenderable {	

	private boolean isLightSpheres = true;
	private boolean AxisOn = true;
	
	//Define colors
	private static final float[] black = {0.0F, 0.0F, 0.0F};
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

	
	public void render(GL gl) {
		GLU glu = new GLU();
		GLUquadric quad = glu.gluNewQuadric();
		lighting(gl, glu, quad); //Add light sources
		//drawSolarSystem(gl, glu, quad); //Add Solar system
		gl.glScaled(0.2D, 0.2D, 0.2D);//scale
		drawSun(gl, glu, quad);//add sun

		//draw planets
		drawAllPlanets(gl, glu, quad);

		//clear
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
			double radius = 1.0D;
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


	private void drawSolarSystem(GL gl, GLU glu, GLUquadric quad) {
//		gl.glScaled(0.2D, 0.2D, 0.2D);//scale
//		drawSun(gl, glu, quad);//add sun
//
//		//draw planets
//		drawAllPlanets(gl, glu, quad);
//
//		//clear
//		glu.gluDeleteQuadric(quad);

	}

	private void drawAllPlanets(GL gl, GLU glu, GLUquadric quad) {
		drawPlanet(gl, glu, quad, 2.0F, 7.0F, 1.5F, 0.1F, MercuryColor, 0.0);
		drawPlanet(gl, glu, quad, 2.0F, 3.39F, 2.0F, 0.2F, VenusColor, 0.5);
		drawPlanet(gl, glu, quad, 23.45F, 0F, 2.6F, 0.18F, EarthColor, 0.8);
		drawPlanet(gl, glu, quad, 24.0F, 1.85F, 3.2F, 0.18F, MarsColor, 2);
		drawPlanet(gl, glu, quad, 3.1F, 1.3F, 4F, 0.28F, JupiterColor, -0.2);
		drawPlanet(gl, glu, quad, 26.7F, 2.49F, 4.8F, 0.25F, SaturnColor, 1);
		drawPlanet(gl, glu, quad, 97.9F, 0.77F, 5.55F, 0.21F, UranusColor,0.6);
		drawPlanet(gl, glu, quad, 28.8F, 1.77F, 6.3F, 0.25F, NeptuneColor,1.2);
		drawPlanet(gl, glu, quad, 57.5F, 17.2F, 6.3F, 0.1F, PlutoColor, 0.5 * Math.PI);
	}



	private void drawPlanet(GL gl, GLU glu, GLUquadric quad, float axisTilt, 
			float orbitInclination, float orbitRadius, float planetRadius, float[] color, double alpha) {
		//draw orbit first
		drawOrbit(gl, orbitInclination, orbitRadius, color);

		gl.glPushMatrix();
		// SET THE MATERIAL of the planet
		setMaterial(gl, color);

		gl.glPushMatrix();
		//place in space
		gl.glRotated(orbitInclination, 0.0D, 0.0D, 1.0D);

		double x = (double)orbitRadius * Math.sin(alpha);
		double z = (double)orbitRadius * Math.cos(alpha);
		gl.glTranslated(x, 0.0D, z);
		//		gl.glTranslated(orbitRadius, 0.0D, 0.0D);

		//set axis tilt
		gl.glRotated(-90.0D, 1.0D, 0.0D, 0.0D);
		gl.glRotated(axisTilt, 0.0D, 1.0D, 0.0D);

		//create the planet (sphere)		
		glu.gluSphere(quad, planetRadius, 48, 48);

		//render axis
		Axis.render(gl, planetRadius * 1.5F, AxisOn);

		//create moon if it's earth
		if (color == EarthColor) {
			gl.glPushMatrix();
			// SET THE MATERIAL of the planet
			setMaterial(gl, MoonColor);
			gl.glPushMatrix();
			gl.glTranslated(0.3D, 0.0D, 0.0D);
			//create moon
			glu.gluSphere(quad, 0.04D, 48, 48);
			//render it's axis
			Axis.render(gl, 0.06F, AxisOn);
			gl.glPopMatrix();
			gl.glPopMatrix();
		}

		//if it's Saturn, create it's ring
		if (color == SaturnColor) {
			gl.glPushMatrix();
			//material
			setMaterial(gl, SaturnRingColor);
			gl.glPushMatrix();
			//create the ring (by creating a disk)
			glu.gluDisk(quad, 0.4D, 0.5D, 48, 48);	    
			gl.glRotated(180.0D, 0.0D, 1.0D, 0.0D);
			//back ring
			glu.gluDisk(quad, 0.4D, 0.5D, 48, 48);	
			gl.glPopMatrix();
			gl.glPopMatrix();	
		}

		gl.glPopMatrix();
		gl.glPopMatrix();
	}


	private void setMaterial(GL gl, float[] color) {
		gl.glColor3fv(color, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, color, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, black, 0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_EMISSION, black, 0);
	}


	private void drawOrbit(GL gl, float inclination, float orbitRadius, float[] color) {
		gl.glPushMatrix();
		//rotate by inclination
		gl.glRotated(inclination, 0.0D, 0.0D, 1.0D);

		//LIGHTS
		boolean lightFlag = gl.glIsEnabled(GL.GL_LIGHTING);
		gl.glDisable(GL.GL_LIGHTING);

		// the color of the orbit
		gl.glColor3f(color[0], color[1], color[2]);

		//draw the orbit in a line loop
		gl.glBegin(GL.GL_LINE_LOOP);
		for (float alpha = 0; alpha < 2.0 * Math.PI; alpha += 0.1) {
			double x = orbitRadius * Math.sin(alpha);
			double z = orbitRadius * Math.cos(alpha);
			gl.glVertex3d(x, 0.0, z);
		}
		gl.glEnd();

		// LIGHTS
		if (lightFlag) {
			gl.glEnable(GL.GL_LIGHTING);
		}
		gl.glPopMatrix();
	}


	private void drawSun(GL gl, GLU glu, GLUquadric quad) {
		gl.glPushMatrix();
		//set material
		setMaterial(gl, SunColor);
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


	private void drawSaturnRing(GL gl, GLU glu, GLUquadric quad) {
		// light
		gl.glEnable(GL.GL_LIGHTING);

		gl.glPushMatrix();
		//material
		setMaterial(gl, SaturnRingColor);
		gl.glPushMatrix();

		//orbitInclination
		gl.glRotated(2.49D, 0.0D, 0.0D, 1.0D);

		//move to saturn's location in space (4.8 is the radius of the saturn's orbit)
		gl.glTranslated(4.8D, 0.0D, 0.0D);
		gl.glRotated(-90.0D, 1.0D, 0.0D, 0.0D);

		//axis tilt
		gl.glRotated(26.7D, 0.0D, 0.0D, 1.0D);

		//create the ring (by creating a disk)
		glu.gluDisk(quad, 0.4D, 0.5D, 48, 48);	    
		gl.glRotated(180.0D, 0.0D, 1.0D, 0.0D);
		//back ring
		glu.gluDisk(quad, 0.4D, 0.5D, 48, 48);	
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
