package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.comm.Message;

import java.nio.ByteBuffer;

/**
 * Created by JA on 2017/5/22.
 */
public class TankMoveMessage implements Message {

    private int id;
    private int x;
    private int y;
    private int direction;

    public static final short TYPE = 8;

    public TankMoveMessage(int id, int x, int y, int direction) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    @Override
    public byte[] getMessage() {
        ByteBuffer buffer = ByteBuffer.allocate(18);
        buffer.putShort(0, TYPE);
        buffer.putInt(2, id);
        buffer.putInt(6, x);
        buffer.putInt(10, y);
        buffer.putInt(14, direction);
        return buffer.array();
    }

}
