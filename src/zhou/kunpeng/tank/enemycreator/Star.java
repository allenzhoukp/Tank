package zhou.kunpeng.tank.enemycreator;

import zhou.kunpeng.tank.display.Clip;
import zhou.kunpeng.tank.GameMap;
import zhou.kunpeng.tank.display.ImageComponent;
import zhou.kunpeng.tank.MapUtils;

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

    static void preload(){
        preloadSequence = new ArrayList<>();
        for(int i = 1; i <= 39; i++)
            preloadSequence.add(new ImageComponent("/images/star/star" + i + ".png"));
    }

    private static List<ImageComponent> clonePreload() {
        List<ImageComponent> seq = new ArrayList<>();
        for(int i = 0; i < preloadSequence.size() ; i++)
            seq.add(preloadSequence.get(i).clone());
        return seq;
    }

    Star(int battleX, int battleY, GameMap gameMap, EnemyCreator creator){
        super(clonePreload(), MapUtils.toScreenCoordinate(battleX) + 9,
                MapUtils.toScreenCoordinate(battleY) + 9);
    }

}
