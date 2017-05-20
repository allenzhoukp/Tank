package zhou.kunpeng.tank;

import zhou.kunpeng.tank.tank.Tank;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
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
public class Cannon extends JPanel {

    private static final int BLAST_TIME = 2;

    private Tank launcher;
    private int speed;
    private int direction;
    private final GameMap gameMap;

    private ImageComponent cannonBall;
    private ImageComponent blast;

    private class CannonFlyThread extends Thread {

        private void blast() {
            // Looks like something weird happens:
            // the white bg keeps showing, even if i checked it is opaque.
            // I choose to give up the blast animation for now: cannon simply disappear.
//            blast = new ImageComponent("/images/blast.png", 0, 0);
//            setBounds(getX() + cannonBall.getWidth() / 2 - blast.getWidth() / 2,
//                    getY() + cannonBall.getHeight() / 2 - blast.getHeight() / 2,
//                    blast.getWidth(), blast.getHeight());
//            remove(cannonBall);
//            add(blast);
//            repaint();
//
//            try {
//                sleep(1000 / GameMap.FPS * BLAST_TIME);
//            } catch (InterruptedException e) {
//                // Anything necessary?
//            }
//
//            remove(blast);

            disappear();
        }

        private void disappear() {
            gameMap.remove(Cannon.this);
            gameMap.repaint();
            launcher.enableFire(true);
        }

        private boolean checkTankHit() {
            List<Tank> tankList;
            synchronized (gameMap.getEnemyTankList()) {
                tankList = new ArrayList<>();
                tankList.add(gameMap.getP1Tank());
                tankList.add(gameMap.getP2Tank());
                tankList.addAll(gameMap.getEnemyTankList());
            }
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

                        // Will block the thread.
                        tank.triggerHit();

                    } else {
                        // Will block the thread.
                        disappear();
                    }

                    return true;
                }
            }
            return false;
        }

        private boolean checkWallHit() {
            int battleX = GameMap.toBattleCoordinate(getX());
            int battleY = GameMap.toBattleCoordinate(getY());
            final int[][] additive = {
                    {0, 0}, {0, 1}, {1, 0}, {1, 1}
            };
            boolean blast = false;
            for (int i = 0; i < 4; i++) {
                int newx = battleX + additive[i][0], newy = battleY + additive[i][1];

                //Border hit
                if (!(newx >= 0 && newx < GameMap.BATTLE_WIDTH &&
                        newy >= 0 && newy < GameMap.BATTLE_HEIGHT))
                    blast = true;
                else {
                    //Hit the brick wall: blast and destroy the walls
                    if (gameMap.getMap()[newy][newx] == GameMap.BRICK) {
                        blast = true;
                        gameMap.getMap()[newy][newx] = GameMap.NORMAL;
                        gameMap.remove(gameMap.getTerrainImage()[newy][newx]);
                        gameMap.getTerrainImage()[newy][newx] = null;

                        //Hit the concrete wall: blast
                    } else if (gameMap.getMap()[newy][newx] == GameMap.CONCRETE) {
                        blast = true;
                    }
                }
            }
            if (blast)
                blast();

            return blast;
        }

        @Override
        public void run() {

            final int[][] dir = {
                    {0, speed}, {speed, 0}, {0, -speed}, {-speed, 0}
            };

            while (true) {
                if (checkWallHit() || checkTankHit()) {
                    return;
                }

                setLocation(getX() + dir[direction][0], getY() + dir[direction][1]);
                try {
                    sleep(GameMap.FPS);
                } catch (InterruptedException e) {
                    // Anything necessary?
                }
            }
        }
    }

    public Cannon(int cannonSpeed, int direction, int x, int y, GameMap gameMap, Tank launcher) {
        this.setLayout(null);
        cannonBall = new ImageComponent("/images/cannon.png", 0, 0);
        this.add(cannonBall);
        this.setBounds(x - cannonBall.getWidth() / 2, y - cannonBall.getHeight() / 2,
                cannonBall.getWidth(), cannonBall.getHeight());

        gameMap.add(this, GameMap.CANNON_LAYER);

        this.speed = cannonSpeed;
        this.direction = direction;
        this.gameMap = gameMap;
        this.launcher = launcher;

        CannonFlyThread flyThread = new CannonFlyThread();
        flyThread.start();
    }

}
