package zhou.kunpeng.tank.tanks;

import zhou.kunpeng.tank.GameMap;
import zhou.kunpeng.tank.display.ImageComponent;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by JA on 2017/5/21.
 * <p>
 *     ToughTank requires 3 hits to get destroyed.
 *     It has the highest score as well.
 * </p>
 */
public class ToughTank extends EnemyTank {

    public static final int SPEED = 5;
    public static final int CANNON_SPEED = 18;

    private int life;
    public ToughTank(int initX, int initY, GameMap gameMap) {
        super(SPEED, CANNON_SPEED, new ArrayList<>(Arrays.asList(
                new ImageComponent("/images/tt31.png"),
                new ImageComponent("/images/tt32.png"))),
                initX, initY, gameMap);
        life = 3;
    }

    @Override
    public void triggerHit(Tank attacker) {
        life--;
        if(life == 2 || life == 1) {
            changeDisplay();
            return;
        }
        tankDestroy();
        if(!(attacker instanceof PlayerTank))
            return;
        getGameMap().getPlayerState(((PlayerTank)attacker).isP1()).toughTankCount ++;
    }

    private void changeDisplay() {
        if(life == 0 || life > 3)
            return;

        this.setSequence(new ArrayList<>(Arrays.asList(
                new ImageComponent("/images/tt" + life + "1.png"),
                new ImageComponent("/images/tt" + life + "2.png")
        )));

        for(ImageComponent image : this.getSequence())
            image.rotate(- this.getDirection() * Math.PI / 2);

        this.gotoAndPlay(0);
    }
}
