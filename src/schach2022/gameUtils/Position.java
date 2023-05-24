package schach2022.gameUtils;

public class Position {

    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(x=" + x + ", y=" + y +")";
    }

    public boolean equals(Position p) {
        return this.x == p.x && this.y == p.y;
    }
}