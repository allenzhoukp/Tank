package zhou.kunpeng.tank.tanks;

import zhou.kunpeng.tank.GameMap;
import zhou.kunpeng.tank.display.ImageComponent;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by JA on 2017/5/20.
 * <p>
 *     NormalTank is the most basic EnemyTank with low speed and low score.
 * </p>
 */
public class NormalTank extends EnemyTank {

    public static final int SPEED = 3;
    public static final int CANNON_SPEED = 12;

    public NormalTank(int initX, int initY, GameMap gameMap) {
        super(SPEED, CANNON_SPEED, new ArrayList<>(Arrays.asList(
                new ImageComponent("/images/t11.png"),
                new ImageComponent("/images/t12.png"))),
                initX, initY, gameMap);

    }

    @Override
    public void triggerHit(Tank attacker) {
        tankDestroy();
        if(!(attacker instanceof PlayerTank))
            return;
        getGameMap().getScoreCounter(((PlayerTank)attacker).isP1()).normalTankCount ++;
    }
}
