package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.battle.GameMap;
import zhou.kunpeng.tank.comm.ByteUtil;
import zhou.kunpeng.tank.comm.NetListener;
import zhou.kunpeng.tank.battle.Tank;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JA on 2017/5/22.
 */
public class TankFireListener implements NetListener {
    private final GameMap gameMap;

    public TankFireListener(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    @Override
    public boolean tryInterpret(byte[] line) {

        if(ByteUtil.getShort(line, 0) != TankFireMessage.TYPE)
            return false;

        int id = ByteUtil.getInt(line, 2);
        int x = ByteUtil.getInt(line, 6);
        int y = ByteUtil.getInt(line, 10);
        int dir = ByteUtil.getInt(line, 14);

        Tank tank = null;
        for(Tank t : gameMap.getAllTanks())
            if(t != null && t.getId() == id)
                tank = t;

        if (tank == null)
            return false;

        tank.setLocation(x, y);
        tank.changeDirection(dir);
        tank.fire();

        return true;
    }
}
