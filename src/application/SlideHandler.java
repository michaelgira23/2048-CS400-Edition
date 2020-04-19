package application;

/**
 * Represents a handler for animating the GUI in response to tile sliding
 * 
 * @author Michael Gira
 *
 */
public interface SlideHandler {
	// Receive array of SlidEvents that will represent the diffs from the old board
	// to the current board
	public void handle(SlideEvent[] event);
}
