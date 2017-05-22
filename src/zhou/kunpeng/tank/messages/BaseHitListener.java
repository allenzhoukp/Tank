package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.GameMap;
import zhou.kunpeng.tank.comm.NetListener;

/**
 * Created by JA on 2017/5/22.
 */
public class BaseHitListener implements NetListener {

    private final GameMap gameMap;

    public BaseHitListener(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    @Override
    public boolean tryInterpret(String line) {
        if (!line.startsWith("basehit\\s*"))
            return false;

        gameMap.getBase().triggerHit();

        return true;
    }
}
