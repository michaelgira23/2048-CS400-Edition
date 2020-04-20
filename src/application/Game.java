package application;

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

	// Functional interface to call when new changes occur
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

	public void resetCombineStatus() {
		for (int r = 0; r < HEIGHT; r++) {
			for (int c = 0; c < WIDTH; c++) {
				if (board[r][c] != null) {
					board[r][c].setComb(false);
				}
			}
		}
	}

	/**
	 * Helper method when GameSquares slide up
	 */
	public List<SlideEvent> slideUpHelp() {
		List<SlideEvent> slides = new LinkedList<SlideEvent>();
		GameSquare tempGS;
		for (int r = 0; r < HEIGHT; r++) {
			for (int c = 0; c < WIDTH; c++) {
				// if the slot has a gamesquare
				if (board[r][c] != null) {
					tempGS = board[r][c];
					int tempR = r;
					boolean moved = false;
					boolean repeat = true;
					if (tempR != 0 && board[tempR - 1][c] == null) {
						// while it's not row 0 and there's no gamesquare above it keep moving the
						// gamesquare up
						while (repeat == true) {
							board[tempR - 1][c] = tempGS;
							tempGS.setPos(c, tempR - 1);
							board[tempR][c] = null;
							tempR--;
							if (tempR == 0) {
								repeat = false;
							} else if (board[tempR - 1][c] != null) {
								repeat = false;
							}
						}

						slides.add(new SlideEvent(tempGS, SlideEventAction.None, r, c, tempR, c));
						moved = true;
					}
					// if theres a gamesquare above it (aka tempR is not 0) check if combo is
					// possible
					if (tempR != 0 && board[tempR - 1][c].getComb() == false) {
						if (board[tempR][c].getValue() == board[tempR - 1][c].getValue()) {

							// Change current slide event for tile
							SlideEvent slideOverEvent = new SlideEvent(tempGS, SlideEventAction.CombineOver, r, c,
									tempR - 1, c);
							if (moved) {
								slides.set(slides.size() - 1, slideOverEvent);
							} else {
								slides.add(slideOverEvent);
							}

							// Edit any slide event with the square we're sliding over
							GameSquare combinedWith = board[tempR - 1][c];
							boolean existing = false;
							for (SlideEvent slide : slides) {
								if (slide.tile.equals(combinedWith)) {
									slide.action = SlideEventAction.CombineUnder;
									existing = true;
								}
							}

							// If no preexisting slide event for what we're sliding over, create our own
							if (!existing) {
								slides.add(new SlideEvent(combinedWith, SlideEventAction.CombineUnder, tempR - 1, c,
										tempR - 1, c));
							}

							board[tempR - 1][c] = board[tempR][c];
							board[tempR - 1][c].increment();
							board[tempR - 1][c].setComb(true);
							board[tempR][c] = null;
						}
					}
				}
			}
		}
		resetCombineStatus();
		return slides;
	}

	public List<SlideEvent> slideDownHelp() {
		List<SlideEvent> slides = new LinkedList<SlideEvent>();
		GameSquare tempGS;
		for (int r = 3; r >= 0; r--) {
			for (int c = 0; c < WIDTH; c++) {
				// if the slot has a gamesquare
				if (board[r][c] != null) {
					tempGS = board[r][c];
					int tempR = r;
					boolean moved = false;
					boolean repeat = true;
					if (tempR != 3 && board[tempR + 1][c] == null) {
						// while it's not row 3 and there's no gamesquare below it keep moving the
						// gamesquare down
						while (repeat == true) {
							board[tempR + 1][c] = tempGS;
							tempGS.setPos(c, tempR + 1);
							board[tempR][c] = null;
							tempR++;
							if (tempR == 3) {
								repeat = false;
							} else if (board[tempR + 1][c] != null) {
								repeat = false;
							}
						}

						slides.add(new SlideEvent(tempGS, SlideEventAction.None, r, c, tempR, c));
						moved = true;
					}
					// if theres a gamesquare below it (aka tempR is not 3) check if combo is
					// possible
					if (tempR != 3 && board[tempR + 1][c].getComb() == false) {
						if (board[tempR][c].getValue() == board[tempR + 1][c].getValue()) {

							// Change current slide event for tile
							SlideEvent slideOverEvent = new SlideEvent(tempGS, SlideEventAction.CombineOver, r, c,
									tempR + 1, c);
							if (moved) {
								slides.set(slides.size() - 1, slideOverEvent);
							} else {
								slides.add(slideOverEvent);
							}

							// Edit any slide event with the square we're sliding over
							GameSquare combinedWith = board[tempR + 1][c];
							boolean existing = false;
							for (SlideEvent slide : slides) {
								if (slide.tile.equals(combinedWith)) {
									slide.action = SlideEventAction.CombineUnder;
									existing = true;
								}
							}

							// If no preexisting slide event for what we're sliding over, create our own
							if (!existing) {
								slides.add(new SlideEvent(combinedWith, SlideEventAction.CombineUnder, tempR + 1, c,
										tempR + 1, c));
							}

							board[tempR + 1][c] = board[tempR][c];
							board[tempR + 1][c].increment();
							board[tempR + 1][c].setComb(true);
							board[tempR][c] = null;
						}
					}
				}
			}
		}
		resetCombineStatus();
		return slides;
	}

	public List<SlideEvent> slideLeftHelp() {
		List<SlideEvent> slides = new LinkedList<SlideEvent>();
		GameSquare tempGS;
		for (int r = 0; r < HEIGHT; r++) {
			for (int c = 0; c < WIDTH; c++) {
				// if the slot has a gamesquare
				if (board[r][c] != null) {
					tempGS = board[r][c];
					int tempC = c;
					boolean moved = false;
					boolean repeat = true;
					// while it's not col 0 and there's no gamesquare to the left of it keep moving
					// the gamesquare left
					if (tempC != 0 && board[r][tempC - 1] == null) {
						while (repeat == true) {
							board[r][tempC - 1] = tempGS;
							tempGS.setPos(tempC - 1, r);
							board[r][tempC] = null;
							tempC--;
							if (tempC == 0) {
								repeat = false;
							} else if (board[r][tempC - 1] != null) {
								repeat = false;
							}
						}

						slides.add(new SlideEvent(tempGS, SlideEventAction.None, r, c, r, tempC));
						moved = true;
					}
					// if theres a gamesquare to the left of it (aka tempC is not 0) check if combo
					// is possible
					if (tempC != 0 && board[r][tempC - 1].getComb() == false) {
						if (board[r][tempC - 1].getValue() == board[r][tempC].getValue()) {

							// Change current slide event for tile
							SlideEvent slideOverEvent = new SlideEvent(tempGS, SlideEventAction.CombineOver, r, c, r,
									tempC - 1);
							if (moved) {
								slides.set(slides.size() - 1, slideOverEvent);
							} else {
								slides.add(slideOverEvent);
							}

							// Edit any slide event with the square we're sliding over
							GameSquare combinedWith = board[r][tempC - 1];
							boolean existing = false;
							for (SlideEvent slide : slides) {
								if (slide.tile.equals(combinedWith)) {
									slide.action = SlideEventAction.CombineUnder;
									existing = true;
								}
							}

							// If no preexisting slide event for what we're sliding over, create our own
							if (!existing) {
								slides.add(new SlideEvent(combinedWith, SlideEventAction.CombineUnder, r, tempC - 1, r,
										tempC - 1));
							}

							board[r][tempC - 1] = board[r][tempC];
							board[r][tempC - 1].increment();
							board[r][tempC - 1].setComb(true);
							board[r][tempC] = null;
						}
					}
				}
			}
		}
		resetCombineStatus();
		return slides;
	}

	public List<SlideEvent> slideRightHelp() {
		List<SlideEvent> slides = new LinkedList<SlideEvent>();
		GameSquare tempGS;
		for (int r = 0; r < HEIGHT; r++) {
			for (int c = 3; c >= 0; c--) {
				// if the slot has a gamesquare
				if (board[r][c] != null) {
					tempGS = board[r][c];
					int tempC = c;
					boolean moved = false;
					boolean repeat = true;
					// while it's not col 3 and there's no gamesquare to the right of it keep moving
					// the gamesquare right
					if (tempC != 3 && board[r][tempC + 1] == null) {
						while (repeat == true) {
							board[r][tempC + 1] = tempGS;
							tempGS.setPos(tempC + 1, r);
							board[r][tempC] = null;
							tempC++;
							if (tempC == 3) {
								repeat = false;
							} else if (board[r][tempC + 1] != null) {
								repeat = false;
							}
						}

						slides.add(new SlideEvent(tempGS, SlideEventAction.None, r, c, r, tempC));
						moved = true;
					}
					// if theres a gamesquare to the right of it (aka tempC is not 3)
					if (tempC < 3 && board[r][tempC + 1].getComb() == false) {
						if (board[r][tempC + 1].getValue() == board[r][tempC].getValue()) {

							// Change current slide event for tile
							SlideEvent slideOverEvent = new SlideEvent(tempGS, SlideEventAction.CombineOver, r, c, r,
									tempC + 1);
							if (moved) {
								slides.set(slides.size() - 1, slideOverEvent);
							} else {
								slides.add(slideOverEvent);
							}

							// Edit any slide event with the square we're sliding over
							GameSquare combinedWith = board[r][tempC + 1];
							boolean existing = false;
							for (SlideEvent slide : slides) {
								if (slide.tile.equals(combinedWith)) {
									slide.action = SlideEventAction.CombineUnder;
									existing = true;
								}
							}

							// If no preexisting slide event for what we're sliding over, create our own
							if (!existing) {
								slides.add(new SlideEvent(combinedWith, SlideEventAction.CombineUnder, r, tempC + 1, r,
										tempC + 1));
							}

							board[r][tempC + 1] = board[r][tempC];
							board[r][tempC + 1].increment();
							board[r][tempC + 1].setComb(true);
							board[r][tempC] = null;
						}
					}
				}
			}
		}
		resetCombineStatus();
		return slides;
	}

	/**
	 * Slide all the squares in a particular direction, possibly combining similar
	 * squares
	 *
	 * @param direction Direction to slide
	 */
	public void slide(Direction direction) {
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
