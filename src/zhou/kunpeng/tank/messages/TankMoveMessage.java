package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.comm.Message;

/**
 * Created by JA on 2017/5/22.
 */
public class TankMoveMessage implements Message {

    private int id;
    private int x;
    private int y;
    private int direction;

    static final int MESSAGE_TYPE = 1;

    public TankMoveMessage(int id, int x, int y, int direction) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    @Override
    public String getMessage() {
        return "move: id=" + id + ",x=" + x + ",y=" + y + ",dir=" + direction;
    }

}
