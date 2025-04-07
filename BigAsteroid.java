import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import java.lang.Math;
/** BigAsteroid.java - A class to represent a large asteroid
* @author Daniel Andrews
* 
*/
public class BigAsteroid extends GamePiece {
	private double x, // x and y coordination and direction
				   y,
				   direction;
	private double speed; // Speed of asteroid
	private double imageDirection; // Orientation of image

	/** No-args Constructor sets direction of movement and image to random value
	* This constructor is meant to be called by subclasses to inherit direction
	*/
	public BigAsteroid() {
		direction = Math.random() * 360; // Random values for movement and image direction
		imageDirection = Math.random() * 360;
	}

	/** Full Constructor sets location of asteroid
	 * @param x x-coordinate for asteroid location
	 * @param y y-coordinate for asteriod location
	*/
	public BigAsteroid(double x, double y) {
		this.x = x; // Set x, y coordinates
		this.y = y;
		direction = Math.random() * 360; // Set random values for movement and image direction
		imageDirection = Math.random() * 360;
		speed = 1.0;
		setImage(Asteroids.gc.getBigAsteroidImage()); // Retrieve sprite image from controller
		setRotatedImage(getImage());
	}

	/** Sets x coordinate
	* @param x x coordinate
	*/
	public void xSet(double x) {
		this.x = x;
	}

	/** Sets y coordinate
	* @param y y coordinate
	*/
	public void ySet(double y) {
		this.y = y;
	}

	/** Returns x coordinate
	* @return x coordinate
	*/
	public double x() {
		return x;
	}

	/** Returns y coordinate
	* @return y coordinate
	*/
	public double y() {
		return y;
	}

	/** Sets direction
	* @param direction New direction of travel
	*/
	public void setDirection(double direction) {
		this.direction = direction;
	}

	/** Returns direction of trave;
	* @return Direction of travel
	*/
	public double getDirection() {
		return direction;
	}

	/** Sets speed
	* @param speed New speed
	*/
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	/** Returns speed
	* @return Object speed
	*/
	public double getSpeed() {
		return speed;
	}

	/** Paints asteroid on graphics canvas
	* @param g Graphics canvas to paint on
	*/
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g; // Cast canvas to 2d canvas

		g2.drawImage(getRotatedImage(),(int) x,(int) y, this); // Draw rotated image sprite
	}

	/** Update object every frame
	*/
	public void tick() {
		move(); // Move object
		imageDirection += 1; // Rotate asteroid
		rotateImage(); // Rotate image
		screenWrap(); // Wrap around screen
		repaint(); // Update image on canvas
	}

	/** Returns if object is enabled
	* @return true if object is enabled
	*/
	public boolean isEnabled() {
		return true;
	}

	/** Rotates the sprite image
	*/
	public void rotateImage() {
		double rotationRequired = Math.toRadians(imageDirection); // Find rotation needed
		double locationX = getImage().getWidth() / 2; // Get center of asteroid
		double locationY = getImage().getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, 
			locationX, locationY); // Create rotation object
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR); // New transformation operation
		BufferedImage newImage = new BufferedImage(getImage().getWidth(), getImage().getHeight(), 
			getImage().getType()); // Use original image to minimize distortion
		op.filter(getImage(), newImage); // Apply rotation filter to image

		setRotatedImage(newImage); // Update rotated image sprite
	}

	/** Move object based on direction and speed
	*/
	public void move() {
		x += Math.cos(Math.toRadians(direction)) * speed; // Calculate change in x and y based on speed and direction of travel
		y += Math.sin(Math.toRadians(direction)) * speed;
	}

	/** Wraps bullet around screen if it exceeds game window boundaries
	*/
	public void screenWrap() {
		if (x > Asteroids.CANVAS_WIDTH - getImage().getWidth() / 2) {x = 0 - getImage().getWidth() / 2;} // Right wrap
		if (x < 0 - getImage().getWidth()) {x = Asteroids.CANVAS_WIDTH - getImage().getWidth();} // Left wrap
		if (y > Asteroids.CANVAS_HEIGHT - getImage().getHeight() / 2) {y = 0 - getImage().getHeight() / 2;} // Bottom wrap
		if (y < 0 - getImage().getHeight()) {y = Asteroids.CANVAS_HEIGHT - getImage().getHeight();} // Top wrap
	}
	
	/** Destroys object
	*/
	public void destroy() {
		Asteroids.gc.removeAsteroid(this); // Remove from controller
		MediumAsteroid[] newAsteroids = {new MediumAsteroid(x, y), new MediumAsteroid(x, y)}; // Create two new medium asteroids at current location
		Asteroids.gc.addAsteroids(newAsteroids); // Pass to game controller
	}

	/** Returns the radius of collision mask
	* @return Radius of collision mask
	*/
	public double getMaskRadius() {
		return (double) getImage().getWidth() / 2; // Half of image width
	}

	/** Returns center of object
	* @return double array representing object center
	*/
	public double[] getCenter() {
		double[] center = new double[2]; // Array to hold center
		center[0] = x + (getImage().getWidth() / 2); // Center is based on center of image
		center[1] = y + (getImage().getHeight() / 2);
		return center;
	}

	/** Compares this object to another
	* @param another Object to be compared
	* @return true if objects are equal
	*/
	public boolean equals(Object another) {
		if (another == null) {return false;}
		if (another.getClass() != this.getClass()) {return false;}
		BigAsteroid other = (BigAsteroid) another;
		return other.x() == x &&
			   other.y() == y &&
			   other.getSpeed() == speed &&
			   other.getDirection() == direction;
	}

	public String toString() {
		return "Asteroid at (" + (int) x + "," + (int) y + ") traveling at heading " + (int) direction + " at a speed of " + (int) speed;
	}
}