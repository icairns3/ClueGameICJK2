package clueGame;

public class BoardCell {
	private int row, column;
	char initial = ' ';
	private boolean isDoorway = false;
	private boolean isWalkway = false;
	boolean isRoom = false;
	DoorDirection doorDirection = DoorDirection.NONE;

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public BoardCell(int row, int column, char myChar) {
		super();
		this.row = row;
		this.column = column;
		this.initial = myChar;
		if (myChar == 'W'){
			isWalkway = true;
		}
		else if (myChar != 'X')
			isRoom = true;

	}

	public boolean isDoorway() {
		return isDoorway;
	}

	public void setToDoorway(DoorDirection doorDirection) {
		if (!(doorDirection == DoorDirection.NONE)) {
			isDoorway = true;
			this.doorDirection = doorDirection;
		}
	}

	public boolean isWalkway() {
		return isWalkway;
	}

	public boolean isRoom() {
		return isRoom;
	}

	public DoorDirection getDoorDirection() {
		return this.doorDirection;
	}

	public char getInitial() {
		return initial;
	}

}
