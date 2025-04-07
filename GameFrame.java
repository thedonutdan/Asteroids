import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.Insets;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/** GameFrame.java - A class to generate game window and handle GUI
* @author Daniel Andrews
*
*/
public class GameFrame extends JFrame {
	private static final String TITLE = "Asteroids"; // Window title should always be "Asteroids"
	
	private JPanel gamePanel, // Declare game and menu panels and give scope to class
				   menuPanel;
	
	private JLabel scoreLabel, // Declare labels to display score, lives, menu title, and highscore
				   livesLabel,
				   menuTitleLabel,
				   menuScoreLabel;

	private ImageIcon startButtonImage, // Declare ImageIcons to be assigned to menu buttons
					  exitButtonImage;

	/** Constructor initializes JFrame attributes and retrieves images from game controller
	*/
	public GameFrame() {
		setSize(Asteroids.CANVAS_WIDTH, Asteroids.CANVAS_HEIGHT); // Set frame dimensions for game
		setTitle(TITLE); // Set game title
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set default operation upon close button
		setLocationRelativeTo(null); // Center on screen
		setResizable(false);
		startButtonImage = Asteroids.gc.getStartButtonImage(); // Initialize button sprites with
		exitButtonImage = Asteroids.gc.getExitButtonImage();   // corresponding button images
	}

	/** Setup window to display main menu
	* @param highscore Highscore to be displayed on main menu
	*/
	public void goToMenu(int highscore) {
		menuPanel = new JPanel(); // Initialize panel
		menuPanel.setLayout(new GridBagLayout()); // Use GridBagLayout to control element position
		menuPanel.setBackground(Color.BLACK); // Set background color to the empty black void of space

		GridBagConstraints constraints = new GridBagConstraints(); // Initialize constraints object for layout

		// Set constraints and attributes then add to menu panel
		constraints.gridx = 0; // Set component grid location in layout
		constraints.gridy = 1;
		constraints.insets = new Insets(10, 10, 0, 10); // Set buffer for component
		menuTitleLabel = new JLabel(); // Make new label for title
		menuTitleLabel.setForeground(Color.WHITE); // Set label to display in white
		menuTitleLabel.setFont(new Font("Courier", Font.PLAIN, 50)); // Set label font to courier for that sweet, retro feel
		menuTitleLabel.setText("ASTEROIDS"); // Set label text
		menuPanel.add(menuTitleLabel, constraints); // Add to label with defined constraints

		// Set constraints and attributes then add to menu panel
		constraints.gridx = 0; 
		constraints.gridy = 2;
		constraints.insets = new Insets(0, 10, 15, 10); 
		menuScoreLabel = new JLabel(); 
		menuScoreLabel.setForeground(Color.WHITE); 
		menuScoreLabel.setFont(new Font("Courier", Font.PLAIN, 14));
		menuScoreLabel.setText("High Score: " + highscore); 
		menuPanel.add(menuScoreLabel, constraints); 
		
		// Set constraints and attributes then add to menu panel
		constraints.gridx = 0; 
		constraints.gridy = 3;
		constraints.insets = new Insets(10, 10, 25, 10);
		JButton startButton = new JButton(startButtonImage); // JButton to start game using preloaded image
		// Set action to be performed upon click
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Asteroids.gc.startGame(); // Tell controller to begin game
			}
		});
		// Set default JButton graphic invisible
		startButton.setOpaque(false);
		startButton.setContentAreaFilled(false);
		startButton.setBorderPainted(false);
		startButton.setFocusPainted(false);
		menuPanel.add(startButton, constraints);

		// Set constraints and attributes then add to menu panel
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.insets = new Insets(10, 10, 15, 10);
		JButton exitButton = new JButton(exitButtonImage);
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Asteroids.gc.exitGame(); // Tell controller to begin closing procedures
			}
		});
		exitButton.setOpaque(false);
		exitButton.setContentAreaFilled(false);
		exitButton.setBorderPainted(false);
		exitButton.setFocusPainted(false);
		menuPanel.add(exitButton, constraints);
		
		this.add(menuPanel); // Add completed menu panel to frame
		setVisible(true); // Display completed frame
	}

	/** Setup window to display game 
	*/
	public void startGame() {
		gamePanel = new JPanel(); // Initialize JPanel to render game objects
		gamePanel.setBounds(0, 0, Asteroids.CANVAS_WIDTH, Asteroids.CANVAS_HEIGHT); // Set size to the same as window
		gamePanel.setLayout(new BorderLayout()); // Use basic border layout
		gamePanel.setBackground(Color.BLACK); // Set background color

		this.add(gamePanel); // Add game panel to window
		setVisible(true); // Display soulless black void

		// Setup label to display current score
		scoreLabel = new JLabel("Score: 0"); // Score should start at zero
		scoreLabel.setBounds(0, 0, 100, 10); // Set to top left of screen, small and out of the way
		scoreLabel.setForeground(Color.WHITE); // Set color to white
		scoreLabel.setFont(new Font("Courier", Font.PLAIN, 14)); // Set font
		gamePanel.add(scoreLabel); // Add to game panel

		// Setup label to display current lives
		livesLabel = new JLabel("Lives: 3"); // Lives should start at three
		livesLabel.setBounds(0, 15, 100, 10); // Set to display under score label
		livesLabel.setForeground(Color.WHITE);
		livesLabel.setFont(new Font("Courier", Font.PLAIN, 14));
		gamePanel.add(livesLabel);
	}

	/** Adds a component to be drawn on the game panel
	* This method uses Polymorphism to allow all game objects to be handled by 
	* one method as they are all descendents of JComponent
	* @param components An array of components to be added one by one to panel
	*/
	public void addToPanel(JComponent[] components) {
		for (JComponent component : components) {
			gamePanel.add(component); // Add each component to game panel
			gamePanel.validate(); // Validate new panel state after each new component is added
		}
	}

	/** Removes a component from the game panel
	* This method uses Polymorphism to allow all game objects to be handled by 
	* one method as they are all descendents of JComponent
	* @param component Component to be removed from panel
	*/
	public void removeFromPanel(JComponent component) {
		gamePanel.remove(component); // Remove component from game panel
		gamePanel.revalidate(); // Validate new panel state after removal
	}

	/** Updates score label in game
	* @param score New score to be displayed 
	*/
	public void updateScore(int score) {
		scoreLabel.setText("Score: " + score); // Set score label text to display new score
		gamePanel.revalidate(); // Validate new panel state
	}

	/** Updates lives label in game
	* @param lives New lives to be displayed
	*/
	public void updateLives(int lives) {
		livesLabel.setText("Lives: " + lives); // Set lives label text to display new lives
		gamePanel.revalidate(); // Validate new panel state
	}

	/** Updates game panel to show new state
	*/
	public void tick() {
		gamePanel.repaint(); // Panel will update position of all game pieces
	}

	/** Purges game window to prevent unexpected behavior
	*/
	public void clearGame() {
		this.remove(gamePanel); // Remove game panel
	}

	/** Purges game window to prevent unexpected behavior
	*/
	public void clearMenu() {
		this.remove(menuPanel); // Remove menu panel
	}
}