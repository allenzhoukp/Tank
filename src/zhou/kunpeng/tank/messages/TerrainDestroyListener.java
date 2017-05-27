package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.battle.GameMap;
import zhou.kunpeng.tank.comm.ByteUtil;
import zhou.kunpeng.tank.comm.NetListener;

/**
 * Created by JA on 2017/5/22.
 */
public class TerrainDestroyListener implements NetListener {

    private final GameMap gameMap;

    public TerrainDestroyListener(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    @Override
    public boolean tryInterpret(byte[] line) {
        if (ByteUtil.getShort(line, 0) != TerrainDestroyMessage.TYPE)
            return false;

        int battleX = ByteUtil.getInt(line, 2);
        int battleY = ByteUtil.getInt(line, 6);

        gameMap.destroyTerrain(battleX, battleY);

        return true;
    }
}
