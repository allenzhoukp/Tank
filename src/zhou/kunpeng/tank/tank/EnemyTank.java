package zhou.kunpeng.tank.tank;

import zhou.kunpeng.tank.GameMap;
import zhou.kunpeng.tank.ImageComponent;

import java.util.List;

/**
 * Created by JA on 2017/5/20.
 * <p>
 * A base class for all enemy tanks: abstracted the AI control over the tank.
 * </p>
 */
public abstract class EnemyTank extends Tank {

    private class AIThread extends Thread {
        private boolean running = true;

        @Override
        public void run() {
            while(running)
                doAIThread();
        }

        public void stopRunning() {
            running = false;
        }
    }

    protected void doAIThread () {
        //Logic: if blocked or it happens to decide to turn (in 20% rate), then it turns.
        //If it decides to fire (in 40% rate), then try to fire.
        if(isBlocked() || Math.random() >= 0.8)
            startMove((int)(Math.random() * 4));

        if(Math.random() >= 0.6)
            fire();

        try {
            Thread.sleep((int)(0.5 * 1000));
        } catch (InterruptedException e) {
            // Anything to do?
        }
    }

    private AIThread aiThread;

    /**
     * Create a new enemy tank.
     * The params will be passed to base class, and side = 1 (enemy). <br>
     * The AI controller thread will start.
     */
    protected EnemyTank(int speed, int cannonSpeed, List<ImageComponent> clipSequence, int x, int y, GameMap gameMap) {
        super(GameMap.ENEMY_SIDE, speed, cannonSpeed, clipSequence, x, y, gameMap);
        aiThread = new AIThread();
        aiThread.start();
    }

    @Override
    protected void tankDestroy() {
        super.tankDestroy();
        getGameMap().getEnemyTankList().remove(this);
        aiThread.stopRunning();
    }

    @Override
    public abstract void triggerHit();
}
