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
	public void slideUpHelp() {
		GameSquare tempGS = new GameSquare();
		for (int r = 0; r < HEIGHT; r++) {
			for (int c = 0; c < WIDTH; c++) {
				// if the slot has a gamesquare
				if (board[r][c] != null) {
					tempGS = board[r][c];
					int tempR = r;
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
					}
					// if theres a gamesquare above it (aka tempR is not 0) check if combo is
					// possible
					if (tempR != 0 && board[tempR - 1][c].getComb() == false) {
						if (board[tempR][c].getValue() == board[tempR - 1][c].getValue()) {
							board[tempR][c] = null;
							board[tempR - 1][c].increment();
							board[tempR - 1][c].setComb(true);
						}
					}
				}
			}
		}
		resetCombineStatus();
	}

	public void slideDownHelp() {
		GameSquare tempGS = new GameSquare();
		for (int r = 3; r >= 0; r--) {
			for (int c = 0; c < WIDTH; c++) {
				// if the slot has a gamesquare
				if (board[r][c] != null) {
					tempGS = board[r][c];
					int tempR = r;
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
					}
					// if theres a gamesquare below it (aka tempR is not 3) check if combo is
					// possible
					if (tempR != 3 && board[tempR + 1][c].getComb() == false) {
						if (board[tempR][c].getValue() == board[tempR + 1][c].getValue()) {
							board[tempR][c] = null;
							board[tempR + 1][c].increment();
						}
					}
				}
			}
		}
		resetCombineStatus();
	}

	public void slideLeftHelp() {
		GameSquare tempGS = new GameSquare();
		for (int r = 0; r < HEIGHT; r++) {
			for (int c = 0; c < WIDTH; c++) {
				// if the slot has a gamesquare
				if (board[r][c] != null) {
					tempGS = board[r][c];
					int tempC = c;
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
					}
					// if theres a gamesquare to the left of it (aka tempC is not 0) check if combo
					// is possible
					if (tempC != 0 && board[r][tempC - 1].getComb() == false) {
						if (board[r][tempC - 1].getValue() == board[r][tempC].getValue()) {
							board[r][tempC] = null;
							board[r][tempC - 1].increment();
						}
					}
				}
			}
		}
		resetCombineStatus();
	}

	public void slideRightHelp() {
		GameSquare tempGS = new GameSquare();
		for (int r = 0; r < HEIGHT; r++) {
			for (int c = 3; c >= 0; c--) {
				// if the slot has a gamesquare
				if (board[r][c] != null) {
					tempGS = board[r][c];
					int tempC = c;
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
					}
					// if theres a gamesquare to the right of it (aka tempC is not 3)
					if (tempC < 3 && board[r][tempC + 1].getComb() == false) {
						if (board[r][tempC + 1].getValue() == board[r][tempC].getValue()) {
							board[r][tempC] = null;
							board[r][tempC + 1].increment();
						}
					}
				}
			}
		}
		resetCombineStatus();
	}

	/**
	 * Slide all the squares in a particular direction, possibly combining similar
	 * squares
	 * 
	 * @param direction Direction to slide
	 */
	public void slide(Direction direction) {
		if (direction == Direction.Up) {
			slideUpHelp();
		}
		if (direction == Direction.Down) {
			slideDownHelp();
		}
		if (direction == Direction.Left) {
			slideLeftHelp();
		}
		if (direction == Direction.Right) {
			slideRightHelp();
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
