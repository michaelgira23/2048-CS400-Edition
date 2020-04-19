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
	// Functional interface called
	private SlideHandler slideHandler = null;

	public Game() {
		board[2][3] = new GameSquare();
		board[2][2] = new GameSquare();
		board[1][2] = new GameSquare();
		board[1][2].increment();
		board[1][2].increment();
		board[1][2].increment();
		board[1][2].increment();
	}

	/**
	 * Set the handler for triggering animations once they have
	 * 
	 * @param handler Handler which will receive all the new slide events
	 */
	public void setSlideHandler(SlideHandler handler) {
		slideHandler = handler;
	}

	/**
	 * Slide all the squares in a particular direction, possibly combining similar
	 * squares
	 * 
	 * @param direction Direction to slide
	 */
	public void slide(Direction direction) {
		/** @TODO */

		// Mimic slide events for now
		SlideEvent[] slides = { new SlideEvent(board[2][3], true, 2, 3, 2, 0),
				new SlideEvent(board[2][2], false, 2, 2, 2, 0), new SlideEvent(board[1][2], false, 1, 2, 1, 0) };

		SlideDoneHandler doneHandler = () -> {
			System.out.println("All done!");
			/** @TODO Actually update internal game state to reflect new slide */
		};

		if (slideHandler == null) {
			doneHandler.done();
		} else {
			slideHandler.handle(slides, doneHandler);
		}
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
