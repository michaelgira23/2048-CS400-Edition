package application;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Keeps track of the internal game state
 * 
 * @author Hanyuan Wu, Michael Gira
 *
 */
public class Game {

	static final int HEIGHT = 4;
	static final int WIDTH = 4;
	private int numOfSquare;
	private int score = 0;
	private GameSquare[][] board;
	private Random rnd;
	private boolean isGameOver;
	private SlideHandler slideHandler = null;

	public Game(int seed) {
		rnd = new Random(seed);
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
	public void setSlideHandler(SlideHandler handler) {
		slideHandler = handler;
	}


	private boolean squareGen() {
		// check full
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
				return true;
			}
		}
	}

	/**
	 * Slide all the squares in a particular direction, possibly combining similar
	 * squares
	 *
	 * @param direction Direction to slide
	 */
	public void slide(Direction direction) {
		List<SlideEvent> slides = new LinkedList<SlideEvent>();
		switch (direction) {
			case Up:
				slides = mergeUp();
				break;
			case Left:
				slides = mergeLeft();
				break;
			case Down:
				slides = mergeDown();
				break;
			case Right:
				slides = mergeRight();
				break;
		}
		if (slideHandler != null) {
			slideHandler.handle(slides);
		}
		placeSquare();
	}

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
					board[row][j] = null;
				}
			}
		}
		return slides;
	}

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
					board[i][col] = null;
				}
			}
		}
		return slides;
	}

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
					board[i][col] = null;
				}
			}
		}
		return slides;
	}

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
					board[row][j] = null;
				}
			}
		}
		return slides;
	}


	/**
	 * Place a square on a particular side of the playing board
	 *
	 */
	public void placeSquare() {
		if(!squareGen()) isGameOver = true;
	}

	/**
	 * Indicates whether the game is over and the player can no longer move
	 * 
	 * @return Whether the game is over
	 */
	public boolean isGameOver() {
		return isGameOver;
	}

	/**
	 * Get the player's current score
	 * 
	 * @return Current score
	 */
	public int getScore() {
		return score;
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
