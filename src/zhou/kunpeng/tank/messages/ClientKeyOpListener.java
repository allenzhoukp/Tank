package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.battle.GameMap;
import zhou.kunpeng.tank.comm.ByteUtil;
import zhou.kunpeng.tank.comm.NetListener;
import zhou.kunpeng.tank.battle.PlayerTank;
import zhou.kunpeng.tank.battle.Tank;

import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JA on 2017/5/22.
 */
public class ClientKeyOpListener implements NetListener {

    private final GameMap gameMap;

    public ClientKeyOpListener(GameMap gameMap) {
        this.gameMap = gameMap;
    }


    @Override
    public boolean tryInterpret(byte[] line) {

        if (ByteUtil.getShort(line, 0) != ClientKeyOpMessage.TYPE)
            return false;

        int keyCode = ByteUtil.getInt(line, 2);
        boolean isP1 = line[6] == 1;
        PlayerTank tank = isP1 ? gameMap.getP1Tank() : gameMap.getP2Tank();

        if(tank == null)
            return true;

        switch (keyCode) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                tank.appendMove(Tank.NORTH);
                break;

            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                tank.appendMove(Tank.WEST);
                break;

            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                tank.appendMove(Tank.SOUTH);
                break;

            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                tank.appendMove(Tank.EAST);
                break;

            case KeyEvent.VK_F:
            case KeyEvent.VK_SPACE:
                tank.fire();
                break;

            case KeyEvent.VK_UNDEFINED:
                tank.stopMove();
                break;

            default:
                return false;

        }
        return true;
    }
}
