package ex3;

import java.io.File;

import ex3.gui.MainFrame;

/**
 * You basically don't need to add things here to the this package
 */
public class Ex3 {

	private static String getAboutMessage() {
		// TODO: Add any specific instructions here if you changed any thing or
		// had any improvements document them all
		return "GUI for exercise3\nPlease include any specific information you may "
				+ "want the grader to see while testing this.";
	}

	/**
	 * Main method. Command line usage is: <input scene filename> <canvas width>
	 * <canvas height> <target image filename>
	 */
	public static void main(String[] args) {

		String sceneFilename = null;
		String imageFilename = null;
		int canvasWidth = 480;
		int canvasHeight = 360;

		if (args.length > 0) {
			sceneFilename = args[0];
		}
		if (args.length > 1) {
			canvasWidth = Integer.valueOf(args[1]);
		}
		if (args.length > 2) {
			canvasHeight = Integer.valueOf(args[2]);
		}
		if (args.length > 3) {
			imageFilename = args[3];
		}

		// Init GUI
		MainFrame mainFrame = new MainFrame();
		mainFrame.initialize(sceneFilename, canvasWidth, canvasHeight,
				getAboutMessage());

		if (imageFilename == null) {
			mainFrame.setVisible(true);
		} else {
			// Render to file and quit
			mainFrame.render();
			mainFrame.saveRenderedImage(new File(imageFilename));
			System.exit(1);
		}
	}
}
