package schach2022.gameUtils;

public class PositionTuple {

    private final Position origin;
    private final Position dest;

    public PositionTuple(Position origin, Position dest) {
        this.origin = origin;
        this.dest = dest;
    }

    public Position getOrigin() {
        return origin;
    }

    public Position getDest() {
        return dest;
    }
}