package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.comm.Message;

/**
 * Created by JA on 2017/5/22.
 */
public class TankHitMessage implements Message {

    private int id;
    private int attacker;

    public TankHitMessage(int tankId, int attackerId) {
        this.id = tankId;
        this.attacker = attackerId;
    }

    @Override
    public String getMessage() {
        return "tankhit: id=" + id + ",atker=" + attacker;
    }
}
