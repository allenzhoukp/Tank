package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.battle.GameMap;
import zhou.kunpeng.tank.comm.NetListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JA on 2017/5/22.
 */
public class TerrainDestroyListener implements NetListener{

    private final GameMap gameMap;

    public TerrainDestroyListener(GameMap gameMap){
        this.gameMap = gameMap;
    }

    @Override
    public boolean tryInterpret(String line) {
        Matcher matcher = Pattern.compile("terrainhit: x=(\\d+),y=(\\d+)\\s*").matcher(line);
        if(!matcher.lookingAt())
            return false;

        int battleX = Integer.valueOf(matcher.group(1));
        int battleY = Integer.valueOf(matcher.group(2));

        gameMap.destroyTerrain(battleX, battleY);

        return true;
    }
}
