package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.comm.ByteUtil;
import zhou.kunpeng.tank.comm.Message;

/**
 * Created by JA on 2017/5/23.
 */
public class StartLevelMessage implements Message{

    public static final short TYPE = 5;
    @Override
    public byte[] getMessage() {
        return ByteUtil.getByteArray(TYPE);
    }
}
