package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Board {

	// variable used for singleton pattern
	private static Board theInstance = new Board();
	// ctor is private to ensure only one can be created
	private String layout;
	private String legend;

	private Board() {
	}

	private final int MAX_BOAR_SIZE = 50;
	private BoardCell[][] grid;
	private int numRows, numCols;
	private Map<Character, String> legendMap;
	private Set<BoardCell> adjList;
	private Map<BoardCell, Set<BoardCell>> adjMap= new HashMap<BoardCell, Set<BoardCell>>();
	

	// this method returns the only Board
	public static Board getInstance() {
		return theInstance;
	}

	public void setConfigFiles(String x, String y) {
		layout = x;
		legend = y;

	}

	public void initialize() {

		// Create Legend
		FileReader reader = null;
		try {
			reader = new FileReader(legend);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scanner in = new Scanner(reader);
		legendMap = new HashMap<Character, String>();
		while (in.hasNextLine()) {
			String line = in.nextLine();
			int tracker = 3;
			while (line.charAt(tracker) != ',')
				tracker++;
			legendMap.put(line.charAt(0), line.substring(3, tracker));
		}

		// Create Layout
		try {
			reader = new FileReader(layout);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		grid = new BoardCell[MAX_BOAR_SIZE][MAX_BOAR_SIZE];
		in = new Scanner(reader);
		numRows = 0;
		String line = "";
		int tracker = 0;
		while (in.hasNextLine()) {
			line = in.nextLine();
			numCols = 0;
			char last = ',';
			for (int i = 0; i < line.length(); i++) {
				if (last == ',') {
					grid[numCols][numRows] = new BoardCell(numCols, numRows, line.charAt(i));
					numCols++;
				} else if (line.charAt(i) != ',') {
					if (grid[numCols - 1][numRows] != null) {
						switch (line.charAt(i)) {
						case 'D':
							grid[numCols - 1][numRows].setToDoorway(DoorDirection.DOWN);
							break;
						case 'U':
							grid[numCols - 1][numRows].setToDoorway(DoorDirection.UP);
							break;
						case 'R':
							grid[numCols - 1][numRows].setToDoorway(DoorDirection.RIGHT);
							break;
						case 'L':
							grid[numCols - 1][numRows].setToDoorway(DoorDirection.LEFT);
							break;
						default:
							grid[numCols - 1][numRows].setToDoorway(DoorDirection.NONE);
							break;
						}
					}

				}
				last = line.charAt(i);
			}
			numRows++;

		}
		BoardCell boardKey;
		BoardCell boardValue;
		for (int x = 0; x < numRows; x++) {
			for (int y = 0; y < numCols; y++) {
				boardKey = grid[y][x];
				if(boardKey ==grid[22][14]){
				
				}
				
				if(boardKey!=null){
				if(boardKey.isDoorway()){
					switch (boardKey.getDoorDirection()){
					case RIGHT:
						if (x + 1 < numCols) {
							boardValue = grid[y+1][x];
							addToAdjMap(boardKey, boardValue);
						}
						break;
					case LEFT:
						if (x - 1 >= 0) {
							boardValue = grid[y-1][x];
							addToAdjMap(boardKey, boardValue);
						}
						break;
					case UP:
						if (y - 1 >= 0) {
							boardValue = grid[y][x-1];					
							addToAdjMap(boardKey, boardValue);
								
						}
						break;
					case DOWN:
						if (y + 1 < numRows) {
							boardValue = grid[y][x+1];
							addToAdjMap(boardKey, boardValue);
						}
						break;
					case NONE:
						break;
					}
				}
				else{
					if (x - 1 >= 0) {
						boardValue = grid[y][x - 1];
						if(boardValue.isWalkway()||boardValue.getDoorDirection()==DoorDirection.DOWN)
							addToAdjMap(boardKey, boardValue);
					}
					if (y - 1 >= 0) {
						boardValue = grid[y - 1][x];					
						if(boardValue.isWalkway()||boardValue.getDoorDirection()==DoorDirection.RIGHT)
							addToAdjMap(boardKey, boardValue);
							
					}
					if (x + 1 < numRows) {
						boardValue = grid[y][x + 1];					
						if(boardValue.isWalkway()||boardValue.getDoorDirection()==DoorDirection.UP)
							addToAdjMap(boardKey, boardValue);
							
					}
					if (y + 1 < numCols) {
						boardValue = grid[y + 1][x];
						if(boardValue.isWalkway()||boardValue.getDoorDirection()==DoorDirection.LEFT)
							addToAdjMap(boardKey, boardValue);
					
					}
				}
				
					if(!adjMap.containsKey(boardKey)){
						Set<BoardCell> temp = new HashSet<BoardCell>();
						adjMap.put(boardKey, temp);
					}
				}
			}
				
		}

	}

	private void addToAdjMap(BoardCell boardKey, BoardCell boardValue) {
		// TODO Auto-generated method stub
		if(boardValue!=null && boardKey!=null){
			if(boardKey.isWalkway()|| boardKey.isDoorway()){
				if(boardValue.isWalkway() || boardValue.isDoorway()){
					if(adjMap.containsKey(boardKey)){
						adjMap.get(boardKey).add(boardValue);
					}
					else{
						Set<BoardCell> temp = new HashSet<BoardCell>();
						temp.add(boardValue);
						adjMap.put(boardKey, temp);
					}
				}
			}
		}
	}

	public void loadRoomConfig() throws BadConfigFormatException {
		// Create Legend
		FileReader reader = null;
		try {
			reader = new FileReader(legend);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scanner in = new Scanner(reader);
		legendMap = new HashMap<Character, String>();
		while (in.hasNextLine()) {
			String line = in.nextLine();
			int tracker = 3;
			while (line.charAt(tracker) != ',')
				tracker++;
			legendMap.put(line.charAt(0), line.substring(3, tracker));
			System.out.println(line.substring(tracker + 2));
			if (!line.substring(tracker + 2).equals("Card") && !line.substring(tracker + 2).equals("Other"))
				throw new BadConfigFormatException();
		}
	}

	public void loadBoardConfig() throws BadConfigFormatException {
		int numColsPast = -1;
		FileReader reader = null;
		try {
			reader = new FileReader(layout);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		grid = new BoardCell[MAX_BOAR_SIZE][MAX_BOAR_SIZE];
		Scanner in = new Scanner(reader);
		numRows = 0;
		String line = "";
		while (in.hasNextLine()) {
			line = in.nextLine();
			numCols = 0;
			char last = ',';
			for (int i = 0; i < line.length(); i++) {
				if (last == ',') {
					if (!legendMap.containsKey(line.charAt(i)))
						throw new BadConfigFormatException();
					grid[numCols][numRows] = new BoardCell(numCols, numRows, line.charAt(i));
					numCols++;
				} else if (line.charAt(i) != ',') {
					System.out.println("did it");
					if (grid[numCols - 1][numRows] != null) {
						switch (line.charAt(i)) {
						case 'D':
							grid[numCols - 1][numRows].setToDoorway(DoorDirection.DOWN);
							break;
						case 'U':
							grid[numCols - 1][numRows].setToDoorway(DoorDirection.UP);
							break;
						case 'R':
							grid[numCols - 1][numRows].setToDoorway(DoorDirection.RIGHT);
							break;
						case 'L':
							grid[numCols - 1][numRows].setToDoorway(DoorDirection.LEFT);
							break;
						default:
							grid[numCols - 1][numRows].setToDoorway(DoorDirection.NONE);
							break;
						}
					}

				}
				last = line.charAt(i);
			}
			if (numColsPast != -1 && numColsPast != numCols)
				throw new BadConfigFormatException();
			numColsPast = numCols;
			numRows++;

		}
	}

	public Map<Character, String> getLegend() {
		return legendMap;
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numCols;
	}

	public BoardCell getCellAt(int x, int y) {
		return grid[y][x];
	}

	public Set<BoardCell> getAdjList(int x, int y) {
		return adjMap.get(grid[y][x]);
	}

	public void calcTargets(int i, int j, int k) {
		// TODO Auto-generated method stub

	}

	public Set<BoardCell> getTargets() {
		// TODO Auto-generated method stub
		return new HashSet<BoardCell>();
	}
}
