package application;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * Renders the game in the classic 2048 style but with binary numbers
 * 
 * @author Michael Gira
 *
 */
public class BinaryTheme implements GameTheme {

	/**
	 * Renders the game
	 */
	public Node render(Game game) {
		GridPane grid = new GridPane();
		grid.getStylesheets().addAll("application/binary-theme.css");
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(8);
		grid.setVgap(8);

		GameSquare[][] board = game.getBoard();
		for (int i = 0; i < board.length; i++) {
			GameSquare[] row = board[i];

			for (int j = 0; j < row.length; j++) {
				GameSquare tile = row[j];

				VBox displayTile = new VBox();
				displayTile.getStyleClass().add("tile");
				displayTile.setAlignment(Pos.CENTER);

				if (tile == null) {
					// Place empty placeholder tile
					displayTile.getStyleClass().add("empty");
					Label label = new Label("empty");
					displayTile.getChildren().add(label);
				} else {
					// Place game tile
					displayTile.getStyleClass().addAll("value", "value-" + tile.getValue());
					Label label = new Label(String.valueOf(tile.getValue()));
					displayTile.getChildren().add(label);
				}

				grid.add(displayTile, j, i);
			}
		}

		return grid;
	}
}
