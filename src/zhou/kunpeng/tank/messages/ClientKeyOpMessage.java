package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.comm.Message;

import java.nio.ByteBuffer;

/**
 * Created by JA on 2017/5/22.
 */
public class ClientKeyOpMessage implements Message {

    public static final short TYPE = 2;

    private int key;
    private boolean isP1;

    public ClientKeyOpMessage(int key, boolean isP1) {
        this.key = key;
        this.isP1 = isP1;
    }

    @Override
    public byte[] getMessage() {
        ByteBuffer buffer = ByteBuffer.allocate(7);
        buffer.putShort(0, TYPE);
        buffer.putInt(2, key);
        buffer.put(6, (byte) (isP1 ? 1 : 0));
        return buffer.array();
    }
}
