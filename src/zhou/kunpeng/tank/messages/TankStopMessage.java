package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.comm.Message;

/**
 * Created by JA on 2017/5/22.
 */
public class TankStopMessage implements Message {

    private int id;

    static final int MESSAGE_TYPE = 4;

    public TankStopMessage(int id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "stop: id=" + id;
    }

}
