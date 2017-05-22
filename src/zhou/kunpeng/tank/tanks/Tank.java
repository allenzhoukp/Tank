package zhou.kunpeng.tank.tanks;

import zhou.kunpeng.tank.Cannon;
import zhou.kunpeng.tank.GameMap;
import zhou.kunpeng.tank.MapUtils;
import zhou.kunpeng.tank.display.Clip;
import zhou.kunpeng.tank.display.ImageComponent;
import zhou.kunpeng.tank.messages.TankFireMessage;
import zhou.kunpeng.tank.messages.TankMoveMessage;
import zhou.kunpeng.tank.messages.TankStopMessage;

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

    public static final int SOUTH = 2;
    public static final int WEST = 1;
    public static final int NORTH = 0;
    public static final int EAST = 3;

    private int id;

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

    protected Tank(int side, int speed, int cannonSpeed, List<ImageComponent> clipSequence, int x, int y, GameMap gameMap, int id) {
        super(clipSequence, x, y);
        this.side = side;
        this.speed = speed;
        this.cannonSpeed = cannonSpeed;
        this.gameMap = gameMap;
        this.id = id;

        gameMap.add(this, GameMap.TANK_LAYER);
        gameMap.getTimer().registerListener(this);

    }

    private boolean moving = false;
    private int nextMove = -1;

    private boolean blocked = false;

    @Override
    public void onTimer() {
        //Frame change for clip
        super.onTimer();

        //movement
        moveProgress();
    }

    protected void moveProgress() {
        final int[][] DIR = {
                {0, -speed}, {-speed, 0}, {0, speed}, {speed, 0}
        };

        boolean aligned = MapUtils.isCoordinateAligned(getX()) && MapUtils.isCoordinateAligned(getY());

        //Stop moving. Check the next movement.
        if (!moving && aligned) {
            if (nextMove != -1) {
                changeDirection(nextMove);
                moving = true;
                nextMove = -1;
            }

        } else {

            // Moveup.
            int newX = getX() + DIR[direction][0];
            int newY = getY() + DIR[direction][1];

            // If trying to stop moving and after the move, the battle coordinate has changed;
            // or, the tank is blocked;
            // then the tank should stop at the aligned position, rather than the accurate one.

            boolean tooFarAhead = !moving &&
                    (MapUtils.toBattleCoordinate(newX) != MapUtils.toBattleCoordinate(getX()) ||
                            MapUtils.toBattleCoordinate(newY) != MapUtils.toBattleCoordinate(getY()));

            this.blocked = gameMap.tankBlocked(newX, newY, this);

            // This is a little bit complicated..
            if (tooFarAhead || this.blocked) {
                switch (direction) {
                    case WEST:
                        newX = MapUtils.alignScreenCoordinate(getX());
                        break;
                    case NORTH:
                        newY = MapUtils.alignScreenCoordinate(getY());
                        break;
                    case EAST:
                        newX = MapUtils.alignScreenCoordinate(newX);
                        break;
                    case SOUTH:
                        newY = MapUtils.alignScreenCoordinate(newY);
                        break;
                }
            }

            setLocation(newX, newY);
        }
    }

    private void changeDirection(int direction) {
        direction %= 4;
        for (ImageComponent image : this.getSequence())
            image.rotate(Math.PI * ((this.direction - direction + 4) % 4) / 2);
        this.direction = direction;
    }

    public void startMove(int direction) {

        // Net Communication
        if(gameMap.isServer() && gameMap.isOnline())
            gameMap.getNetComm().send(new TankMoveMessage(getId(), direction));

        if (moving)
            return;
        changeDirection(direction);
        moving = true;
    }

    public void appendMove(int direction) {

        // Net Communication
        if(gameMap.isServer() && gameMap.isOnline())
            gameMap.getNetComm().send(new TankMoveMessage(getId(), direction));

        moving = false; //force stop
        nextMove = direction;
    }

    public void stopMove() {

        // Net Communication
        if(gameMap.isServer() && gameMap.isOnline())
            gameMap.getNetComm().send(new TankStopMessage(getId()));

        nextMove = -1;
        moving = false;
    }

    private boolean enableFire = true;

    public void enableFire(boolean enabled) {
        enableFire = enabled;
    }

    protected void tankDestroy() {
        this.stopMove();

        gameMap.remove(this);
        gameMap.getTimer().removeListener(this);

        gameMap.repaint();
    }

    public abstract void triggerHit(Tank attacker);

    public void fire() {

        // Net Communication
        if(gameMap.isServer() && gameMap.isOnline())
            gameMap.getNetComm().send(new TankFireMessage(getId()));

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

    public int getDirection() {
        return direction;
    }

    public int getId() {
        return id;
    }
}
