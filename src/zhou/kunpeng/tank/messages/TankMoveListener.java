package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.GameMap;
import zhou.kunpeng.tank.comm.NetListener;
import zhou.kunpeng.tank.tanks.EnemyTank;
import zhou.kunpeng.tank.tanks.Tank;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JA on 2017/5/22.
 */
public class TankMoveListener implements NetListener {

    private final GameMap gameMap;

    public TankMoveListener(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    @Override
    public boolean tryInterpret(String line) {
        Matcher matcher = Pattern.compile("move:\\s*id=(-*\\d+),\\s*dir=(\\d+)\\s*").matcher(line);
        if (!matcher.matches())
            return false;

        int id = Integer.valueOf(matcher.group(1));
        int direction = Integer.valueOf(matcher.group(2));

        Tank tank = null;
        if (id == -1)
            tank = gameMap.getP1Tank();
        else if (id == -2)
            tank = gameMap.getP2Tank();
        else {
            for (EnemyTank t : gameMap.getEnemyTankList()) {
                if (t.getId() == id) {
                    tank = t;
                    break;
                }
            }
        }
        if (tank == null)
            return false;

        tank.appendMove(direction);

        return true;
    }
}
