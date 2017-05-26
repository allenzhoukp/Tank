package zhou.kunpeng.tank.battle;

import zhou.kunpeng.tank.Sound;
import zhou.kunpeng.tank.display.ImageComponent;

import java.util.List;

/**
 * Created by JA on 2017/5/20.
 * <p>
 * A base class for all enemy tanks: abstracted the AI control over the tank.
 * </p>
 */
public abstract class EnemyTank extends Tank {

    public static int ID = 1;

    /**
     * Create a new enemy tank.
     * The params will be passed to base class, and side = 1 (enemy). <br>
     * The AI controller thread will start.
     */
    protected EnemyTank(int speed, int cannonSpeed, List<ImageComponent> clipSequence, int x, int y, GameMap gameMap) {
        super(GameMap.ENEMY_SIDE, speed, cannonSpeed, clipSequence, x, y, gameMap, EnemyTank.ID++);
    }

    @Override
    protected void tankDestroy() {
        super.tankDestroy();
        Sound.play("/sounds/boom.wav");
        getGameMap().getEnemyTankList().remove(this);

        if (getGameMap().getEnemyRemaining() == 0 &&
                getGameMap().getEnemyTankList().size() == 0)
            getGameMap().victory();
    }

}
