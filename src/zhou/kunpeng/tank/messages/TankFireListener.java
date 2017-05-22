package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.GameMap;
import zhou.kunpeng.tank.comm.ByteUtil;
import zhou.kunpeng.tank.comm.NetListener;
import zhou.kunpeng.tank.tanks.EnemyTank;
import zhou.kunpeng.tank.tanks.Tank;

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
    public boolean tryInterpret(String line) {

        Matcher matcher = Pattern.compile("fire: id=(-*\\d+),x=(\\d+),y=(\\d+),dir=(\\d+)\\s*").matcher(line);
        if(!matcher.matches())
            return false;
        int id = Integer.valueOf(matcher.group(1));
        int x = Integer.valueOf(matcher.group(2));
        int y = Integer.valueOf(matcher.group(3));
        int dir = Integer.valueOf(matcher.group(4));

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
