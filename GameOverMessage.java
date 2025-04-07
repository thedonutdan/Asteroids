import javax.swing.JComponent;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
/** GameOverMessage.java - A class to display game over message at end of game
* @author Daniel Andrews
*/
public class GameOverMessage extends JComponent {
	private double x, // x and y coordinates to display message
		  		   y;
	private int score; // Final score is displayed in message
	
	/** Constructor sets coordinates and final score
	* @param x x coordinate to display message
	* @param y y coordinate to display message
	* @param score Final score to display in message
	*/
	public GameOverMessage(double x, double y, int score) {
		this.x = x; // Set coordinates and final score
		this.y = y;
		this.score = score;

		setForeground(Color.WHITE); // Set color and font of text
		setFont(new Font("Courier", Font.PLAIN, 14));

	}

	/** Paints component on graphics canvas
	* @param g Graphics canvas to paint on
	*/
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g; // Cast to 2d graphics canvas

		// Draw message in center of screen
		g2.drawString("Game Over", (int) x - 50, (int) y - 50);
		g2.drawString("Final Score: " + score, (int) x - 70, (int) y - 35);
	}
}