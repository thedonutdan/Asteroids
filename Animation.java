import javax.swing.JComponent;

import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class Animation extends JComponent {
	Image image;
	double x,
		   y;

	public Animation(double x, double y, String animationType) {
		this.x = x;
		this.y = y;
		if (animationType.equals("player")) {
			image = Asteroids.gc.getPlayerDestroyedImage();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.drawImage(image, (int) x, (int) y, this);
	}

	public String toString() {
		return "animation";
	}
}