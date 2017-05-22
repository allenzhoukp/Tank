package zhou.kunpeng.tank.test;

import zhou.kunpeng.tank.states.BattleState;

import javax.swing.*;

/**
 * Created by JA on 2017/5/22.
 */
class TestServer {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Server");
        frame.setSize(700, 600);
        BattleState battleState = new BattleState(frame, true, true, "");
        frame.setContentPane(battleState);
        frame.setVisible(true);
    }
}
