package zhou.kunpeng.tank.ai;

import zhou.kunpeng.tank.GameMap;
import zhou.kunpeng.tank.messages.EnemyGenerateMessage;
import zhou.kunpeng.tank.messages.TankMoveMessage;
import zhou.kunpeng.tank.timer.Timeline;
import zhou.kunpeng.tank.timer.TimerListener;

/**
 * Created by JA on 2017/5/20.
 * <p>
 * Decides where to create new enemy tank, and then call EnemyCreator to create.
 * This happpens in a 2-second interval search.
 * If the creator finds that the enemy tank number is less than 4,
 * then it will try to create tanks.
 * </p>
 */
public class AIEnemyCreationOperator implements TimerListener {

    private final GameMap gameMap;
    private int counter;

    public static final double CREATE_INTERVAL = 4.0;

    /**
     * Will NOT register listener immediately!
     *
     * @param gameMap to keep a GameMap reference.
     */
    public AIEnemyCreationOperator(GameMap gameMap) {
        this.gameMap = gameMap;
        this.counter = 0;
        Star.preload();
        createStars();
    }

    //Every some seconds, check whether a new tank can be created.
    //If true, create Star in slots from left to right, and add CreationListener to wait for creation.
    public void onTimer() {
        this.counter++;
        if (this.counter == (int) Math.floor(CREATE_INTERVAL * Timeline.FPS)) {
            counter = 0;
            createStars();
        }

    }

    private void createStars() {
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
        //Outcome determined NOW, not after the star finishes its play.
        for (int i = 0; i < tanksToCreate; i++) {
            new EnemyCreator().createStar(gameMap, slots[i] * 12, 0, Math.random());
        }
    }

}
