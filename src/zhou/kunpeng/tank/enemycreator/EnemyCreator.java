package zhou.kunpeng.tank.enemycreator;

import zhou.kunpeng.tank.GameMap;
import zhou.kunpeng.tank.Timeline;
import zhou.kunpeng.tank.TimerListener;
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
public class EnemyCreator implements TimerListener {

    private final GameMap gameMap;
    private int counter;

    // wait for all the frames of Star has been displayed, and call createTank().
    private class CreationListener implements TimerListener {
        private int counter;
        private int tankX;
        private int tankY;

        CreationListener(int waitTime, int tankX, int tankY) {
            counter = waitTime;
            this.tankX = tankX;
            this.tankY = tankY;
        }

        @Override
        public void onTimer() {
            counter--;
            if (counter != 0)
                return;

            createNewTank(tankX, tankY);
            gameMap.getTimer().removeListener(this);
        }
    }

    public EnemyCreator(GameMap gameMap) {
        this.gameMap = gameMap;
        this.counter = 0;
    }

    //Every 4 second, check whether a new tank can be created.
    //If true, create Star in slots from left to right, and add CreationListener to wait for creation.
    public void onTimer() {
        this.counter++;
        if (this.counter == 4 * Timeline.FPS) {
            counter = 0;

            int tanksToCreate = Math.min(4 - gameMap.getEnemyTankList().size(), gameMap.getEnemyRemaining());
            int slot = 0;

            //Only 3 tanks can be created at a time
            while (tanksToCreate != 0 && slot < 3) {

                //Create a star. The star will call tank creation when it disappears.
                Star star = new Star(slot * 12, 0, gameMap, EnemyCreator.this);
                gameMap.add(star, GameMap.TANK_LAYER);

                tanksToCreate--;
                slot++;

                gameMap.getTimer().registerListener(
                        new CreationListener(star.getFrameCount(),
                                GameMap.toScreenCoordinate(slot * 12),
                                GameMap.toScreenCoordinate(0)));
            }
        }

    }

    //TODO add other tanks. Only Normal tank avaliable now.
    private void createNewTank(int x, int y) {
        EnemyTank tank = new NormalTank(x, y, gameMap);
        gameMap.getEnemyTankList().add(tank);
        gameMap.setEnemyRemaining(gameMap.getEnemyRemaining() - 1);
    }

}
