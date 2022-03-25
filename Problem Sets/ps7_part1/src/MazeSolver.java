import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;

public class MazeSolver implements IMazeSolver {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
			{ -1, 0 }, // North
			{ 1, 0 }, // South
			{ 0, 1 }, // East
			{ 0, -1 } // West
	};

	private Maze maze;
	private Queue<Room> nextFrontier = new LinkedList<>();
	private Integer solvedValue = null;
	private ArrayList<Integer> numReachableArr = new ArrayList<>();

	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
	}

	private boolean canGo(int row, int col, int dir) {
		if (row + DELTAS[dir][0] < 0 || row + DELTAS[dir][0] >= maze.getRows()) return false;
		if (col + DELTAS[dir][1] < 0 || col + DELTAS[dir][1] >= maze.getColumns()) return false;

		switch (dir) {
			case NORTH:
				return !maze.getRoom(row, col).hasNorthWall();
			case SOUTH:
				return !maze.getRoom(row, col).hasSouthWall();
			case EAST:
				return !maze.getRoom(row, col).hasEastWall();
			case WEST:
				return !maze.getRoom(row, col).hasWestWall();
		}
		return false;
	}

	public void resetMaze(Maze maze) {
		for (int i = 0; i < maze.getRows(); i++) {
			for (int j = 0; j < maze.getColumns(); j++) {
				Room currentRoom = maze.getRoom(i, j);
				currentRoom.isDestination = false;
				currentRoom.isVisited = false;
				currentRoom.onPath = false;
				currentRoom.parentRoom = null;
				currentRoom.isAddedToFrontier = false;
			}
		}
		this.nextFrontier = new LinkedList<>();
		this.solvedValue = null;
		this.numReachableArr = new ArrayList<>() {
			{add(1);}
		};
	}

	public void markPath(Room destinationRoom) {
		Room currentRoom = destinationRoom;
		while (true) {
			Room parentRoom = currentRoom.parentRoom;
			if (parentRoom == null) {
				currentRoom.onPath = true;
				break;
			}
			currentRoom.onPath = true;
			currentRoom = parentRoom;
		}
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		resetMaze(maze);

		Room endRoom = maze.getRoom(endRow, endCol);
		endRoom.isDestination = true;
		endRoom.row = endRow;
		endRoom.col = endCol;

		Room startRoom = maze.getRoom(startRow, startCol);
		startRoom.row = startRow;
		startRoom.col = startCol;
		startRoom.currentCount = 0;

		nextFrontier.add(startRoom);
		return solve(0);
	}

	public Integer solve(Integer roomCount) {
		// System.out.println("\nCurrent Frontier: " + nextFrontier.toString());
		Room nextRoom = nextFrontier.peek();
		if (nextRoom == null) return this.solvedValue;
		return traverse(nextRoom, nextRoom.currentCount);
	}

	public void getNextFrontier(Room currentRoom) {
		for (int direction = 0; direction < 4; direction++) {
			if (!canGo(currentRoom.row, currentRoom.col, direction)) continue;

			int nextRow = currentRoom.row + DELTAS[direction][0];
			int nextCol = currentRoom.col + DELTAS[direction][1];
			Room nextRoom = this.maze.getRoom(nextRow, nextCol);

			if (nextRoom.isVisited || nextRoom.isAddedToFrontier) continue;
			// System.out.println(currentRoom.row + ", " + currentRoom.col
			// 	 	+ "|| next: " + nextRow + ", " + nextCol + " || count: " + (currentRoom.currentCount + 1));

			nextRoom.row = nextRow;
			nextRoom.col = nextCol;
			nextRoom.currentCount = currentRoom.currentCount + 1;
			nextRoom.parentRoom = currentRoom;
			nextRoom.isAddedToFrontier = true;
			nextFrontier.add(nextRoom);

			// System.out.println(nextRoom.currentCount + " > " + (this.numReachableArr.size() - 1));
			if (nextRoom.currentCount > this.numReachableArr.size() - 1) {
				this.numReachableArr.add(1);
				// System.out.println(this.numReachableArr);
				continue;
			}
			int currVal = this.numReachableArr.get(nextRoom.currentCount);
			this.numReachableArr.set(nextRoom.currentCount, currVal + 1);
			// System.out.println(this.numReachableArr);
		}
	}

	public Integer traverse(Room currentRoom, Integer roomCount) {
		if (currentRoom.isDestination) {
			// System.out.println("---Reached Destination---");
			markPath(currentRoom);
			this.solvedValue = roomCount;
			currentRoom.isVisited = true;
			return roomCount;
		}
		nextFrontier.poll();
		if (currentRoom.isVisited) return solve(roomCount);

		// System.out.println(currentRoom + " Current Count: " + roomCount);
		getNextFrontier(currentRoom);
		currentRoom.isVisited = true;

		return solve(roomCount);
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}
		if (k < 0) {
			throw new IllegalArgumentException("Invalid k value");
		}

		// System.out.println("Calling Loop: " + numReachableArr);
		while (true) {
			Room nextRoom = nextFrontier.peek();
			if (nextRoom == null || nextRoom.currentCount >= k)
				break;
			getNextFrontier(nextFrontier.poll());
		}
		if (k >= this.numReachableArr.size()) return 0;
		return this.numReachableArr.get(k);
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("maze-empty.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);

//			System.out.println(solver.pathSearch(1, 1, 1, 1)); // --> 8
//			MazePrinter.printMaze(maze);
//			System.out.println(solver.pathSearch(0, 0, 2, 3)); // --> 7
//			ImprovedMazePrinter.printMaze(maze);
//			System.out.println(solver.pathSearch(1, 1, 3, 0)); // --> 8
//			ImprovedMazePrinter.printMaze(maze);
//			System.out.println(solver.pathSearch(1, 1, 2, 3)); // --> 7
//			ImprovedMazePrinter.printMaze(maze);
//			System.out.println(solver.pathSearch(1, 1, 3, 1)); // --> 7
//			ImprovedMazePrinter.printMaze(maze);
			System.out.println(solver.pathSearch(0, 0, 1, 1)); // --> 4
			ImprovedMazePrinter.printMaze(maze);

			for (int i = 0; i <= 9; ++i) {
				// System.out.println("");
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
