package zhou.kunpeng.tank.battle;

import zhou.kunpeng.tank.Levels;
import zhou.kunpeng.tank.display.ImageComponent;
import zhou.kunpeng.tank.messages.BaseHitMessage;
import zhou.kunpeng.tank.messages.TankHitMessage;
import zhou.kunpeng.tank.messages.TerrainDestroyMessage;
import zhou.kunpeng.tank.timer.TimerListener;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by JA on 2017/5/19.
 * <p>
 * The cannon ball that the tank launched.
 * Will fly in a certain direction. <br>
 * 1. if hit concrete wall / map border,  it will blast.
 * 2. if hit friendly tank, it will disappear.
 * 3. if hit enemy tank, it will trigger tank's onHit() method.
 * 4. if hit brick wall, will destroy a wall in range of 2 * 2.
 * </p>
 */

/*
   I don't feel good to add isNotClient() directly to Cannon, but looks like this becomes not-quite-avoidable.
*/
public class Cannon extends JPanel implements TimerListener {

    private static ImageComponent preloadCannon;

    private static ImageComponent getCannon() {
        if(preloadCannon == null)
            preloadCannon = new ImageComponent("/images/cannon.png", 0, 0);
        return preloadCannon.clone();
    }

    private static final int BLAST_TIME = 2;

    private Tank launcher;
    private int speed;
    private int direction;
    private final GameMap gameMap;

    private ImageComponent cannonBall;
    private ImageComponent blast;

    private boolean doHitTriggers;


    public Cannon(int cannonSpeed, int direction, int x, int y, GameMap gameMap, Tank launcher) {
        this.setLayout(null);
        cannonBall = getCannon();
        this.add(cannonBall);
        this.setBounds(x - cannonBall.getWidth() / 2, y - cannonBall.getHeight() / 2,
                cannonBall.getWidth(), cannonBall.getHeight());

        gameMap.add(this, GameMap.CANNON_LAYER);

        this.speed = cannonSpeed;
        this.direction = direction;
        this.gameMap = gameMap;
        this.launcher = launcher;

        //If online and client, cannon balls do not create any destruction.
        //Destruction is calculated by server.
        this.doHitTriggers = (!gameMap.isOnline()) || gameMap.isNotClient();
    }

    //explosive animation.
    private void blast() {
        // Looks like something weird happens:
        // the white bg keeps showing, even if i checked it is opaque.
        // I choose to give up the blast animation for now: cannon simply disappear.

        // blast = new ImageComponent("/images/blast.png", 0, 0);
        // setBounds(getX() + cannonBall.getWidth() / 2 - blast.getWidth() / 2,
        //         getY() + cannonBall.getHeight() / 2 - blast.getHeight() / 2,
        //         blast.getWidth(), blast.getHeight());
        // remove(cannonBall);
        // add(blast);
        // repaint();
        //
        // try {
        //     sleep(1000 / GameMap.FPS * BLAST_TIME);
        // } catch (InterruptedException e) {
        //     // Anything necessary?
        // }
        //
        // remove(blast);
    }

    //the cannon no longer exists on the map.
    private void disappear() {
        gameMap.remove(Cannon.this);
        gameMap.repaint();

        gameMap.getTimer().removeListener(this);

        //launcher can fire again only if the previous cannon disappears
        //Add: no longer like this. Fire interval is 1 second.
        //launcher.enableFire(true);
    }

    private boolean checkTankHit() {

        //get all tanks on the map
        List<Tank> tankList = gameMap.getAllTanks();

        for (Tank tank : tankList) {
            if (tank == null)
                continue;

            //Intersect
            if (new Rectangle(getX(), getY(), getWidth(), getHeight()).intersects(
                    new Rectangle(tank.getX(), tank.getY(), tank.getWidth(), tank.getHeight()))) {

                //When launching, cannon will intersect with launcher.
                //Go ahead for flying.
                if (tank == launcher)
                    return false;

                //Hit!
                //Not friendly fire
                if (tank.getSide() != launcher.getSide()) {
                    blast(); //boom!

                    if (doHitTriggers) {

                        tank.triggerHit(launcher);

                        //Net Communication
                        if (gameMap.isOnline() && gameMap.isNotClient())
                            gameMap.getNetComm().send(new TankHitMessage(tank.getId(), launcher.getId()));
                    }

                }
                //No matter it is friendly fire or not, it is a hit, isn't it?
                return true;
            }
        }
        return false;
    }

    private boolean checkWallHit() {
        int battleX = MapUtils.toBattleCoordinate(getX());
        int battleY = MapUtils.toBattleCoordinate(getY());
        final int[][] additive = {
                {0, 0}, {0, 1}, {1, 0}, {1, 1}
        };
        boolean blast = false;
        for (int i = 0; i < 4; i++) {
            int newx = battleX + additive[i][0], newy = battleY + additive[i][1];

            //Border hit
            if (!(newx >= 0 && newx < Levels.BATTLE_WIDTH &&
                    newy >= 0 && newy < Levels.BATTLE_HEIGHT))
                blast = true;
            else {
                //Hit the brick wall: blast and destroy the walls
                if (gameMap.getMap()[newy][newx] == Levels.BRICK) {

                    blast = true;

                    if (doHitTriggers) {
                        gameMap.destroyTerrain(newx, newy);

                        //Net Communication
                        if (gameMap.isOnline() && gameMap.isNotClient())
                            gameMap.getNetComm().send(new TerrainDestroyMessage(newx, newy));
                    }

                    //Hit the concrete wall: blast
                } else if (gameMap.getMap()[newy][newx] == Levels.CONCRETE) {
                    blast = true;
                }
            }
        }
        if (blast)
            blast();

        return blast;
    }

    private boolean checkBaseHit() {

        //Switch strategy: use Screen coordinate to judge intersection

        Rectangle cannonRect = new Rectangle(getX(), getY(), getWidth(), getHeight());
        Base base = gameMap.getBase();
        Rectangle baseRect = new Rectangle(base.getX(), base.getY(), base.getWidth(), base.getHeight());
        if (cannonRect.intersects(baseRect)) {

            if (doHitTriggers) {
                base.triggerHit();

                //Net Communication
                if (gameMap.isOnline() && gameMap.isNotClient())
                    gameMap.getNetComm().send(new BaseHitMessage());
            }

            return true;
        }
        return false;
    }


    @Override
    public void onTimer() {
        final int dir[][] = {
                {0, -speed}, {-speed, 0}, {0, speed}, {speed, 0}
        };
        //move
        this.setLocation(this.getX() + dir[direction][0], this.getY() + dir[direction][1]);

        //check hit
        if (checkTankHit() || checkWallHit() || checkBaseHit())
            disappear();

    }
}
