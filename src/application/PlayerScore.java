/**
 * Represents a player's name and their high score
 * 
 * @author Quan Nguyen 
 *
 */
package application;

import java.util.Date;

/**
 * Represents a player's name and their high score
 * 
 * @author Quan Nguyen
 *
 */
public class PlayerScore implements Comparable<PlayerScore> {

	private String name;
	private Long score;
	private Date date;

	/**
	 * Initialize player score with a given name and score
	 * 
	 * @param name  Name of the player
	 * @param score Score of the player's game
	 */
	public PlayerScore(String name, Long score) {
		this(name, score, new Date());
	}

	/**
	 * Initialize a player score with a given name, score, and date
	 * 
	 * @param name  Name of the player
	 * @param score Score of the player's game
	 * @param date  Date that the player achieved that score
	 */
	public PlayerScore(String name, Long score, Date date) {
		this.name = name;
		this.score = score;
		this.date = date;
	}

	/**
	 * Get the name associated with this entry
	 * 
	 * @return Player's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name associated with this entry
	 * 
	 * @param name Player's name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the score associated with this entry
	 * 
	 * @return Player's score
	 */
	public Long getScore() {
		return score;
	}

	/**
	 * Set the score associated with this entry
	 * 
	 * @param score Player's score
	 */
	public void setScore(Long score) {
		this.score = score;
	}

	/**
	 * Get the date associated with this entry
	 * 
	 * @return The date the entry was created
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Compares score to the score of another given PlayerScore
	 * 
	 * @param other PlayerScore to be compared with
	 * @return -1 if personal score is higher, 0 if it's equal, and 1 if it's lower
	 */
	@Override
	public int compareTo(PlayerScore other) {
		return other.score.compareTo(score);
	}

}
