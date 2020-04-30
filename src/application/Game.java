package application;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.LinkedList;
import java.util.List;

/**
 * Keeps track of the internal game state
 * 
 * @author Hanyuan Wu, Michael Gira
 *
 */

public class Game {
	static final boolean DEBUG = true;

	static final int HEIGHT = 4;
	static final int WIDTH = 4;
	private Main main;
	private int numOfSquare;
	private int score = 0;
	private GameSquare[][] board;
	private Random rnd;
	private boolean isGameOver;
	private SlideHandler slideHandler = null;
	private boolean moved;

	public Game(int seed, Main main) {
		if(seed == 0) rnd = new Random();
		else rnd = new Random(seed);
		this.main = main;
		score = 0;
		numOfSquare = 0;
		isGameOver = false;
		board = new GameSquare[HEIGHT][WIDTH];
		squareGen();
		squareGen();
	}

	/**
	 * Set the handler for triggering animations once they have
	 *
	 * @param handler Handler which will receive all the new slide events
	 */
	public void setSlideHandler(SlideHandler handler) { slideHandler = handler; }

	private boolean squareGen() {
		// This should never happen! (Prepared for possible GUI bug)
		if(numOfSquare == HEIGHT*WIDTH) return false;
		int value = 2;
		if(rnd.nextBoolean()) {
			value = 4;
		}
		while(true){
			int x = rnd.nextInt(HEIGHT);
			int y = rnd.nextInt(WIDTH);
			if (board[x][y] == null){
				board[x][y] = new GameSquare(value);
				numOfSquare++;
				if(numOfSquare == HEIGHT*WIDTH) checkGameOver();
				return true;
			}
		}
	}

	/**
	 * Helper method checking if game is over based on whether there's any other option
	 * left to slide.
	 */
	private void checkGameOver(){
		isGameOver = true;
		for(int row = 0; row < HEIGHT; row++){
			for(int col = 0; col < WIDTH; col++){
				if(row < HEIGHT-1 && board[row][col].equals(board[row+1][col])) isGameOver = false;
				if(row > 0 && board[row][col].equals(board[row-1][col])) isGameOver = false;
				if(col < WIDTH-1 && board[row][col].equals(board[row][col+1])) isGameOver = false;
				if(col > 0 && board[row][col].equals(board[row][col-1])) isGameOver = false;
			}
		}
		// DEBUG
		if(isGameOver && DEBUG) main.gameOver();//System.out.println("Game Over!");
	}

	/**
	 * Slide all the squares in a particular direction, possibly combining similar
	 * squares
	 *
	 * @param direction Direction to slide
	 */
	public void slide(Direction direction) {
		// check if any square actually moved
		moved = false;
		List<SlideEvent> slides = new LinkedList<SlideEvent>();
		switch (direction) {
			case Up: slides = mergeUp();
				break;
			case Left: slides = mergeLeft();
				break;
			case Down: slides = mergeDown();
				break;
			case Right: slides = mergeRight();
				break;
		}
		if (slideHandler != null) slideHandler.handle(slides);
		if(moved) squareGen();
	}

	/**
	 * Helper method if board is shifted left
	 *
	 * @return List<SlideEvent> slide events to be executed
	 */ 
	private List<SlideEvent> mergeLeft() {
		List<SlideEvent> slides = new LinkedList<SlideEvent>();
		for(int row = 0; row < HEIGHT; row++){
			for(int col = 0; col < WIDTH; col++){
				if(board[row][col] == null) continue;
				GameSquare curSquare = board[row][col];
				int j;
				for(j = col-1; j >= 0 && board[row][j] == null; j--);
				board[row][col] = null;
				board[row][j+1] = curSquare;
				int newCol = j+1;
				if(newCol != col) moved = true;
				slides.add(new SlideEvent(curSquare, SlideEventAction.None, row, col, row, newCol));
				for(j = col+1; j < WIDTH && board[row][j] == null; j++);
				if(j == WIDTH) continue;
				if(board[row][j].equals(curSquare)){
					GameSquare combinedWith = board[row][j];
					slides.add(new SlideEvent(curSquare, SlideEventAction.CombineOver,
							row, newCol, row, newCol));
					slides.add(new SlideEvent(combinedWith, SlideEventAction.CombineUnder,
							row, j, row, newCol));
					curSquare.increment();
					score += curSquare.getValue();
					board[row][j] = null;
					moved = true;
					numOfSquare--;
				}
			}

		}
		return slides;
	}

