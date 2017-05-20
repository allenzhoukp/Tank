package zhou.kunpeng.tank;

import java.util.Arrays;
import java.util.List;

/**
 * Created by JA on 2017/5/20.
 */
public class PlayerTank extends Tank {

    private static final int CANNON_SPEED = 12;
    private static final int SPEED = 5;

    private boolean isP1;
    private boolean immunity = false;

    //This one is for test.
    public PlayerTank(boolean isP1, int initX, int initY, GameMap gameMap) {
        super(0, SPEED, CANNON_SPEED,
                isP1 ? Arrays.asList(new ImageComponent("/images/1p1.png"),
                        new ImageComponent("/images/1p2.png"))
                        : Arrays.asList(new ImageComponent("/images/2p1.png"),
                        new ImageComponent("/images/2p2.png")),
                initX, initY, gameMap);
        if (isP1)
            gameMap.setP1Tank(this);
        else
            gameMap.setP2Tank(this);
        this.isP1 = isP1;
    }

    public PlayerTank(boolean isP1, GameMap gameMap) {
        this(isP1, GameMap.toScreenCoordinate(isP1 ? GameMap.P1_BORN_BATTLE_X : GameMap.P2_BORN_BATTLE_X),
                GameMap.toScreenCoordinate(isP1 ? GameMap.P1_BORN_BATTLE_Y : GameMap.P2_BORN_BATTLE_Y),
                gameMap);
    }

    @Override
    public void triggerHit() {
        if(immunity)
            return;

        tankDestroy();
        if (isP1)
            gameMap.setP1Tank(null);
        else
            gameMap.setP2Tank(null);

        gameMap.getClipManager().getClipList().remove(this);

        int life = isP1 ? gameMap.getP1Life() : gameMap.getP2Life();
        if (life == 0) {
            if (gameMap.getP1Life() == 0 &&
                    gameMap.getP2Life() == 0)
                gameMap.gameOver();
            return;
        }
        life--;
        if (isP1)
            gameMap.setP1Life(life);
        else
            gameMap.setP2Life(life);

        gameMap.repaint();

        // it takes time to revive.
        try {
            Thread.sleep((int) (GameMap.BORN_SEC * 1000));
        } catch (InterruptedException e) {
            // Anything to do?
        }

        // the old tank (this) will go to gc afterwards.
        Tank newTank = new PlayerTank(isP1, gameMap);
        gameMap.add(newTank, GameMap.TANK_LAYER);
        ImageComponent pic1 = newTank.getSequence().get(0);
        ImageComponent pic2 = newTank.getSequence().get(0);
        ImageComponent blank = new ImageComponent("/images/null.png");

        List<ImageComponent> seqBackup = newTank.getSequence();

        //When revive, the tank will flash for a while, and becomes immune.
        //This is a 8-frame sequence, where half of the time is blank.
        newTank.setSequence(Arrays.asList(pic1, pic2, pic1, pic2, blank, blank, blank, blank));

        gameMap.getClipManager().getClipList().add(newTank);
        newTank.gotoAndPlay(0);
        immunity = true;

        // shield will exist for a while
        try {
            Thread.sleep((int) (GameMap.BORN_SHIELD_SEC * 1000));
        } catch (InterruptedException e) {
            // Anything to do?
        }

        //Remove shield
        immunity = false;
        newTank.setSequence(seqBackup);
        newTank.gotoAndPlay(0);
    }
}
