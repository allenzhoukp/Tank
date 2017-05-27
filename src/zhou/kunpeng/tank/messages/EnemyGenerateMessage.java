package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.comm.Message;

import java.nio.ByteBuffer;

/**
 * Created by JA on 2017/5/22.
 */
public class EnemyGenerateMessage implements Message {

    public static final short TYPE = 3;

    private int battleX;
    private int battleY;
    private double dice;

    public EnemyGenerateMessage(int battleX, int battleY, double dice) {
        this.battleX = battleX;
        this.battleY = battleY;
        this.dice = dice;
    }

    @Override
    public byte[] getMessage() {
        ByteBuffer buffer = ByteBuffer.allocate(14);
        buffer.putShort(0, TYPE);
        buffer.putInt(2, battleX);
        buffer.putInt(6, battleY);
        buffer.putFloat(10, (float) dice);
        return buffer.array();
    }
}
