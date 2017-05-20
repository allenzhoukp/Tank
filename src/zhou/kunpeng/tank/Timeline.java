package zhou.kunpeng.tank;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by JA on 2017/5/20.
 * <p>
 * Core animation controller.
 * Will notify all listeners FPS times in a second.
 * </p>
 */
public class Timeline implements ActionListener {

    public static final int FPS = 25;

    private Timer timer;
    private Set<TimerListener> listeners;

    public Timeline() {
        this.timer = new Timer(1000 / FPS, this);
        this.listeners = new HashSet<>();
    }

    public void registerListener(TimerListener listener) {
        listeners.add(listener);
    }

    public void removeListener(TimerListener listener) {
        listeners.remove(listener);
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    @Override
    public synchronized void actionPerformed(ActionEvent e) {
        for (TimerListener listener : listeners)
            listener.onTimer();
    }
}
