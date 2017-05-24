package zhou.kunpeng.tank.battle.ai;

import zhou.kunpeng.tank.battle.GameMap;
import zhou.kunpeng.tank.battle.MapUtils;
import zhou.kunpeng.tank.battle.EnemyTank;
import zhou.kunpeng.tank.battle.Tank;
import zhou.kunpeng.tank.messages.EnemyGenerateMessage;
import zhou.kunpeng.tank.battle.enemies.*;
import zhou.kunpeng.tank.timer.TimerListener;

/**
 * Created by JA on 2017/5/22.
 * <p>
 *     Create enemy tank using an outcome.
 * </p>
 */
public class EnemyCreator {
    /**
     * Create enemy tank.
     * @param gameMap gameMap of the battle.
     * @param battleX creation location.
     * @param battleY creation location.
     * @param dice a random outcome between 0 and 1.
     */
    public void createStar(GameMap gameMap, int battleX, int battleY, double dice) {

        //Net Communication
        if(gameMap.isNotClient() && gameMap.isOnline())
            gameMap.getNetComm().send(new EnemyGenerateMessage(battleX, battleY, dice));

        //Create a star. The star will call tank creation when it disappears.
        Star star = new Star(battleX, battleY);
        gameMap.add(star, GameMap.TANK_LAYER);
        gameMap.getTimer().registerListener(star);

        gameMap.getTimer().registerListener(
                new CreationListener(gameMap, star,
                        MapUtils.toScreenCoordinate(battleX),
                        MapUtils.toScreenCoordinate(battleY),
                        dice));
    }

    // wait for all the frames of Star has been displayed, and call createTank().
    private class CreationListener implements TimerListener {
        private GameMap gameMap;
        private int counter;
        private int tankX;
        private int tankY;
        private Star star;
        private double dice;

        CreationListener(GameMap gameMap, Star star, int tankX, int tankY, double dice) {
            this.gameMap = gameMap;
            this.star = star;
            counter = star.getFrameCount();
            this.tankX = tankX;
            this.tankY = tankY;
            this.dice = dice;
        }

        @Override
        public void onTimer() {
            counter--;
            if (counter > 0)
                return;

            createNewTank(gameMap, tankX, tankY, dice);
            gameMap.getTimer().removeListener(this);
            gameMap.remove(star);
            gameMap.getTimer().removeListener(star);
        }
    }

    //TODO add tanks that could generate plus.
    //problem: a constructor call may not be sufficient... has to be written in direct call, rather than reflect.
    private void createNewTank(GameMap gameMap, int x, int y, double dice) {
        //choose a tank to create according to the outcome (dice)
        EnemyTank tank;
        if (dice <= 0.2)
            tank = new NormalTank(x, y, gameMap);
        else if (dice <= 0.4)
            tank = new SecondaryTank(x, y, gameMap);
        else if (dice <= 0.7)
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
