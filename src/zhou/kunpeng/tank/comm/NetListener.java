package zhou.kunpeng.tank.comm;

/**
 * Created by JA on 2017/5/22.
 * <p>
 * NetListener is for interpreting messages from Net Communication.
 * </p>
 */
public interface NetListener {
    /**
     * Try to interpret the message line, and decide what to do with the message.
     * @param line the message from net connection.
     * @return should be true if interpreted correctly. Else return false.
     */
    boolean tryInterpret(byte[] line);
}
