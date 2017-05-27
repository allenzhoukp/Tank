package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.comm.ByteUtil;
import zhou.kunpeng.tank.comm.Message;

/**
 * Created by JA on 2017/5/24.
 */
public class PauseAndContinueMessage implements Message{

    public static final short TYPE = 4;
    @Override
    public byte[] getMessage() {
        return ByteUtil.getByteArray(TYPE);
    }
}
