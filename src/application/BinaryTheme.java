package application;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

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
		Label title = new Label("[PRETEND THIS IS OUR GAME]");
		BorderPane menu = new BorderPane();
		menu.setCenter(title);

		return menu;
	}
}
