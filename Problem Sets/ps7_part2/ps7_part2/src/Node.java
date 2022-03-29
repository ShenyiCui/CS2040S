import java.util.HashMap;
import java.util.Arrays;

public class Node {
    private Room room = null;
    private Node nodeParent;

    private boolean isVisited;
    private boolean isDestination = false;

    private int row;
    private int col;

    public int level;
    private int power;

    public Node(Room room) {
        this.room = room;
    }

    public void resetNode(int allPowers) {
        this.room.onPath = false;
        this.isDestination = false;
        this.isVisited = new boolean[allPowers + 1];
        this.level = new int[allPowers + 1];
        Arrays.fill(this.isVisited, false);
    }

    public boolean isVisited(int powerLevel) {
        return this.isVisited[powerLevel];
    }

    public void visitNode(int powerLevel) {
        this.isVisited[powerLevel] = true;
    }

    public boolean isDestination() {
        return this.isDestination;
    }

    public void setToDestination() {
        this.isDestination = true;
    }

    public void setCoordinates(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int[] getCoordinates() {
        return new int[] {this.row, this.col};
    }

    public void setLevel(int level, int powerLevel) {
        this.level[powerLevel] = level;
    }

    public int getLevel(int powerLevel) {
        return this.level[powerLevel];
    }

    public void setParentNode(Node parentNode, int powerLevel) {
        this.nodeParent[powerLevel] = parentNode;
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

    public Node markPath(int powerLevel) {
        this.room.onPath = true;
        return this.nodeParent[powerLevel];
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
    public Node goDirection(int direction , Maze maze, HashMap<Room, Node> mazeNodeMap, int[][] DELTAS) {
        int nextRow = this.row + DELTAS[direction][0];
        int nextCol = this.col + DELTAS[direction][1];
        Room nextRoom = maze.getRoom(nextRow, nextCol);
        Node nextNode = mazeNodeMap.get(nextRoom);
        nextNode.setCoordinates(nextRow, nextCol);
        return nextNode;
    }

    @Override
    public String toString() {
        return "N:"+Arrays.toString(this.getCoordinates()) + "[power:"+this.power+ "]";
    }
}
