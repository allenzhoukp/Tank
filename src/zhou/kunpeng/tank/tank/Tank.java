package zhou.kunpeng.tank.tank;

import zhou.kunpeng.tank.Cannon;
import zhou.kunpeng.tank.Clip;
import zhou.kunpeng.tank.GameMap;
import zhou.kunpeng.tank.ImageComponent;

import java.util.List;

/**
 * Created by JA on 2017/5/19.
 * <p> Tank is a basic unit in the game.
 * It stores all the information about the tank, e.g. speed and fire rapidity.
 * It also controls the tank Clip on the stage. <br>
 * However, controller of the tank (e.g. keyboard op or AI control) is not in this class. <br>
 * Player tank and some enemy tank have their own identity (plus or multiple life).
 * </p>
 */
public abstract class Tank extends Clip {

    private int speed;
    private int cannonSpeed;

    private int direction = Tank.NORTH;
    private int side;

    private GameMap gameMap;

    public static final int SOUTH = 0;
    public static final int WEST = 3;
    public static final int NORTH = 2;
    public static final int EAST = 1;

    /**
     * When created, a tank will be automatically placed on the map, and start animating.
     *
     * @param side         the side of the tank (player or enemy).
     * @param speed        the speed of the tank (pixel per frame).
     * @param cannonSpeed  the speed of the cannon launched (pixel per frame).
     * @param clipSequence tank animation sequence.
     * @param x            initial x position (screen coordinate).
     * @param y            initial x position (screen coordinate).
     * @param gameMap      a reference of GameMap that the tank shall keep.
     */

    protected Tank(int side, int speed, int cannonSpeed, List<ImageComponent> clipSequence, int x, int y, GameMap gameMap) {
        super(clipSequence, x, y);
        this.side = side;
        this.speed = speed;
        this.cannonSpeed = cannonSpeed;
        this.gameMap = gameMap;

        gameMap.add(this, GameMap.TANK_LAYER);
        gameMap.getTimer().registerListener(this);

        moveToX = x;
        moveToY = y;
    }


    private int moveToX;
    private int moveToY;
    private int prevTankX;
    private int prevTankY;

    private boolean keepMoving = false;

    private boolean blocked = false;

    @Override
    public void onTimer() {
        //Frame change for clip
        super.onTimer();

        //movement
        moveProgress();
    }

    //The basic logic is:
    //If we want to move the tank, then we assign a target destination point,
    //and this thread will move the tank there gradually.
    protected void moveProgress() {

        int vecx = moveToX - this.getX();
        int vecy = moveToY - this.getY();
        if (vecx != 0 || vecy != 0) {
            int movx = (int) ((double) vecx * speed / Math.sqrt(vecx * vecx + vecy * vecy));
            int movy = (int) ((double) vecy * speed / Math.sqrt(vecx * vecx + vecy * vecy));
            int newx = this.getX() + movx;
            int newy = this.getY() + movy;

            //If tank is moving too far ahead, drag it back.
            if (!keepMoving) {
                if ((newx - moveToX) * movx >= 0)
                    newx = moveToX;
                if ((newy - moveToY) * movy >= 0)
                    newy = moveToY;
            }

            this.setLocation(newx, newy);

            //Reach the destination
        } else if (prevTankX != -1 && prevTankY != -1) {
            gameMap.removeTankBlock(prevTankX, prevTankY);
            gameMap.addTankBlock(GameMap.toBattleCoordinate(moveToX),
                    GameMap.toBattleCoordinate(moveToY));
            prevTankX = prevTankY = -1;

            //If the tank is still moving, move up to the next position.
            if (keepMoving)
                move(direction);
        }
    }

    private void changeDirection(int direction) {
        direction %= 4;
        for (ImageComponent image : this.getSequence())
            image.rotate(Math.PI * ((this.direction - direction + 4) % 4) / 2);
        this.direction = direction;
    }

    // Move the tank for once.
    protected void move(int direction) {
        changeDirection(direction);

        final int[][] DIR = {
                {0, 1}, {1, 0}, {0, -1}, {-1, 0}
        };

        // Temporary remove current tank block: one cannot be blocked by itself.
        int x = GameMap.toBattleCoordinate(this.getX());
        int y = GameMap.toBattleCoordinate(this.getY());
        gameMap.removeTankBlock(x, y);

        // block check
        if (!gameMap.tankBlocked(x + DIR[direction][0], y + DIR[direction][1])) {

            // set blocked flag
            blocked = false;

            // add marker @ current and target x & y
            prevTankX = x;
            prevTankY = y;
            moveToX = GameMap.toScreenCoordinate(x + DIR[direction][0]);
            moveToY = GameMap.toScreenCoordinate(y + DIR[direction][1]);

            // block destination
            gameMap.addTankBlock(x + DIR[direction][0], y + DIR[direction][1]);

        } else {
            blocked = true;
        }

        //Recover the block status
        gameMap.addTankBlock(x, y);
    }

    public void startMove(int direction) {
        keepMoving = true;
        move(direction);
    }

    public void stopMove() {
        keepMoving = false;
    }


    private boolean enableFire = true;

    public void enableFire(boolean enabled) {
        enableFire = enabled;
    }

    protected void tankDestroy() {
        this.stopMove();

        gameMap.remove(this);
        gameMap.removeTankBlock(prevTankX, prevTankY);
        gameMap.removeTankBlock(moveToX, moveToY);
        gameMap.removeTankBlock(GameMap.toBattleCoordinate(getX()),
                GameMap.toBattleCoordinate(getY()));

        gameMap.getTimer().removeListener(this);

        gameMap.repaint();
    }

    public abstract void triggerHit();

    public void fire() {
        if (!enableFire)
            return;
        enableFire = false;
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
        gameMap.getTimer().registerListener(cannon);
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getCannonSpeed() {
        return cannonSpeed;
    }

    public void setCannonSpeed(int cannonSpeed) {
        this.cannonSpeed = cannonSpeed;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public int getSide() {
        return side;
    }

}
