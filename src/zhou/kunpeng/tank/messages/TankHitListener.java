package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.battle.GameMap;
import zhou.kunpeng.tank.comm.NetListener;
import zhou.kunpeng.tank.battle.Tank;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JA on 2017/5/22.
 */
public class TankHitListener implements NetListener {

    private final GameMap gameMap;

    public TankHitListener(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    @Override
    public boolean tryInterpret(String line) {
        Matcher matcher = Pattern.compile("tankhit: id=(-*\\d+),atker=(-*\\d+)\\s*").matcher(line);
        if (!matcher.lookingAt())
            return false;

        int id = Integer.valueOf(matcher.group(1));
        int attackerId = Integer.valueOf(matcher.group(2));

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
