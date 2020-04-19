package application;

public class SlideEvent {
	public GameSquare tile;
	public boolean increment;
	public int fromRow;
	public int fromColumn;

	public int toRow;
	public int toColumn;

	public SlideEvent(GameSquare tile, boolean increment, int fromRow, int fromColumn, int toRow, int toColumn) {
		this.tile = tile;
		this.increment = increment;
		this.fromRow = fromRow;
		this.fromColumn = fromColumn;
		this.toRow = toRow;
		this.toColumn = toColumn;
	}
}
