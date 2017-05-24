package zhou.kunpeng.tank.battle;

import zhou.kunpeng.tank.messages.PauseAndContinueMessage;
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

    protected final GameMap gameMap;
    protected boolean isP1;

    private Set<Integer> pressedSet = new HashSet<>();
    private Stack<Integer> moveStack = new Stack<>();

    public PlayerKeyListener(GameMap gameMap, boolean isP1) {
        this.gameMap = gameMap;
        this.isP1 = isP1;
    }

    // I change it to Timeline logic to avoid synchronization problem.
    protected void appendMove(int keycode) {

        gameMap.getTimer().registerListener(new TimerListener() {

            @Override
            public void onTimer() {
                PlayerTank playerTank = isP1 ? gameMap.getP1Tank() : gameMap.getP2Tank();
                if (playerTank == null)
                    return;

                switch (keycode) {
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        playerTank.appendMove(Tank.NORTH);
                        break;
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        playerTank.appendMove(Tank.WEST);
                        break;
                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
                        playerTank.appendMove(Tank.SOUTH);
                        break;
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        playerTank.appendMove(Tank.EAST);
                        break;
                }
                gameMap.getTimer().removeListener(this);
            }
        });
    }

    protected void fire() {
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

    protected void stopMove() {
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

    protected void pauseOrContinue(){
        gameMap.pauseOrContinue();

        //Net Comm
        if(gameMap.isOnline())
            gameMap.getNetComm().send(new PauseAndContinueMessage());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //do nothing
    }

    // The set logic is to avoid constant press.

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

            case KeyEvent.VK_F:
            case KeyEvent.VK_SPACE:
                fire();
                break;

            case KeyEvent.VK_P:
            case KeyEvent.VK_ESCAPE:
                pauseOrContinue();
                break;

            case KeyEvent.VK_W:
            case KeyEvent.VK_A:
            case KeyEvent.VK_S:
            case KeyEvent.VK_D:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_RIGHT:
                moveStack.push(e.getKeyCode());
                appendMove(moveStack.peek());
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
            case KeyEvent.VK_UP:
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_RIGHT:

                // e.g. Press A - Press W - Release A, then A should be no longer in the stack.
                // this time only W will be in the stack.
                // So when W released (pop()), the stack will be empty, and tank should stop.
                int idx;
                if ((idx = moveStack.search(e.getKeyCode())) != -1)
                    moveStack.remove(moveStack.size() - idx);

                if (moveStack.empty())
                    stopMove();
                else
                    appendMove(moveStack.peek());
        }
    }


}

