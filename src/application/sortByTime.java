package application;
import java.util.*;

/**
 * Comparator to enable sorting by date field in each PlayerScore
 * @author frisaac
 *
 */
public class sortByTime implements Comparator<PlayerScore> {
	/**
	 * Compares date o1 date to o2 date
	 * @param o1 first player to be compared
	 * @param o2 second player to be compared
	 * @return negative if o1 precedes, 0 if they are equal, positive if o1 date comes after o2 date
	 */
	@Override
	public int compare(PlayerScore o1, PlayerScore o2) {
		return o1.getDate().compareTo(o2.getDate());
	}
}
