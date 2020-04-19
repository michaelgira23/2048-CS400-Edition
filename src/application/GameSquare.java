package application;

import java.util.Random;

/**
 * A single tile on the game board initialized with a value of 2
 * 
 * @author Michael Gira
 *
 */
public class GameSquare {

	private int value = 2;
	private int posX;
	private int posY;
	private boolean combined = false;

	public GameSquare() {
		Random ran = new Random();
		posX = ran.nextInt(Game.WIDTH + 1);
		posY = ran.nextInt(Game.HEIGHT + 1);
	}

	/**
	 * Double the game square's value (upon combining with another square)
	 * 
	 * @return The square's new value
	 */
	public int increment() {
		value *= 2;
		return value;
	}

	/**
	 * Get the square's current value
	 * 
	 * @return The square's current value
	 */
	public int getValue() {
		return value;
	}

	public void setPos(int x, int y) {
		posX = x;
		posY = y;
	}

	public int getX() {
		return posX;
	}

	public int getY() {
		return posY;
	}

	public boolean getComb() {
		return combined;
	}

	public void setComb(boolean status) {
		combined = status;
	}
}
