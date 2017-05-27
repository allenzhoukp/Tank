package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.battle.GameMap;
import zhou.kunpeng.tank.comm.ByteUtil;
import zhou.kunpeng.tank.comm.NetListener;

/**
 * Created by JA on 2017/5/24.
 */
public class PauseAndContinueListener implements NetListener {

    private final GameMap gameMap;

    public PauseAndContinueListener(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    @Override
    public boolean tryInterpret(byte[] line) {
        if (ByteUtil.getShort(line, 0) == PauseAndContinueMessage.TYPE) {
            gameMap.pauseOrContinue();
            return true;
        }
        return false;
    }
}
