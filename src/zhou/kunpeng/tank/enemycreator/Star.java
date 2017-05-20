package zhou.kunpeng.tank.enemycreator;

import zhou.kunpeng.tank.Clip;
import zhou.kunpeng.tank.GameMap;
import zhou.kunpeng.tank.ImageComponent;
import zhou.kunpeng.tank.SyncLock;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JA on 2017/5/20.
 * <p>
 * A shining star before a new enemy tank creates.
 * Will call EnemyCreator.createNewTank() when the sequence is played to the end. <br>
 * This is an package-internal class: only friendly to EnemyCreator.
 * </p>
 *
 */
class Star extends Clip {
    private int battleX;
    private int battleY;
    private GameMap parent;
    private EnemyCreator creator;
    public Star(int battleX, int battleY, GameMap parent, EnemyCreator creator){
        super(new ArrayList<>(), GameMap.toScreenCoordinate(battleX) + 9,
                GameMap.toScreenCoordinate(battleY) + 9);

        this.battleX = battleX;
        this.battleY = battleY;
        this.parent = parent;
        this.creator = creator;

        List<ImageComponent> seq = new ArrayList<>();
        for(int i = 1; i <= 39; i++)
            seq.add(new ImageComponent("/images/star/star" + i + ".png"));
        this.setSequence(seq);
    }

    @Override
    public void nextFrame() {
        if(getFrame() == getSequence().size() - 1) {
            new Thread() {
                @Override
                public void run() {
                    //Two locks to ensure there is no strange things happen.
                    //Indeed, i don't quite care different behavior when searching for tanks, or iterating animations.
                    synchronized (parent.getEnemyTankList()) {
                        synchronized (parent.getClipManager().getClipList()) {
                            creator.createNewTank(getX() - 9, 0);
                            parent.remove(Star.this);
                            parent.getClipManager().getClipList().remove(Star.this);
                        }
                    }
                }
            }.start();
            return;
        }
        super.nextFrame();
    }
}
