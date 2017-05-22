package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.comm.Message;

/**
 * Created by JA on 2017/5/22.
 */
public class TankFireMessage implements Message {

    static final int MESSAGE_TYPE = 2;
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
    public String getMessage() {
        return "fire: id=" + id + ",x=" + x + ",y=" + y + ",dir=" + dir;
    }
}
