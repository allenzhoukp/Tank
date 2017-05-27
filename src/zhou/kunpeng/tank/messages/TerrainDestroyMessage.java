package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.comm.Message;

import java.nio.ByteBuffer;

/**
 * Created by JA on 2017/5/22.
 */
public class TerrainDestroyMessage implements Message {

    private int battleX;
    private int battleY;

    public static final short TYPE = 10;

    public TerrainDestroyMessage(int battleX, int battleY) {
        this.battleX = battleX;
        this.battleY = battleY;
    }

    @Override
    public byte[] getMessage() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.putShort(0, TYPE);
        buffer.putInt(2, battleX);
        buffer.putInt(6, battleY);
        return buffer.array();
    }
}
