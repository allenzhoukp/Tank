package zhou.kunpeng.tank;

import zhou.kunpeng.tank.tank.Tank;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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

    public static final int PLAYER_SIDE = 0;
    public static final int ENEMY_SIDE = 1;

    public static final int P1_BORN_BATTLE_X = 9;
    public static final int P1_BORN_BATTLE_Y = 24;
    public static final int P2_BORN_BATTLE_X = 15;
    public static final int P2_BORN_BATTLE_Y = 24;
    public static final int INIT_LIFE = 4;
    public static final int INIT_ENEMY = 20;
    public static final int BORN_SHIELD_SEC = 4;
    public static final double BORN_SEC = 1.5;

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
    private List<Tank> enemyTankList = new ArrayList<>();

    private int p1Life = GameMap.INIT_LIFE;
    private int p2Life = GameMap.INIT_LIFE;
    private int enemyRemaining = GameMap.INIT_ENEMY;

    private Timeline timer;

    public GameMap(int[][] mapContent, Timeline timer) {
        super();
        this.map = mapContent;
        this.timer = timer;
        initMap();
    }

    private void initMap() {

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
                else if (map[y][x] == WATER)
                    this.setLayer(terrainImage[y][x], WATER_LAYER);
                else
                    this.setLayer(terrainImage[y][x], WALL_LAYER);
            }
        }

        this.setBounds(0, 0, BATTLE_WIDTH * SLOT_SIZE, BATTLE_HEIGHT * SLOT_SIZE);
    }

    private boolean isTankPassable(int battleX, int battleY) {
        return map[battleY][battleX] == GameMap.NORMAL ||
                map[battleY][battleX] == GameMap.GRASS;
    }

    public boolean tankBlocked(int battleX, int battleY) {
        return !(battleX >= 0 && battleX < GameMap.BATTLE_WIDTH - 1 &&
                battleY >= 0 && battleY < GameMap.BATTLE_HEIGHT - 1 &&
                isTankPassable(battleX, battleY) &&
                isTankPassable(battleX + 1, battleY) &&
                isTankPassable(battleX, battleY + 1) &&
                isTankPassable(battleX + 1, battleY + 1));
    }

    public void addTankBlock(int battleX, int battleY) {
        map[battleY][battleX] = map[battleY][battleX + 1] =
                map[battleY + 1][battleX] = map[battleY + 1][battleX + 1] = TANK;
    }

    public void removeTankBlock(int battleX, int battleY) {
        final int[][] additive = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
        for (int i = 0; i < 4; i++)
            if (battleY + additive[i][0] >= 0 && battleY + additive[i][0] < GameMap.BATTLE_HEIGHT &&
                    battleX + additive[i][1] >= 0 && battleX + additive[i][1] < GameMap.BATTLE_WIDTH &&
                    map[battleY + additive[i][0]][battleX + additive[i][1]] == TANK)
                map[battleY + additive[i][0]][battleX + additive[i][1]] = NORMAL;
    }

    public void gameOver() {
        //TODO game over
    }

    //Getters and setters

    public int[][] getMap() {
        return map;
    }

    public ImageComponent[][] getTerrainImage() {
        return terrainImage;
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

    public int getP1Life() {
        return p1Life;
    }

    public void setP1Life(int p1Life) {
        this.p1Life = p1Life;
    }

    public int getP2Life() {
        return p2Life;
    }

    public void setP2Life(int p2Life) {
        this.p2Life = p2Life;
    }

    public int getEnemyRemaining() {
        return enemyRemaining;
    }

    public void setEnemyRemaining(int enemyRemaining) {
        this.enemyRemaining = enemyRemaining;
    }

    public List<Tank> getEnemyTankList() {
        return enemyTankList;
    }

    public Timeline getTimer() {
        return timer;
    }

    public void setTimer(Timeline timer) {
        this.timer = timer;
    }

}
