package application;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Main game and GUI control
 *
 * @author Michael Gira, Quan Nguyen, Hanyuan Wu, Will Cong, Faith Isaac
 *
 */
public class Main extends Application {

	// Height of GUI window
	private static final int WINDOW_WIDTH = 600;
	private static final int WINDOW_HEIGHT = 700;

	// Title to display on GUI
	private static final String APP_TITLE = "2048: CS400 Edition";

	// Random number generator seed
	private int seed = 0;

	// Java program arguments
	private List<String> args;

	// Primary JavaFX stage
	private Stage primaryStage;

	// Instance internal logic of the game
	private Game game;

	// Current game theme that renders the game tiles
	private GameTheme currentTheme = new BinaryTheme();

	// JaveFX file chooser for leaderboard JSON file
	FileChooser fileChooser = new FileChooser();

	// Default path to load leaderboard data JSON file
	private String leaderboardPath = "leaderboard.json";
	private boolean isDefaultLeaderboardPath = true;

	// Leader board score
	private GameLeaderboard leaderboard;

	/**
	 * Sets up initial game screen
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		args = this.getParameters().getRaw();
		System.out.println("Args: " + args);

		fileChooser.setTitle("Load 2048 Leaderboard");
		fileChooser.setInitialDirectory(new File("."));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));

		this.primaryStage = primaryStage;
		primaryStage.setTitle(APP_TITLE);
		renderMenu();
		primaryStage.show();
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

		// Play icon
		ImageView playIcon = new ImageView(new Image(getClass().getResourceAsStream("assets/play-icon.png")));
		playIcon.setPreserveRatio(true);
		playIcon.setFitWidth(22);

		// Play button
		Button playButton = new Button("", playIcon);
		playButton.setId("menu-button");
		playButton.setOnAction(e -> renderGameWithTheme(currentTheme));

		// Leaderboard icon
		ImageView leaderboardIcon = new ImageView(
				new Image(getClass().getResourceAsStream("assets/leaderboard-icon.png")));
		leaderboardIcon.setPreserveRatio(true);
		leaderboardIcon.setFitWidth(10);

		// Leaderboard button
		Button leaderboardButton = new Button(" Leaderboard", leaderboardIcon);
//		leaderboardButton.setId("menu-button");
		leaderboardButton.getStyleClass().add("small");
		leaderboardButton.setOnAction(e -> renderLeaderboard(false, GameLeaderboardSortMode.Score));

		// File selector for leaderboard JSON file
		Label leaderboardPathLabel = new Label(
				isDefaultLeaderboardPath ? "Default: " + leaderboardPath : leaderboardPath);
		Button chooseLeaderboardPath = new Button("Load Leaderboard File");
		chooseLeaderboardPath.setOnAction(e -> {
			File file = fileChooser.showOpenDialog(primaryStage);
			if (file != null) {
				leaderboardPath = file.getPath();
				leaderboardPathLabel.setText(leaderboardPath);
				isDefaultLeaderboardPath = false;
			}
		});
		VBox leaderboardSelect = new VBox(15, chooseLeaderboardPath, leaderboardPathLabel);
		leaderboardSelect.setAlignment(Pos.CENTER);

//		HBox menuButtons = new HBox(15, playButton, leaderboardButton);
		VBox menuButtons = new VBox(20, playButton, leaderboardButton, leaderboardSelect);
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

		ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("assets/back-icon.png")));
		backIcon.setId("back-icon");
		backIcon.setPreserveRatio(true);
		backIcon.setFitWidth(12);

		// Menu button
		Button menuButton = new Button("Menu");
		menuButton.setId("menu-small");
		menuButton.setOnAction(e -> renderMenu());

		// Restart button
		ImageView restartIcon = new ImageView(new Image(getClass().getResourceAsStream("assets/restart-icon.png")));
		restartIcon.setId("restart-icon");
		restartIcon.setPreserveRatio(true);
		restartIcon.setFitWidth(24);
		Button restartButton = new Button("", restartIcon);
		restartButton.setId("restart");
		restartButton.setOnAction(e -> renderGameWithTheme(theme));

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);

		HBox actionButtons = new HBox(330, menuButton, restartButton);
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

	/**
	 * Handler for when the game ends
	 */
	public void gameOver() {
		renderLeaderboard(true, GameLeaderboardSortMode.Score);
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

		if (direction != null)
			game.slide(direction);
	}

	/**
	 * On the primary stage, display leaderboard of high scores
	 *
	 * @param inputScore Whether to display form for inputting user score;
	 *                   otherwise, just display existing scores.
	 */
	private void renderLeaderboard(boolean inputScore, GameLeaderboardSortMode sortMode) {
		leaderboard = GameLeaderboard.load(leaderboardPath);

		// Center layout
		VBox centerLayout = new VBox();
		centerLayout.setId("center-layout");
		centerLayout.setAlignment(Pos.TOP_CENTER);

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
				scoreValue = new Label(Long.toBinaryString(0));
			} else {
				scoreValue = new Label(Long.toBinaryString(Math.max(game.getScore(), 0)));
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

			Label submittedLabel = new Label("Score has been submitted!");
			submittedLabel.setId("submitted");

			Label errorLabel = new Label("Error saving score!");
			errorLabel.setId("error");

			// Form submit behavior
			EventHandler<ActionEvent> submitLeaderboard = e -> {
				if (game != null) {
					System.out.println("Register " + nameInput.getText() + " with a score of " + game.getScore());
					// Get current score
					PlayerScore currentScore = new PlayerScore(nameInput.getText(), game.getScore());
					// Add current score to top score list
					leaderboard.add(currentScore);
					// Save top score to file
					try {
						leaderboard.export(leaderboardPath);
						centerLayout.getChildren().remove(form);
						centerLayout.getChildren().add(2, submittedLabel);
					} catch (IOException error) {
						System.out.println("Error saving leaderboard to file!");
						error.printStackTrace();
						centerLayout.getChildren().remove(form);
						centerLayout.getChildren().add(2, errorLabel);
					}
				}

			};

			nameInput.setOnAction(submitLeaderboard);
			submit.setOnAction(submitLeaderboard);

		}

