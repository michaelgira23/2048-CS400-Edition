import java.util.*;

public class sortByTime implements Comparator<PlayerScore> {
	@Override
	public int compare(PlayerScore o1, PlayerScore o2) {
		return o1.getDate().compareTo(o2.getDate());
	}
}
