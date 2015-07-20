// IdoMichael-201157138
// DanaErlich-200400950
package ex5.models;

import javax.media.opengl.GL;

/**
 * A renderable model
 * 
 */
public interface IRenderable {

	//Constants for types passed to the control function.
	//You can add more here, or add model-specific constants in the model class.
	public static final int TOGGLE_LIGHT_SPHERES = 0;
	
	/**
	 * Render the model
	 * 
	 * @param gl
	 *            GL context
	 */
	public void render(GL gl, boolean isAxis);

	/**
	 * Initialize the model
	 * 
	 * @param gl
	 *            GL context
	 */
	public void init(GL gl);
	
	/**
	 * Render the model
	 * 
	 * @param  type
	 *            which setting to change
	 * @param params
	 * 			  Optional parameters needed to control the setting
	 */
	public void control(int type, Object params);
	
	/**
	 * (ex6)
	 * @return should this model be re-rendered repeatedly?
	 */
	public boolean isAnimated();
	
	/**
	 * (ex6)
	 * Set the camera to a point of view relative to the model
	 * 
	 * @param gl
	 *            GL context
	 */
	public void setCamera(GL gl);

}
