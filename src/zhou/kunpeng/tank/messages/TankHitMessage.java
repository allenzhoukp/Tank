package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.comm.Message;

import java.nio.ByteBuffer;

/**
 * Created by JA on 2017/5/22.
 */
public class TankHitMessage implements Message {

    public static final short TYPE = 7;

    private int id;
    private int attacker;

    public TankHitMessage(int tankId, int attackerId) {
        this.id = tankId;
        this.attacker = attackerId;
    }

    @Override
    public byte[] getMessage() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.putShort(0, TYPE);
        buffer.putInt(2, id);
        buffer.putInt(6, attacker);
        return buffer.array();
    }
}
