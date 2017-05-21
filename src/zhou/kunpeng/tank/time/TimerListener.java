package zhou.kunpeng.tank.time;

/**
 * Created by JA on 2017/5/20.
 * <p>
 * Use with Timeline.
 * In every frame triggered, Timeline will notify all TimerListeners registered, calling onTimer() each.
 * </p>
 */
public interface TimerListener {
    void onTimer();
}
