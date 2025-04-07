import javax.swing.JPanel;

import java.awt.Color;

public class GamePanel extends JPanel {
	private int width,
				height;

	public GamePanel(int width, int height, GameController gc) {
		this.width = width;
		this.height = height;	
	}

}