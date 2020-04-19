package application;

/**
 * A single tile on the game board initialized with a value of 1
 * 
 * @author Michael Gira
 *
 */
public class GameSquare {
	private int value = 1;
	private int posX;
	private int posY;
	private boolean combined;

	public GameSquare() {
		value = 1;
		Random ran = new Random();
		posX = ran.nextInt(5);
		posY = ran.nextInt(5);
		combined = false;

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
