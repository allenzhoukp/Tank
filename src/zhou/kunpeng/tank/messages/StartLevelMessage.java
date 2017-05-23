package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.comm.Message;

/**
 * Created by JA on 2017/5/23.
 */
public class StartLevelMessage implements Message{

    @Override
    public String getMessage() {
        return "levelstart";
    }
}
