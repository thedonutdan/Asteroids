import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.ImageIcon;

import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Color;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.Scanner;

import java.lang.Math;
/** GameController.java - A class to control flow of asteroids game
* This class features PolyMorphism in storing parent and child classes in same arraylists 
* and calling inherited methods per requirement b in project specifications
* this class also features text file I/O to retrieve and record high scores per requirement
* c. in project specifications
* 
* @author Daniel Andrews
* @version Final Project
*/
public class GameController {
	BufferedImage playerImage, // Declare variables to hold game sprites
				  bulletImage,
				  bigAsteroidImage,
				  mediumAsteroidImage,
				  smallAsteroidImage;
	Image playerDestroyedImage, // Declare variables to hold game sprite animations
		  asteroidDestroyedImage,
		  alienAnimation;
	ImageIcon startButtonImage, // Declare variables to hold menu button images
			  exitButtonImage;

	private GameFrame gameFrame; // Declare variable to give Controller access to frame
	private ArrayList<Bullet> bullets; // Declare arraylists to hold game elements
	private ArrayList<BigAsteroid> asteroids;
	private ArrayList<Alien> aliens;
	private ArrayList<Bullet> alienBullets;
	private ArrayList<GamePiece> cleanup; // Arraylist to hold items to be removed from game after frame
										  // cleanup uses Polymorphism as all game objects are descendants
										  // of GamePiece and can be treated as the same data type
	private ArrayList<BigAsteroid> newAsteroids; // Arraylist to hold items to be added to game after frame
												 // newAsteroids uses Polymorphism as all asteroids are descendants
										  		 // of BigAsteroid and can be treated as the same data type
	private Player player; // Player object

	private int score, // Declare variables to hold current score, lives, and highscore
				lives,
				highscore;

	private Timer gameTimer; // Declare timer to control game objects

	private boolean gameOver = false; // Game is not over yet

	private javax.swing.Timer asteroidTimer; // Swing timer to periodically add new asteroids
	javax.swing.Timer alienTimer;

	/** Constructor initializes arraylists to hold game objects, as well as score and lives
	*/
	public GameController() {
		bullets = new ArrayList<Bullet>(); // On-screen bullets
		asteroids = new ArrayList<BigAsteroid>(); // On-screen asteroids
		aliens = new ArrayList<Alien>(); // On-screen aliens
		alienBullets = new ArrayList<Bullet>();
		newAsteroids = new ArrayList<BigAsteroid>(); // Asteroids to be added to screen
		cleanup = new ArrayList<GamePiece>(); // Game elements to be removed from screen
		score = 0; // Score should start at zero
		lives = 3; // Lives should start at three
	}

	/** Load images for game object sprites
	* Throws an IOException if any images are absent
	*/
	public void loadImages() throws IOException {
		playerImage = ImageIO.read(new File("images/playertransparent.png"));
		bulletImage = ImageIO.read(new File("images/bullet.png"));
		bigAsteroidImage = ImageIO.read(new File("images/bigasteroidtransparent.png"));
		mediumAsteroidImage = ImageIO.read(new File("images/mediumasteroidtransparent.png"));
		smallAsteroidImage = ImageIO.read(new File("images/smallasteroidtransparent.png"));
		startButtonImage = new ImageIcon("images/startbuttonimage.png");
		exitButtonImage = new ImageIcon("images/exitbuttonimage.png");
		playerDestroyedImage = new ImageIcon("images/explosion.gif").getImage();
		asteroidDestroyedImage = new ImageIcon("images/asteroiddestroy.gif").getImage();
		alienAnimation = new ImageIcon("images/alienanimation.gif").getImage();
	}

	/** Load current highscore from file
	* Throws IOException if no file is present
	* This method implements text file I/O
	* Known exploit: Text file is unsecure and easy to edit
	*/
	public void loadHighscore() throws IOException {
		Scanner in = new Scanner(new File("highscore.txt")); // Scanner to read text file
		if (in.hasNextInt()) {
			highscore = in.nextInt(); // Set highscore to that in file
		} else {
			highscore = 0; // If file is empty, easy mode
		}
		in.close(); // Close input file when done
	}

	/** Return current highscore
	* @return Highscore 
	*/
	public int getHighscore() {
		return highscore;
	}

	/** Returns player sprite
	* @return Player sprite
	*/
	public BufferedImage getPlayerImage() {
		return playerImage;
	}

