// IdoMichael-201157138
// DanaErlich-200400950
package ex5.models;

import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL;
import javax.media.opengl.GLException;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;


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

	//Define textures - texture files
	private final String SunTextureFile = "Bitmaps/sunmap.bmp";
	private final String MercuryTextureFile = "Bitmaps/mercurymap.bmp";
	private final String VenusTextureFile = "Bitmaps/venusmap.bmp";
	private final String EarthTextureFile = "Bitmaps/earthmap.bmp";
	private final String MarsTextureFile = "Bitmaps/marsmap.bmp";
	private final String JupiterTextureFile = "Bitmaps/jupitermap.bmp";
	private final String SaturnTextureFile = "Bitmaps/saturnmap.bmp";
	private final String SaturnRingTextureFile = "Bitmaps/saturnRing.bmp";
	private final String UranusTextureFile = "Bitmaps/uranusmap.bmp";
	private final String NeptuneTextureFile = "Bitmaps/neptunemap.bmp";
	private final String PlutoTextureFile = "Bitmaps/plutomap.bmp";
	private final String MoonTextureFile = "Bitmaps/moonmap.bmp";
	private final String GalaxyTextureFile = "Bitmaps/galaxymap.bmp";

	//Textures - initiated in texturesInit
	Texture SunTexture;
	Texture MercuryTexture;
	Texture VenusTexture;
	Texture EarthTexture;
	Texture MarsTexture;
	Texture JupiterTexture;
	Texture SaturnTexture;
	Texture SaturnRingTexture;
	Texture UranusTexture;
	Texture NeptuneTexture;
	Texture PlutoTexture;
	Texture MoonTexture;
	Texture GalaxyTexture;

	private float AnimationRotation;
	private double currentOrbitInclination = 0.0D;
	private double currentOrbitalPeriod = 364.0D;
	private double currentOrbitRadius = 2.6D;
	private int currplanet = 0;

	
	public void render(GL gl,boolean isAxis ) {
		AxisOn = isAxis;

		GLU glu = new GLU();
		GLUquadric quad = glu.gluNewQuadric();
		
		lighting(gl, glu, quad); //Add light sources
		
		if (isAnimated()) {
			this.AnimationRotation += 0.01F;
		}

		gl.glScaled(0.2D, 0.2D, 0.2D);//scale
		drawSun(gl, glu, quad);//add sun
		drawPlanets(gl, glu, quad);//add planets
		background(gl);//add background
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
		drawPlanetX(gl, glu, quad, 2.0F, 7.0F, 1.5F, 0.1F, MercuryColor, 9.0, MercuryTexture,  58.65F, 87.97F);
		drawPlanetX(gl, glu, quad, 2.0F, 3.39F, 2.0F, 0.15F, VenusColor, 9.0, VenusTexture, 243.01F, 224.70F);
		drawPlanetX(gl, glu, quad, 23.45F, 0F, 2.5F, 0.2F, EarthColor, 9.0, EarthTexture, 1.0F, 365.26F);
		drawPlanetX(gl, glu, quad, 24.0F, 1.85F, 3.0F, 0.12F, MarsColor, 9.0, MarsTexture, 1.0F, 686.98F);
		drawPlanetX(gl, glu, quad, 3.1F, 1.3F, 4.0F, 0.28F, JupiterColor, 9.0, JupiterTexture, 0.41F, 4332.82F);
		drawPlanetX(gl, glu, quad, 26.7F, 2.49F, 4.5F, 0.26F, SaturnColor, 9.0, SaturnTexture, 0.44F, 10755.70F);
		drawPlanetX(gl, glu, quad, 97.9F, 0.77F, 5.5F, 0.24F, UranusColor,9.0, UranusTexture, 0.72F, 30687.15F);
		drawPlanetX(gl, glu, quad, 28.8F, 1.77F, 6.5F, 0.22F, NeptuneColor,9.0, NeptuneTexture, 0.67F, 60190.03F);
		drawPlanetX(gl, glu, quad, 57.5F, 17.2F, 6.5F, 0.1F, PlutoColor, 9.0, PlutoTexture, 6.38F, 90800.0F);
	}


	private void drawPlanetX(GL gl, GLU glu, GLUquadric quad, float axialTilt, 
			float orbitInclination, float orbitRadius, float planetRadius, float[] color, double initPos, Texture planetTexture, float selfRevolution, float orbitalPeriod) {
		//draw orbit 
		drawOrbit(gl, orbitInclination, orbitRadius, color);
		gl.glPushMatrix();
		gl.glPushMatrix();
		
		//rotate according to planet orbit inclination
		gl.glRotated(orbitInclination, 0.0D, 0.0D, 1.0D);
		
		//shift planet to some initial position on its orbit.
		gl.glRotated(initPos, 0.0D, 1.0D, 0.0D);
		//animate spin around planet orbit
		gl.glRotated(360.0D * (this.AnimationRotation / Math.log(orbitalPeriod)), 0.0D, 1.0D, 0.0D);
			
		gl.glTranslated(orbitRadius, 0.0D, 0.0D);

		//set axial tilt
		gl.glRotated(-90.0D, 1.0D, 0.0D, 0.0D); //map y to -z
		gl.glRotated(axialTilt, 0.0D, 1.0D, 0.0D);

		gl.glPushMatrix();
		//animate the planet's spin around itself
		gl.glRotated(360.0D * (this.AnimationRotation / selfRevolution), 0.0D, 0.0D, 1.0D);

		//render axis
		Axis.render(gl, planetRadius * 1.5F, AxisOn);
		
		// set planet's texture
		planetTexture.bind();
		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
		gl.glEnable(GL.GL_TEXTURE_2D);
		glu.gluQuadricTexture(quad, true);

		//create planet
		double radius = planetRadius;
		int slices = 50;
		int stacks = 50;
		glu.gluSphere(quad, radius, slices, stacks);

		gl.glDisable(GL.GL_TEXTURE_2D);

		gl.glPopMatrix();

		//draw moon for planet earth
		if (color == EarthColor) {
			gl.glPushMatrix();
			gl.glPushMatrix();
			//draw orbit 
			gl.glRotated(90.0D, 1.0D, 0.0D, 0.0D);
			drawOrbit(gl, 0.0F, 0.25F, MoonColor);
			gl.glPopMatrix();
			
			//moon rotation around the Earth
			gl.glRotated(360.0D * (this.AnimationRotation / Math.log(15.0)), 0.0D, 0.0D, 1.0D);
			gl.glTranslated(0.25D, 0.0D, 0.0D);			
			//render axis
			Axis.render(gl, 0.06F, AxisOn);
			
			// set moon's texture
			MoonTexture.bind();
			gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
			gl.glEnable(GL.GL_TEXTURE_2D);
			glu.gluQuadricTexture(quad, true);
			
			//create moon
			glu.gluSphere(quad, 0.03D, 50, 50);
			gl.glDisable(GL.GL_TEXTURE_2D);
			gl.glPopMatrix();
		}

		//draw Saturn's ring
		if (color == SaturnColor) {
			gl.glPushMatrix();
			
			gl.glRotated(360.0D * (this.AnimationRotation / selfRevolution), 0.0D, 0.0D, 1.0D);
			
			// set texture
			SaturnRingTexture.bind();
			gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
			gl.glEnable(GL.GL_TEXTURE_2D);
			glu.gluQuadricTexture(quad, true);
			
			//create ring
			glu.gluDisk(quad, 0.5D, 0.5D, 50, 50);	    
			gl.glRotated(180.0D, 0.0D, 1.0D, 0.0D);
			
			//back ring
			glu.gluDisk(quad, 0.5D, 0.5D, 50, 50);	
			gl.glDisable(GL.GL_TEXTURE_2D);
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
		this.SunTexture.bind();
		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
		gl.glPushMatrix();
	
		// sun rotates around itself
		gl.glRotated(360.0D * (this.AnimationRotation/7.0), 0.0D, 1.0D, 0.0D);

		//create sun
		gl.glEnable(GL.GL_TEXTURE_2D);
		glu.gluQuadricTexture(quad, true);
		double radius = 1.0D;
		int slices = 50;
		int stacks = 50;
		glu.gluSphere(quad, radius, slices, stacks);
		gl.glDisable(GL.GL_TEXTURE_2D);

		gl.glPopMatrix();

	}

	private void texturesInit(GL gl) {
		try	{
			this.SunTexture = TextureIO.newTexture(new File(SunTextureFile), true);
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP);
		} catch (GLException e)	{
			e.printStackTrace();
		} catch (IOException e)	{
			e.printStackTrace();
		}

		try	{
			this.MercuryTexture = TextureIO.newTexture(new File(MercuryTextureFile), true);
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP);
		} catch (GLException e)	{
			e.printStackTrace();
		} catch (IOException e)	{
			e.printStackTrace();
		}

		try	{
			this.VenusTexture = TextureIO.newTexture(new File(VenusTextureFile), true);
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP);
		} catch (GLException e)	{
			e.printStackTrace();
		} catch (IOException e)	{
			e.printStackTrace();
		}

		try	{
			this.EarthTexture = TextureIO.newTexture(new File(EarthTextureFile), true);
			//			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP);
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
		} catch (GLException e)	{
			e.printStackTrace();
		} catch (IOException e)	{
			e.printStackTrace();
		}

		try	{
			this.MarsTexture = TextureIO.newTexture(new File(MarsTextureFile), true);
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP);
		} catch (GLException e)	{
			e.printStackTrace();
		} catch (IOException e)	{
			e.printStackTrace();
		}

		try	{
			this.JupiterTexture = TextureIO.newTexture(new File(JupiterTextureFile), true);
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP);
		} catch (GLException e)	{
			e.printStackTrace();
		} catch (IOException e)	{
			e.printStackTrace();
		}

		try	{
			this.SaturnTexture = TextureIO.newTexture(new File(SaturnTextureFile), true);
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP);
		} catch (GLException e)	{
			e.printStackTrace();
		} catch (IOException e)	{
			e.printStackTrace();
		}


		try	{
			this.UranusTexture = TextureIO.newTexture(new File(UranusTextureFile), true);
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP);
		} catch (GLException e)	{
			e.printStackTrace();
		} catch (IOException e)	{
			e.printStackTrace();
		}


		try	{
			this.NeptuneTexture = TextureIO.newTexture(new File(NeptuneTextureFile), true);
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP);
		} catch (GLException e)	{
			e.printStackTrace();
		} catch (IOException e)	{
			e.printStackTrace();
		}


		try	{
			this.PlutoTexture = TextureIO.newTexture(new File(PlutoTextureFile), true);
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP);
		} catch (GLException e)	{
			e.printStackTrace();
		} catch (IOException e)	{
			e.printStackTrace();
		}		

		try	{
			this.SaturnRingTexture = TextureIO.newTexture(new File(SaturnRingTextureFile), true);
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP);
		} catch (GLException e)	{
			e.printStackTrace();
		} catch (IOException e)	{
			e.printStackTrace();
		}

		try	{
			this.MoonTexture = TextureIO.newTexture(new File(MoonTextureFile), true);
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP);
		} catch (GLException e)	{
			e.printStackTrace();
		} catch (IOException e)	{
			e.printStackTrace();
		}

		try	{
			this.GalaxyTexture = TextureIO.newTexture(new File(GalaxyTextureFile), true);
			//			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP);
		} catch (GLException e)	{
			e.printStackTrace();
		} catch (IOException e)	{
			e.printStackTrace();
		}

	}

	private void background(GL gl) {
		
		GalaxyTexture.bind();
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glOrtho(0, 1, 0, 1, 0, 1.6);
		gl.glRotated(270D, 0.0D, 0.0D, 1.0D);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();

		//Background shouldn't write to depth buffer
		gl.glDepthMask(false);
		int r = 50;
		gl.glBegin( GL.GL_QUADS ); {
			gl.glTexCoord2f( 0f, 0f );
			gl.glVertex3d(-r, -r, r);
			gl.glTexCoord2f( 0f, 1f );
			gl.glVertex3d(r, -r, r);
			gl.glTexCoord2f( 1f, 1f );
			gl.glVertex3d(r, r, r);
			gl.glTexCoord2f( 1f, 0f );
			gl.glVertex3d(-r, r, r);
		} 
		gl.glEnd();

		gl.glDepthMask(true);

		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW);	
	}


	@Override
	public void init(GL gl) {
		this.AnimationRotation = 0.0F;
		texturesInit(gl);
	}

	@Override
	public String toString() {
		return "Solar System"; 
	}



	//If your scene requires more control (like keyboard events), you can define it here.
	@Override
	public void control(int type, Object params) {
		if (type == 0) {
			isLightSpheres = !isLightSpheres;
			} else {
				setPlanets();
			}
	}

	@Override
	public boolean isAnimated() {
		return true;
	}

	@Override
	public void setCamera(GL gl) {
		gl.glLoadIdentity();
		gl.glTranslated(0.0D, -0.08, -0.08);
		gl.glTranslated(-this.currentOrbitRadius/5.0D, 0.0D, 0.0D);
		gl.glRotated(-360.0D * (this.AnimationRotation / Math.log(currentOrbitalPeriod)), 0.0D, 1.0D, 0.0D);
		gl.glRotated(-this.currentOrbitInclination, 0.0D, 0.0D, 1.0D);
	}
	
	public void setPlanets() {
		this.currplanet = (this.currplanet %9 + 1);
		switch (this.currplanet){
		case 1: //Merury
			this.currentOrbitRadius = 1.5D;
			this.currentOrbitalPeriod = 87.97;
			this.currentOrbitInclination = 7.0D;
			break;
		case 2: //Venus
			this.currentOrbitRadius = 2.0D;
			this.currentOrbitalPeriod = 224.70D;
			this.currentOrbitInclination = 3.39D;
			break;
		case 3: //Earth
			this.currentOrbitRadius = 2.5D;
			this.currentOrbitalPeriod = 365.26D;
			this.currentOrbitInclination = 0.0D;
			break;
		case 4: // Mars
			this.currentOrbitRadius = 3.0D;
			this.currentOrbitalPeriod = 686.98D;
			this.currentOrbitInclination = 1.85D;
			break;
		case 5: // Jupiter
			this.currentOrbitRadius = 4.0D;
			this.currentOrbitalPeriod = 4332.82D;
			this.currentOrbitInclination = 1.3D;
			break;
		case 6: // Saturn
			this.currentOrbitInclination =  2.49D;
			this.currentOrbitalPeriod = 10755.70D;
			this.currentOrbitRadius = 4.5D;
			break;
		case 7: // Uranus
			this.currentOrbitInclination =  0.77D;
			this.currentOrbitalPeriod = 30687.15D;
			this.currentOrbitRadius = 5.5D;
			break;
		case 8: // Neptune
			this.currentOrbitInclination =  1.77D;
			this.currentOrbitalPeriod = 60190.03D;
			this.currentOrbitRadius = 6.5D;
			break;
		case 9: // Pluto
			this.currentOrbitInclination =  17.2D;
			this.currentOrbitalPeriod = 90800.0D;
			this.currentOrbitRadius = 6.5D;
			break;	
		}
	}
}