		Button menuButton = new Button("Menu");
		menuButton.setId("menu");
		menuButton.setOnAction(e -> renderMenu());

		// Do not display play button if its in leaderboard-only mode
		HBox actionButtons;
		if (inputScore) {

			ImageView playIcon = new ImageView(new Image(getClass().getResourceAsStream("assets/play-icon.png")));
			playIcon.setId("play-icon");
			playIcon.setPreserveRatio(true);
			playIcon.setFitWidth(12);
			Button playButton = new Button(" Play Again", playIcon);
			playButton.setId("play-again");
			playButton.setOnAction(e -> renderGameWithTheme(currentTheme));

			actionButtons = new HBox(15, menuButton, playButton);

		} else {
			// Button for changing whether to sort by date or score
			Button sortModeButton;
			if (sortMode == GameLeaderboardSortMode.Score || sortMode == GameLeaderboardSortMode.ScoreReversed) {
				sortModeButton = new Button("by date");
				sortModeButton.setOnAction(e -> {
					renderLeaderboard(false, GameLeaderboardSortMode.Date);
				});
			} else {
				sortModeButton = new Button("by score");
				sortModeButton.setOnAction(e -> {
					renderLeaderboard(false, GameLeaderboardSortMode.Score);
				});
			}
			sortModeButton.setId("sort-mode");

			// Icon for manipulating sorting direction
			ImageView sortIcon = null;
			if (sortMode == GameLeaderboardSortMode.Score || sortMode == GameLeaderboardSortMode.Date) {
				sortIcon = new ImageView(new Image(getClass().getResourceAsStream("assets/sort-asc-icon.png")));
			} else if (sortMode == GameLeaderboardSortMode.ScoreReversed
					|| sortMode == GameLeaderboardSortMode.DateReversed) {
				sortIcon = new ImageView(new Image(getClass().getResourceAsStream("assets/sort-dsc-icon.png")));
			}
			sortIcon.setId("sort-icon");
			sortIcon.setPreserveRatio(true);
			sortIcon.setFitWidth(14);

			// Reverse the direction for the respective sorting modes
			Button sortButton = new Button("", sortIcon);
			sortButton.setId("sort");
			sortButton.setOnAction(e -> {
				switch (sortMode) {
					case Score:
						renderLeaderboard(false, GameLeaderboardSortMode.ScoreReversed);
						break;
					case ScoreReversed:
						renderLeaderboard(false, GameLeaderboardSortMode.Score);
						break;
					case Date:
						renderLeaderboard(false, GameLeaderboardSortMode.DateReversed);
						break;
					case DateReversed:
						renderLeaderboard(false, GameLeaderboardSortMode.Date);
						break;
				}
			});

			ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("assets/back-icon.png")));
			backIcon.setId("back-icon");
			backIcon.setPreserveRatio(true);
			backIcon.setFitWidth(6);

			menuButton = new Button(" Menu", backIcon);
			menuButton.setId("menu-small");
			menuButton.setOnAction(e -> renderMenu());

			Region spacer = new Region();
			HBox.setHgrow(spacer, Priority.ALWAYS);
			actionButtons = new HBox(15, menuButton, spacer, sortModeButton, sortButton);
		}
		actionButtons.setAlignment(Pos.CENTER);

		centerLayout.getChildren().add(actionButtons);

		if (inputScore) {
			// Top scores
			Label topScoresHeader = new Label("Top Scores");
			topScoresHeader.setId("leaderboard-header");
			centerLayout.getChildren().add(topScoresHeader);
		}

		VBox topScoresList = new VBox();
		// Display more scores if there's no form taking up space
		if (inputScore) {
			listScores(topScoresList, sortMode, 5);
		} else {
			listScores(topScoresList, sortMode, 15);
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
	 * List a certain amount of scores in a certain order
	 * 
	 * @param sortMode Mode for which to sort the scores
	 */
	private void listScores(VBox list, GameLeaderboardSortMode sortMode, int numToList) {

		PlayerScore[] sortedScores;
		switch (sortMode) {
			case Score:
				sortedScores = leaderboard.getTopScores(false);
				break;
			case ScoreReversed:
				sortedScores = leaderboard.getTopScores(true);
				break;
			case Date:
				sortedScores = leaderboard.sortedTopScoresByTime(false);
				break;
			case DateReversed:
				sortedScores = leaderboard.sortedTopScoresByTime(true);
				break;
			default:
				sortedScores = new PlayerScore[] {};
				break;
		}

		for (int i = 0; i < Math.min(numToList, sortedScores.length); i++) {
			PlayerScore currentScore = sortedScores[i];
			Label name = new Label(currentScore.getName());
			name.setId("leaderboard-name");

			Label score = new Label(Long.toBinaryString(currentScore.getScore()));
			score.setId("leaderboard-score");

			// Filler element for spacing out other elements in an HBox
			Region spacer = new Region();
			HBox.setHgrow(spacer, Priority.ALWAYS);

			HBox row = new HBox(name, spacer, score);
			row.setId("leaderboard-player");
			list.getChildren().add(row);
		}
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

}
