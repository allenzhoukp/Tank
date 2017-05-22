package zhou.kunpeng.tank.comm;

/**
 * Created by JA on 2017/5/22.
 */
public interface NetListener {
    boolean tryInterpret(String line);
}
