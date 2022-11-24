package schach2022.communication;

import schach2022.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SerializingTools {

    public static Stream<Byte> serialize(Operation o, List<Position> pos) {
        return Stream.concat(Stream.of(o.sequence).sequential(), posToByte(pos)).sequential();
    }

    private static Stream<Byte> posToByte(List<Position> pos) {
        List<Byte> result = new ArrayList<>();
        byte b;
        for (Position p : pos) {
            b = (byte) (p.getX() << 4);
            b = (byte) (b + p.getY());
            result.add(b);
        }
        return result.stream().sequential();
    }

    public static List<Position> deSerialize(Stream<Byte> b) {
        return b.skip(1).map(e -> new Position(e >>> 4, (byte) ((e << 4)) >>> 4)).toList();
    }

/*
    public static void main(String[] args) {

        //List<Position> b = new ArrayList<>();

        //b.add(new Position(7, 7));
        //b.add(new Position(1, 2));

        //var x = posToByte(b).toList();
        //System.out.println(x.get(1));
        //System.out.println(Integer.toBinaryString(x.get(1)));
        //System.out.println(deSerialize(posToByte(b)).get(0));


        List<Position> b = new ArrayList<>();

        b.add(new Position(7, 7));
        b.add(new Position(1, 2));

        System.out.println(serialize(Operation.figureSelect, b).limit(1).toList().get(0));
        System.out.println(deSerialize(serialize(Operation.figureSelect, b)));

    }
    */
}
