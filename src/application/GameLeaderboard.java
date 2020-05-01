package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.PriorityQueue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * A list of the top player scores
 *
 * @author Quan Nguyen, Faith Isaac, Michael Gira
 *
 */
public class GameLeaderboard {

	// Priority queue of leaderboard, sorted by the highest score
	private PriorityQueue<PlayerScore> topScores = new PriorityQueue<PlayerScore>();

	/**
	 * Get an array of the top scores sorted by the timestamp they were created
	 * 
	 * @param reversed Whether the score order should be reversed
	 *
	 * @return An array of top scores sorted by timestamp
	 */
	public PlayerScore[] sortedTopScoresByTime(boolean reversed) {
		PlayerScore[] pQArray = new PlayerScore[topScores.size()];
		topScores.toArray(pQArray);
		Arrays.sort(pQArray, new SortByTime(reversed));
		return pQArray;
	}

	/**
	 * Get an array of the top scores from the leaderboard sorted from highest score
	 * to lowest
	 *
	 * @param reversed Whether the scores should be reversed (least to greatest)
	 *                 instead of greatest to least.
	 */
	public PlayerScore[] getTopScores(boolean reversed) {
		PlayerScore[] pQArray = new PlayerScore[topScores.size()];
		topScores.toArray(pQArray);
		if (!reversed) {
			Arrays.sort(pQArray);
		} else {
			Arrays.sort(pQArray, Collections.reverseOrder());
		}
		return pQArray;
	}

	/**
	 * Add a player's score to the leaderboard. Note: this does not save the new
	 * leaderboard to file!
	 * 
	 * @param currentScore Name and score of the player
	 */
	public void add(PlayerScore currentScore) {
		topScores.add(currentScore);
	}

	/**
	 * Save the current leaderboard into a specified JSON path.
	 *
	 * @param jsonPath Path to the JSON file to write to
	 * @throws IOException If there's an error writing to the JSON path
	 */
	@SuppressWarnings("unchecked")
	public void export(String jsonPath) throws IOException {

		JSONArray scores = new JSONArray();

		for (PlayerScore score : getTopScores(false)) {
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
	 * Loads and returns a game leaderboard instance according to data from a given
	 * path. If path doesn't exist, returns empty leaderboard.
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

					// Check for any missing properties
					if (name == null || score == null || timestamp == null) {
						System.out.println("Missing leaderboard JSON property in entry " + i + ". Skipping.");
						continue;
					}

					Date date = new Date(timestamp);
					leaderboard.add(new PlayerScore(name, score, date));
					i++;
				}
			}

			System.out.println("Successfully loaded " + leaderboard.getTopScores(false).length + " score(s).");

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
