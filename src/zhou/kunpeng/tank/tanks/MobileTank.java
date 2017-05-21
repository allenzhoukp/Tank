package zhou.kunpeng.tank.tanks;

import zhou.kunpeng.tank.GameMap;
import zhou.kunpeng.tank.display.ImageComponent;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by JA on 2017/5/21.
 * <p>
 *     MobileTank has a extremely high speed.
 *     It has a higher score as well.
 * </p>
 */
public class MobileTank extends EnemyTank {

    public static final int SPEED = 8;
    public static final int CANNON_SPEED = 12;

    public MobileTank(int initX, int initY, GameMap gameMap) {
        super(SPEED, CANNON_SPEED, new ArrayList<>(Arrays.asList(
                new ImageComponent("/images/ts1.png"),
                new ImageComponent("/images/ts2.png"))),
                initX, initY, gameMap);

    }

    @Override
    public void triggerHit(Tank attacker) {
        tankDestroy();
        if(!(attacker instanceof PlayerTank))
            return;
        getGameMap().getScoreCounter(((PlayerTank)attacker).isP1()).mobileTankCount ++;
    }
}
