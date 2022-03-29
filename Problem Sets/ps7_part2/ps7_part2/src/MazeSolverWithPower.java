import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;

public class MazeSolverWithPower implements IMazeSolverWithPower {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
		{ -1, 0 }, // North
		{ 1, 0 }, // South
		{ 0, 1 }, // East
		{ 0, -1 } // West
	};
	private Maze maze;
	HashMap<Room, NodePowerPair> mazeNodeMap = new HashMap<>();
	private Queue<NodePowerPair> nextFrontier = new LinkedList<>();
	private Integer solvedValue = null;

	private boolean canGo(int row, int col, int dir) {
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

	private boolean canGoWithPower(int row, int col, int dir) {
		if (row + this.DELTAS[dir][0] < 0 || row + this.DELTAS[dir][0] >= this.maze.getRows()) return false;
		if (col + this.DELTAS[dir][1] < 0 || col + this.DELTAS[dir][1] >= this.maze.getColumns()) return false;
		return true;
	}

	public void resetMaze(int allPowers) {
		for (int i = 0; i < this.maze.getRows(); i++) {
			for (int j = 0; j < this.maze.getColumns(); j++) {
				Room currentRoom = this.maze.getRoom(i, j);
				NodePowerPair currentNode = this.mazeNodeMap.get(currentRoom);
				currentNode.resetNode(allPowers);
			}
		}
	}

	public void markPath(NodePowerPair destNode) {
		Node nextNode = destNode.node.markPath(destNode.currentPower());
		Room nextRoom = maze.getRoom(nextNode.getCoordinates()[0], nextNode.getCoordinates()[1]);
		NodePowerPair nextPair = mazeNodeMap.get(nextRoom);
		for (int i = 0; i < 5; i++) {
			if (nextNode == null) break;
			nextNode = nextPair.node.markPath(nextPair.currentPower());
			nextRoom = maze.getRoom(nextNode.getCoordinates()[0], nextNode.getCoordinates()[1]);
			nextPair = mazeNodeMap.get(nextRoom);
			System.out.println(nextPair);
		}
	}

	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
		for (int i = 0; i < this.maze.getRows(); i++) {
			for (int j = 0; j < this.maze.getColumns(); j++) {
				Room currentRoom = this.maze.getRoom(i, j);
				Node currentNode = new Node(currentRoom);
				NodePowerPair newNode = new NodePowerPair(currentNode);
				mazeNodeMap.put(currentRoom, newNode);
			}
		}
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// TODO: Find shortest path to an end room.
		return 0;
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		// TODO: Find number of reachable rooms.
		return 0;
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow,
							  int endCol, int superpowers) throws Exception {
		if (this.maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= this.maze.getRows() || startCol >= this.maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= this.maze.getRows() || endCol >= this.maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}
		this.resetMaze(superpowers);

		Room endRoom = this.maze.getRoom(endRow, endCol);
		NodePowerPair endNode = this.mazeNodeMap.get(endRoom);
		endNode.node.setToDestination();
		endNode.node.setCoordinates(endRow, endCol);

		Room startRoom = this.maze.getRoom(startRow, startCol);
		NodePowerPair startNode = this.mazeNodeMap.get(startRoom);
		startNode.node.setCoordinates(startRow, startCol);
		startNode.setPower(superpowers);
		startNode.node.setLevel(0, superpowers);

		nextFrontier.add(startNode);

		return solve();
	}

	public Integer solve() {
		System.out.println("\nNext Frontier: " + nextFrontier);
		NodePowerPair nextNode = nextFrontier.poll();
		if (nextNode == null) {
			return this.solvedValue;
		}
		return traverse(nextNode);
	}

	public Integer traverse(NodePowerPair currentNode) {
		if (currentNode.node.isVisited(currentNode.currentPower())) {
			System.out.println(currentNode + " is visited.");
			return solve();
		}
		if (currentNode.node.isDestination()) {
			System.out.println("--- " + currentNode + " Reached Destination---");
			System.out.println(Arrays.toString(currentNode.node.level));
			this.solvedValue = currentNode.node.getLevel(currentNode.currentPower());
			currentNode.node.visitNode(currentNode.currentPower());
			this.markPath(currentNode);
		}

		System.out.println(currentNode);
		this.getNextFrontier(currentNode);
		currentNode.node.visitNode(currentNode.currentPower());

		return solve();
	}

	public void getNextFrontier(NodePowerPair currentNode) {
		for (int direction = 0; direction < 4; direction++) {
			if (!canGo(currentNode.node.getCoordinates()[0], currentNode.node.getCoordinates()[1], direction)
					&& !canGoWithPower(currentNode.node.getCoordinates()[0], currentNode.node.getCoordinates()[1], direction)) continue;

			int currentPower = currentNode.currentPower();
			int nextPower = currentPower;
			String toString = "Normal";
			if (!canGo(currentNode.node.getCoordinates()[0], currentNode.node.getCoordinates()[1], direction)
					&& !currentNode.canUsePower()) continue;

			if (!canGo(currentNode.node.getCoordinates()[0], currentNode.node.getCoordinates()[1], direction)) {
				toString = "Powered";
				nextPower--;
			}
			NodePowerPair nextNode = currentNode.node.goDirection(direction, this.maze, this.mazeNodeMap, this.DELTAS);
			if (nextNode.node.isVisited(nextPower)) continue;
			nextNode.node.setLevel(currentNode.node.getLevel(currentPower) + 1, nextPower);
			nextNode.node.setParentNode(currentNode.node, nextPower);
			nextNode.setPower(nextPower);
			System.out.println(toString + ": " + currentNode + " || Next: " + nextNode);

			nextFrontier.add(nextNode);
		}
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("maze-sample.txt");
			IMazeSolverWithPower solver = new MazeSolverWithPower();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 4, 3, 6));
			MazePrinter.printMaze(maze);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
