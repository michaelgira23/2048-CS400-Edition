package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.PriorityQueue;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

/**
 * Main game file
 *
 * @author Michael Gira and Quan Nguyen
 *
 */
public class Main extends Application {

	private static final boolean DEBUG = true;

	private static final int WINDOW_WIDTH = 600;
	private static final int WINDOW_HEIGHT = 700;
	private static final String APP_TITLE = "2048: CS400 Edition";

	private int seed = 0;
	private List<String> args;
	private Stage primaryStage;

	// Internal logic of the game
	private Game game;

	// Current game theme that renders the game tiles
	private GameTheme currentTheme = new BinaryTheme();

	// Default path to load leaderboard data JSON file
	private String leaderboardPath = "leaderboard.json";
	// Leader board score
	private GameLeaderboard leaderboard;

	/**
	 * Sets up initial game screen
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		args = this.getParameters().getRaw();
		System.out.println("Args: " + args);

		this.primaryStage = primaryStage;
		primaryStage.setTitle(APP_TITLE);
		renderMenu();
		primaryStage.show();

		// Load game leader board
		loadScoreFromFile();

//		// Du lieu gia
//		listGameLeaderboard.getTopScores().add(new PlayerScore("Faith Issac", 8));
//		listGameLeaderboard.getTopScores().add(new PlayerScore("William Cong", 4));
//		listGameLeaderboard.getTopScores().add(new PlayerScore("Hanyuan Wu", 32));
//		listGameLeaderboard.getTopScores().add(new PlayerScore("Michael Gira", 64));
//		listGameLeaderboard.getTopScores().add(new PlayerScore("Quan Nguyen", 16));
//		saveScoreToFile();
	}

	/**
	 * Launches JavaFX
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * On the primary stage, display a menu with buttons to play
	 */
	private void renderMenu() {

		// Action buttons
		ImageView playIcon = new ImageView(new Image(getClass().getResourceAsStream("assets/play-icon.png")));

		playIcon.setPreserveRatio(true);
		playIcon.setFitWidth(22);

		Button playButton = new Button("", playIcon);
		playButton.setId("menu-button");
		playButton.setOnAction(e -> renderGameWithTheme(currentTheme));

		// Leaderboard button
		ImageView leaderboardIcon = new ImageView(
				new Image(getClass().getResourceAsStream("assets/leaderboard-icon.png")));
		leaderboardIcon.setPreserveRatio(true);
		leaderboardIcon.setFitWidth(10);

		Button leaderboardButton = new Button(" Leaderboard", leaderboardIcon);
//		leaderboardButton.setId("menu-button");
		leaderboardButton.getStyleClass().add("small");
		leaderboardButton.setOnAction(e -> renderLeaderboard(false));

//		HBox menuButtons = new HBox(15, playButton, leaderboardButton);
		VBox menuButtons = new VBox(20, playButton, leaderboardButton);
		menuButtons.setAlignment(Pos.CENTER);

		// Stack game title above and action buttons below
		VBox gameMenu = new VBox();
		gameMenu.setAlignment(Pos.CENTER);
		gameMenu.getChildren().addAll(getGameHeader(true), menuButtons);

		gameMenu.setSpacing(20);

		// Entire page layout
		BorderPane menuLayout = new BorderPane();
		menuLayout.setCenter(gameMenu);

		Scene menuScene = new Scene(menuLayout, WINDOW_WIDTH, WINDOW_HEIGHT);
		menuScene.getStylesheets().addAll("application/application.css", "application/menu.css");
		primaryStage.setScene(menuScene);
	}

