package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.comm.Message;

/**
 * Created by JA on 2017/5/24.
 */
public class PauseAndContinueMessage implements Message{

    @Override
    public String getMessage() {
        return "pause&cont";
    }
}
