package application;

import java.util.Objects;

/**
 * Represents a single tile (with a numbered value) on the game board
 * 
 * @author Hanyuan Wu, Michael Gira, Faith Isaac
 *
 */
public class GameSquare {

	private int value;

	/**
	 * Initialize a new square with a given value
	 * 
	 * @param value
	 */
	public GameSquare(int value) {
		this.value = value;
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

	/**
	 * Set the square's current value
	 *
	 * @return The square's current value
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * Returns whether two game squares have the same value
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		GameSquare that = (GameSquare) o;
		return value == that.value;
	}

	/**
	 * Use the hash code of the square's value
	 */
	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

}
