package schach2022.communication;

import schach2022.gameUtils.Position;
import schach2022.gameUtils.PositionTuple;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SerializingTools {

    public static Stream<Byte> serialize(List<Position> pos) {
        return posToByte(pos).sequential();
    }

    public static Stream<Byte> serialize(PositionTuple pos) {
        return Stream.of((byte) ((byte) (pos.getOrigin().getX() << 4) + (byte) (pos.getOrigin().getY())),
                        (byte) ((byte) (pos.getDest().getX() << 4)
                        + (byte) (pos.getDest().getY())));
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
/*
    public static List<Position> deSerialize(Stream<Byte> b) {
        return b.map(e -> new Position(e >>> 4, (byte) ((e << 4)) >>> 4)).toList();
    }

 */

    public static PositionTuple deSerialize(Stream<Byte> b) {
        var list = b.map(e -> new Position(e >>> 4, (byte) ((e << 4)) >>> 4)).toList();
        return new PositionTuple(list.get(0), list.get(1));
    }

    public static void main(String[] args) {
        PositionTuple x = deSerialize(serialize(new PositionTuple(new Position(6, 3), new Position(4, 3))));
        System.out.println(x.getOrigin() + " : " + x.getDest());
    }

    public static Byte[] box(byte[] arr) {
        Byte[] newArr = new Byte[arr.length];
        for (int i = 0; i < newArr.length; i++) {
            newArr[i] = arr[i];
        }
        return newArr;
    }
    public static  byte[] unBox(Byte[] arr) {
        byte[] newArr = new byte[arr.length];
        for (int i = 0; i < newArr.length; i++) {
            newArr[i] = arr[i];
        }
        return newArr;
    }
}