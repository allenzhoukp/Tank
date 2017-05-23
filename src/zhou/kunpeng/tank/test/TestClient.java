package zhou.kunpeng.tank.test;

import zhou.kunpeng.tank.MainFrame;
import zhou.kunpeng.tank.comm.ClientNetComm;
import zhou.kunpeng.tank.states.BattleState;
import zhou.kunpeng.tank.timer.Timeline;

import javax.swing.*;

/**
 * Created by JA on 2017/5/22.
 */
class TestClient {
    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setTimer(new Timeline());
        mainFrame.setNetComm(new ClientNetComm("127.0.0.1", 8079));
        BattleState battleState = new BattleState(mainFrame);
        mainFrame.nextState(battleState);
        mainFrame.setVisible(true);
    }
}
