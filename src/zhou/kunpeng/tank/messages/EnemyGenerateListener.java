package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.battle.GameMap;
import zhou.kunpeng.tank.battle.ai.EnemyCreator;
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
    public boolean tryInterpret(String line) {

        Matcher matcher = Pattern.compile("enemy gen:\\s*x\\s*=\\s*(\\d+),\\s*y\\s*=\\s*(\\d+),\\s*dice\\s*=\\s*(\\d*\\.\\d*)\\s*")
                .matcher(line.toLowerCase());

        if (!matcher.lookingAt())
            return false;

        int battleX = Integer.valueOf(matcher.group(1));
        int battleY = Integer.valueOf(matcher.group(2));
        double dice = Double.valueOf(matcher.group(3));

        new EnemyCreator().createStar(gameMap, battleX, battleY, dice);

        return true;
    }
}
