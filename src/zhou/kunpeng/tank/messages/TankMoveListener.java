package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.battle.GameMap;
import zhou.kunpeng.tank.battle.Tank;
import zhou.kunpeng.tank.comm.ByteUtil;
import zhou.kunpeng.tank.comm.NetListener;

/**
 * Created by JA on 2017/5/22.
 */
public class TankMoveListener implements NetListener {

    private final GameMap gameMap;

    public TankMoveListener(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    @Override
    public boolean tryInterpret(byte[] line) {
        if (ByteUtil.getShort(line, 0) != TankMoveMessage.TYPE)
            return false;

        int id = ByteUtil.getInt(line, 2);
        int x = ByteUtil.getInt(line, 6);
        int y = ByteUtil.getInt(line, 10);
        int direction = ByteUtil.getInt(line, 14);

        //System.out.println("FireMarkerL: " + System.nanoTime() / 1000000L + "id=" + id + ",x=" + x + ",y=" + y + ",dir=" + direction);

        Tank tank = null;
        for (Tank t : gameMap.getAllTanks())
            if (t != null && t.getId() == id)
                tank = t;
        if (tank == null)
            return false;

        tank.setLocation(x, y);
        tank.appendMove(direction);

        return true;
    }
}
