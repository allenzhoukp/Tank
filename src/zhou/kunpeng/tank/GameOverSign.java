package zhou.kunpeng.tank;

import zhou.kunpeng.tank.display.ImageComponent;
import zhou.kunpeng.tank.time.TimerListener;

/**
 * Created by JA on 2017/5/22.
 */
public class GameOverSign extends ImageComponent implements TimerListener {
    private final int moveX;
    private final int moveY;
    private final GameMap gameMap;
    private int counter;

    public GameOverSign(int initX, int initY, int moveToX, int moveToY, int frameCount, GameMap gameMap) {
        super("/images/gameover.png", initX, initY);
        this.moveX = (moveToX - initX) / frameCount;
        this.moveY = (moveToY - initY) / frameCount;
        counter = frameCount;
        this.gameMap = gameMap;
        gameMap.add(this, GameMap.PLUS_LAYER);
        gameMap.getTimer().registerListener(this);
    }

    @Override
    public void onTimer() {
        counter--;
        this.setLocation(this.getX() + moveX, this.getY() + moveY);
        if(counter == 0)
            gameMap.getTimer().removeListener(this);
    }
}
