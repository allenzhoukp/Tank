package zhou.kunpeng.tank.comm;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by JA on 2017/5/22.
 * <p>
 * A specified NetComm using ServerSocket.
 * When start() is called, it will block the current thread.
 * </p>
 */
public class ServerNetComm extends NetComm {

    private final int port;
    private ServerSocket server = null;

    public ServerNetComm(int port) {
        this.port = port;
    }

    @Override
    protected Socket getSocket() {
        try {
            ServerSocket server = new ServerSocket(port);
            return server.accept();
        } catch (Exception e) {
            //IOException, or simply NullPointer (server == null).
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void extraClose() {
        try {
            server.close();
        } catch (Exception e) {
            //IOException, or simply NullPointer (server == null).
            e.printStackTrace();
            server = null;
        }
    }
}
