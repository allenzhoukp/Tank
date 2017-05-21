package zhou.kunpeng.tank.enemycreator;

import zhou.kunpeng.tank.GameMap;
import zhou.kunpeng.tank.MapUtils;
import zhou.kunpeng.tank.Timeline;
import zhou.kunpeng.tank.TimerListener;
import zhou.kunpeng.tank.tanks.*;

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

    public static final double CREATE_INTERVAL = 4.0;

    /**
     * Will NOT register listener immediately!
     *
     * @param gameMap to keep a GameMap reference.
     */
    public EnemyCreator(GameMap gameMap) {
        this.gameMap = gameMap;
        this.counter = 0;
        Star.preload();
        createStar();
    }

    //Every some seconds, check whether a new tank can be created.
    //If true, create Star in slots from left to right, and add CreationListener to wait for creation.
    public void onTimer() {
        this.counter++;
        if (this.counter == (int) Math.floor(CREATE_INTERVAL * Timeline.FPS)) {
            counter = 0;
            createStar();
        }

    }

    private void createStar() {
        int tanksToCreate = Math.min(4 - gameMap.getEnemyTankList().size(), gameMap.getEnemyRemaining());
        tanksToCreate = Math.min(tanksToCreate, 3);

        int[] slots = {0, 1, 2};

        if (tanksToCreate == 0)
            return;
        if (tanksToCreate == 1)
            slots[0] = (int) Math.floor(Math.random() * 3);
        else if (tanksToCreate == 2) {
            int miss = (int) Math.floor(Math.random() * 3);
            for (int i = 0, j = 0; i < 2; i++, j++) {
                if (j == miss) j++;
                slots[i] = j;
            }
        } // else slots = {0,1,2}


        //Only 3 tanks can be created at a time
        for (int i = 0; i < tanksToCreate; i++) {

            //Create a star. The star will call tank creation when it disappears.
            Star star = new Star(slots[i] * 12, 0, gameMap, EnemyCreator.this);
            gameMap.add(star, GameMap.TANK_LAYER);
            gameMap.getTimer().registerListener(star);

            gameMap.getTimer().registerListener(
                    new CreationListener(star,
                            MapUtils.toScreenCoordinate(slots[i] * 12),
                            MapUtils.toScreenCoordinate(0)));
        }
    }

    // wait for all the frames of Star has been displayed, and call createTank().
    private class CreationListener implements TimerListener {
        private int counter;
        private int tankX;
        private int tankY;
        private Star star;

        CreationListener(Star star, int tankX, int tankY) {
            this.star = star;
            counter = star.getFrameCount();
            this.tankX = tankX;
            this.tankY = tankY;
        }

        @Override
        public void onTimer() {
            counter--;
            if (counter > 0)
                return;

            createNewTank(tankX, tankY);
            gameMap.getTimer().removeListener(this);
            gameMap.remove(star);
            gameMap.getTimer().removeListener(star);
        }
    }

    //TODO add tanks that could generate plus.
    //problem: a constructor call may not be sufficient... has to be written in direct call, rather than reflect.
    private void createNewTank(int x, int y) {
        //randomly choose a tank to create
        double dice = Math.random();
        EnemyTank tank;
        if(dice <= 0.2)
            tank = new NormalTank(x, y, gameMap);
        else if(dice <= 0.4)
            tank = new SecondaryTank(x, y, gameMap);
        else if(dice <= 0.7)
            tank = new MobileTank(x, y, gameMap);
        else
            tank = new ToughTank(x, y, gameMap);

        //add to map's tank list and update tank remaining
        gameMap.getEnemyTankList().add(tank);
        gameMap.setEnemyRemaining(gameMap.getEnemyRemaining() - 1);

        //default direction
        tank.startMove(Tank.SOUTH);
    }

}
