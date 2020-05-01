package application;

import java.util.Comparator;

/**
 * Comparator to enable sorting by date field in each PlayerScore
 * 
 * @author frisaac
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
	 * @param o1 first player to be compared
	 * @param o2 second player to be compared
	 * @return negative if o1 precedes, 0 if they are equal, positive if o1 date
	 *         comes after o2 date
	 */
	@Override
	public int compare(PlayerScore o1, PlayerScore o2) {
		if (reversed) {
			return o1.getDate().compareTo(o2.getDate());
		} else {
			return o2.getDate().compareTo(o1.getDate());
		}
	}
}
