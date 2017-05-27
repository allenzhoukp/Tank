package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.battle.GameMap;
import zhou.kunpeng.tank.battle.ai.EnemyCreator;
import zhou.kunpeng.tank.comm.ByteUtil;
import zhou.kunpeng.tank.comm.NetListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JA on 2017/5/22.
 */
public class EnemyGenerateListener implements NetListener {

    private final GameMap gameMap;

    public EnemyGenerateListener(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    @Override
    public boolean tryInterpret(byte[] line) {

        if(ByteUtil.getShort(line, 0) != EnemyGenerateMessage.TYPE)
            return false;

        int battleX = ByteUtil.getInt(line, 2);
        int battleY = ByteUtil.getInt(line, 6);
        double dice = ByteUtil.getFloat(line, 10);

        new EnemyCreator().createStar(gameMap, battleX, battleY, dice);

        return true;
    }
}
