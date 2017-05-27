package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.comm.Message;

import java.nio.ByteBuffer;

/**
 * Created by JA on 2017/5/22.
 */
public class TankFireMessage implements Message {

    public static final short TYPE = 6;
    private int id;
    private int x;
    private int y;
    private int dir;

    public TankFireMessage(int id, int x, int y, int dir) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    @Override
    public byte[] getMessage() {
        ByteBuffer buffer = ByteBuffer.allocate(18);
        buffer.putShort(0, TYPE);
        buffer.putInt(2, id);
        buffer.putInt(6, x);
        buffer.putInt(10, y);
        buffer.putInt(14, dir);
        return buffer.array();
    }
}
