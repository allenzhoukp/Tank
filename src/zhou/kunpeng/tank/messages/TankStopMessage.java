package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.comm.Message;

/**
 * Created by JA on 2017/5/22.
 */
public class TankStopMessage implements Message {

    private int id;
    private int x;
    private int y;
    private int dir;

    static final int MESSAGE_TYPE = 4;

    public TankStopMessage(int id, int x, int y, int dir) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    @Override
    public String getMessage() {
        return "stop: id=" + id + ",x=" + x + ",y=" + y + ",dir=" + dir;
    }

}
