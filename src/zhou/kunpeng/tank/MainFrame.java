package zhou.kunpeng.tank;

import zhou.kunpeng.tank.comm.NetComm;
import zhou.kunpeng.tank.comm.ServerNetComm;
import zhou.kunpeng.tank.states.WelcomeState;
import zhou.kunpeng.tank.timer.Timeline;

import javax.swing.*;
import java.awt.*;

/**
 * Created by JA on 2017/5/23.
 * <p>
 * The Main JFrame. States are its Content Panes, and they shall keep a MainFrame reference to switch game states.<br>
 * Also, MainFrame keeps the Timer and NetComm references globally.
 * All other classes shall use them to animate, communicate with client/server,
 * and decide its own action depending on whether it is single player, 1p or 2p.
 * </p>
 */
public class MainFrame extends JFrame {

    private Timeline timer;
    private NetComm netComm;

    public MainFrame() {
        super("Tank");
        this.setSize(700, 600);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        WelcomeState firstState = new WelcomeState(this);
        nextState(firstState);
    }

    public Container getCurrentState() {
        return getContentPane();
    }

    public void nextState(Container state) {
        setContentPane(state);
        revalidate();
        repaint();
    }

    public Timeline getTimer() {
        return timer;
    }

    public void setTimer(Timeline timer) {
        this.timer = timer;
    }

    public NetComm getNetComm() {
        return netComm;
    }

    public void setNetComm(NetComm netComm) {
        this.netComm = netComm;
    }


    public boolean isOnline() {
        return this.netComm != null;
    }

    public boolean isServer() {
        // I don't feel quite good since ServerNetComm is an exact class.
        return isOnline() && this.netComm instanceof ServerNetComm;
    }
}
