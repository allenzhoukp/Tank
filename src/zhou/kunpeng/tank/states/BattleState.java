package zhou.kunpeng.tank.states;

import zhou.kunpeng.tank.*;
import zhou.kunpeng.tank.ai.AIEnemyCreationOperator;
import zhou.kunpeng.tank.ai.AIOperator;
import zhou.kunpeng.tank.messages.*;
import zhou.kunpeng.tank.timer.Timeline;
import zhou.kunpeng.tank.timer.TimerListener;

import javax.swing.*;
import java.awt.event.KeyListener;

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

    private MainFrame mainFrame;
    private GameMap gameMap;
    private KeyListener keyListener;

    public BattleState(MainFrame frame) {

        this.mainFrame = frame;

        if (mainFrame.getTimer() == null)
            mainFrame.setTimer(new Timeline());
        Timeline timer = mainFrame.getTimer();

        gameMap = new GameMap(this, level1, timer, 1, mainFrame.isOnline(),
                mainFrame.isOnline() && !mainFrame.isServer());

        if (mainFrame.isOnline()) {

            gameMap.setNetComm(mainFrame.getNetComm());

            gameMap.getNetComm().registerListener(new BaseHitListener(gameMap));
            gameMap.getNetComm().registerListener(new ClientOpListener(gameMap));
            gameMap.getNetComm().registerListener(new EnemyGenerateListener(gameMap));
            gameMap.getNetComm().registerListener(new TankFireListener(gameMap));
            gameMap.getNetComm().registerListener(new TankHitListener(gameMap));
            gameMap.getNetComm().registerListener(new TankMoveListener(gameMap));
            gameMap.getNetComm().registerListener(new TankStopListener(gameMap));
            gameMap.getNetComm().registerListener(new TerrainDestroyListener(gameMap));

            gameMap.getNetComm().start();
        }

        if (!mainFrame.isOnline() || mainFrame.isServer()) {
            AIEnemyCreationOperator creator = new AIEnemyCreationOperator(gameMap);
            timer.registerListener(creator);

            AIOperator aiOperator = new AIOperator(gameMap);
            timer.registerListener(aiOperator);
        }


        this.setLayout(null);
        this.add(gameMap);

        if (!mainFrame.isOnline() || mainFrame.isServer())
            keyListener = new PlayerKeyListener(gameMap, true);
        else
            keyListener = new ClientKeyListener(gameMap, false);
        //The ONLY possible KeyEvent dispatcher is Frame!
        mainFrame.addKeyListener(keyListener);

        timer.start();

    }

    public void endState(ScoreCounter p1Score, ScoreCounter p2Score, boolean isVictory) {
        gameMap.getNetComm().removeAllListeners();
        mainFrame.getTimer().removeAllListeners();
        mainFrame.removeKeyListener(keyListener);

        //TODO next state calc points.
    }

}
