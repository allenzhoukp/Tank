package zhou.kunpeng.tank.comm;

import java.net.Socket;

/**
 * Created by JA on 2017/5/22.
 * <p>
 * Will try to connect to the server.
 * If failed, an exception stack trace is printed to console.
 * </p>
 */
public class ClientNetComm extends NetComm {

    private String targetAddr;
    private int targetPort;

    public ClientNetComm(String targetAddr, int targetPort) {
        this.targetAddr = targetAddr;
        this.targetPort = targetPort;
    }

    @Override
    protected Socket getSocket() throws Exception {
        return new Socket(targetAddr, targetPort);
    }

    @Override
    protected void extraClose() {
        // Do nothing: nothing extra to close.
    }
}
