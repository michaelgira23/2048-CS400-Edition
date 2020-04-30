package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.PriorityQueue;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * A list of the top player scores
 * 
 * @author Quan Nguyen
 *
 */
public class GameLeaderboard implements Serializable {
	private static final long serialVersionUID = 1L;

	// Priority game leader board
	private PriorityQueue<PlayerScore> topScores = new PriorityQueue<PlayerScore>();

	/**
	 * Check if current score is in top score
	 * 
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
	 * 
	 * @return tSort array of topScores sorted by timestamp using comparator
	 */
	public PlayerScore[] sortedTopScoresByTime() {
		PlayerScore[] tSort = new PlayerScore[topScores.size()];
		int i = 0;
		for (PlayerScore temp : topScores) {
			tSort[i] = temp;
			i++;
		}
		Arrays.sort(tSort, new sortByTime());
		return tSort;
	}

	/**
	 * Add score to the list
	 * 
	 * @param currentScore score to be added
	 */
	public void addScoreToList(PlayerScore currentScore) {
		topScores.add(currentScore);
	}

	/**
	 * Getter for topScores
	 * 
	 * @return topScores priority queue of top scorers
	 */
	public PriorityQueue<PlayerScore> getTopScores() {
		return topScores;
	}

	/**
	 * Setter method for topScores
	 * 
	 * @param topScores for topScores field to be set to
	 */
	public void setTopScores(PriorityQueue<PlayerScore> topScores) {
		this.topScores = topScores;
	}

	/**
	 * Loads a game leaderboard from a given path. If path doesn't exist, returns
	 * empty leaderboard.
	 * 
	 * @param jsonPath Path to JSON file containing leaderboard data
	 * @return GameLeaderboard instance with loaded scores, if they exist
	 */
	static GameLeaderboard load(String jsonPath) {
		GameLeaderboard leaderboard = new GameLeaderboard();

		try {
			JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(jsonPath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return leaderboard;
	}
}
