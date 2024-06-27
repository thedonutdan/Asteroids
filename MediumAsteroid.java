/** MediumAsteroid.java - A class to represent a medium asteroid
* This class features inheritence by extending BigAsteroid by requirement a. in
* project specification
* @author Daniel Andrews
* @version Final Project
*/
public class MediumAsteroid extends BigAsteroid {
	/** Full constructor sets location, speed and image
	 * @param x x-coordinate of asteroid location
	 * @param y y-coordinate of asteroid location
	*/
	public MediumAsteroid(double x, double y) {
		super(); // Call to super for direction and image rotation
		xSet(x); // Set coordinates and speed
		ySet(y);
		setSpeed(2.0);
		setImage(Asteroids.gc.getMediumAsteroidImage()); // Retrieve image sprit from controller
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
		Asteroids.gc.removeAsteroid(this); // Remove from game controller
		SmallAsteroid[] newAsteroids = {new SmallAsteroid(x(), y()), new SmallAsteroid(x(), y()), new SmallAsteroid(x(), y())}; // Create three small asteroids in place
		Asteroids.gc.addAsteroids(newAsteroids); // Pass small asteroids to controller
	}
}