import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.TreeSet;
import java.util.HashMap;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MazeSolver implements IMazeSolver {
	private static final int TRUE_WALL = Integer.MAX_VALUE;
	private static final int EMPTY_SPACE = 0;
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private Integer fearLevel = null;
	private boolean destinationFound = false;
	private static final List<Function<Room, Integer>> WALL_FUNCTIONS = Arrays.asList(
			Room::getNorthWall,
			Room::getSouthWall,
			Room::getEastWall,
			Room::getWestWall
	);
	private static int[][] DELTAS = new int[][] {
			{ -1, 0 }, // North
			{ 1, 0 }, // South
			{ 0, 1 }, // East
			{ 0, -1 } // West
	};

	private class Node implements Comparable<Node>{
		public final Room room;
		public Integer distance = -1;
		public final int row;
		public final int col;
		public boolean isDestination = false;

		public Node(Room room, int row, int col) {
			this.room = room;
			this.row = row;
			this.col = col;
		}

		public void setAsSource() {
			this.distance = 0;
		}

		public void setAsDestination() {
			this.isDestination = true;
		}

		public void relaxAllNeighbours() {
			// System.out.println("Current Node: " + this);
			int direction = 0;
			for (Function<Room, Integer> getWeight : WALL_FUNCTIONS) {
				if (!canGo(this.row, this.col, direction)) {
					direction++;
					continue;
				}
				int nextRow = this.row + DELTAS[direction][0];
				int nextCol = this.col + DELTAS[direction][1];
				direction++;

				Room adjacentRoom = maze.getRoom(nextRow, nextCol);
				if (roomNodeMap.get(adjacentRoom) == null) {
					Node newNode = new Node(adjacentRoom, nextRow, nextCol);
					roomNodeMap.put(adjacentRoom, newNode);
				}
				Node adjacentNode = roomNodeMap.get(adjacentRoom);
				if (alreadyLocked.get(adjacentNode) != null) continue;
				// System.out.println("Adding: " + adjacentNode);
				Integer betweenDistance = 1;
				if (getWeight.apply(this.room) != EMPTY_SPACE) {
					betweenDistance = getWeight.apply(this.room);
				}
				Integer newWeight = betweenDistance + this.distance;
				adjacentNode.relax(newWeight);
			}
			alreadyLocked.put(this, true);
			pq.remove(this);
			if (this.isDestination) {
				// System.out.println("Found Destination: " + this);
				fearLevel = this.distance;
				destinationFound = true;
			}
			// System.out.println("Node " + this + " visited\n");
		}

		private void relax(Integer newDistance) {
			boolean isShorter = newDistance < this.distance || this.distance == -1;
			if (isShorter) {
				pq.decreaseKey(this, newDistance);
			}
			// System.out.println("Relaxing Node: " + this);
			// System.out.println("PQ: " + pq);
		}

		@Override
		public int compareTo(Node newNode) {
			if (this.distance == -1 && newNode.distance != -1) return 1;
			if (this.distance != -1 && newNode.distance == -1) return -1;
			if (this.distance - newNode.distance == 0) return -1;
			return this.distance - newNode.distance;
		}

		@Override
		public String toString() {
			return "[" + this.row + ", " + this.col + "] {" + this.distance + "}";
		}
	}

	private class PriorityQ extends TreeSet<Node> {
		public void decreaseKey(Node n, Integer newDistance) {
			this.remove(n);
			n.distance = newDistance;
			this.add(n);
		}
	}

	private HashMap<Room, Node> roomNodeMap = new HashMap<>();
	private HashMap<Node, Boolean> alreadyLocked = new HashMap<>();
	private PriorityQ pq = new PriorityQ();
	private Maze maze;

	public boolean canGo(int row, int col, int dir) {
		if (row + this.DELTAS[dir][0] < 0 || row + this.DELTAS[dir][0] >= this.maze.getRows()) return false;
		if (col + this.DELTAS[dir][1] < 0 || col + this.DELTAS[dir][1] >= this.maze.getColumns()) return false;

		switch (dir) {
			case NORTH:
				return this.maze.getRoom(row, col).getNorthWall() != TRUE_WALL;
			case SOUTH:
				return this.maze.getRoom(row, col).getSouthWall() != TRUE_WALL;
			case EAST:
				return this.maze.getRoom(row, col).getEastWall() != TRUE_WALL;
			case WEST:
				return this.maze.getRoom(row, col).getWestWall() != TRUE_WALL;
		}
		return false;
	}

	public MazeSolver() {
		// TODO: Initialize variables.
	}

	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		this.roomNodeMap.clear();
		this.alreadyLocked.clear();
		this.pq.clear();
		this.fearLevel = null;
		this.destinationFound = false;

		if (startRow == endRow && startCol == endCol) return 0;

		Room startRoom = maze.getRoom(startRow, startCol);
		Node startNode = new Node(startRoom, startRow, startCol);
		startNode.setAsSource();
		roomNodeMap.put(startRoom, startNode);
		pq.add(startNode);

		Room endRoom = maze.getRoom(endRow, endCol);
		Node endNode = new Node(endRoom, endRow, endCol);
		endNode.setAsDestination();
		roomNodeMap.put(endRoom, endNode);

		while (!this.pq.isEmpty() && !this.destinationFound) {
			pq.pollFirst().relaxAllNeighbours();
		}
		return this.fearLevel;
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// TODO: Find minimum fear level given new rules.
		return null;
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol, int sRow, int sCol) throws Exception {
		// TODO: Find minimum fear level given new rules and special room.
		return null;
	}

	public static void main(String[] args) {
		FileWriter fw = null;
		BufferedWriter bw = null;
		PrintWriter pw = null;
		try {
			Maze maze = Maze.readMaze("haunted-maze-sample.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);

//			String workingDir = "/Users/shenyicui/Desktop/";
//			String fileName = "SampleMaze-Shenyi.txt";
//			//clearing file begin
//			fw = new FileWriter(workingDir + "/" + fileName, false);
//			bw = new BufferedWriter(fw);
//			pw = new PrintWriter(bw);
//			pw.print("");
//			//clearing file end
//
//			fw = new FileWriter(workingDir + "/" + fileName, true);
//			bw = new BufferedWriter(fw);
//			pw = new PrintWriter(bw);
//
//			for (int startRow = 0; startRow < maze.getRows(); startRow++) {
//				for (int startCol = 0; startCol < maze.getColumns(); startCol++) {
//					pw.println("Source," + startRow + "," + startCol);
//					for (int endRow = 0; endRow < maze.getRows(); endRow++) {
//						for (int endCol = 0; endCol < maze.getColumns(); endCol++) {
//							pw.println("Dest," + endRow + "," + endCol + "," + solver.pathSearch(startRow, startCol, endRow, endCol));
//						}
//					}
//				}
//			}
//			System.out.println("Data Successfully appended into file");
//			pw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
