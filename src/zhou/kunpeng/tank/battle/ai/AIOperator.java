package zhou.kunpeng.tank.battle.ai;

import zhou.kunpeng.tank.battle.GameMap;
import zhou.kunpeng.tank.battle.EnemyTank;
import zhou.kunpeng.tank.timer.Timeline;
import zhou.kunpeng.tank.timer.TimerListener;

/**
 * Created by JA on 2017/5/21.
 */
public class AIOperator implements TimerListener {

    private int aiOperateCounter = 0;

    public static final double AI_OPERATE_INTERVAL = 0.2;
    public static final double AI_BLOCKED_TURN_RATE = 0.5;
    public static final double AI_TURN_RATE = 0.1;
    public static final double AI_FIRE_RATE = 0.7;

    private final GameMap gameMap;

    public AIOperator(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public void onTimer() {
        aiOperateCounter++;

        if (aiOperateCounter == (int) Math.floor(AI_OPERATE_INTERVAL * Timeline.FPS)) {
            aiOperateCounter = 0;
            doAI();
        }
    }

    private void doAI() {
        for (EnemyTank tank : gameMap.getEnemyTankList()) {
            if ((tank.isBlocked() && Math.random() <= AI_BLOCKED_TURN_RATE)
                    || Math.random() <= AI_TURN_RATE) {
                int direction = (int) Math.floor(Math.random() * 4);
                tank.appendMove(direction);
            }

            if (Math.random() <= AI_FIRE_RATE) {
                tank.fire();
            }
        }
    }
}
