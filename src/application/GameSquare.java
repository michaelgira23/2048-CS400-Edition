package application;

/**
 * A single tile on the game board initialized with a value of 2
 * 
 * @author Michael Gira
 *
 */
public class GameSquare {
	private int value = 2;

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
}
