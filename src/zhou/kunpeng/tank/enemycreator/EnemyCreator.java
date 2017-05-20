package zhou.kunpeng.tank.enemycreator;

import zhou.kunpeng.tank.GameMap;
import zhou.kunpeng.tank.SyncLock;
import zhou.kunpeng.tank.tank.EnemyTank;
import zhou.kunpeng.tank.tank.NormalTank;

/**
 * Created by JA on 2017/5/20.
 * <p>
 * Creates new enemy tank.
 * This happpens in a 2-second interval search.
 * If the creator finds that the enemy tank number is less than 4,
 * then it will try to create tanks.
 * </p>
 */
public class EnemyCreator extends Thread {

    private final GameMap gameMap;
    private boolean running = true;

    public EnemyCreator(GameMap gameMap) {
        this.gameMap = gameMap;
    }


    @Override
    public void run() {
        while (running) {
            int tanksToCreate = Math.min(4 - gameMap.getEnemyTankList().size(), gameMap.getEnemyRemaining());
            int slot = 0;
            while (tanksToCreate != 0 && slot < 3) {

                //Create a star. The star will call tank creation when it disappears.
                Star star = new Star(slot * 12, 0, gameMap, EnemyCreator.this);
                gameMap.getClipManager().getClipList().add(star);
                gameMap.add(star, GameMap.TANK_LAYER);

                tanksToCreate--;
                slot++;
            }
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                //Do nothing
            }
        }
    }

    void createNewTank(int x, int y) {
        synchronized (gameMap) {
            EnemyTank tank = new NormalTank(x, y, gameMap);
            gameMap.getEnemyTankList().add(tank);
            gameMap.setEnemyRemaining(gameMap.getEnemyRemaining() - 1);
        }
    }

    public void stopRunning() {
        running = false;
    }

}
