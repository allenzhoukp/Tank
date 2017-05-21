package zhou.kunpeng.tank.tanks;

import zhou.kunpeng.tank.GameMap;
import zhou.kunpeng.tank.display.ImageComponent;
import zhou.kunpeng.tank.Timeline;

import java.util.List;

/**
 * Created by JA on 2017/5/20.
 * <p>
 * A base class for all enemy tanks: abstracted the AI control over the tank.
 * </p>
 */
public abstract class EnemyTank extends Tank {

    private int aiOperateCounter = 0;

    public static final double AI_OPERATE_INTERVAL = 0.33;
    public static final double AI_TURN_RATE = 0.05;
    public static final double AI_FIRE_RATE = 0.4;

    /**
     * Create a new enemy tank.
     * The params will be passed to base class, and side = 1 (enemy). <br>
     * The AI controller thread will start.
     */
    protected EnemyTank(int speed, int cannonSpeed, List<ImageComponent> clipSequence, int x, int y, GameMap gameMap) {
        super(GameMap.ENEMY_SIDE, speed, cannonSpeed, clipSequence, x, y, gameMap);
    }

    @Override
    public void onTimer() {
        super.onTimer();

        aiOperateCounter++;

        if(aiOperateCounter == (int) Math.floor(AI_OPERATE_INTERVAL * Timeline.FPS)) {
            aiOperateCounter = 0;

            if(isBlocked() || Math.random() <= AI_TURN_RATE)
                appendMove((int) Math.floor(Math.random() * 4));

            if(Math.random() <= AI_FIRE_RATE)
                fire();
        }
    }

    @Override
    protected void tankDestroy() {
        super.tankDestroy();
        getGameMap().getEnemyTankList().remove(this);

        if(getGameMap().getEnemyRemaining() == 0 &&
                getGameMap().getEnemyTankList().size() == 0)
            getGameMap().victory();
    }

}
