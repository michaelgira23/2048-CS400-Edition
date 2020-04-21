package application;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
 * @author Michael Gira
 *
 */
public class Main extends Application {

	private static final int WINDOW_WIDTH = 600;
	private static final int WINDOW_HEIGHT = 700;
	private static final String APP_TITLE = "2048: CS400 Edition";

	private List<String> args;
	private Stage primaryStage;

	private Game game = new Game();
	private GameTheme currentTheme = new BinaryTheme();

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

		// Play button
		ImageView playIcon = new ImageView(new Image(getClass().getResourceAsStream("assets/play-icon.png")));
		playIcon.setPreserveRatio(true);
		playIcon.setFitWidth(22);

		Button playButton = new Button("Play", playIcon);
		playButton.setId("menu-button");
		playButton.setOnAction(e -> renderGameWithTheme(currentTheme));

		// Leaderboard button
		ImageView leaderboardIcon = new ImageView(
				new Image(getClass().getResourceAsStream("assets/leaderboard-icon.png")));
		leaderboardIcon.setPreserveRatio(true);
		leaderboardIcon.setFitWidth(22);

		Button leaderboardButton = new Button("Leaderboard", leaderboardIcon);
		leaderboardButton.setId("menu-button");
		leaderboardButton.setOnAction(e -> renderLeaderboard(true));

//		HBox menuButtons = new HBox(15, playButton, leaderboardButton);
		VBox menuButtons = new VBox(15, playButton, leaderboardButton);
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
		BorderPane gameLayout = new BorderPane();

		// Game title + action buttons at the top
		Button menuButton = new Button("Menu");
		menuButton.setOnAction(e -> renderMenu());

		Button leaderboardButton = new Button("Leaderboard");
		leaderboardButton.setOnAction(e -> renderLeaderboard(true));

		HBox actionButtons = new HBox(15, menuButton, leaderboardButton);
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

		gameScene.addEventFilter(KeyEvent.KEY_PRESSED, e -> handleKeyPress(e));

		primaryStage.setScene(gameScene);
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

		System.out.println("slide direction: " + direction);
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

		// Either "Leaderboard" or "Game Over" header
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

			Label scoreValue = new Label(String.valueOf(game.getScore()));
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

			// Form submit behavior
			EventHandler<ActionEvent> submitLeaderboard = e -> {
				System.out.println("Register " + nameInput.getText() + " with a score of " + game.getScore());
			};

			nameInput.setOnAction(submitLeaderboard);
			submit.setOnAction(submitLeaderboard);

			HBox form = new HBox(5, nameInput, submit);
			form.setId("form");
			centerLayout.getChildren().add(form);
		}

		// "Play Again" button

		/** @TODO Image is purple so we need to make play icon white */
//		ImageView playIcon = new ImageView(new Image(getClass().getResourceAsStream("assets/play-icon.png")));
//		playIcon.setPreserveRatio(true);
//		playIcon.setFitWidth(12);

		Button playButton;
		if (inputScore) {
			playButton = new Button("Play Again");
		} else {
			playButton = new Button("Play");
		}
		playButton.setId("play-again");
		playButton.setOnAction(e -> renderGameWithTheme(currentTheme));

		Button menuButton = new Button("Menu");
		menuButton.setOnAction(e -> renderMenu());

		HBox actionButtons = new HBox(15, playButton, menuButton);
		actionButtons.setAlignment(Pos.CENTER);

		centerLayout.getChildren().add(actionButtons);

		if (inputScore) {
			// Top scores
			Label topScoresHeader = new Label("Top Scores");
			topScoresHeader.setId("leaderboard-header");
			centerLayout.getChildren().add(topScoresHeader);
		}

		// Pretend high scores
		Map<String, Integer> topScores = new LinkedHashMap<String, Integer>();
		topScores.put("William Cong", 1194);
		topScores.put("Faith Isaac", 1000);
		topScores.put("Quan Nguyen", 870);
		topScores.put("Hanyuan Wu", 512);
		topScores.put("Michael Gira", 64);

		VBox topScoresList = new VBox();
		for (Map.Entry<String, Integer> entry : topScores.entrySet()) {

			Label name = new Label(entry.getKey());
			name.setId("leaderboard-name");

			Label score = new Label(entry.getValue().toString());
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
}
