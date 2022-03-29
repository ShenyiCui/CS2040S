import java.util.*;

public class MazeSolverWithPower implements IMazeSolverWithPower {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
		{ -1, 0 }, // North
		{ 1, 0 }, // South
		{ 0, 1 }, // East
		{ 0, -1 } // West
	};
	private Maze maze;
	private HashMap<Room, HashMap<Integer, Node>> mazeNodeMap = new HashMap<>();
	private HashMap<Room, Boolean> isAddedToReachable = new HashMap<>();
	private HashMap<Node, Boolean> isAddedToFrontier = new HashMap<>();
	private Queue<Node> nextFrontier = new LinkedList<>();
	private ArrayList<Integer> numReachableArr = new ArrayList<>();
	private int[] destinationCoor = new int[2];
	private Integer solvedValue = null;

	public MazeSolverWithPower() {
		// TODO: Initialize variables.
	}

	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
		for (int i = 0; i < this.maze.getRows(); i++) {
			for (int j = 0; j < this.maze.getColumns(); j++) {
				Room currentRoom = this.maze.getRoom(i, j);
				Node currentNode = new Node(currentRoom);
				currentNode.setCoordinates(i, j);
				HashMap<Integer, Node> currentNodeMap = new HashMap<>();
				currentNodeMap.put(0, currentNode);
				this.mazeNodeMap.put(currentRoom, currentNodeMap);
			}
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

		initPower(0);
		resetMaze();

		this.destinationCoor[0] = endRow;
		this.destinationCoor[1] = endCol;

		Room startRoom = this.maze.getRoom(startRow, startCol);
		this.isAddedToReachable.put(startRoom, true);
		Node startNode = this.mazeNodeMap.get(startRoom).get(0);
		startNode.setLevel(0);


		nextFrontier.add(startNode);
		return solveNormal();
	}

	public Integer solveNormal() {
		Node nextNode = nextFrontier.poll();
		if (nextNode == null) return this.solvedValue;
		return traverseNormal(nextNode);
	}

	public Integer traverseNormal(Node currentNode) {
		boolean isDestination = currentNode.getCoordinates()[0] == (this.destinationCoor[0])
				&& currentNode.getCoordinates()[1] == (this.destinationCoor[1]);
		if (isDestination) {
			// System.out.println("---Reached Destination---");
			this.markPath(currentNode);
			this.solvedValue = currentNode.getLevel();
		}
		if (currentNode.isVisited()) {
			return solveNormal();
		}

		getNextFrontier(currentNode);
		currentNode.visitNode();

		return solveNormal();
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}
		if (k < 0) {
			throw new IllegalArgumentException("Invalid k value");
		}
		if (k >= this.numReachableArr.size()) return 0;
		return this.numReachableArr.get(k);
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol, int superpowers) throws Exception {
		if (this.maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= this.maze.getRows() || startCol >= this.maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= this.maze.getRows() || endCol >= this.maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}
		this.initPower(superpowers);
		this.resetMaze();

		this.destinationCoor[0] = endRow;
		this.destinationCoor[1] = endCol;

		Room startRoom = this.maze.getRoom(startRow, startCol);
		this.isAddedToReachable.put(startRoom, true);
		Node startNode = this.mazeNodeMap.get(startRoom).get(superpowers);

		HashMap<Integer, Node> currentNodeMap = this.mazeNodeMap.get(startRoom);
		for (Map.Entry<Integer, Node> set : currentNodeMap.entrySet()) {
			set.getValue().visitNode();
		}
		startNode.isVisited = false;
		startNode.setLevel(0);
		nextFrontier.add(startNode);
		return solve();
	}

	public Integer solve() {
		// System.out.println("\nNext Frontier: " + nextFrontier);
		Node nextNode = nextFrontier.poll();
		if (nextNode == null) {
			Node smallestNode = this.findMinimumDestination();
			if (smallestNode == null) return null;
			this.markPath(smallestNode);
			return smallestNode.getLevel();
		}
		return traverse(nextNode);
	}

	public Integer traverse(Node currentNode) {
		if (currentNode.isVisited()) {
			// System.out.println(currentNode + " ..VISITED..");
			return solve();
		}
		boolean isDestination = currentNode.getCoordinates()[0] == (this.destinationCoor[0])
				&& currentNode.getCoordinates()[1] == (this.destinationCoor[1]);
		if (isDestination) {
			// System.out.println("--- " + currentNode + " Reached Destination---");
			// System.out.println(currentNode.nodeParent);
			currentNode.visitNode();
		}

		// System.out.println(currentNode);
		this.getNextFrontier(currentNode);
		currentNode.visitNode();

		return solve();
	}

	public Integer getNextFrontier(Node currentNode) {
		for (int direction = 0; direction < 4; direction++) {
			boolean isOuterWall = !canGo(currentNode.getCoordinates()[0], currentNode.getCoordinates()[1], direction)
					&& !canGoWithPower(currentNode.getCoordinates()[0], currentNode.getCoordinates()[1], direction);
			if (isOuterWall) continue;

			int currentPower = currentNode.currentPower();
			int nextPower = currentPower;
			String toString = "Normal";
			boolean isOutOfPower = !canGo(currentNode.getCoordinates()[0], currentNode.getCoordinates()[1], direction) && !currentNode.canUsePower();
			if (isOutOfPower) continue;

			boolean isPowered = !canGo(currentNode.getCoordinates()[0], currentNode.getCoordinates()[1], direction);
			if (isPowered) {
				toString = "Powered";
				nextPower--;
			}
			Node nextNode = currentNode.goDirection(direction, nextPower, this.maze, this.mazeNodeMap, this.DELTAS);
			if (nextNode.isVisited()) continue;
			if (this.isAddedToFrontier.get(nextNode) != null) continue;
			this.isAddedToFrontier.put(nextNode, true);

			nextNode.setLevel(currentNode.getLevel() + 1);
			nextNode.setParentNode(currentNode);
			// System.out.println(toString + ": " + currentNode + " || Next: " + nextNode);

			nextFrontier.add(nextNode);

			Room nextRoom = nextNode.room;
			boolean isRoomAddedBefore = this.isAddedToReachable.get(nextRoom) != null;
			if (isRoomAddedBefore) continue;
			this.isAddedToReachable.put(nextRoom, true);

			boolean doesLevelExist = this.numReachableArr.size() > nextNode.getLevel();
			if (!doesLevelExist) {
				this.numReachableArr.add(1);
				// System.out.println(this.numReachableArr);
				continue;
			}
			int currentVal = this.numReachableArr.get(nextNode.getLevel());
			this.numReachableArr.set(nextNode.getLevel(), currentVal + 1);
			// System.out.println(this.numReachableArr);
		}
		return 0;
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("maze-dense.txt");
			IMazeSolverWithPower solver = new MazeSolverWithPower();
			solver.initialize(maze);
//			System.out.println(solver.pathSearch(0, 0, 3, 3, 6)); // --> 8
//			MazePrinter.printMaze(maze);
//			System.out.println(solver.pathSearch(1, 2, 2, 3, 6)); // --> 7
//			MazePrinter.printMaze(maze);
//			System.out.println(solver.pathSearch(1, 2, 2, 3, 4)); // --> 8
//			MazePrinter.printMaze(maze);
//			System.out.println(solver.pathSearch(1, 2, 3, 2, 6)); // --> 7
//			MazePrinter.printMaze(maze);
			System.out.println(solver.pathSearch(0, 0, 0, 0, 3)); // --> 7
			MazePrinter.printMaze(maze);
//			System.out.println(solver.pathSearch(1, 1, 0, 0, 1000)); // --> 7
//			MazePrinter.printMaze(maze);

//			System.out.println(solver.pathSearch(1, 1, 0, 0)); // --> 7
//			MazePrinter.printMaze(maze);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean canGo(int row, int col, int dir) {
		if (row + this.DELTAS[dir][0] < 0 || row + this.DELTAS[dir][0] >= this.maze.getRows()) return false;
		if (col + this.DELTAS[dir][1] < 0 || col + this.DELTAS[dir][1] >= this.maze.getColumns()) return false;

		switch (dir) {
			case NORTH:
				return !this.maze.getRoom(row, col).hasNorthWall();
			case SOUTH:
				return !this.maze.getRoom(row, col).hasSouthWall();
			case EAST:
				return !this.maze.getRoom(row, col).hasEastWall();
			case WEST:
				return !this.maze.getRoom(row, col).hasWestWall();
		}
		return false;
	}

	public boolean canGoWithPower(int row, int col, int dir) {
		if (row + this.DELTAS[dir][0] < 0 || row + this.DELTAS[dir][0] >= this.maze.getRows()) return false;
		if (col + this.DELTAS[dir][1] < 0 || col + this.DELTAS[dir][1] >= this.maze.getColumns()) return false;
		return true;
	}

	public void initPower(int power) {
		for (int i = 0; i < this.maze.getRows(); i++) {
			for (int j = 0; j < this.maze.getColumns(); j++) {
				Room currentRoom = this.maze.getRoom(i, j);
				for (int k = 1; k < power + 1; k++) {
					Node currentNode = new Node(currentRoom);
					currentNode.setPower(k);
					currentNode.setCoordinates(i, j);
					this.mazeNodeMap.get(currentRoom).put(k, currentNode);
				}
			}
		}
	}

	public void resetMaze() {
		for (int i = 0; i < this.maze.getRows(); i++) {
			for (int j = 0; j < this.maze.getColumns(); j++) {
				Room currentRoom = this.maze.getRoom(i, j);
				HashMap<Integer, Node> currentNodeMap = this.mazeNodeMap.get(currentRoom);
				for (Map.Entry<Integer, Node> set : currentNodeMap.entrySet()) {
					set.getValue().resetNode();
				}
			}
		}
		this.nextFrontier.clear();
		this.isAddedToReachable.clear();
		this.isAddedToFrontier.clear();
		this.numReachableArr.clear();
		this.numReachableArr.add(1);
		this.solvedValue = null;
	}

	public Node findMinimumDestination() {
		Room room = this.maze.getRoom(this.destinationCoor[0], this.destinationCoor[1]);
		HashMap<Integer, Node> currentNodeMap = this.mazeNodeMap.get(room);
		int smallestLevel = Integer.MAX_VALUE;
		int smallestPower = Integer.MAX_VALUE;
		for (Map.Entry<Integer, Node> set : currentNodeMap.entrySet()) {
			// System.out.println("PowerLeft: " + set.getKey() + " || Level:" + set.getValue());
			if (set.getValue().getLevel() == null) continue;
			if (set.getValue().getLevel() < smallestLevel) {
				smallestLevel = set.getValue().getLevel();
				smallestPower = set.getKey();
			}
		}
		return this.mazeNodeMap.get(room).get(smallestPower);
	}

	public void markPath(Node destNode) {
		Node nextNode = destNode.markPath();
		while (true) {
			if (nextNode == null) break;
			nextNode = nextNode.markPath();
		}
	}

	private class Node {
		public Room room = null;
		private Node nodeParent = null;

		private boolean isVisited;
		private int row;
		private int col;

		public Integer level = null;
		private int power;

		public Node(Room room) {
			this.room = room;
		}

		public void resetNode() {
			this.room.onPath = false;
			this.isVisited = false;
			this.nodeParent = null;
		}

		public boolean isVisited() {
			return this.isVisited;
		}

		public void visitNode() {
			this.isVisited = true;
		}

		public void setCoordinates(int row, int col) {
			this.row = row;
			this.col = col;
		}

		public int[] getCoordinates() {
			return new int[] {this.row, this.col};
		}

		public void setLevel(int level) {
			this.level = level;
		}

		public Integer getLevel() {
			return this.level;
		}

		public void setParentNode(Node parentNode) {
			this.nodeParent = parentNode;
		}

		public void setPower(int power) {
			this.power = power;
		}

		public int currentPower() {
			return this.power;
		}

		public boolean canUsePower() {
			return this.power > 0;
		}

		public Node markPath() {
			this.room.onPath = true;
			return this.nodeParent;
		}

		/**
		 * Will return the node from the direction you want to go to
		 *
		 * @param direction  the direction you want to travel.
		 * @param maze  the maze you are currently traversing on
		 * @param mazeNodeMap  the maze Map you are traversing on
		 * @param DELTAS  the directions you can travel on
		 * @return the next node you will traverse on.
		 */
		public Node goDirection(int direction, int power, Maze maze, HashMap<Room, HashMap<Integer, Node>> mazeNodeMap, int[][] DELTAS) {
			int nextRow = this.row + DELTAS[direction][0];
			int nextCol = this.col + DELTAS[direction][1];
			Room nextRoom = maze.getRoom(nextRow, nextCol);
			HashMap<Integer, Node> nextNodeMap = mazeNodeMap.get(nextRoom);
			Node nextNode = nextNodeMap.get(power);

			nextNode.setCoordinates(nextRow, nextCol);
			return nextNode;
		}

		@Override
		public String toString() {
			return "(N:"+ Arrays.toString(this.getCoordinates()) + "[P: " + this.power + ", L: " + this.level + "])";
		}
	}
}
