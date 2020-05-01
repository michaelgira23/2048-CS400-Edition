package application;

import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Renders the game in the classic 2048 style but with binary numbers
 * 
 * @author Michael Gira
 *
 */
public class BinaryTheme implements GameTheme {

	// Grid layout to render all the tiles
	GridPane grid = new GridPane();

	/**
	 * Renders the game with binary numbers
	 * 
	 * @param game Game state to render
	 */
	public Node render(Game game) {
		grid.getStylesheets().addAll("application/binary-theme.css");
		grid.setAlignment(Pos.CENTER);

		// Set a margin of 8 pixels between tiles
		grid.setHgap(8);
		grid.setVgap(8);

		// Initially draw tiles
		updateBoard(game.getBoard());

		// Reflect any new changes when the game state changes
		game.setSlideHandler(slides -> {
			// Batch the transitions altogether
			ParallelTransition translations = new ParallelTransition();

			for (SlideEvent slide : slides) {

				// Get the node we're supposed to move
				Node tile = getTileFromGrid(grid, slide.fromRow, slide.fromColumn);

				// If tile is null, the player may be sliding the tiles very fast.
				// This means the previous move's animation hasn't stopped playing, but it will
				// update the real game state later anyways
				if (tile == null) {
					continue;
				}

				// Make sure tile is in front of background nodes
				tile.toFront();

				Bounds tileBounds = tile.getBoundsInParent();

				// Get the empty tile at the position we're trying to move to, so that we can
				// get the new coordinates
				Node targetTile = getBackgroundTileFromGrid(grid, slide.toRow, slide.toColumn);

				if (targetTile == null) {
					continue;
				}

				Bounds targetBounds = targetTile.getBoundsInParent();

				TranslateTransition translate = new TranslateTransition(Duration.millis(200), tile);
				translate.setInterpolator(Interpolator.EASE_OUT);
				translate.setByX(targetBounds.getCenterX() - tileBounds.getCenterX());
				translate.setByY(targetBounds.getCenterY() - tileBounds.getCenterY() + 5);

				translations.getChildren().add(translate);
			}

			// Once animation finishes, replace the animated tiles with the new ones at
			// their updated position
			translations.setOnFinished(e -> {
				updateBoard(game.getBoard());
			});

			translations.play();
		});

		return grid;
	}

	/**
	 * Update the grid with a 2D array of game tiles
	 * 
	 * @param board 2D array of game tiles
	 */
	private void updateBoard(GameSquare[][] board) {
		// Clear grid
		grid.getChildren().clear();

		for (int i = 0; i < board.length; i++) {
			GameSquare[] row = board[i];

			for (int j = 0; j < row.length; j++) {
				GameSquare tile = row[j];

				VBox backgroundTile = new VBox();
				backgroundTile.getStyleClass().addAll("tile", "empty");
				backgroundTile.setAlignment(Pos.CENTER);
				grid.add(backgroundTile, j, i);

				// Place game tile
				if (tile != null) {
					VBox displayTile = new VBox();
					displayTile.setAlignment(Pos.CENTER);
					displayTile.getStyleClass().addAll("tile", "value", "value-" + tile.getValue());

					// Display number value of tile
					Label label = new Label(Integer.toBinaryString(tile.getValue()));
					label.getStyleClass().add("value-label");
					displayTile.getChildren().add(label);

					grid.add(displayTile, j, i);
				}

			}
		}
	}

	/**
	 * Search for a specific game tile by its position in a grid
	 * 
	 * @param grid   Grid containing nodes
	 * @param row    Row of the target node
	 * @param column Column of the target node
	 * @return Node in the specified position
	 */
	private Node getTileFromGrid(GridPane grid, int row, int column) {
		for (Node node : grid.getChildren()) {
			if (node.getStyleClass().contains("value") && GridPane.getRowIndex(node) == row
					&& GridPane.getColumnIndex(node) == column) {
				return node;
			}
		}
		return null;
	}

	/**
	 * Search for a specific background tile by its position in a grid
	 * 
	 * @param grid   Grid containing nodes
	 * @param row    Row of the target node
	 * @param column Column of the target node
	 * @return Node in the specified position
	 */
	private Node getBackgroundTileFromGrid(GridPane grid, int row, int column) {
		for (Node node : grid.getChildren()) {
			if (node.getStyleClass().contains("empty") && GridPane.getRowIndex(node) == row
					&& GridPane.getColumnIndex(node) == column) {
				return node;
			}
		}
		return null;
	}
}
