package application;

import java.util.Objects;

/**
 * A single tile on the game board initialized with a value of 1
 * 
 * @author Hanyuan Wu, Michael Gira, Faith
 *
 */
public class GameSquare {
	private int value;

	public GameSquare(int value){
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		GameSquare that = (GameSquare) o;
		return value == that.value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
