package application;

/**
 * Represents a handler for animating the GUI in response to tile sliding
 * 
 * @author Michael Gira
 *
 */
public interface SlideHandler {
	public void handle(SlideEvent[] event, SlideDoneHandler done);
}
