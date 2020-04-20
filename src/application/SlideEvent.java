package application;

/**
 * A slide event which represents one tile moving when the player slides the
 * entire board
 * 
 * @author Michael Gira
 *
 */
public class SlideEvent {
	public GameSquare tile;
	public SlideEventAction action;

	public int fromRow;
	public int fromColumn;

	public int toRow;
	public int toColumn;

	/**
	 * Create a slide event that represents one tile moving
	 * 
	 * @param tile       Internal game square that is being moved
	 * @param action     What happens after the square moves (Nothing, combined,
	 *                   etc.)
	 * @param fromRow    Original row tile is moving from
	 * @param fromColumn Original column tile is moving from
	 * @param toRow      Target row tile is moving to
	 * @param toColumn   Target column tile is moving to
	 */
	public SlideEvent(GameSquare tile, SlideEventAction action, int fromRow, int fromColumn, int toRow, int toColumn) {
		this.tile = tile;
		this.action = action;
		this.fromRow = fromRow;
		this.fromColumn = fromColumn;
		this.toRow = toRow;
		this.toColumn = toColumn;
	}

	/**
	 * Two slide events are equal if they have the same tile reference
	 * 
	 * @return Whether the slide events have the same tile
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof SlideEvent) {
			return tile.equals(((SlideEvent) other).tile);
		} else {
			return false;
		}
	}

	/**
	 * Return the hash code of the tile
	 * 
	 * @return Hash code of the tile
	 */
	@Override
	public int hashCode() {
		return tile.hashCode();
	}
}