	/**
	 * On the primary stage, display the game state rendered with a given theme
	 *
	 * @param theme Current theme with which to render the game
	 */
	private void renderGameWithTheme(GameTheme theme) {
		game = new Game(seed, this);
		BorderPane gameLayout = new BorderPane();

		// Game title + action buttons at the top

		// TODO: LeaderBoard should not be directly accessible from the game body
		// , unless a "return" button is set, and a parameter showing whether it
		// should display "Game Over"
//		Button leaderboardButton = new Button("Leaderboard");
//		leaderboardButton.setOnAction(e -> renderLeaderboard(true));

		// TODO: implement a restart button
		Button menuButton = new Button("Menu");
		menuButton.setOnAction(e -> renderMenu());

		HBox actionButtons = new HBox(15, menuButton);
		actionButtons.setAlignment(Pos.CENTER);

		VBox gameHeader = new VBox();
		gameHeader.setAlignment(Pos.CENTER);
		gameHeader.getChildren().addAll(getGameHeader(false), actionButtons);
		gameLayout.setTop(gameHeader);

		// Game rendered with theme in the center
		gameLayout.setCenter(theme.render(game));

		// Game instructions on the bottom
		TextFlow instructions = new TextFlow();
		instructions.setId("instructions");

		// Add various instructions with regular and bold text
		Label text1 = new Label("use the ");
		text1.getStyleClass().add("instructions-text");
		Label text2 = new Label("arrow keys");
		text2.getStyleClass().addAll("instructions-text", "instructions-bold");
		Label text3 = new Label(" or ");
		text3.getStyleClass().add("instructions-text");
		Label text4 = new Label("WASDF");
		text4.getStyleClass().addAll("instructions-text", "instructions-bold");
		Label text5 = new Label(" to move the board");
		text5.getStyleClass().add("instructions-text");

		instructions.getChildren().addAll(text1, text2, text3, text4, text5);

		HBox footer = new HBox();
		footer.setAlignment(Pos.CENTER);
		footer.getChildren().add(instructions);

		gameLayout.setBottom(footer);

		Scene gameScene = new Scene(gameLayout, WINDOW_WIDTH, WINDOW_HEIGHT);
		gameScene.getStylesheets().addAll("application/application.css", "application/game.css");

		gameScene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);

