package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.comm.ByteUtil;
import zhou.kunpeng.tank.comm.Message;

/**
 * Created by JA on 2017/5/22.
 */
public class EnemyGenerateMessage implements Message {

    static final int MESSAGE_TYPE = 3;

    private int battleX;
    private int battleY;
    private double dice;

    public EnemyGenerateMessage(int battleX, int battleY, double dice) {
        this.battleX = battleX;
        this.battleY = battleY;
        this.dice = dice;
    }

    @Override
    public String getMessage() {
        return "enemy gen: x=" + battleX + ",y="  + battleY + ",dice=" + dice;
    }
}
