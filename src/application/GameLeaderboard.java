package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.PriorityQueue;

import org.json.simple.JSONArray;
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
		topScores.toArray(tSort);
		Arrays.sort(tSort);
		Arrays.sort(tSort, new sortByTime());
		return tSort;
	}

	/**
	 * Getter for topScores
	 *
	 * @return topScores priority queue of top scorers
	 */
	public PlayerScore[] getTopScores() {
		PlayerScore[] pQArray = new PlayerScore[topScores.size()];
		topScores.toArray(pQArray);
		Arrays.sort(pQArray);
		return pQArray;
	}

	/**
	 * Add score to the list
	 *
	 * @param currentScore score to be added
	 */
	public Object[] getTopScores(int mode) {
		Object[] pQArray = topScores.toArray();
		if (mode == 1) {
			Arrays.sort(pQArray);
		} else if (mode == 2) {
			Arrays.sort(pQArray, Collections.reverseOrder());
		} else {
			throw new UnsupportedOperationException();
		}
		return pQArray;
}

	public void add(PlayerScore currentScore) {
		topScores.add(currentScore);
	}



	/**
	 * Save the current leaderboard into a specified JSON path.
	 *
	 * @param jsonPath
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void export(String jsonPath) throws IOException {

		JSONArray scores = new JSONArray();

		for (PlayerScore score : getTopScores()) {
			JSONObject scoreEntry = new JSONObject();
			scoreEntry.put("name", score.getName());
			scoreEntry.put("score", score.getScore());
			scoreEntry.put("timestamp", score.getDate().getTime());
			scores.add(scoreEntry);
		}

		JSONObject json = new JSONObject();
		json.put("scores", scores);

		try (FileWriter file = new FileWriter(jsonPath)) {
			file.write(json.toJSONString());
		}
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

		try (Reader reader = new FileReader(jsonPath)) {
			JSONObject json = (JSONObject) new JSONParser().parse(reader);
			JSONArray scores = (JSONArray) json.get("scores");

			if (scores != null) {
				int i = 0;
				for (Object scoreEntryObj : scores) {
					JSONObject scoreEntry = (JSONObject) scoreEntryObj;

					if (scoreEntry == null) {
						continue;
					}

					String name = (String) scoreEntry.get("name");
					Long score = (Long) scoreEntry.get("score");
					Long timestamp = (Long) scoreEntry.get("timestamp");

					if (name == null || score == null || timestamp == null) {
						System.out.println("Missing leaderboard JSON property in entry " + i + ". Skipping.");
						continue;
					}

					Date date = new Date(timestamp);
					leaderboard.add(new PlayerScore(name, score, date));
					i++;
				}
			}

			System.out.println("Successfully loaded " + leaderboard.getTopScores().length + " score(s).");

		} catch (FileNotFoundException e) {
			// If file not found
			System.out.println("Leaderboard file not found. Creating a new one.");
		} catch (IOException e) {
			// Error reading file
			System.out.println("Error reading leaderboard file. Creating a new one.");
		} catch (ParseException e) {
			// Invalid JSON syntax
			System.out.println("Invalid leaderboard JSON syntax. Creating a new one.");
		} catch (ClassCastException e) {
			// If JSON structure is not as expected
			System.out.println("Unexpected leaderboard JSON structure. Creating a new one.");
		}

		return leaderboard;
	}
}
