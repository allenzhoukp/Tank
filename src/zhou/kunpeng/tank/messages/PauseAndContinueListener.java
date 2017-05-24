package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.battle.GameMap;
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
    public boolean tryInterpret(String line) {
        if (line.startsWith("pause&cont")) {
            gameMap.pauseOrContinue();
            return true;
        } else if (line.startsWith("pause")) {
            gameMap.pauseGame();
            return true;
        } else if (line.startsWith("cont")) {
            gameMap.continueGame();
            return true;
        }
        return false;
    }
}
