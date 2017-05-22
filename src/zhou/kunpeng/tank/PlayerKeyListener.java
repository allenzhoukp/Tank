package zhou.kunpeng.tank;

import zhou.kunpeng.tank.tanks.PlayerTank;
import zhou.kunpeng.tank.tanks.Tank;
import zhou.kunpeng.tank.timer.TimerListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Created by JA on 2017/5/21.
 * <p>
 * The KeyListener that listens to player's keyboard operation.
 * Player can control the tank to move(W/A/S/D), and to fire(J).
 * </p>
 */
public class PlayerKeyListener implements KeyListener {

    private final GameMap gameMap;
    private boolean isP1;

    private Set<Integer> pressedSet = new HashSet<>();
    private Stack<Integer> moveStack = new Stack<>();

    public PlayerKeyListener(GameMap gameMap, boolean isP1) {
        this.gameMap = gameMap;
        this.isP1 = isP1;
    }

    // I change it to Timeline logic to avoid synchronization problem.
    private void appendMove() {
        int keycode = moveStack.peek();

        gameMap.getTimer().registerListener(new TimerListener() {

            @Override
            public void onTimer() {
                PlayerTank playerTank = isP1 ? gameMap.getP1Tank() : gameMap.getP2Tank();
                if (playerTank == null)
                    return;

                switch (keycode) {
                    case KeyEvent.VK_W:
                        playerTank.appendMove(Tank.NORTH);
                        break;
                    case KeyEvent.VK_A:
                        playerTank.appendMove(Tank.WEST);
                        break;
                    case KeyEvent.VK_S:
                        playerTank.appendMove(Tank.SOUTH);
                        break;
                    case KeyEvent.VK_D:
                        playerTank.appendMove(Tank.EAST);
                        break;
                }
                gameMap.getTimer().removeListener(this);
            }
        });
    }

    private void fire() {
        gameMap.getTimer().registerListener(new TimerListener() {

            @Override
            public void onTimer() {
                PlayerTank playerTank = isP1 ? gameMap.getP1Tank() : gameMap.getP2Tank();
                if (playerTank != null)
                    playerTank.fire();

                gameMap.getTimer().removeListener(this);
            }
        });
    }

    private void stopMove() {
        gameMap.getTimer().registerListener(new TimerListener() {

            @Override
            public void onTimer() {
                PlayerTank playerTank = isP1 ? gameMap.getP1Tank() : gameMap.getP2Tank();
                if (playerTank != null)
                    playerTank.stopMove();

                gameMap.getTimer().removeListener(this);
            }
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //do nothing
    }

    // The set logic is to avoid constant press. For ANY press this make sense!

    // The stack logic is to get most recently pressed (and not released) W/A/S/D key.
    // e.g. Press A - A in the stack - move West
    //      Press W - W in the top - move North
    //      Release W - W pop - A in the top - move West
    // Other presses are not affected.
    @Override
    public void keyPressed(KeyEvent e) {

        if (pressedSet.contains(e.getKeyCode()))
            return;

        pressedSet.add(e.getKeyCode());

        switch (e.getKeyCode()) {

            case KeyEvent.VK_J:
                fire();
                break;

            case KeyEvent.VK_W:
            case KeyEvent.VK_A:
            case KeyEvent.VK_S:
            case KeyEvent.VK_D:
                moveStack.push(e.getKeyCode());
                appendMove();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        pressedSet.remove(e.getKeyCode());

        switch (e.getKeyCode()) {

            case KeyEvent.VK_W:
            case KeyEvent.VK_A:
            case KeyEvent.VK_S:
            case KeyEvent.VK_D:

                // e.g. Press A - Press W - Release A, then A should be no longer in the stack.
                // this time only W will be in the stack.
                // So when W released (pop()), the stack will be empty, and tank should stop.
                int idx;
                if ((idx = moveStack.search(e.getKeyCode())) != -1)
                    moveStack.remove(moveStack.size() - idx);

                if (moveStack.empty())
                    stopMove();
                else
                    appendMove();
        }
    }


}

