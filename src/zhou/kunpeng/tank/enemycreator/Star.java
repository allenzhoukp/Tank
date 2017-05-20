package zhou.kunpeng.tank.enemycreator;

import zhou.kunpeng.tank.Clip;
import zhou.kunpeng.tank.GameMap;
import zhou.kunpeng.tank.ImageComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JA on 2017/5/20.
 * <p>
 * A shining star before a new enemy tank creates.
 * Because the sequence is quite long, frames are pre-loaded and reused.
 * </p><p>
 * Note the call to create enemy has been removed
 * because it is easier to just calculate the end animation time and do following stuff.
 * </p>
 *
 */
class Star extends Clip {

    private static List<ImageComponent> preloadSequence;

    static{
        preloadSequence = new ArrayList<>();
        for(int i = 1; i <= 39; i++)
            preloadSequence.add(new ImageComponent("/images/star/star" + i + ".png"));
    }

    private static List<ImageComponent> clonePreload() {
        List<ImageComponent> seq = new ArrayList<>();
        for(int i = 0; i < preloadSequence.size() ; i++)
            seq.add(seq.get(i).clone());
        return seq;
    }

    Star(int battleX, int battleY, GameMap gameMap, EnemyCreator creator){
        super(clonePreload(), GameMap.toScreenCoordinate(battleX) + 9,
                GameMap.toScreenCoordinate(battleY) + 9);
    }

}