	/** Returns player destroyed animation
	* @return Player destroyed animation
	*/
	public Image getPlayerDestroyedImage() {
		return playerDestroyedImage;
	}

	/** Returns bullet sprite
	* @return Bullet sprite
	*/
	public BufferedImage getBulletImage() {
		return bulletImage;
	}

	/** Returns big asteroid sprite
	* @return Big asteroid sprite
	*/
	public BufferedImage getBigAsteroidImage() {
		return bigAsteroidImage;
	}

	/** Returns medium asteroid sprite
	* @return Medium asteroid sprite
	*/
	public BufferedImage getMediumAsteroidImage() {
		return mediumAsteroidImage;
	}

	/** Returns small asteroid sprite
	* @return Small asteroid sprite
	*/
	public BufferedImage getSmallAsteroidImage() {
		return smallAsteroidImage;
	}

	/** Returns asteroid destroy animation
	* @return Asteroid destroy animation
	*/
	public Image getAsteroidDestroyedImage() {
		return asteroidDestroyedImage;
	}

	/** Returns alien spaceship animation sprite
	* @return Alien spaceship animation
	*/
	public Image getAlienAnimation() {
		return alienAnimation;
	}

	/** Returns start button image
	* @return Start button image
	*/
	public ImageIcon getStartButtonImage() {
		return startButtonImage;
	}

	/** Returns exit button image
	* @return Exit button image
	*/
	public ImageIcon getExitButtonImage() {
		return exitButtonImage;
	}

	/** Gives game controller access to game frame
	* @param gameFrame Game frame generating game window
	*/
	public void addGameFrame(GameFrame gameFrame) {
		this.gameFrame = gameFrame;
	}

	/** Add a bullet to game when player shoots
	* @param bullet Bullet to be added
	*/
	public void addBullet(Bullet bullet) {
		if (bullets.size() < 10) {
			bullets.add(bullet); // Limit number of bullets on screen to less than ten
			Bullet[] newBullet = {bullet}; // Create an array to use game frame's add method
			gameFrame.addToPanel(newBullet); // Add to game window
		}
	}

	/** Add an alien bullet to game when alien shoots
	* @param bullet Bullet to be added
	*/
	public void addAlienBullet(Bullet bullet) {
		alienBullets.add(bullet);
		Bullet[] newBullet = {bullet}; // Create an array to use game frame's add method
		gameFrame.addToPanel(newBullet); // Add to game window
	}

	/** Add an alien spaceship to the game
	* @param alien Bullet to be added
	*/
	public void addAlien(Alien alien) {
		aliens.add(alien);
		Alien[] newAlien = {alien}; // Create an array to use game frame's add method
		gameFrame.addToPanel(newAlien); // Add to game window		
	}

	/** Remove a bullet from game when it is destroyed
	* @param bullet Bullet to be removed
	*/
	public void removeBullet(Bullet bullet) {
		cleanup.add(bullet); // Add bullet to cleanup list
		gameFrame.removeFromPanel(bullet); // Remove bullet from game window
	}

	/** Remove an asteroid from the game when it is destroyed
	* This method takes advantage of polymorphism to remove big, medium, and small asteroids
	* as both medium and small asteroids inherit from big asteroid class so can be treated
	* as the same data type
	* @param asteroid Asteroid to be removed
	*/
	public void removeAsteroid(BigAsteroid asteroid) {
		cleanup.add(asteroid); // Add bullet to cleanup list
		gameFrame.removeFromPanel(asteroid); // Remove bullet from game window
	}

	/** Add multiple asteroids to game
	* This method takes advantage of polymorphism to add big, medium, and small asteroids
	* as both medium and small asteroids inherit from big asteroid class so can be treated
	* as the same data type
	* @param asteroidsToAdd Array of asterodis to be added
	*/
	public void addAsteroids(BigAsteroid[] asteroidsToAdd) {
		for (BigAsteroid asteroid : asteroidsToAdd) {
			// Add each new asteroid to asteroids waiting to be added next frame
			newAsteroids.add(asteroid); 
		}
		gameFrame.addToPanel(asteroidsToAdd); // Add new asteroids to game window
	}

	/** Add player to game window
	* @param player Player object to be added
	*/
	public void addPlayer(Player player) {
		this.player = player; // Set instance variable to player
		Player[] playerArray = {player}; // Create an array to use game frame add method
		gameFrame.addToPanel(playerArray); // Add player to panel
	}

