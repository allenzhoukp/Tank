package zhou.kunpeng.tank.comm;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JA on 2017/5/22.
 * <p>
 * This class is used for socket communication.<br>
 * Regardless of whether the socket is server or client,
 * it uses a thread to listen all the messages received,
 * and call NetListeners to deal with this.<br>
 * And, on the other hand, it uses Message class to format the message to send,
 * and send the message through Socket using send() method.
 * </p>
 * <p>
 * Use start() to start the communication. <br>
 * Before that, make sure all the listeners are registered.
 * </p>
 */
public abstract class NetComm {

    private Socket socket;

    private List<NetListener> listeners = new ArrayList<>();

    private ReceiverThread thread;

    protected abstract Socket getSocket();

    protected abstract void extraClose();

    public void registerListener(NetListener listener) {
        listeners.add(listener);
    }

    public void removeListener(NetListener listener) {
        listeners.remove(listener);
    }

    public void removeAllListeners() {
        listeners.clear();
    }

    /**
     * Start the communication by connecting Socket. <br>
     * Warning: This method may block the current thread!
     * (e.g. ServerSocket::accept() in getSocket() call)
     */
    public void start() {
        synchronized (this) {
            socket = getSocket();

            thread = new ReceiverThread();
            thread.start();
        }
    }

    /**
     * Send a message that formatted by a Message instance.
     *
     * @param message the message to send.
     */
    public void send(Message message) {
        if (socket == null) {
            close();
            return;
        }
        try {
            System.out.println("t=" + System.nanoTime() / 1000000L + " " + message.getMessage());

            byte[] msgBytes = message.getMessage().getBytes();
            byte[] lengthBytes = ByteUtil.getByteArray(msgBytes.length);
            socket.getOutputStream().write(ByteUtil.append(lengthBytes, msgBytes));
            socket.getOutputStream().flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Close the socket.
     * An extraClose() is called to ensure all the IO APIs are properly closed.
     */
    public void close() {
        synchronized (this) {
            closeSocketAndInput();
            extraClose();
        }
    }

    public boolean isClosed() {
        return socket == null;
    }

    protected class ReceiverThread extends Thread {
        @Override
        public void run() {
            boolean exceptionFlag = false;
            while (socket != null /*&& reader != null*/) {
                byte[] lineBytes = null;
                try {
                    byte[] lengthBytes = new byte[4];
                    if (socket.getInputStream().read(lengthBytes, 0, 4) != 4)
                        continue;
                    int length = ByteUtil.getInt(lengthBytes, 0);

                    lineBytes = new byte[length];
                    int bytesRead = 0;
                    while (bytesRead != length)
                        bytesRead += socket.getInputStream().read(lineBytes, bytesRead, length - bytesRead);

                } catch (IOException e) {
                    //Due to sync problem, this WILL happen when NetComm is forced closed.
                    //The thread will auto stop then, by setting exceptionFlag and break.
                    e.printStackTrace();
                    exceptionFlag = true;
                }


                if (exceptionFlag) {
                    close();
                    break;
                }

                String line = new String(lineBytes);
                System.out.println("t=" + System.nanoTime() / 1000000L + " " + line);


                //Exception java.lang.ArrayIndexOutOfBoundsException @ java.awt.Container.add
                //   potential solution: avoid direct call for Swing components.

                SwingUtilities.invokeLater(new InterpretThread(line));
            }
        }
    }

    private class InterpretThread implements Runnable {

        private String line;

        InterpretThread(String line) {
            this.line = line;
        }

        @Override
        public void run() {
            //Another optimization: since there are no multiple listeners
            // corresponding to one type of message,
            // once find a proper listener, the loop shall break.
            //List<NetListener> listenersInUse = new ArrayList<>(listeners);
            for (NetListener listener : listeners) {
                if (listener.tryInterpret(line))
                    break;
            }
        }
    }

    private void closeSocketAndInput() {
        try {
            socket.close();
        } catch (Exception e) { //IOException or NullPointerException
            e.printStackTrace();
        } finally {
            socket = null;
        }
    }

}
