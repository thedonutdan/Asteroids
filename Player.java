import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.SwingUtilities;

import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.lang.Math;
/** Player.java - A ship for the player to manipulate during game
* This class features inheritence as it inherits from GamePiece which is a requirement
* per project specification a. 
* @author Daniel Andrews
* @author Final Project
*/
public class Player extends GamePiece{
	private Image destroyedImage; // Ship explosion animation

	private double x, // Store ship's x and y coordinates 
				   y;

	private double direction, // Store ship's kinetic direction and ship's physical orientation
				imageDirection; 

	private double turningSpeed, // Ship's ability to turn and move
				   movingSpeed;

	private double speedCap, // Ship's speed cap, friction to slow down, and thrust acceleration
				   acceleration,
				   friction,
				   thrustSpeed;

	private boolean turningLeft, // Store ship's states
					turningRight,
					moving,
					reloaded,
					enabled = true,
					invulnerable,
					flashing,
					destroyed;

	private Action leftPressed, // Actions to register keyboard input
				   leftReleased,
				   rightPressed,
				   rightReleased,
				   upPressed,
				   upReleased,
				   spacePressed,
				   spaceReleased;

	private double momentumX,
				   momentumY,
				   accelX,
				   accelY,
				   speed;
	// Action listener to control ship's invulnerable animation
	private ActionListener flash = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						flashing = !flashing; // Alternate ship flashes
					}
				};

	private Timer flashTimer = new Timer(250, flash); // Timer to activate flash event

	/** Constructor takes player's x and y coordinates in the game window
	* @param x Player x coordinate
	* @param y Player y coordinate
	*/
	public Player(double x, double y) {
		this.x = x; // Set x and y
		this.y = y;
		imageDirection = 0; // Set ship attributes
		direction = 360;
		turningSpeed = 10.0;
		movingSpeed = 0.0;
		speedCap = 13.0;
		acceleration = 1.0;
		friction = 1.05;
		thrustSpeed = 0;
		speed = 0;

		momentumX = 0;
		momentumY = 0;
		accelX = 0;
		accelY = 0;

		// Retrieve player sprites from controller
		setImage(Asteroids.gc.getPlayerImage());
		setRotatedImage(getImage());
		destroyedImage = Asteroids.gc.getPlayerDestroyedImage();

		// Set all states
		turningLeft = false;
		turningRight = false;
		moving = false;
		reloaded = true; // Reloaded indicates if ship can shoot
		destroyed = false;

		// Create Action objects to listen for keyboard input
		leftPressed = new LeftPressed();
		leftReleased = new LeftReleased();
		rightPressed = new RightPressed();
		rightReleased = new RightReleased();
		upPressed = new UpPressed();
		upReleased = new UpReleased();
		spacePressed = new SpacePressed();
		spaceReleased = new SpaceReleased();

		// Map keyboard input to corresponding action
		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "leftPressed");
		this.getActionMap().put("leftPressed", leftPressed);
		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "leftReleased");
		this.getActionMap().put("leftReleased", leftReleased);
		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "rightPressed");
		this.getActionMap().put("rightPressed", rightPressed);
		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "rightReleased");
		this.getActionMap().put("rightReleased", rightReleased);
		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "upPressed");
		this.getActionMap().put("upPressed", upPressed);
		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "upReleased");
		this.getActionMap().put("upReleased", upReleased);
		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "spacePressed");
		this.getActionMap().put("spacePressed", spacePressed);
		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), "spaceReleased");
		this.getActionMap().put("spaceReleased", spaceReleased);
		
		// Request focus at start of next frame
		SwingUtilities.invokeLater( new Runnable() { 
			public void run() { 
    	   		requestFocusInWindow(); // Request focus in window to ensure key input is received
    		} 
		});

		setInvulnerable(); // Player starts game out invulnerable
	}

	/** Paints component on graphics canvas
	* @param g Canvas to paint on
	*/
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // Call super paintComponent
		Graphics2D g2 = (Graphics2D) g; // Convert canvas to 2d graphics

		// If player is enabled, draw ship
		if (enabled) {
			if (!invulnerable) {
				g2.drawImage(getRotatedImage(),(int) x,(int) y, this); // Draw solid ship
			} else if (invulnerable && flashing) {
				g2.drawImage(getRotatedImage(), (int) x, (int) y, this); // Draw flashing ship
			}
		} else if (!destroyed) {
			// Play explosion animation after player dies
			g2.drawImage(destroyedImage, (int) x, (int) y, this); 
		}
	}

	/** Set invulnerability for two seconds
	*/
	public void setInvulnerable() {
		invulnerable = true; // Ship cannot be harmed
		flashing = true; // Animate flashing ship
		ActionListener delay = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				invulnerable = false; // Player is no longer invulnerable
			}
		};
		Timer timer = new Timer(2000, delay); // Player is invulnerable for two second duration
		timer.setRepeats(false); // Timer should stop after one execution
		timer.start(); // Start invulnerability
		flashTimer.start(); // Start flash animation 
	}

	/** Update player each frame
	*/
	public void tick() {
		// Ensure player is not dead or between spawns
		if (enabled) {
			// Turn image left
			if (turningLeft) {
				imageDirection -= turningSpeed;
				rotateImage();
			}
			// Turn image right
			if (turningRight) {
				imageDirection += turningSpeed;
				rotateImage();
			}
			
			if (moving) {
				// Calculate acceleration vector if player is applying thrust
				// Acceleration is calculated based on angle of ship
				accelX = acceleration * Math.cos(Math.toRadians(imageDirection));
				accelY = acceleration * Math.sin(Math.toRadians(imageDirection)) * -1; // y value is inverted from standard trigonometric function
				// Apply acceleration vector to momentum vector
				momentumX += accelX;
				momentumY -= accelY;
				// Calculate speed based on magnitude of momentum vector
				speed = Math.sqrt(Math.pow(momentumX, 2) + Math.pow(momentumY, 2));
				// Check that speed has not exceeded maximum
				if (speed > speedCap) {
					double scale = speedCap / speed; // Calculate amount to scale speed
					momentumX *= scale; // Scale momentum vector
					momentumY *= scale;
				}
			} else {
				momentumX /= friction; // If player is not applying thrust, apply friction to momentum vector
				momentumY /= friction;
			}
			move(); // Move ship, wrapping around screen if outside boundaries
			screenWrap();
		}

		repaint(); // Update ship on game canvas
	}
	/** Wrap ship around screen if window boundaries are exceeded
	*/
	public void screenWrap() {
		if (x > Asteroids.CANVAS_WIDTH - getImage().getWidth() / 2) {x = 0 - getImage().getWidth() / 2;} // Wrap right side
		if (x < 0 - getImage().getWidth()) {x = Asteroids.CANVAS_WIDTH - getImage().getWidth();} // Wrap left side
		if (y > Asteroids.CANVAS_HEIGHT - getImage().getHeight() / 2) {y = 0 - getImage().getHeight() / 2;} // Wrap bottom
		if (y < 0 - getImage().getHeight()) {y = Asteroids.CANVAS_HEIGHT - getImage().getHeight();} // Wrap top
	}
	
	/** Move ship based on momentum vector
	*/
	public void move() {
		x += momentumX; // Change x and y position based on change in position from momentum vector
		y += momentumY;
	}

	/** Creat a bullet that travels in direction ship is currently facing
	*/
	public void shoot() {
		double gunX = (x + (getImage().getWidth() / 2)) + Math.cos(Math.toRadians(imageDirection)) * 22; // Calculate where bullet should originate based on
		double gunY = (y + (getImage().getHeight() / 2)) + Math.sin(Math.toRadians(imageDirection)) * 22; // ship heading and position
		Bullet bullet = new Bullet(gunX, gunY, imageDirection, 15, true); // Create new bullet moving in direction ship is facing
		Asteroids.gc.addBullet(bullet); // Hand bullet off to controller
	}

	/** Rotate image based on ship heading
	*/
	public void rotateImage() {
		double rotationRequired = Math.toRadians(imageDirection); // Calculate rotation needed based on zero pointing to the right
		double locationX = getImage().getWidth() / 2; // Find center of image
		double locationY = getImage().getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, 
			locationX, locationY); // Use affine transform to create rotation
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR); // New transformation operation based on needed rotation
		BufferedImage newImage = new BufferedImage(getImage().getWidth(), getImage().getHeight(), 
			getImage().getType()); // Use original image to minimize distortion
		op.filter(getImage(), newImage); // Apply filter to image

		setRotatedImage(newImage); // Set image to be rendered
	}
	
	/** An inner class to respond to left arrow keyboard input (Pressing)
	*/
	public class LeftPressed extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (enabled) {
				turningRight = false; // If ship is enabled cancel any right turns and begin left turn
				turningLeft = true;
			}
		}
	}

	/** An inner class to respond to left arrow keyboard input (Releasing)
	*/
	public class LeftReleased extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			turningLeft = false; // Stop turning left upon key release
		}
	}

	/** An inner class to respond to right arrow keyboard input (Pressing)
	*/
	public class RightPressed extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (enabled) {
				turningLeft = false; // If ship is enabled cancel any left turns and being right turn
				turningRight = true;
			}
		}
	}

	/** An inner class to respond to right arrow keyboard input (Releasing)
	*/
	public class RightReleased extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			turningRight = false; // Stop turning right upon key release
		}
	}
	
	/** An inner class to respond to up arrow keyboard input (Pressing)
	*/
	public class UpPressed extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (enabled) 
				moving = true; // Begin moving if ship is enabled
		}
	}
	
	/** An inner class to respond to up arrow keyboard input (Releasing)
	*/
	public class UpReleased extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			moving = false; // Stop applying movement
		}
	}

	/** An inner class to respond to space keyboard input (Pressing)
	*/
	public class SpacePressed extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (reloaded && enabled) {shoot();} // Shoot if gun is reloaded and ship is enabled
			reloaded = false; // Disable gun
		}
	}

	/** An inner class to respond to space keyboard input (Releasing)
	*/
	public class SpaceReleased extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			reloaded = true; // Re-enabled gun
		}
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

	/** Returns radius of mask to be used in collision checking
	* @return Collision mask
	*/
	public double getMaskRadius() {
		return (double) getImage().getWidth() / 2 - 22;
	}

	/** Returns double array holding x and y coordinates of ship center
	* @return double array representing object center
	*/
	public double[] getCenter() {
		double[] center = new double[2]; // Create array
		center[0] = x + (getImage().getWidth() / 2); // Calculate center based on sprite dimensions
		center[1] = y + (getImage().getHeight() / 2);
		return center;
	}

	/** Destroys this object, disabling it permanently
	*/
	public void destroy() {
		enabled = false; // Disable object
		ActionListener destroy = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				destroyed = true; // Set object state to destroyed
			}
		};
		Timer delay = new Timer(1000, destroy); // Set timer to destroy after explosion animation
		delay.setRepeats(false);
		delay.start();
	}

	/** Returns if this object is in an enabled state
	* @return Returns true if object is in an enabled state
	*/
	public boolean isEnabled() {
		return enabled;
	}

	/** Returns if this object is invulnerable
	* @return Returns true if object is invulnerable
	*/
	public boolean isInvulnerable() {
		return invulnerable;
	}

	/** Temporarily disables object, respawning it at center of screen
	*/
	public void respawn() {
		enabled = false; // Disable
		momentumX = 0; // Stop any movement
		momentumY = 0;
		ActionListener delay = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				x = Asteroids.CANVAS_WIDTH / 2; // Reset position to center of screen
				y = Asteroids.CANVAS_HEIGHT / 2;
				
				turningLeft = false; // Reset state of object
				turningRight = false;
				moving = false;
				enabled = true;
				destroyedImage.flush(); // Reset image animation
				setInvulnerable(); // Set invulnerability to prevent chain of deaths
			}
		};
		Timer timer = new Timer(1000, delay); // Set timer allowing explosion animation to play
		timer.setRepeats(false);
		timer.start();
	}
}