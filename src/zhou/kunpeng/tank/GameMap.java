package zhou.kunpeng.tank;

import zhou.kunpeng.tank.display.ImageComponent;
import zhou.kunpeng.tank.tanks.PlayerTank;
import zhou.kunpeng.tank.tanks.Tank;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JA on 2017/5/19. <br>
 * GameMap stores the terrain situation of map.
 */
public class GameMap extends JLayeredPane {

    //static constants
    public static final int BATTLE_WIDTH = 26;
    public static final int BATTLE_HEIGHT = 26;
    public static final int SLOT_SIZE = 21;

    public static final int NORMAL = 0;
    public static final int BRICK = 1;
    public static final int CONCRETE = 2;
    public static final int GRASS = 3;
    public static final int WATER = 4;

    public static final Integer BACKGROUND_LAYER = 0;
    public static final Integer WATER_LAYER = 100;
    public static final Integer CANNON_LAYER = 300;
    public static final Integer WALL_LAYER = 400;
    public static final Integer TANK_LAYER = 900;
    public static final Integer GRASS_LAYER = 1000;
    public static final Integer PLUS_LAYER = 1200;

    public static final int PLAYER_SIDE = 0;
    public static final int ENEMY_SIDE = 1;

    public static final int INIT_LIFE = 4;
    public static final int INIT_ENEMY = 20;


    //map properties and references kept
    private int[][] map;
    private ImageComponent[][] terrainImage;

    private PlayerTank p1Tank;
    private PlayerTank p2Tank;
    private List<Tank> enemyTankList = new ArrayList<>();

    private int p1Life = GameMap.INIT_LIFE;
    private int p2Life = GameMap.INIT_LIFE;
    private int enemyRemaining = GameMap.INIT_ENEMY;

    private InfoPanel infoPanel;

    private final Timeline timer;
    private ScoreCounter p1Score = new ScoreCounter();
    private ScoreCounter p2Score = new ScoreCounter();

    public GameMap(int[][] mapContent, Timeline timer, int level) {
        super();
        this.map = mapContent;
        this.timer = timer;

        initMap();

        p1Tank = new PlayerTank(true, this);
        p2Tank = new PlayerTank(false, this);

        initInfoPanel(level);
    }

    //Install all terrain images, and background.
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
                terrainImage[y][x] = new ImageComponent(path, MapUtils.toScreenCoordinate(x), MapUtils.toScreenCoordinate(y),
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

    //install InfoPanel on the right of the map.
    private void initInfoPanel(int level) {
        infoPanel = new InfoPanel(INIT_ENEMY, INIT_LIFE, INIT_LIFE, level);
        infoPanel.setLocation(BATTLE_WIDTH * SLOT_SIZE, 0);
        this.add(infoPanel, BACKGROUND_LAYER);
        this.setSize(this.getWidth() + infoPanel.getWidth(), this.getHeight());
    }


    /**
     * @return a temporary ArrayList that contains all the tanks (player & enemy) on the map.
     */
    public List<Tank> getAllTanks() {
        List<Tank> tankList = new ArrayList<>();
        tankList.add(getP1Tank());
        tankList.add(getP2Tank());
        tankList.addAll(getEnemyTankList());
        return tankList;
    }

    private boolean isTerrainPassable(int battleX, int battleY) {
        return map[battleY][battleX] == GameMap.NORMAL ||
                map[battleY][battleX] == GameMap.GRASS;
    }

    /**
     * Returns if the tank is blocked.
     * Note that battleX and battleY should be upperLeft corner of the mover.
     */
    public boolean tankBlocked(int x, int y, Tank mover) {
        Rectangle destRect = MapUtils.convertBattleRect(x, y, mover.getWidth(), mover.getHeight());

        //check if in the border
        //note in screen coordinate
        if (x < 0 || x + mover.getWidth() > BATTLE_WIDTH * SLOT_SIZE ||
                y < 0 || y + mover.getHeight() > BATTLE_HEIGHT * SLOT_SIZE)
            return true;

        for (Tank tank : getAllTanks()) {
            if (tank == null || tank == mover)
                continue;

            //check tank
            //note if there is collision between tanks, the block does not happen between them.
            Rectangle tankRect = MapUtils.convertBattleRect(tank.getX(), tank.getY(), tank.getWidth(), tank.getHeight());
            Rectangle moverRect = MapUtils.convertBattleRect(mover.getX(), mover.getY(), mover.getWidth(), mover.getHeight());
            if (!moverRect.intersects(tankRect) && destRect.intersects(tankRect))
                return true;
        }

        //check if the terrain has blocked
        for (int i = destRect.x; i < destRect.x + destRect.width; i++)
            for (int j = destRect.y; j < destRect.y + destRect.height; j++)
                if (!isTerrainPassable(i, j))
                    return true;

        return false;
    }

    public void victory() {
        //TODO victory
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

    public PlayerTank getP1Tank() {
        return p1Tank;
    }

    public void setP1Tank(PlayerTank p1Tank) {
        this.p1Tank = p1Tank;
    }

    public PlayerTank getP2Tank() {
        return p2Tank;
    }

    public void setP2Tank(PlayerTank p2Tank) {
        this.p2Tank = p2Tank;
    }

    public int getP1Life() {
        return p1Life;
    }

    public void setP1Life(int p1Life) {
        this.p1Life = p1Life;
        this.infoPanel.updateLife(p1Life, true);
    }

    public int getP2Life() {
        return p2Life;
    }

    public void setP2Life(int p2Life) {
        this.p2Life = p2Life;
        this.infoPanel.updateLife(p2Life, false);
    }

    public int getEnemyRemaining() {
        return enemyRemaining;
    }

    public void setEnemyRemaining(int enemyRemaining) {
        this.enemyRemaining = enemyRemaining;
        this.infoPanel.updateEnemyCount(enemyRemaining);
    }

    public List<Tank> getEnemyTankList() {
        return enemyTankList;
    }

    public Timeline getTimer() {
        return timer;
    }

    public ScoreCounter getScoreCounter(boolean isP1) {
        return isP1 ? p1Score : p2Score;
    }


}
