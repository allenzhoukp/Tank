package zhou.kunpeng.tank;

import java.util.List;

/**
 * Created by JA on 2017/5/19.
 * <p> Tank is a basic unit in the game.
 * It stores all the information about the zhou.kunpeng.tank, e.g. speed and fire rapidity.
 * It also controls the zhou.kunpeng.tank Clip on the stage. <br>
 * However, controller of the zhou.kunpeng.tank (e.g. keyboard op or AI control) is not in this class. <br>
 * Player zhou.kunpeng.tank and some enemy zhou.kunpeng.tank have their own identity (plus or multiple life).
 * </p>
 */
public abstract class Tank extends Clip {

    protected int speed;
    protected int cannonSpeed;

    private int direction = Tank.NORTH;
    private int side;

    public int getSide() {
        return side;
    }


    protected GameMap gameMap;

    public static final int SOUTH = 0;
    public static final int WEST = 3;
    public static final int NORTH = 2;
    public static final int EAST = 1;

    protected TankMoveThread moveThread;

    private int moveToX;
    private int moveToY;
    private int prevTankBlockX;
    private int prevTankBlockY;

    protected Tank(int side, int speed, int cannonSpeed, List<ImageComponent> clipSequence, int x, int y, GameMap gameMap) {
        super(clipSequence, x, y);
        this.side = side;
        this.speed = speed;
        this.cannonSpeed = cannonSpeed;
        this.gameMap = gameMap;
        moveThread = new TankMoveThread();
        moveThread.start();
    }

    //The thread to move the zhou.kunpeng.tank.
    private class TankMoveThread extends Thread {
        private boolean running = true;

        //The basic logic is:
        //If we want to move the zhou.kunpeng.tank, then we assign a target destination point,
        //and this thread will move the zhou.kunpeng.tank there gradually.


        @Override
        public void run() {
            //initially the zhou.kunpeng.tank shouldn't move
            moveToX = getX();
            moveToY = getY();
            prevTankBlockX = prevTankBlockY = -1;

            while (running) {
                int vecx = moveToX - getX();
                int vecy = moveToY - getY();
                if (vecx != 0 || vecy != 0) {
                    int movx = (int) ((double) vecx * speed / Math.sqrt(vecx * vecx + vecy * vecy));
                    int movy = (int) ((double) vecy * speed / Math.sqrt(vecx * vecx + vecy * vecy));
                    int newx = getX() + movx;
                    int newy = getY() + movy;
                    //If zhou.kunpeng.tank is moving too far ahead, drag it back.
                    if ((newx - moveToX) * movx >= 0)
                        newx = moveToX;
                    if ((newy - moveToY) * movy >= 0)
                        newy = moveToY;
                    setLocation(newx, newy);

                } else if (prevTankBlockX != -1 && prevTankBlockY != -1) {
                    gameMap.removeTankBlock(prevTankBlockX, prevTankBlockY);
                    gameMap.addTankBlock(GameMap.toBattleCoordinate(moveToX),
                            GameMap.toBattleCoordinate(moveToY));
                    prevTankBlockX = prevTankBlockY = -1;
                }

                try {
                    sleep(1000 / GameMap.FPS);
                } catch (InterruptedException e) {
                    //Do nothing
                }
            }
        }

        public void stopRunning() {
            running = false;
        }
    }

    private void changeDirection(int direction) {
        direction %= 4;
        for (ImageComponent image : this.getSequence())
            image.rotate(Math.PI * ((this.direction - direction + 4) % 4) / 2);
        this.direction = direction;
    }

    // Move the zhou.kunpeng.tank gradually.
    public void move(int direction) {
        changeDirection(direction);

        final int[][] DIR = {
                {0, 1}, {1, 0}, {0, -1}, {-1, 0}
        };

        int x = GameMap.toBattleCoordinate(this.getX());
        int y = GameMap.toBattleCoordinate(this.getY());
        // Temporary remove current zhou.kunpeng.tank block: one cannot be blocked by itself.
        gameMap.removeTankBlock(x, y);
        if (!gameMap.tankBlocked(x + DIR[direction][0], y + DIR[direction][1])) {
            // add marker @ current x & y
            prevTankBlockX = x;
            prevTankBlockY = y;

            // block destination
            gameMap.addTankBlock(x + DIR[direction][0], y + DIR[direction][1]);

            moveToX = GameMap.toScreenCoordinate(x + DIR[direction][0]);
            moveToY = GameMap.toScreenCoordinate(y + DIR[direction][1]);
        }
        //Recover the block status
        gameMap.addTankBlock(x, y);
    }

    public void move() {
        move(direction);
    }

    protected void tankDestroy() {
        gameMap.remove(this);
        gameMap.removeTankBlock(prevTankBlockX, prevTankBlockY);
        gameMap.removeTankBlock(moveToX, moveToY);
        gameMap.removeTankBlock(GameMap.toBattleCoordinate(getX()),
                GameMap.toBattleCoordinate(getY()));
    }

    public abstract void triggerHit();

    public void fire() {
        int fireX, fireY;
        switch (direction) {
            case SOUTH:
                fireX = this.getX() + this.getWidth() / 2;
                fireY = this.getY() + this.getHeight();
                break;
            case EAST:
                fireX = this.getX() + this.getWidth();
                fireY = this.getY() + this.getHeight() / 2;
                break;
            case NORTH:
                fireX = this.getX() + this.getWidth() / 2;
                fireY = this.getY();
                break;
            case WEST:
                fireX = this.getX();
                fireY = this.getY() + this.getHeight() / 2;
                break;
            default:
                return;
        }
        Cannon cannon = new Cannon(cannonSpeed, direction, fireX, fireY, gameMap, this);
    }

}
