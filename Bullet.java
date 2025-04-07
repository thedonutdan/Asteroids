import java.awt.Graphics;
import java.awt.Graphics2D;

import java.lang.Math;
/** Bullet.java - A bullet shot by the player\
*
* @author Daniel Andrews
*
*/
public class Bullet extends GamePiece {
	private double x, // x and y coordinates
				   y,
				   direction, // Direction and maximum range to travel
				   range;

	private int speed; // Speed to travel at
	private boolean enabled = true; // Bullet state
	private boolean isPlayerBullet;

	public Bullet(double x, double y, double direction, int speed, boolean isPlayerBullet) {
		this.x = x; // Set x, y, and direction
		this.y = y;
		this.direction = direction;
		this.speed = speed;
		this.isPlayerBullet = isPlayerBullet;
		range = Asteroids.CANVAS_WIDTH * 2; // Bullet should be able to travel twice across the game canvas
		setImage(Asteroids.gc.getBulletImage()); // Retrieve image from controller
	}
	
	/** Wraps bullet around screen if it exceeds game window boundaries
	 * @return true if object is offscreen, false otherwise
	*/
	public boolean screenWrap() {
		if (x > Asteroids.CANVAS_WIDTH - getImage().getWidth() / 2) {
			x = 0 - getImage().getWidth() / 2;
			return true;
		} // Right wrap
		if (x < 0 - getImage().getWidth()) {
			x = Asteroids.CANVAS_WIDTH - getImage().getWidth();
			return true;
		} // Left wrap
		if (y > Asteroids.CANVAS_HEIGHT - getImage().getHeight() / 2) {
			y = 0 - getImage().getHeight() / 2;
			return true;
		} // Bottom wrap
		if (y < 0 - getImage().getHeight()) {
			y = Asteroids.CANVAS_HEIGHT - getImage().getHeight();
			return true;
		} // Top wrap
		return false; // False if object is on screen
	}
	
	/** Update player each frame
	*/
	public void tick() {
		move(); // Move bullet
		if (range <= 0.0) {
			this.destroy(); // If bullet has exhausted its range, destroy
		}
		if (screenWrap() && !isPlayerBullet) {
			destroy();
		} // Wrap around screen and repaint each frame
		repaint();
	}

	/** Paint the object on the graphics canvas
	* @param g Graphics canvas to paint object on
	*/
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g; // Cast canvas to 2d canvas

		g2.drawImage(getImage(), (int) x, (int) y, this); // Draw image at position
	}

	/** Move bullet based on direction and speed
	*/
	public void move() {
		x += Math.cos(Math.toRadians(direction)) * speed; // Calculate change in x and y based on direction of travel
		y += Math.sin(Math.toRadians(direction)) * speed;
		if (isPlayerBullet) {
			range -= speed; // Update remaining range
		} else {
			range -= speed * 2;
		}
	}

	/** Destroy bullet
	*/
	public void destroy() {
		enabled = false; // Disable bullet
		Asteroids.gc.removeBullet(this); // Remove bullet from game controller
	}

	/** Returns x coordinate of object
	* @return x coordinate of object
	*/
	public double x() {
		return x;
	}

	/** Returns y coordinate of objecct
	* @return y coordinate of object
	*/
	public double y() {
		return y;
	}

	/** Returns this objects speed
	* @return Speed of object
	*/
	public int getSpeed() {
		return speed;
	}

	/** Returns this objects direction
	* @return Direction of object
	*/
	public double getDirection() {
		return direction;
	}

	/** Returns if object is enabled
	* @return true if object is enabled
	*/
	public boolean isEnabled() {
		return enabled;
	}

	/** Returns radius of collision mask
	* @return Radius of collision mask
	*/
	public double getMaskRadius() {
		return 1;
	}

	/** Returns double array representing center of object
	* @return Center of object as double array
	*/
	public double[] getCenter() {
		double[] center = new double[2];
		center[0] = x;
		center[1] = y;
		return center;
	}

	/** Compares this object to another
	* @param another Object to be compared
	* @return true if objects are equal
	*/
	public boolean equals(Object another) {
		if (another == null) {return false;}
		if (another.getClass() != this.getClass()) {return false;}
		Bullet other = (Bullet) another;
		return other.x() == x &&
			   other.y() == y &&
			   other.getSpeed() == speed &&
			   other.getDirection() == direction;
	}

	public String toString() {
		return "Bullet at (" + (int) x + "," + (int) y + ") traveling at heading " + (int) direction + " at a speed of " + (int) speed;
	}
}