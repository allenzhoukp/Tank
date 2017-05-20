package zhou.kunpeng.tank;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;

/**
 * Created by JA on 2017/5/19. <br>
 * GameMap stores the terrain situation of map.
 */
public class GameMap extends JLayeredPane {
    public static final int BATTLE_WIDTH = 26;
    public static final int BATTLE_HEIGHT = 26;
    public static final int SLOT_SIZE = 21;

    public static final int NORMAL = 0;
    public static final int BRICK = 1;
    public static final int CONCRETE = 2;
    public static final int GRASS = 3;
    public static final int WATER = 4;
    public static final int TANK = 5;

    public static final Integer BACKGROUND_LAYER = 0;
    public static final Integer WATER_LAYER = 100;
    public static final Integer CANNON_LAYER = 300;
    public static final Integer WALL_LAYER = 400;
    public static final Integer TANK_LAYER = 900;
    public static final Integer GRASS_LAYER = 1000;
    public static final Integer PLUS_LAYER = 1200;

    public static final int FPS = 25;

    public static int toBattleCoordinate(int coordinate) {
        return coordinate / SLOT_SIZE;
    }

    public static int toScreenCoordinate(int coordinate) {
        return coordinate * SLOT_SIZE;
    }

    private int[][] map;
    private ImageComponent[][] terrainImage;

    private Tank p1Tank;
    private Tank p2Tank;
    private List<Tank> enemyTankList;

    public GameMap(int[][] mapContent) {
        super();
        this.map = mapContent;
        enemyTankList = new ArrayList<>();
        loadMap();
    }

    public int[][] getMap() {
        return map;
    }

    public ImageComponent[][] getTerrainImage() {
        return terrainImage;
    }

    public List<Tank> getEnemyTankList() {
        return enemyTankList;
    }

    public Tank getP1Tank() {
        return p1Tank;
    }

    public void setP1Tank(Tank p1Tank) {
        this.p1Tank = p1Tank;
    }

    public Tank getP2Tank() {
        return p2Tank;
    }

    public void setP2Tank(Tank p2Tank) {
        this.p2Tank = p2Tank;
    }


    private void loadMap() {

        JComponent background = new JComponent() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, BATTLE_WIDTH * SLOT_SIZE, BATTLE_HEIGHT * SLOT_SIZE);
            }
        };
        background.setBounds(0, 0, BATTLE_WIDTH * SLOT_SIZE, BATTLE_HEIGHT * SLOT_SIZE);
        this.add(background, BACKGROUND_LAYER);

        terrainImage = new ImageComponent[GameMap.BATTLE_HEIGHT][GameMap.BATTLE_WIDTH];
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                String path;
                switch (map[y][x]) {
                    case BRICK:
                        path = "/images/brick.png";
                        break;
                    case CONCRETE:
                        path = "/images/concrete.png";
                        break;
                    case GRASS:
                        path = "/images/grass.png";
                        break;
                    case WATER:
                        path = "/images/water.png";
                        break;
                    default:
                        continue;
                }
                terrainImage[y][x] = new ImageComponent(path, GameMap.toScreenCoordinate(x), GameMap.toScreenCoordinate(y),
                        SLOT_SIZE, SLOT_SIZE);
                this.add(terrainImage[y][x]);
                if (map[y][x] == GRASS)
                    this.setLayer(terrainImage[y][x], GRASS_LAYER);
                else if(map[y][x] == WATER)
                    this.setLayer(terrainImage[y][x], WATER_LAYER);
                else
                    this.setLayer(terrainImage[y][x], WALL_LAYER);
            }
        }

        this.setBounds(0, 0, BATTLE_WIDTH * SLOT_SIZE, BATTLE_HEIGHT * SLOT_SIZE);
    }

    private boolean isTankThroughable(int battleX, int battleY) {
        return map[battleY][battleX] == GameMap.NORMAL ||
                map[battleY][battleX] == GameMap.GRASS;
    }

    public boolean tankBlocked(int battleX, int battleY) {
        return !(battleX >= 0 && battleX < GameMap.BATTLE_WIDTH - 1 &&
                battleY >= 0 && battleY < GameMap.BATTLE_HEIGHT - 1 &&
                isTankThroughable(battleX, battleY) &&
                isTankThroughable(battleX + 1, battleY) &&
                isTankThroughable(battleX, battleY + 1) &&
                isTankThroughable(battleX + 1, battleY + 1));
    }

    public void addTankBlock(int battleX, int battleY) {
        map[battleY][battleX] = map[battleY][battleX + 1] =
                map[battleY + 1][battleX] = map[battleY + 1][battleX + 1] = TANK;
    }

    public void removeTankBlock(int battleX, int battleY) {
        final int[][] additive = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
        for (int i = 0; i < 4; i++)
            if (map[battleY + additive[i][0]][battleX + additive[i][1]] == TANK)
                map[battleY + additive[i][0]][battleX + additive[i][1]] = NORMAL;
    }

}