	/** Remove a player from the screen when ship is destroyed
	*/
	public void removePlayer() {
		gameFrame.removeFromPanel(player); // Remove player from game window
	}

	public void removeAlien(Alien alien) {
		cleanup.add(alien);
		gameFrame.removeFromPanel(alien);
	}

	public Player getPlayer() {
		return player;
	}

	/** Start timer to control game from frame to frame
	*/
	public void startGameTimer() {
		gameTimer = new Timer(); // Create new timer and task
		TimerTask tick = new TimerTask() { 
			@Override
			public void run() {
				// Ensure changes are made at start of next frame
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						player.tick(); // Update player
						for (Bullet alienBullet : alienBullets) {
							alienBullet.tick();
							if (alienBullet.isCollidingWith(player)) {
								// Act only if both are enabled and player is vulnerable
								if (alienBullet.isEnabled() && !player.isInvulnerable() && player.isEnabled()) {
									alienBullet.destroy(); // Bullet is always destroyed
									
									// Decrement lives and check if any remain
									if (--lives > 0) {
										player.respawn(); // Respawn if lives remain
									} else {
										player.destroy(); // Destroy if out of lives
										endGame(); // Start game ending procedures
									}
									gameFrame.updateLives(lives); // Update HUD
								}
							}
						}
						for (Bullet bullet : bullets) {
							bullet.tick(); // Update each bullet
							for (BigAsteroid asteroid : asteroids) {
								// Check collisions between all bullets and asteroids
								if (bullet.isCollidingWith(asteroid)) {
									// Act only if bullet is enabled
									if (bullet.isEnabled()) {
										bullet.destroy(); // Collsions between two enabled objects of different types always destroy both
										asteroid.destroy();
										if (!gameOver) {
											gameFrame.updateScore(++score); // Only update score if game is active
										}
									}
								}
							}
							for (Alien alien : aliens) {
								// Check collisions between all bullets and aliens
								if (bullet.isCollidingWith(alien)) {
									// Act only if bullet is enabled
									if (bullet.isEnabled() && alien.isEnabled()) {
										bullet.destroy(); // Collsions between two enabled objects of different types always destroy both
										alien.destroy();
										if (!gameOver) {
											score += 5;
											gameFrame.updateScore(score); // Only update score if game is active
										}
									}
								}
							}
							// Check if player has shot themself
							if (bullet.isCollidingWith(player)) {
									// Act only if both are enabled and player is vulnerable
									if (bullet.isEnabled() && !player.isInvulnerable() && player.isEnabled()) {
										bullet.destroy(); // Bullet is always destroyed
										
										// Decrement lives and check if any remain
										if (--lives > 0) {
											player.respawn(); // Respawn if lives remain
										} else {
											player.destroy(); // Destroy if out of lives
											endGame(); // Start game ending procedures
										}
										gameFrame.updateLives(lives); // Update HUD
									}
								}
						}
						for (BigAsteroid asteroid : asteroids) {
							asteroid.tick(); // Update each asteroid
							// Check all asteroid/player collisions
							if (asteroid.isCollidingWith(player)) {
								// Act only if both are enabled and player is vulnerable
								if (player.isEnabled() && !player.isInvulnerable() && asteroid.isEnabled()){
									asteroid.destroy(); // Asteroid is always destroyed

									// Decrement lives and check if any remain
									if (--lives > 0) {
										player.respawn(); // Respawn if lives remain
									} else {
										player.destroy(); // Destroy if player is a scrub
										endGame(); // Start game ending procedures
									}
									gameFrame.updateLives(lives); // Update HUD
								}
							}
						}

						for (Alien alien : aliens) {
							alien.tick();
							if (alien.isCollidingWith(player)) {
								if (player.isEnabled() && !player.isInvulnerable() && alien.isEnabled()) {
									alien.destroy();

									if (--lives > 0) {
										player.respawn();
									} else {
										player.destroy();
										endGame();
									}
									gameFrame.updateLives(lives);
								}
							}
						}
						// Cleanup all objects deactivated since last frame update
						for (GamePiece piece : cleanup) {
							bullets.remove(piece); // Remove all bullets
							asteroids.remove(piece); // Remove all asteroids
							aliens.remove(piece);
							alienBullets.remove(piece);
						}
						// Add all new asteroids created since last frame update
						for (BigAsteroid asteroid : newAsteroids) {
							asteroids.add(asteroid); // Add asteroids to active arraylist
						}
						cleanup.clear(); // Clear cleanup list after updating
						newAsteroids.clear(); // Clear new asteroid list to prevent duplication
					}
				});
			}
		};

		gameTimer.scheduleAtFixedRate(tick, 33, 33); // Set game timer to run at about thirty frames/second
	}

	/** Starts timer that adds new asteroids to game periodically
	*/
	public void startAsteroidTimer() {
		// Create listener to respond to timer pings
		ActionListener makeAsteroid = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Create new asteroid object to be added to game
				BigAsteroid asteroidToAdd = new BigAsteroid(Math.random() * Asteroids.CANVAS_WIDTH, Math.random() * Asteroids.CANVAS_HEIGHT);
				//Ensure asteroid will not spawn too close to player
				if (asteroidToAdd.distanceTo(player) > 100) {
					BigAsteroid[] toAdd = {asteroidToAdd}; // Add to array to work with method to add asteroids
					addAsteroids(toAdd); // Add asteroid to game
				}
			}
		};
		asteroidTimer = new javax.swing.Timer(10000, makeAsteroid); // Make asteroids about every ten seconds
		asteroidTimer.setRepeats(true); // Timer should repeat until game ends
		asteroidTimer.start(); // Start timer
	}

	public void startAlienTimer() {
		ActionListener makeAlien = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Alien newAlien = new Alien(100, Asteroids.CANVAS_HEIGHT / 2 - 50);
				if (aliens.isEmpty()) {
					addAlien(new Alien(100, Asteroids.CANVAS_HEIGHT / 2 - 50));
				}
			}
		};

		alienTimer = new javax.swing.Timer(5000, makeAlien);
		alienTimer.setRepeats(true);
		alienTimer.start();
	}

	/** Tells window to set up game window and sets up all game pieces
	*/
	public void startGame() {
		lives = 3; // Reset lives and score
		score = 0;
		gameOver = false; // Not dead yet
		gameFrame.clearMenu(); // Tell window to flush all information from menu window
		gameFrame.startGame(); // Tell window to set up game window
		startGameTimer(); // Start game timer
		startAsteroidTimer(); // Start timer to add asteroids
		startAlienTimer();
		addPlayer(new Player(Asteroids.CANVAS_WIDTH / 2, Asteroids.CANVAS_HEIGHT / 2)); // Add player ship to game
		BigAsteroid[] startAsteroids = new BigAsteroid[3]; // Add three asteroids at random locations to start off
		for (int i = 0; i < 3; i++) {
			startAsteroids[i] = new BigAsteroid(Math.random() * Asteroids.CANVAS_WIDTH, Math.random() * Asteroids.CANVAS_HEIGHT);
		}
		addAsteroids(startAsteroids); // Add asteroids to game window
	}

	/** Execute ending game procedures
	*/
	public void endGame() {
		asteroidTimer.stop(); // Stop adding new asteroids
		alienTimer.stop();
		GameOverMessage[] message = {new GameOverMessage(Asteroids.CANVAS_WIDTH / 2, Asteroids.CANVAS_HEIGHT / 2, score)}; // Display game over message
		gameFrame.addToPanel(message); // Add message to game window
		// Return to menu after delay
		ActionListener close = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				returnToMenu(); // Return to menu
			}
		};
		javax.swing.Timer delay = new javax.swing.Timer(5000, close); // Timer to display game over message for five seconds to allow player to reflect upon what lead them to this moment
		delay.setRepeats(false); // Timer should not repeat
		delay.start(); // Start of delay
		gameOver = true; // Game is over
	}

	/** Tells game window to set up menu and updates highscore
	*/
	public void returnToMenu() {
		if (score > highscore) {
			highscore = score; // Update highscore if beat by player
		}
		gameTimer.cancel(); // Cancel timer updating every frame
		asteroids.clear();
		bullets.clear();
		aliens.clear();
		alienBullets.clear();
		gameFrame.clearGame(); // Clear the game window
		gameFrame.goToMenu(highscore); // Return to the menu, maybe with a new highscore
	}

	/** Execute exit game procedures
	* This method involves text-file output
	*/
	public void exitGame() {
		// Try writing highscore to output file
		try {
			PrintWriter out = new PrintWriter(new File("highscore.txt")); // Printwriter to record highscore
			out.println(highscore); 
			out.close(); // Close file output
		}
		catch (IOException e) {
			System.out.println("Error Saving Highscore: " + e.getMessage()); // Inform user of failure to record high scores
		}

		System.exit(0); // Close application
	}

}