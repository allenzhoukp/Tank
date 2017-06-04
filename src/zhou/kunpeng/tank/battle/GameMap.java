package zhou.kunpeng.tank.battle;

import zhou.kunpeng.tank.Levels;
import zhou.kunpeng.tank.PlayerState;
import zhou.kunpeng.tank.Sound;
import zhou.kunpeng.tank.comm.NetComm;
import zhou.kunpeng.tank.display.Background;
import zhou.kunpeng.tank.display.ImageComponent;
import zhou.kunpeng.tank.states.BattleState;
import zhou.kunpeng.tank.timer.Timeline;
import zhou.kunpeng.tank.timer.TimerListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JA on 2017/5/19. <br>
 * GameMap stores the terrain situation of map. <br>
 * And, this is the core of all battle operations,
 * since it keeps all the references of tanks, player info and display components.
 */
public class GameMap extends JLayeredPane {

    //Constants
    public static final int SLOT_SIZE = 21;

    public static final Integer BACKGROUND_LAYER = 0;
    public static final Integer WATER_LAYER = 100;
    public static final Integer CANNON_LAYER = 300;
    public static final Integer WALL_LAYER = 1000;
    public static final Integer TANK_LAYER = 1500;
    public static final Integer GRASS_LAYER = 1800;
    public static final Integer PLUS_LAYER = 2000;

    public static final int PLAYER_SIDE = 0;
    public static final int ENEMY_SIDE = 1;

    public static final int INIT_ENEMY = 20;


    //map properties and references kept

    private BattleState parentState;

    private int[][] map;
    private ImageComponent[][] terrainImage;
    private int level;
    private InfoPanel infoPanel;

    private boolean paused = false;
    private PauseScreen pauseScreen;

    private final Timeline timer;

    private NetComm netComm = null;
    private boolean isClient;

    private PlayerTank p1Tank;
    private PlayerTank p2Tank;
    private List<EnemyTank> enemyTankList = new ArrayList<>();
    private Base base;

    private int p1Life;
    private int p2Life;
    private int enemyRemaining = GameMap.INIT_ENEMY;

    private PlayerState p1State;
    private PlayerState p2State;

    public GameMap(BattleState viewState, int[][] mapContent, Timeline timer,
                   int level, PlayerState p1State, PlayerState p2State,
                   boolean hasP2, boolean isClient) {
        super();

        //init references and states
        this.parentState = viewState;
        this.map = mapContent;
        this.timer = timer;
        this.level = level;
        this.isClient = isClient;

        this.p1State = p1State;
        this.p2State = p2State;
        this.p1Life = p1State.life;
        this.p2Life = p2State.life;

        initMap();

        //init player tanks
        p1Tank = new PlayerTank(true, this);
        if (hasP2)
            p2Tank = new PlayerTank(false, this);

        //Init info panel and update player scores
        initInfoPanel(level);
        infoPanel.updateScore(p1State.totalScore, true);
        infoPanel.updateScore(p2State.totalScore, false);

        pauseScreen = new PauseScreen(this.getWidth(), this.getHeight());
    }

