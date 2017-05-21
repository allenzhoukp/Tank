package zhou.kunpeng.tank.tanks;

import zhou.kunpeng.tank.GameMap;
import zhou.kunpeng.tank.display.ImageComponent;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by JA on 2017/5/21.
 * <p>
 *     SecondaryTank is all the same as NormalTank,
 *     except for a (slightly) different display and a higher score.
 * </p>
 */
public class SecondaryTank extends EnemyTank{
    public static final int SPEED = 3;
    public static final int CANNON_SPEED = 12;

    public SecondaryTank(int initX, int initY, GameMap gameMap) {
        super(SPEED, CANNON_SPEED, new ArrayList<>(Arrays.asList(
                new ImageComponent("/images/t21.png"),
                new ImageComponent("/images/t22.png"))),
                initX, initY, gameMap);

    }

    @Override
    public void triggerHit(Tank attacker) {
        tankDestroy();
        if(!(attacker instanceof PlayerTank))
            return;
        getGameMap().getScoreCounter(((PlayerTank)attacker).isP1()).secondaryTankCount ++;
    }
}
