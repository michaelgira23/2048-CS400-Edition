package application;

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

	public Game(int seed) {
		rnd = new Random(seed);
		score = 0;
		numOfSquare = 0;
		isGameOver = false;
		board = new GameSquare[HEIGHT][WIDTH];
		squareGen();
		squareGen();
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
		GameSquare[][] shadowBoard = new GameSquare[HEIGHT][WIDTH];
		switch (direction) {
			case Up:
				mergeUp();
				break;
			case Left:
				mergeLeft();
				break;
			case Bottom:
				mergeBottom();
				break;
			case Right:
				mergeRight();
				break;
		}
	}

	private void mergeLeft() {
		for(int row = 0; row < HEIGHT; row++){
			for(int col = 0; col < WIDTH; col++){
				if(board[row][col] == null) continue;
				GameSquare curSquare = board[row][col];
				int j;
				for(j = col-1; j >= 0 && board[row][j] == null; j--);
				board[row][col] = null;
				board[row][j+1] = curSquare;
				for(j = col+1; board[row][j] == null; j++);
				if(board[row][j].equals(curSquare)){
					curSquare.increment();
					board[row][j] = null;
				}
			}
		}
	}

	private void mergeBottom() {
		for(int col = WIDTH-1; col >= 0; col--){
			for(int row = HEIGHT-1; row >= 0 ; row--){
				if(board[row][col] == null) continue;
				GameSquare curSquare = board[row][col];
				int i;
				for(i = row+1;i < HEIGHT && board[i][col] == null; i++);
				board[row][col] = null;
				board[i-1][col] = curSquare;
				for(i = row-1; board[i][col] == null; i--);
				if(board[i][col].equals(curSquare)){
					curSquare.increment();
					board[i][col] = null;
				}
			}
		}
	}

	private void mergeUp() {
		for(int col = 0; col < WIDTH; col++){
			for(int row = 0; row < WIDTH ; row++){
				if(board[row][col] == null) continue;
				GameSquare curSquare = board[row][col];
				int i;
				for(i = row-1;i >= 0 && board[i][col] == null; i--);
				board[row][col] = null;
				board[i+1][col] = curSquare;
				for(i = row+1; board[i][col] == null; i++);
				if(board[i][col].equals(curSquare)){
					curSquare.increment();
					board[i][col] = null;
				}
			}
		}
	}
	private void mergeRight() {
		for(int row = HEIGHT-1; row >= 0 ; row--){
			for(int col = WIDTH-1; col >= 0; col--){
				if(board[row][col] == null) continue;
				GameSquare curSquare = board[row][col];
				int j;
				for(j = col+1;j < HEIGHT && board[row][j] == null; j++);
				board[row][col] = null;
				board[row][j-1] = curSquare;
				for(j = col-1; board[row][j] == null; j--);
				if(board[row][j].equals(curSquare)){
					curSquare.increment();
					board[row][j] = null;
				}
			}
		}
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
