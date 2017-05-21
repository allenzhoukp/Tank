package zhou.kunpeng.tank.ai;

import zhou.kunpeng.tank.GameMap;
import zhou.kunpeng.tank.time.Timeline;
import zhou.kunpeng.tank.time.TimerListener;
import zhou.kunpeng.tank.tanks.EnemyTank;

/**
 * Created by JA on 2017/5/21.
 */
public class AIOperator implements TimerListener {

    private int aiOperateCounter = 0;

    public static final double AI_OPERATE_INTERVAL = 0.2;
    public static final double AI_BLOCKED_TURN_RATE = 0.5;
    public static final double AI_TURN_RATE = 0.02;
    public static final double AI_FIRE_RATE = 0.2;

    private final GameMap gameMap;

    public AIOperator(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public void onTimer() {
        if(!AI.AI_ENABLED)
            return;

        aiOperateCounter++;

        if(aiOperateCounter == (int) Math.floor(AI_OPERATE_INTERVAL * Timeline.FPS)) {
            aiOperateCounter = 0;
            doAI();
        }
    }

    private void doAI() {
        for(EnemyTank tank : gameMap.getEnemyTankList()) {
            if ((tank.isBlocked() && Math.random() <= AI_BLOCKED_TURN_RATE)
                    || Math.random() <= AI_TURN_RATE)
                tank.appendMove((int) Math.floor(Math.random() * 4));

            if (Math.random() <= AI_FIRE_RATE)
                tank.fire();
        }
    }
}
