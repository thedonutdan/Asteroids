import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Timer;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.lang.Math;

import javax.swing.SwingUtilities;

public class Alien extends GamePiece {
	private double x, y;

	private Timer shootTimer;

	private static final double SPEED = 3.0;

	public Alien(double x, double y) {
		this.x = x;
		this.y = y;

		setAnimationImage(Asteroids.gc.getAlienAnimation());

		ActionListener shooter = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						shoot();
					}
				});
				shoot();
			}
		};

		shootTimer = new Timer(5000, shooter);
		shootTimer.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.drawImage(getAnimationImage(), (int) x, (int) y, this);
	}

	public double[] getCenter() {
		double[] center = new double[2];
		center[0] = x + getAnimationImage().getWidth(this) / 2;
		center[1] = y + getAnimationImage().getHeight(this) / 2;
		return center;
	}

	public double getMaskRadius() {
		return getAnimationImage().getWidth(this) / 2;
	}

	public void destroy() {
		shootTimer.stop();
		Asteroids.gc.removeAlien(this);
	}

	public void move() {
		x += SPEED;
	}

	public void tick() {
		move();
		screenWrap();
	}

	/** Wraps bullet around screen if it exceeds game window boundaries
	*/
	public void screenWrap() {
		if (x > Asteroids.CANVAS_WIDTH - getAnimationImage().getWidth(this) / 2) {x = 0 - getAnimationImage().getWidth(this) / 2;} // Right wrap
		if (x < 0 - getAnimationImage().getWidth(this)) {x = Asteroids.CANVAS_WIDTH - getAnimationImage().getWidth(this);} // Left wrap
		if (y > Asteroids.CANVAS_HEIGHT - getAnimationImage().getHeight(this) / 2) {y = 0 - getAnimationImage().getHeight(this) / 2;} // Bottom wrap
		if (y < 0 - getAnimationImage().getHeight(this)) {y = Asteroids.CANVAS_HEIGHT - getAnimationImage().getHeight(this);} // Top wrap
	}

	public boolean isEnabled() {
		return true;
	}

	public void shoot() {
		double[] playerCenter = Asteroids.gc.getPlayer().getCenter();
		double[] distance = {playerCenter[0] - getCenter()[0], playerCenter[1] - getCenter()[1]};
		double direction = Math.toDegrees(Math.atan(distance[1] / distance[0]));
		double gunX = (x + (getAnimationImage().getWidth(this) / 2)) + Math.cos(Math.toRadians(direction)) * 22;
		double gunY = (y + (getAnimationImage().getHeight(this) / 2)) + Math.sin(Math.toRadians(direction)) * 22;
		Bullet bullet = new Bullet(gunX, gunY, direction, 5, false);
		Asteroids.gc.addAlienBullet(bullet);
	}
}