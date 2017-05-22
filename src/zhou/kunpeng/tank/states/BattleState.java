package zhou.kunpeng.tank.states;

import zhou.kunpeng.tank.GameMap;
import zhou.kunpeng.tank.PlayerKeyListener;
import zhou.kunpeng.tank.ai.AIEnemyCreationOperator;
import zhou.kunpeng.tank.ai.AIOperator;
import zhou.kunpeng.tank.comm.ClientNetComm;
import zhou.kunpeng.tank.comm.ServerNetComm;
import zhou.kunpeng.tank.messages.EnemyGenerateListener;
import zhou.kunpeng.tank.messages.TankFireListener;
import zhou.kunpeng.tank.messages.TankMoveListener;
import zhou.kunpeng.tank.messages.TankStopListener;
import zhou.kunpeng.tank.timer.Timeline;

import javax.swing.*;

/**
 * Created by JA on 2017/5/22.
 */
public class BattleState extends JPanel {

    private int[][] level1 = new int[][]{
            {0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
            {3, 3, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0},
            {3, 3, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 2, 2, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0},
            {3, 3, 3, 3, 3, 3, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0},
            {3, 3, 3, 3, 3, 3, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0},
            {3, 3, 4, 4, 4, 4, 4, 4, 0, 0, 4, 4, 2, 2, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 4, 4},
            {3, 3, 4, 4, 4, 4, 4, 4, 0, 0, 4, 4, 2, 2, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 4, 4},
            {0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0},
            {0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0},
            {0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
            {2, 2, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 2, 2},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0},
            {0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0},
            {0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0},
            {0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0},
            {0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0},
            {0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0},
            {0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };

    public BattleState(JFrame frame, boolean online, boolean server, String targetAddress) {

        Timeline timer = new Timeline();

        GameMap map = new GameMap(level1, timer, 1, online, server);

        if (online) {
            if (server)
                map.setNetComm(new ServerNetComm(8078));
            else
                map.setNetComm(new ClientNetComm(targetAddress, 8078));
            map.getNetComm().registerListener(new TankMoveListener(map));
            map.getNetComm().registerListener(new TankFireListener(map));
            map.getNetComm().registerListener(new EnemyGenerateListener(map));
            map.getNetComm().registerListener(new TankStopListener(map));
            map.getNetComm().start();
        }

        if (server) {
            AIEnemyCreationOperator creator = new AIEnemyCreationOperator(map);
            timer.registerListener(creator);

            AIOperator aiOperator = new AIOperator(map);
            timer.registerListener(aiOperator);
        }


        this.setLayout(null);
        this.add(map);

        frame.addKeyListener(new PlayerKeyListener(map, true));

        timer.start();

    }

}
