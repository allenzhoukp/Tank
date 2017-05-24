package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.comm.Message;

/**
 * Created by JA on 2017/5/22.
 */
public class ClientKeyOpMessage implements Message {
    private int key;
    private boolean isP1;

    public ClientKeyOpMessage(int key, boolean isP1) {
        this.key = key;
        this.isP1 = isP1;
    }


    @Override
    public String getMessage() {
        return "key: k=" + key + ",p=" + (isP1 ? 1 : 0);
    }
}
