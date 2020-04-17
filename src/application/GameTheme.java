package application;

import javafx.scene.Node;

/**
 * Defines a game theme which takes in the game's state and outputs the JavaFX
 * stuff
 * 
 * @author Michael Gira
 *
 */
public interface GameTheme {
	public Node render(Game game);
}
