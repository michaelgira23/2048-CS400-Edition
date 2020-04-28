/**
 * GameLeaderBoard class
 * 
 * @author Quan Nguyen 
 *
 */
package application;

import java.io.Serializable;

public class GameLeaderBoard implements Comparable<GameLeaderBoard>, Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private int score;

	public GameLeaderBoard(String name, int score) {
		this.name = name;
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	// So sanh voi 1 game leader board khac
	// Tra ve -1 neu game leader board kia nho hon (game leader board nay lon hon)
	@Override
	public int compareTo(GameLeaderBoard o) {
		if (this.score > o.score) {
			return -1;
		} else if (this.score < o.score) {
			return 1;
		} else {
			return 0;
		}
	}

}
