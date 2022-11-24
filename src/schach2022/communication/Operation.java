package schach2022.communication;

public enum Operation {
    figureSelect((byte) 1),
    emptySelect((byte) 2),
    draw((byte) 3);

    public final Byte sequence;
    Operation(Byte b) {
        this.sequence = b;
    }
}