package application;

/**
 * Represents the actions that can happen to a square after sliding it
 * 
 * @author Michael Gira
 *
 */
public enum SlideEventAction {
	// Literally nothing
	None,
	// Square (moving farther) combines by sliding over another square
	CombineOver,
	// Square (moving least) combines by being slid over
	CombineUnder
}
