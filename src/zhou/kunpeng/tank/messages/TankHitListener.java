package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.battle.GameMap;
import zhou.kunpeng.tank.battle.Tank;
import zhou.kunpeng.tank.comm.ByteUtil;
import zhou.kunpeng.tank.comm.NetListener;

import java.util.List;

/**
 * Created by JA on 2017/5/22.
 */
public class TankHitListener implements NetListener {

    private final GameMap gameMap;

    public TankHitListener(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    @Override
    public boolean tryInterpret(byte[] line) {
        if (ByteUtil.getShort(line, 0) != TankHitMessage.TYPE)
            return false;

        int id = ByteUtil.getInt(line, 2);
        int attackerId = ByteUtil.getInt(line, 6);

        Tank defender = null;
        Tank attacker = null;
        List<Tank> tankList = gameMap.getAllTanks();
        for (Tank tank : tankList) {
            if (tank == null)
                continue;
            if (tank.getId() == id)
                defender = tank;
            else if (tank.getId() == attackerId)
                attacker = tank;
        }

        if (defender == null)
            return false;

        defender.triggerHit(attacker);
        return true;
    }
}
