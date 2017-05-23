package zhou.kunpeng.tank.test;

import zhou.kunpeng.tank.MainFrame;
import zhou.kunpeng.tank.PlayerState;
import zhou.kunpeng.tank.comm.ClientNetComm;
import zhou.kunpeng.tank.states.PrepareLevelState;
import zhou.kunpeng.tank.timer.Timeline;

/**
 * Created by JA on 2017/5/22.
 */
class TestClient {
    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setTimer(new Timeline());
        mainFrame.setNetComm(new ClientNetComm("127.0.0.1", 8079));
        mainFrame.getNetComm().start();
        PrepareLevelState state = new PrepareLevelState(mainFrame, 1, new PlayerState(), new PlayerState());
        //BattleState state = new BattleState(mainFrame, 1);
        mainFrame.nextState(state);
        mainFrame.setVisible(true);
    }
}
