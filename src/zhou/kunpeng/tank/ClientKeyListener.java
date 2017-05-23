package zhou.kunpeng.tank;

import zhou.kunpeng.tank.messages.ClientOpMessage;

import java.awt.event.KeyEvent;

/**
 * Created by JA on 2017/5/22.
 * <p>
 * Used for client. It will NOT cause GameMap change immediately.
 * Instead, it sends message through Comm, and wait for server
 * to send info back, causing GameMap change.
 * </p>
 * <p>
 * Note: I don't like this way of extend-relationship, simply for reusing code. JA.
 * </p>
 */
public class ClientKeyListener extends PlayerKeyListener {
    public ClientKeyListener(GameMap gameMap, boolean isP1) {
        super(gameMap, isP1);
    }

    @Override
    protected void appendMove(int keycode) {
        if (gameMap.isOnline() && !gameMap.isNotClient())
            synchronized (gameMap.getNetComm()) {
                gameMap.getNetComm().send(new ClientOpMessage(keycode, isP1));
            }
    }

    @Override
    protected void fire() {
        if (gameMap.isOnline() && !gameMap.isNotClient())
            synchronized (gameMap.getNetComm()) {
                gameMap.getNetComm().send(new ClientOpMessage(KeyEvent.VK_F, isP1));
            }
    }

    // use VK_UNDEFINED as release.
    @Override
    protected void stopMove() {
        if (gameMap.isOnline() && !gameMap.isNotClient())
            synchronized (gameMap.getNetComm()) {
                gameMap.getNetComm().send(new ClientOpMessage(KeyEvent.VK_UNDEFINED, isP1));
            }
    }
}