    //Install all terrain images, and background.
    private void initMap() {


        this.add(new Background(Levels.BATTLE_WIDTH * SLOT_SIZE, Levels.BATTLE_HEIGHT * SLOT_SIZE),
                BACKGROUND_LAYER);

        terrainImage = new ImageComponent[Levels.BATTLE_HEIGHT][Levels.BATTLE_WIDTH];
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                String path;
                switch (map[y][x]) {
                    case Levels.BRICK:
                        path = "/images/brick.png";
                        break;
                    case Levels.CONCRETE:
                        path = "/images/concrete.png";
                        break;
                    case Levels.GRASS:
                        path = "/images/grass.png";
                        break;
                    case Levels.WATER:
                        path = "/images/water.png";
                        break;
                    default:
                        continue;
                }
                terrainImage[y][x] = new ImageComponent(path, MapUtils.toScreenCoordinate(x), MapUtils.toScreenCoordinate(y),
                        SLOT_SIZE, SLOT_SIZE);
                this.add(terrainImage[y][x]);
                if (map[y][x] == Levels.GRASS)
                    this.setLayer(terrainImage[y][x], GRASS_LAYER);
                else if (map[y][x] == Levels.WATER)
                    this.setLayer(terrainImage[y][x], WATER_LAYER);
                else
                    this.setLayer(terrainImage[y][x], WALL_LAYER);
            }
        }

        base = new Base(this, Levels.BASE_BATTLE_X, Levels.BASE_BATTLE_Y);

        this.setBounds(0, 0, Levels.BATTLE_WIDTH * SLOT_SIZE, Levels.BATTLE_HEIGHT * SLOT_SIZE);
    }

    //install InfoPanel on the right of the map.
    private void initInfoPanel(int level) {
        infoPanel = new InfoPanel(INIT_ENEMY, p1Life, p2Life, level);
        infoPanel.setLocation(Levels.BATTLE_WIDTH * SLOT_SIZE, 0);
        this.add(infoPanel, BACKGROUND_LAYER);
        this.setSize(this.getWidth() + infoPanel.getWidth(), this.getHeight());
    }


    //public interfaces

    /**
     * Destroy the terrain at certain battle coordinate (regardless of what it is before).
     *
     * @param battleX battle x of wall.
     * @param battleY battle y of wall.
     */
    public void destroyTerrain(int battleX, int battleY) {
        map[battleY][battleX] = Levels.NORMAL;
        this.remove(terrainImage[battleY][battleX]);
        terrainImage[battleY][battleX] = null;
        repaint();
    }

    public void pauseOrContinue() {
        if (!paused) {
            paused = true;
            timer.stop();

            this.add(pauseScreen, PLUS_LAYER);

        } else {
            paused = false;
            timer.start();

            this.remove(pauseScreen);
        }
    }

    public void pauseGame() {
        if (paused)
            return;
        pauseOrContinue();
    }

    public void continueGame() {
        if (!paused)
            return;
        pauseOrContinue();
    }


    /**
     * Returns if the tank is blocked.
     * Note that battleX and battleY should be upperLeft corner of the mover.
     */
    public boolean tankBlocked(int x, int y, Tank mover) {
        Rectangle destRect = MapUtils.convertBattleRect(x, y, mover.getWidth(), mover.getHeight());

        //check if in the border
        //note in screen coordinate
        if (x < 0 || x + mover.getWidth() > Levels.BATTLE_WIDTH * SLOT_SIZE ||
                y < 0 || y + mover.getHeight() > Levels.BATTLE_HEIGHT * SLOT_SIZE)
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

    public void victory() {

        p1State.life = p1Life;
        p2State.life = p2Life;

        timer.registerListener(new TimerListener() {

            //3 seconds
            private int counter = 3 * Timeline.FPS;

            @Override
            public void onTimer() {
                counter--;
                if (counter == 0) {
                    parentState.endState(p1State, p2State, true);
                    timer.removeListener(this);
                }
            }
        });

    }

    public void gameOver() {

        Sound.play("/sounds/gameover.wav");

        p1State.life = p1Life;
        p2State.life = p2Life;

        new GameOverSign(140, this.getHeight() + 100,
                140, this.getHeight() / 2 - 100,
                Timeline.FPS, this);

        timer.registerListener(new TimerListener() {

            //4 seconds
            private int counter = 4 * Timeline.FPS;

            @Override
            public void onTimer() {
                counter--;
                if (counter == 0) {
                    parentState.endState(p1State, p2State, false);
                    timer.removeListener(this);
                }
            }
        });
    }


    //private util

    private boolean isTerrainPassable(int battleX, int battleY) {
        return map[battleY][battleX] == Levels.NORMAL ||
                map[battleY][battleX] == Levels.GRASS;
    }


    //Getters and setters

    public int[][] getMap() {
        return map;
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

    public List<EnemyTank> getEnemyTankList() {
        return enemyTankList;
    }

    public Timeline getTimer() {
        return timer;
    }

    public PlayerState getPlayerState(boolean isP1) {
        return isP1 ? p1State : p2State;
    }

    public void updateScore() {
        infoPanel.updateScore(p1State.totalScore, true);
        infoPanel.updateScore(p2State.totalScore, false);
    }

    public Base getBase() {
        return base;
    }

    public NetComm getNetComm() {
        return netComm;
    }

    public void setNetComm(NetComm netComm) {
        this.netComm = netComm;
    }

    public boolean isNotClient() {
        return !isClient;
    }

    public boolean isOnline() {
        return netComm != null;
    }

    public int getLevel() {
        return level;
    }
}
