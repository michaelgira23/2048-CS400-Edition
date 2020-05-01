package application;

import java.util.Comparator;

/**
 * Comparator to enable sorting by date field in each PlayerScore
 * 
 * @author Faith Isaac
 *
 */
public class SortByTime implements Comparator<PlayerScore> {

	// Whether or not to reverse the order
	private boolean reversed;

	/**
	 * Initializes a comparator that sorts player scores by the timestamp they were
	 * created
	 * 
	 * @param reversed Whether or not to reverse the order
	 */
	public SortByTime(boolean reversed) {
		this.reversed = reversed;
	}

	/**
	 * Compares date o1 date to o2 date
	 * 
	 * @param score1 first player score to compare
	 * @param score2 second player score to compare
	 * @return negative if o1 precedes, 0 if they are equal, positive if o1 date
	 *         comes after o2 date
	 */
	@Override
	public int compare(PlayerScore score1, PlayerScore score2) {
		if (reversed) {
			return score1.getDate().compareTo(score2.getDate());
		} else {
			return score2.getDate().compareTo(score1.getDate());
		}
	}
}
