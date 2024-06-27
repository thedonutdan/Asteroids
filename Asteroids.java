import java.io.IOException;
/** Asteroids.java - A clone of classic Atari arcade game Asteroids
* 
* 	Algorithm:
*	 <ol>
*		<li>Create gamecontroller with public scope to give all objects access</li>
*		<li>Load images and highscore, exit if error occurs</li>
*		<li>Create game main menu with start and exit options</li>
*		<li>If player selects exit skip to </li>
*		<li>If player selects start clear menu and make game window</li>
*		<li>Create three starting asteroids in game</li>
*		<li>Put player at middle of screen</li>
*		<li>Player turns with left and right, moves with up, and shoots with space</li>
*		<li>Check all bullets for collisions with asteroids, skip to step if none</li>
*		<li>Destroy bullet and asteroid and increment score</li>
*		<li>Check all bullets for collisions with player, skip to step if none</li>
*		<li>Destroy bullet and player and decrement lives</li>
*		<li>Check all asteroids for collisions with player, skip to step if none</li>
*		<li>Destroy asteroid and player and decrement lives</li>
*		<li>If player lives reach zero, display game over message and return to step 3, updating highscore if necessary</li>
*		<li>If player lives above zero, return to step 9</li>
*		<li>Save high scores and terminate program</li>
*	 </ol>
* 
* 
* @author Daniel Andrews
* @version Final Project
*/
public class Asteroids {
	// Constants to set the game window width and height
	public static final int CANVAS_WIDTH = 750,
							CANVAS_HEIGHT = 600;
	// Object to control game flow, given public scope to allow access to
	// all game objects
	public static GameController gc;

	public static void main(String[] args) {
		// Instantiate new game controller
		gc = new GameController();
		try {
			gc.loadImages(); // Load game sprites
			gc.loadHighscore(); // Load highscore data
		}
		catch (IOException e) {
			// Indicate to user if error occurs in reading files and exit
			System.out.println("Error Loading Game Files: " + e.getMessage()); 
			System.exit(1);
		}
		
		GameFrame gf = new GameFrame(); // Create game window
		gc.addGameFrame(gf); // Pass game window to controller
		gf.goToMenu(gc.getHighscore()); // Start at game menu

	}
}