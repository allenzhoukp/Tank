package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.comm.Message;

/**
 * Created by JA on 2017/5/22.
 */
public class BaseHitMessage implements Message {
    @Override
    public String getMessage() {
        return "basehit";
    }
}
