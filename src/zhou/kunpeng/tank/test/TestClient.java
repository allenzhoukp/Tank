package zhou.kunpeng.tank.test;

import zhou.kunpeng.tank.states.BattleState;

import javax.swing.*;

/**
 * Created by JA on 2017/5/22.
 */
class TestClient {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Client");
        frame.setSize(700, 600);
        BattleState battleState = new BattleState(frame, true, false, "127.0.0.1");
        frame.setContentPane(battleState);
        frame.setVisible(true);
    }
}
