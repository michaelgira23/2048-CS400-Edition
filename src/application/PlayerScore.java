/**
 * Represents a player's name and their high score
 * 
 * @author Quan Nguyen 
 *
 */
package application;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents a player's name and their high score
 * 
 * @author Quan
 *
 */
public class PlayerScore implements Comparable<PlayerScore>, Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private Long score;
	private Date date;

	public PlayerScore(String name, Long score) {
		this(name, score, new Date());
	}

	public PlayerScore(String name, Long score, Date date) {
		this.name = name;
		this.score = score;
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public Date getDate() {
		return date;
	}

	// So sanh voi 1 game leader board khac
	// Tra ve -1 neu game leader board kia nho hon (game leader board nay lon hon)
	/**
	 * Compares score to the score of another given PlayerScore
	 * 
	 * @param o PlayerScore to be compared with
	 * @return -1 if personal score is higher, 0 if it's equal, and 1 if it's lower
	 */
	@Override
	public int compareTo(PlayerScore o) {
		return o.score.compareTo(score);
	}

}
