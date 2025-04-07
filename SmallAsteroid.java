/** SmallAsteroid.java - A class to represent a small asteroid
* 
* @author Daniel Andrews
* 
*/
public class SmallAsteroid extends BigAsteroid {
	/** Full constructor sets coordinates, speed, and loads images
	* @param x x coordinate
	* @param y y coordinate
	*/
	public SmallAsteroid(double x, double y) {
		super(); // Call to super for direction and image rotation
		xSet(x); // Set coordinates and speed
		ySet(y);
		setSpeed(3.0);
		setImage(Asteroids.gc.getSmallAsteroidImage()); // Retrieve image sprit from controller
		setRotatedImage(getImage());
	}

	/** Return if object is enabled
	* @return true if object is enabled 
	*/
	public boolean isEnabled() {
		return true;
	}
	
	/** Destroys object
	*/
	public void destroy() {
		Asteroids.gc.removeAsteroid(this); // Remove this object from game controller
	}

	/** Returns the radius of collision mask
	* @return Radius of collision mask
	*/
	public double getMaskRadius() {
		return (double) getImage().getWidth() / 2 + 5; // Adjusted to work better in gameplay
	}
}