	/**
	 * Helper method if board is shifted down
	 *
	 * @return List<SlideEvent> slide events to be executed
	 */ 
	private List<SlideEvent> mergeDown() {
		List<SlideEvent> slides = new LinkedList<SlideEvent>();
		for(int col = WIDTH-1; col >= 0; col--){
			for(int row = HEIGHT-1; row >= 0 ; row--){
				if(board[row][col] == null) continue;
				GameSquare curSquare = board[row][col];
				int i;
				for(i = row+1; i < HEIGHT && board[i][col] == null; i++);
				board[row][col] = null;
				board[i-1][col] = curSquare;
				int newRow = i-1;
				if(newRow != row) moved = true;
				slides.add(new SlideEvent(curSquare, SlideEventAction.None, row, col, newRow, col));
				for(i = row-1; i >= 0 && board[i][col] == null; i--);
				if(i == -1) continue;
				if(board[i][col].equals(curSquare)){
					GameSquare combinedWith = board[i][col];
					slides.add(new SlideEvent(curSquare, SlideEventAction.CombineOver,
							newRow, col, newRow, col));
					slides.add(new SlideEvent(combinedWith, SlideEventAction.CombineUnder,
							i, col, newRow, col));
					curSquare.increment();
					score += curSquare.getValue();
					board[i][col] = null;
					moved = true;
					numOfSquare--;
				}
			}
		}
		return slides;
	}

	/**
	 * Helper method if board is shifted up
	 *
	 * @return List<SlideEvent> slide events to be executed
	 */ 
	private List<SlideEvent> mergeUp() {
		List<SlideEvent> slides = new LinkedList<SlideEvent>();
		for(int col = 0; col < WIDTH; col++){
			for(int row = 0; row < HEIGHT; row++){
				if(board[row][col] == null) continue;
				GameSquare curSquare = board[row][col];
				int i;
				for(i = row-1;i >= 0 && board[i][col] == null; i--);
				board[row][col] = null;
				board[i+1][col] = curSquare;
				int newRow = i+1;
				if(newRow != row) moved = true;
				slides.add(new SlideEvent(curSquare, SlideEventAction.None, row, col, newRow, col));
				for(i = row+1; i < WIDTH && board[i][col] == null; i++);
				if(i == WIDTH) continue;
				if(board[i][col].equals(curSquare)){
					GameSquare combinedWith = board[i][col];
					slides.add(new SlideEvent(curSquare, SlideEventAction.CombineOver,
							newRow, col, newRow, col));
					slides.add(new SlideEvent(combinedWith, SlideEventAction.CombineUnder,
							i, col, newRow, col));
					curSquare.increment();
					score += curSquare.getValue();
					board[i][col] = null;
					moved = true;
					numOfSquare--;
				}
			}
		}
		return slides;
	}

	/**
	 * Helper method if board is shifted right
	 *
	 * @return List<SlideEvent> slide events to be executed
	 */ 
	private List<SlideEvent> mergeRight() {
		List<SlideEvent> slides = new LinkedList<SlideEvent>();
		for(int row = HEIGHT-1; row >= 0 ; row--){
			for(int col = WIDTH-1; col >= 0; col--){
				if(board[row][col] == null) continue;
				GameSquare curSquare = board[row][col];
				int j;
				for(j = col+1;j < HEIGHT && board[row][j] == null; j++);
				board[row][col] = null;
				board[row][j-1] = curSquare;
				int newCol = j-1;
				if(newCol != col) moved = true;
				slides.add(new SlideEvent(curSquare, SlideEventAction.None, row, col, row, newCol));
				for(j = col-1; j >= 0 && board[row][j] == null; j--);
				if(j == -1) continue;
				if(board[row][j].equals(curSquare)){
					GameSquare combinedWith = board[row][j];
					slides.add(new SlideEvent(curSquare, SlideEventAction.CombineOver,
							row, newCol, row, newCol));
					slides.add(new SlideEvent(combinedWith, SlideEventAction.CombineUnder,
							row, j, row, newCol));
					curSquare.increment();
					score += curSquare.getValue();
					board[row][j] = null;
					moved = true;
					numOfSquare--;
				}
			}
		}
		return slides;
	}


	/**
	 * Place a square on a particular side of the playing board
	 * This may be public, if needed, just modify the signature
	 * In fact squareGen will never return false in normal cases.
	 * This is prepared for possible GUI bugs
	 */
	private void placeSquare() { if(!squareGen()) isGameOver = true; }

	/**
	 * Indicates whether the game is over and the player can no longer move
	 * 
	 * @return Whether the game is over
	 */
	public boolean isGameOver() { return isGameOver; }

	/**
	 * Get the player's current score
	 * 
	 * @return Current score
	 */
	public int getScore() { return score; }

	/**
	 * Get the current game tile state
	 * 
	 * @return A 2D array of the game state
	 */
	public GameSquare[][] getBoard() { return board; }

	/**
	 * Method to print state of board with '/' for empty slots
	 */
	public void print() {
		for (int r = 0; r < HEIGHT; r++) {
			for (int c = 0; c < WIDTH; c++) {
				if (board[r][c] == null) {
					System.out.print("/");
				} else {
					System.out.print(board[r][c].getValue());
				}
			}
			System.out.println();
		}
	}

}
