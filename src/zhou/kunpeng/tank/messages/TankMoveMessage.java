package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.comm.Message;

/**
 * Created by JA on 2017/5/22.
 */
public class TankMoveMessage implements Message {

    private int id;
    private int direction;

    static final int MESSAGE_TYPE = 1;

    public TankMoveMessage(int id, int direction) {
        this.id = id;
        this.direction = direction;
    }

    @Override
    public String getMessage() {
        return "move: id=" + id + ",dir=" + direction;
    }

}
