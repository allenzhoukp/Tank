package zhou.kunpeng.tank.test;

import zhou.kunpeng.tank.MainFrame;
import zhou.kunpeng.tank.PlayerState;
import zhou.kunpeng.tank.comm.ServerNetComm;
import zhou.kunpeng.tank.states.PrepareLevelState;
import zhou.kunpeng.tank.timer.Timeline;

/**
 * Created by JA on 2017/5/22.
 */
class TestServer {
    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setTimer(new Timeline());
        mainFrame.setNetComm(new ServerNetComm(8079));
        mainFrame.getNetComm().start();
        //BattleState state = new BattleState(mainFrame, 1);
        PrepareLevelState state = new PrepareLevelState(mainFrame, 1, new PlayerState(), new PlayerState());
        mainFrame.nextState(state);

        mainFrame.setVisible(true);
    }
}
