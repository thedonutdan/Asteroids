import javax.swing.JComponent;
import java.awt.image.BufferedImage;

import java.awt.Image;

//import java.awt.geom.AffineTransform;
//import java.awt.image.AffineTransformOp;

import java.lang.Math;
/** GamePiece.java - An abstract class used to build all game pieces in game
* This class is abstract, serving as a blueprint to ensure player, bullet, and all asteroid objects
* can be handled correctly by the game controller
* This class satisfies requirement f. of project specification
* @author Daniel Andrews
* @version Final Project
*/
public abstract class GamePiece extends JComponent {
	private BufferedImage image, // Each gamepiece will use a BufferedImage to paint itself on the game canvas
				  rotatedImage;
	private Image animationImage;

	/** Sets the image to be rendered by the game piece
	 * @param bf Image to be set
	*/
	public  void setImage(BufferedImage bf) {
		image = bf;
	};

	/** Sets the rotated image to be rendered in specific rotation
	 * @param bf Image to be set
	*/
	public void setRotatedImage(BufferedImage bf) {
		rotatedImage = bf;
	}

	public void setAnimationImage(Image animationImage) {
		this.animationImage = animationImage;
	}

	/** Returns original image of sprite
	* @return Original image of sprite
	*/
	public BufferedImage getImage() {
		return image;
	}

	/** Returns rotated image of sprite
	* @return Rotated image of sprite
	*/
	public BufferedImage getRotatedImage() {
		return rotatedImage;
	}

	public Image getAnimationImage() {
		return animationImage;
	}

	/** Returns true if this object is colliding with another GamePiece
	* @param another Object to check for collision
	* @return true if this object is colliding with other GamePiece
	*/
	public boolean isCollidingWith(GamePiece another) {
		double range = this.getMaskRadius() + another.getMaskRadius(); // Range of collision masks of both objects
		double[] myCenter = this.getCenter(); // Get centers of both objects
		double[] otherCenter = another.getCenter();
		// Since the collision masks are circular, a simple calculation of distance between centerpoints can check collision
		double distance = Math.sqrt(Math.pow(myCenter[0] - otherCenter[0], 2) + Math.pow(myCenter[1] - otherCenter[1], 2));
		if (distance < range) {
			return true; // true if objects are colliding
		}

		return false; // false if objects are not colliding
	}

	/** Returns the distance to another GamePiece
	* @param another Object to check distance from
	* @return Distance between objects
	*/
	public double distanceTo(GamePiece another) {
		double[] myCenter = this.getCenter(); // Get centers of both objects
		double[] otherCenter = another.getCenter();
		return Math.sqrt(Math.pow(myCenter[0] - otherCenter[0], 2) + Math.pow(myCenter[1] - otherCenter[1], 2)); // Return distance between objects
	}

	// These methods will differ between classes but should be defined in each game class
	public abstract void tick(); // Called every frame update
	public abstract void move(); // Moves object, all game objects move in asteroids
	public abstract void destroy(); // When object is destroyed
	public abstract double getMaskRadius(); // Masks are fine-tuned based on sprite
	public abstract double[] getCenter(); // Each object should be able to return it's center
}