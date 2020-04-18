package application;

/**
 * Keeps track of the internal game state
 * 
 * @author Michael Gira
 *
 */
public class Game {

	static final int HEIGHT = 4;
	static final int WIDTH = 4;

	private GameSquare[][] board = new GameSquare[HEIGHT][WIDTH];

	public Game() {
		board[3][3] = new GameSquare();
	}

	/**
	 * Slide all the squares in a particular direction, possibly combining similar
	 * squares
	 * 
	 * @param direction Direction to slide
	 */
	public void slide(Direction direction) {
		/** @TODO */
	}

	/**
	 * Place a square on a particular side of the playing board
	 * 
	 * @param direction Direction to place the square
	 */
	public void placeSquare(Direction direction) {
		/** @TODO */
	}

	/**
	 * Indicates whether the game is over and the player can no longer move
	 * 
	 * @return Whether the game is over
	 */
	public boolean isGameOver() {
		/** @TODO */
		return false;
	}

	/**
	 * Get the player's current score
	 * 
	 * @return Current score
	 */
	public int getScore() {
		/** @TODO */
		return -1;
	}

	/**
	 * Get the current game tile state
	 * 
	 * @return A 2D array of the game state
	 */
	public GameSquare[][] getBoard() {
		return board;
	}

}
