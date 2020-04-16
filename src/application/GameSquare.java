package application;

/**
 * A single tile on the game board
 * 
 * @author Michael Gira
 *
 */
public class GameSquare {
	int value = 1;
	int posX;
	int posY;

	public GameSquare(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}
}
