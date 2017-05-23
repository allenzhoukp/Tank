package zhou.kunpeng.tank.tanks;

import zhou.kunpeng.tank.GameMap;
import zhou.kunpeng.tank.MapUtils;
import zhou.kunpeng.tank.display.ImageComponent;
import zhou.kunpeng.tank.timer.Timeline;
import zhou.kunpeng.tank.timer.TimerListener;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by JA on 2017/5/20.
 * <p>
 * PlayerTank is the tank for player.
 * It has a default speed and cannon_speed, and is able to be upgraded and revive.
 * </p>
 */
public class PlayerTank extends Tank {

    public static final int P1_BORN_BATTLE_X = 9;
    public static final int P1_BORN_BATTLE_Y = 24;
    public static final int P2_BORN_BATTLE_X = 15;
    public static final int P2_BORN_BATTLE_Y = 24;
    public static final double BORN_SEC = 1.5;
    public static final int BORN_SHIELD_SEC = 4;

    private static final int CANNON_SPEED = 18;
    private static final int SPEED = 8;

    private boolean isP1;

    private boolean immunity = false;

    //This one is for test.
    public PlayerTank(boolean isP1, int initX, int initY, GameMap gameMap) {
        super(GameMap.PLAYER_SIDE, SPEED, CANNON_SPEED,
                new ArrayList<>(
                        isP1 ? Arrays.asList(
                                new ImageComponent("/images/1p1.png"),
                                new ImageComponent("/images/1p2.png"))
                                : Arrays.asList(
                                new ImageComponent("/images/2p1.png"),
                                new ImageComponent("/images/2p2.png"))),
                initX, initY, gameMap, isP1 ? -1 : -2);

        if (isP1)
            gameMap.setP1Tank(this);
        else
            gameMap.setP2Tank(this);

        this.isP1 = isP1;
    }

    public PlayerTank(boolean isP1, GameMap gameMap) {
        this(isP1, MapUtils.toScreenCoordinate(isP1 ? P1_BORN_BATTLE_X : P2_BORN_BATTLE_X),
                MapUtils.toScreenCoordinate(isP1 ? P1_BORN_BATTLE_Y : P2_BORN_BATTLE_Y),
                gameMap);
    }


    @Override
    public void triggerHit(Tank attacker) {
        //Attacker is no use here. Why should I care who hits me?

        if (immunity)
            return;

        //Basic destroy: remove from gameMap and other issues
        tankDestroy();
        if (isP1)
            getGameMap().setP1Tank(null);
        else
            getGameMap().setP2Tank(null);

        //reduce life
        int life = isP1 ? getGameMap().getP1Life() : getGameMap().getP2Life();
        if (life == 0) {
            if (getGameMap().getP1Life() == 0 &&
                    (!getGameMap().isOnline() || getGameMap().getP2Life() == 0))
                getGameMap().gameOver();
            return;
        }
        life--;
        if (isP1)
            getGameMap().setP1Life(life);
        else
            getGameMap().setP2Life(life);


        TimerListener revive = new TimerListener() {
            private int frameCounter = (int) (Timeline.FPS * (BORN_SHIELD_SEC + BORN_SEC));
            PlayerTank newTank;

            private void revive() {
                // the old tank (PlayerTank.this) will go to gc afterwards.
                newTank = new PlayerTank(isP1, getGameMap());

                //When revive, the tank will appear and disappear repeatedly for a while, and becomes immune.
                //This is a 8-frame sequence, where half of the time is blank.
                ImageComponent pic1 = newTank.getSequence().get(0);
                ImageComponent pic2 = newTank.getSequence().get(1);
                ImageComponent blank = new ImageComponent("/images/null.png");
                newTank.setSequence(new ArrayList<>(
                        Arrays.asList(pic1.clone(), pic2.clone(), pic1.clone(), pic2.clone(), blank, blank, blank, blank)));
                newTank.gotoAndPlay(0);

                newTank.immunity = true;
            }

            private void removeShield() {

                if (newTank == null)
                    return;

                newTank.immunity = false;

                //Remove frames after frame 2: Does not disappear anymore
                for (int i = 7; i >= 2; i--)
                    newTank.getSequence().remove(i);
            }

            @Override
            public void onTimer() {

                frameCounter--;

                //BORN_SEC time has passed
                if (frameCounter == Timeline.FPS * BORN_SHIELD_SEC)
                    revive();

                    //another BORN_SHIELD_SEC time has passed
                else if (frameCounter == 0) {
                    removeShield();

                    //Listener is out of use
                    getGameMap().getTimer().removeListener(this);
                }
            }
        };

        getGameMap().getTimer().registerListener(revive);

    }

    public boolean isP1() {
        return isP1;
    }

    public boolean isImmunity() {
        return immunity;
    }

    public void setImmunity(boolean immunity) {
        this.immunity = immunity;
    }
}
