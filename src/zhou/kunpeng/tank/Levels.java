package zhou.kunpeng.tank;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by JA on 2017/5/23.
 * <p>
 * Levels.getLevel(int) returns the certain terrain map of a level.
 * </p>
 */
public class Levels {

    //static constants
    public static final int BATTLE_WIDTH = 26;
    public static final int BATTLE_HEIGHT = 26;
    public static final int NORMAL = 0;
    public static final int BRICK = 1;
    public static final int CONCRETE = 2;
    public static final int GRASS = 3;
    public static final int WATER = 4;
    public static final int BASE_BATTLE_X = 12;
    public static final int BASE_BATTLE_Y = 24;

    /**
     * Levels.getLevel(int) returns the certain terrain map of a level.
     *
     * @param level level number.
     * @return required terrain map.
     */
    public static int[][] getLevel(int level) {

        int[][] origin = levels.get(level - 1);
        int[][] clone = new int[origin.length][origin[0].length];
        for (int i = 0; i < origin.length; i++)
            System.arraycopy(origin[i], 0, clone[i], 0, origin[0].length);

        return clone;
    }

    private static List<int[][]> levels;

    // Init level info from file. See maps/maps.txt for format.
    static {
        levels = new ArrayList<>();

        Scanner fileIn = new Scanner(Levels.class.getResourceAsStream("/maps/maps.txt"));
        while (fileIn.hasNextLine()) {

            int[][] map = new int[BATTLE_HEIGHT][BATTLE_WIDTH];

            for (int y = 0; y < BATTLE_HEIGHT; ) {
                if (!fileIn.hasNextLine())
                    break;

                String line = fileIn.nextLine();

                if (line.equals(""))
                    continue;

                for (int x = 0; x < BATTLE_WIDTH && x < line.length(); x++)
                    map[y][x] = line.charAt(x) - '0';

                y++;
            }

            levels.add(map);
        }

        fileIn.close();
    }

    public static int getLevelCount() {
        return levels.size();
    }
}
