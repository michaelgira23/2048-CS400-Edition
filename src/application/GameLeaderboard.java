package application;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * A list of the top player scores
 * 
 * @author Quan Nguyen
 *
 */
public class GameLeaderboard implements Serializable {
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		// Creating an empty PriorityQueue
		PriorityQueue<Integer> queue = new PriorityQueue<Integer>();

		// Use add() method to add elements into the Queue
		queue.add(10);
		queue.add(15);
		queue.add(30);
		queue.add(20);
		queue.add(5);
		queue.add(25);
		// Neu co it nhat 1 diem thi lay ra
		// If at least 1 point is removed
		Iterator<Integer> itr2 = queue.iterator();
		// Diem thap nhat
		Integer minScore = itr2.next();
		while (itr2.hasNext()) {
			// Diem dang duyet
			// Unidirectional
			Integer currentScore = itr2.next();
			minScore = currentScore;
			// So sanh diem dang duyet voi diem thap nhat
			// Compose a checkpoint with the lowest point
			if (currentScore < minScore) {
				// Gan diem thap nhat bang diem dang duyet
				// Please update the state with the checkpoint
				minScore = currentScore;
			}
		}

		// Displaying the PriorityQueue
		System.out.println("The PriorityQueue: " + queue);

		// Creating the array and using toArray()
		Object[] arr = queue.toArray();

		System.out.println("The array is:");
		for (int j = 0; j < arr.length; j++)
			System.out.println(arr[j]);

		PriorityQueue<String> pQueue = new PriorityQueue<>();

		// Adding items to the pQueue using add()
		pQueue.add("70");
		pQueue.add("20");
		pQueue.add("50");
		pQueue.add("40");
		pQueue.add("80");
		pQueue.add("20");

		System.out.println("Phan tu dau tien:" + pQueue.peek());
		// Sap xep priority queue
		Object[] pQArray = pQueue.toArray();
		Arrays.sort(pQArray);
		for (Object string : pQArray) {
			System.out.println(string.toString());
		}

		System.out.println("Queue: ");
		Iterator itr = pQueue.iterator();
		while (itr.hasNext())
			System.out.println(itr.next());

		pQueue.remove("G");
		System.out.println("Sau khi xoa phan tu G");
		Iterator<String> itr3 = pQueue.iterator();
		while (itr3.hasNext())
			System.out.println(itr3.next());

	}

	// Priority game leader board
	private PriorityQueue<PlayerScore> topScores = new PriorityQueue<PlayerScore>();

	/**
	 * Check if current score is in top score
	 * @param currentScore
	 * @return whether or not it is
	 */
	public boolean isTopScore(PlayerScore currentScore) {
		Iterator<PlayerScore> topScoresIterator = topScores.iterator();
		while (topScoresIterator.hasNext()) {
			PlayerScore gameLeaderBoard = topScoresIterator.next();
			if (currentScore.getScore() >= gameLeaderBoard.getScore()) {

				return true;
			}
		}

		return false;
	}
	
	/**
	 * Sorts topScores by timestamp
	 * @return tSort array of topScores sorted by timestamp using comparator
	 */
	public PlayerScore[] sortedTopScoresByTime() {
		PlayerScore[] tSort = new PlayerScore[topScores.size()];
		int i = 0;
		for(dateObj temp: topScores) {
			toSort[i] = temp;
			i++;
		}
		Arrays.sort(dates, new sortByTime());
		return tSort;
	}

	/**
	 * Add score to the list
	 * @param currentScore score to be added
	 */
	public void addScoreToList(PlayerScore currentScore) {
		topScores.add(currentScore);
	}

	/**
	 * Sorting top score list
	 * @return pQArray array of scores sorted by score amount
	 */
	public Object[] sortedTopScores() {
		Object[] pQArray = topScores.toArray();
		Arrays.sort(pQArray);
		return pQArray;
	}

	/**
	 * Getter for topScores
	 * @return topScores priority queue of top scorers
	 */
	public PriorityQueue<PlayerScore> getTopScores() {
		return topScores;
	}

	/**
	 * Setter method for topScores
	 * @param topScores for topScores field to be set to
	 */
	public void setTopScores(PriorityQueue<PlayerScore> topScores) {
		this.topScores = topScores;
	}
}