		primaryStage.setScene(gameScene);

	}

	public void gameOver() {
		renderLeaderboard(true);
	}

	/**
	 * Handle a key press for the game
	 *
	 * @param event KeyEvent associated with the key press
	 */
	private void handleKeyPress(KeyEvent event) {

		Direction direction;

		switch (event.getCode()) {
			case UP:
			case W:
				direction = Direction.Up;
				break;
			case RIGHT:
			case D:
				direction = Direction.Right;
				break;
			case DOWN:
			case S:
				direction = Direction.Down;
				break;
			case LEFT:
			case A:
				direction = Direction.Left;
				break;
			default:
				direction = null;
				break;
		}

		// DEBUG
		if (DEBUG)
			System.out.println("slide direction: " + direction);

		if (direction != null)
			game.slide(direction);
	}

	/**
	 * On the primary stage, display leaderboard of high scores
	 *
	 * @param inputScore Whether to display form for inputting user score;
	 *                   otherwise, just display existing scores.
	 */
	private void renderLeaderboard(boolean inputScore) {

		// Center layout
		VBox centerLayout = new VBox();
		centerLayout.setId("center-layout");
		centerLayout.setAlignment(Pos.TOP_CENTER);

		// DONE: implement a return button, and let a parameter to check if
		// it is gameOver or just that leaderBoard button is clicked
		Label title;
		if (inputScore) {
			title = new Label("Game Over");
		} else {
			title = new Label("Leaderboard");
		}
		title.setId("leaderboard-title");
		centerLayout.getChildren().add(title);

		// Possibly display user's score + input form
		if (inputScore) {
			Label scoreLabel = new Label("Your Score");
			scoreLabel.setId("score-label");

			Label scoreValue;
			if (game == null) {
				scoreValue = new Label(Integer.toBinaryString(0));
			} else {
				scoreValue = new Label(Integer.toBinaryString(Math.max(game.getScore(), 0)));
			}
			scoreValue.setId("score-value");

			// Filler element for spacing out other elements in an HBox
			Region spacer = new Region();
			HBox.setHgrow(spacer, Priority.ALWAYS);

			HBox scoreContainer = new HBox(scoreLabel, spacer, scoreValue);
			scoreContainer.setId("score-container");
			scoreContainer.setAlignment(Pos.BASELINE_CENTER);
			centerLayout.getChildren().add(scoreContainer);

			// Form to insert user's name
			TextField nameInput = new TextField();
			nameInput.setPromptText("Enter Name");
			nameInput.setId("form-name");
			HBox.setHgrow(nameInput, Priority.ALWAYS);

			Button submit = new Button("Submit");
			submit.setId("form-submit");
			submit.getStyleClass().add("small");

			HBox form = new HBox(7, nameInput, submit);
			form.setId("form");
			centerLayout.getChildren().add(form);

			Label submitted = new Label("Score has been submitted!");
			submitted.setId("submitted");

			// Form submit behavior
			EventHandler<ActionEvent> submitLeaderboard = e -> {
				if (game != null) {
					System.out.println("Register " + nameInput.getText() + " with a score of " + game.getScore());
					// get current score
					PlayerScore currentScore = new PlayerScore(nameInput.getText(), game.getScore());
					// Add current score to top score list
					leaderboard.addScoreToList(currentScore);
					// save top score to file
					saveScoreToFile();
				}
				centerLayout.getChildren().remove(form);
				centerLayout.getChildren().add(2, submitted);

			};

			nameInput.setOnAction(submitLeaderboard);
			submit.setOnAction(submitLeaderboard);

		}

		// "Play Again" button

		/** @TODO Image is purple so we need to make play icon white */
//		ImageView playIcon = new ImageView(new Image(getClass().getResourceAsStream("assets/play-icon.png")));
//		playIcon.setPreserveRatio(true);
//		playIcon.setFitWidth(12);

		HBox actionButtons;

		Button menuButton = new Button("Menu");
		menuButton.setOnAction(e -> renderMenu());

		// does not display play button if its in leaderboard-only mode
		if (inputScore) {

			ImageView playIcon = new ImageView(new Image(getClass().getResourceAsStream("assets/play-icon.png")));
			playIcon.setId("play-icon");
			playIcon.setPreserveRatio(true);
			playIcon.setFitWidth(12);
			Button playButton = new Button(" Play Again", playIcon);
			playButton.setId("play-again");
			playButton.setOnAction(e -> renderGameWithTheme(currentTheme));

			actionButtons = new HBox(15, playButton, menuButton);
		} else {
			actionButtons = new HBox(15, menuButton);
		}
		actionButtons.setAlignment(Pos.CENTER);

		centerLayout.getChildren().add(actionButtons);

		if (inputScore) {
			// Top scores
			Label topScoresHeader = new Label("Top Scores");
			topScoresHeader.setId("leaderboard-header");
			centerLayout.getChildren().add(topScoresHeader);
		}

		// Pretend high scores
//		Map<String, Integer> topScores = new LinkedHashMap<String, Integer>();
//		topScores.put("William Cong", 1194);
//		topScores.put("Faith Isaac", 1000);
//		topScores.put("Quan Nguyen", 870);
//		topScores.put("Hanyuan Wu", 512);
//		topScores.put("Michael Gira", 64);

		VBox topScoresList = new VBox();
		// sorting top score list
		PriorityQueue<PlayerScore> sortedScores = leaderboard.getTopScores();
		for (PlayerScore currentScore : sortedScores) {

			Label name = new Label(currentScore.getName());
			name.setId("leaderboard-name");

			Label score = new Label(Integer.toBinaryString(currentScore.getScore()));
			score.setId("leaderboard-score");

			// Filler element for spacing out other elements in an HBox
			Region spacer = new Region();
			HBox.setHgrow(spacer, Priority.ALWAYS);

			HBox row = new HBox(name, spacer, score);
			row.setId("leaderboard-player");
			topScoresList.getChildren().add(row);
		}
		centerLayout.getChildren().add(topScoresList);

		// Entire page layout
		BorderPane menuLayout = new BorderPane();
		menuLayout.setCenter(centerLayout);

		Scene leaderboardScene = new Scene(menuLayout, WINDOW_WIDTH, WINDOW_HEIGHT);
		leaderboardScene.getStylesheets().addAll("application/application.css", "application/leaderboard.css");
		primaryStage.setScene(leaderboardScene);
	}

	/**
	 * Get a JavaFX node that has the game's title and subtitle
	 *
	 * @param big Whether to have the large variant (with bigger font size and line
	 *            break in middle)
	 * @return The game header to display throughout the application at the top
	 */
	private Node getGameHeader(boolean big) {
		Label title = new Label(Integer.toBinaryString(2048));
		title.setId("title");

		// Optionally add line break in between the text
		if (big) {
			String titleText = title.getText();
			String firstHalf = titleText.substring(0, titleText.length() / 2);
			String secondHalf = titleText.substring(titleText.length() / 2);
			title.setText(firstHalf + System.lineSeparator() + secondHalf);
		} else {
			title.getStyleClass().add("small");
		}

		Label subtitle = new Label("2048 for nerds");
		subtitle.setId("subtitle");

		VBox gameHeader = new VBox();
		gameHeader.setAlignment(Pos.CENTER);
		gameHeader.getChildren().addAll(title, subtitle);

		return gameHeader;
	}

	// save top score list to file
	public void saveScoreToFile() {
		try {
			FileOutputStream f = new FileOutputStream(new File("leaderboard.json"));
			ObjectOutputStream o = new ObjectOutputStream(f);
			o.writeObject(leaderboard);
			o.close();
			f.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found1");
		} catch (IOException e) {
			System.out.println("Error initializing stream");
		}
	}

	// Read top score list from file
	public void loadScoreFromFile() {
		try {
			FileInputStream fi = new FileInputStream(new File("leaderboard.json"));
			ObjectInputStream oi = new ObjectInputStream(fi);
			// Read objects
			leaderboard = (GameLeaderboard) oi.readObject();
			System.out.println(leaderboard.toString());
			oi.close();
			fi.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found2");
		} catch (IOException e) {
			System.out.println("Error initializing stream");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
