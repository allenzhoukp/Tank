package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.comm.ByteUtil;
import zhou.kunpeng.tank.comm.Message;

/**
 * Created by JA on 2017/5/22.
 */
public class TankFireMessage implements Message {

    static final int MESSAGE_TYPE = 2;
    private int id;

    public TankFireMessage(int id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "fire: id=" + id;
    }
}
