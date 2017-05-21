package zhou.kunpeng.tank;

import java.awt.*;

/**
 * Created by JA on 2017/5/21.
 */
public class MapUtils {
    public static int toBattleCoordinate(int coordinate) {
        return coordinate / GameMap.SLOT_SIZE;
    }

    public static int toScreenCoordinate(int coordinate) {
        return coordinate * GameMap.SLOT_SIZE;
    }

    public static int alignScreenCoordinate(int coordinate) {
        return coordinate - coordinate % GameMap.SLOT_SIZE;
    }

    public static boolean isCoordinateAligned(int coordinate) {
        return alignScreenCoordinate(coordinate) == coordinate;
    }

    public static Rectangle convertBattleRect(int x, int y, int width, int height) {
        return new Rectangle(
                toBattleCoordinate(x),
                toBattleCoordinate(y),
                toBattleCoordinate(x + width - 1) - toBattleCoordinate(x) + 1,
                toBattleCoordinate(y + height - 1) - toBattleCoordinate(y) + 1);
    }
}
