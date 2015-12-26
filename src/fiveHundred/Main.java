package fiveHundred;

import javax.swing.UIManager;

/**
 * This class is the main entry point of the program. It contains
 * the main method to launch the game.
 */
public class Main
{

	public static void main(String[] args)
	{
		// Set the Look to the system's look instead of java's default look
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		Game game = new Game();
		game.start();
	}
}
