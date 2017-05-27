package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.battle.GameMap;
import zhou.kunpeng.tank.comm.ByteUtil;
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
    public boolean tryInterpret(byte[] line) {
        if (ByteUtil.getShort(line, 0) != BaseHitMessage.TYPE)
            return false;
        gameMap.getBase().triggerHit();

        return true;
    }
}
