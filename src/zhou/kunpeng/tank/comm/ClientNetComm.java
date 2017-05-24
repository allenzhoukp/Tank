package zhou.kunpeng.tank.comm;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by JA on 2017/5/22.
 */
public class ClientNetComm extends NetComm {

    private String targetAddr;
    private int targetPort;

    public ClientNetComm(String targetAddr, int targetPort){
        this.targetAddr = targetAddr;
        this.targetPort = targetPort;
    }
    @Override
    protected Socket getSocket() {
        try {
            return new Socket(targetAddr, targetPort);
        } catch (IOException e) {
            //IOException
            //Note UnknownHostException will be THROWN!
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void extraClose() {

    }
}
