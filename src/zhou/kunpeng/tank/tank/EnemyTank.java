package zhou.kunpeng.tank.tank;

import zhou.kunpeng.tank.GameMap;
import zhou.kunpeng.tank.ImageComponent;
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
    public static final double AI_TURN_RATE = 0.2;
    public static final double AI_FIRE_RATE = 0.2;

    protected void doAIThread () {
        //Logic: if blocked or it happens to decide to turn (in 20% rate), then it turns.
        //If it decides to fire (in 40% rate), then try to fire.
        if(isBlocked() || Math.random() >= 0.8)
            startMove((int)(Math.random() * 4));

        if(Math.random() >= 0.6)
            fire();

    }

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

        //every 1/3 second
        aiOperateCounter++;

        if(aiOperateCounter == (int) (AI_OPERATE_INTERVAL * Timeline.FPS)) {
            aiOperateCounter = 0;

            if(isBlocked() || Math.random() <= AI_TURN_RATE)
                startMove((int)(Math.random() * 4));

            if(Math.random() <= AI_FIRE_RATE)
                fire();
        }
    }

    @Override
    protected void tankDestroy() {
        super.tankDestroy();
        getGameMap().getEnemyTankList().remove(this);
    }

    @Override
    public abstract void triggerHit();
}
