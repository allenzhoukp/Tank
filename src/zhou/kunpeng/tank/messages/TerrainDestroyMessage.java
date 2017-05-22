package zhou.kunpeng.tank.messages;

import zhou.kunpeng.tank.comm.Message;

/**
 * Created by JA on 2017/5/22.
 */
public class TerrainDestroyMessage implements Message {

    private int battleX;
    private int battleY;

    public TerrainDestroyMessage(int battleX, int battleY) {
        this.battleX = battleX;
        this.battleY = battleY;
    }

    @Override
    public String getMessage() {
        return "terrainhit: x=" + battleX + ",y=" + battleY;
    }
}
