package zhou.kunpeng.tank.battle.enemies;

import zhou.kunpeng.tank.battle.GameMap;
import zhou.kunpeng.tank.PlayerState;
import zhou.kunpeng.tank.battle.EnemyTank;
import zhou.kunpeng.tank.battle.PlayerTank;
import zhou.kunpeng.tank.battle.Tank;
import zhou.kunpeng.tank.display.ImageComponent;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by JA on 2017/5/21.
 * <p>
 * SecondaryTank is all the same as NormalTank,
 * except for a (slightly) different display and a higher score.
 * </p>
 */
public class SecondaryTank extends EnemyTank {
    public static final int SPEED = 3;
    public static final int CANNON_SPEED = 18;

    public SecondaryTank(int initX, int initY, GameMap gameMap) {
        super(SPEED, CANNON_SPEED, new ArrayList<>(Arrays.asList(
                new ImageComponent("/images/t21.png"),
                new ImageComponent("/images/t22.png"))),
                initX, initY, gameMap);

    }

    @Override
    public void triggerHit(Tank attacker) {
        tankDestroy();
        if (!(attacker instanceof PlayerTank))
            return;
        getGameMap().getPlayerState(((PlayerTank) attacker).isP1()).secondaryTankCount++;
        getGameMap().getPlayerState(((PlayerTank) attacker).isP1()).totalScore += PlayerState.SECONDARY_TANK_SCORE;
        getGameMap().updateScore();
    }
}
