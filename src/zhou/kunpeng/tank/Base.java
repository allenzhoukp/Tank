package zhou.kunpeng.tank;

import zhou.kunpeng.tank.display.ImageComponent;
import zhou.kunpeng.tank.messages.BaseHitMessage;

import javax.swing.*;

/**
 * Created by JA on 2017/5/21.
 * <p>
 *     Base on the bottom of the map.
 * </p>
 */
public class Base extends JPanel {
    private ImageComponent base;
    private ImageComponent crashbase;
    private GameMap gameMap;

    public Base(GameMap gameMap, int battleX, int battleY) {
        base = new ImageComponent("/images/base.png");
        crashbase = new ImageComponent("/images/crashbase.png");
        this.gameMap = gameMap;
        this.setLayout(null);
        this.add(base);
        this.setBounds(MapUtils.toScreenCoordinate(battleX),
                MapUtils.toScreenCoordinate(battleY),
                base.getWidth(), base.getHeight());
        gameMap.add(this, GameMap.WALL_LAYER);
    }

    public void triggerHit() {
        this.removeAll();
        this.add(crashbase);

        // Net Communication
        if(gameMap.isNotClient() && gameMap.isOnline())
            gameMap.getNetComm().send(new BaseHitMessage());

        gameMap.gameOver();
    }
}
