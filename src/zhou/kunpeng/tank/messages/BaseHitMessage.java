package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.comm.ByteUtil;
import zhou.kunpeng.tank.comm.Message;

/**
 * Created by JA on 2017/5/22.
 */
public class BaseHitMessage implements Message {

    public static final short TYPE = 1;

    @Override
    public byte[] getMessage() {
        return ByteUtil.getByteArray(TYPE);
    }
}
