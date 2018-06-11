package ch.jodo.mazerator.util;

public class Cell {

    // walls between neighbors
    private boolean topWall = true;
    private boolean rightWall = true;
    private boolean bottomWall = true;
    private boolean leftWall = true;

    // has cell been visited (needed for generation)
    private boolean visited = false;

    // distance from target to goal (needed for solving)
    private int distance = Integer.MAX_VALUE;

    // previous visited cell (needed for solving)
    private Cell previous = null;

    // coordinates of the cell
    private int x;
    private int y;

    public Cell(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isTopWall() {
        return topWall;
    }

    public void setTopWall(boolean topWall) {
        this.topWall = topWall;
    }

    public boolean isRightWall() {
        return rightWall;
    }

    public void setRightWall(boolean rightWall) {
        this.rightWall = rightWall;
    }

    public boolean isBottomWall() {
        return bottomWall;
    }

    public void setBottomWall(boolean bottomWall) {
        this.bottomWall = bottomWall;
    }

    public boolean isLeftWall() {
        return leftWall;
    }

    public void setLeftWall(boolean leftWall) {
        this.leftWall = leftWall;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Cell getPrevious() {
        return previous;
    }

    public void setPrevious(Cell previous) {
        this.previous = previous;
    }
}
