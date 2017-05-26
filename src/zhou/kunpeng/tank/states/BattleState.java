package zhou.kunpeng.tank.states;

import zhou.kunpeng.tank.Levels;
import zhou.kunpeng.tank.MainFrame;
import zhou.kunpeng.tank.PlayerState;
import zhou.kunpeng.tank.Sound;
import zhou.kunpeng.tank.battle.ClientKeyListener;
import zhou.kunpeng.tank.battle.EnemyTank;
import zhou.kunpeng.tank.battle.GameMap;
import zhou.kunpeng.tank.battle.PlayerKeyListener;
import zhou.kunpeng.tank.battle.ai.AIEnemyCreationOperator;
import zhou.kunpeng.tank.battle.ai.AIOperator;
import zhou.kunpeng.tank.messages.*;
import zhou.kunpeng.tank.timer.Timeline;

import javax.swing.*;
import java.awt.event.KeyListener;

/**
 * Created by JA on 2017/5/22.
 * <p>
 * BattleState is the main state of the game, i.e. the states with tanks firing! <br>
 * Most works are linked to class GameMap. BattleState only configures
 * a proper environment for GameMap, the latter being the mediator of most operations. <br>
 * After the game ends, switch to ScoreCounterState.
 * </p>
 */
public class BattleState extends JPanel {

    private MainFrame mainFrame;
    private GameMap gameMap;
    private KeyListener keyListener;

    /**
     * Warning: NetComm will start listening once constructed.
     * However, game will not start immediately. A start() call is required.
     *
     * @param frame The MainFrame instance.
     * @param level level number.
     */
    public BattleState(MainFrame frame, int level, PlayerState p1State, PlayerState p2State) {
        super();

        this.mainFrame = frame;

        if (mainFrame.getTimer() == null)
            mainFrame.setTimer(new Timeline());
        mainFrame.getTimer().stop();

        gameMap = new GameMap(this, Levels.getLevel(level), mainFrame.getTimer(), level, p1State, p2State, mainFrame.isOnline(),
                mainFrame.isOnline() && !mainFrame.isServer());

        if (mainFrame.isOnline()) {

            gameMap.setNetComm(mainFrame.getNetComm());

            // The order is significant!
            // listeners in front will be checked first.
            // Thus, the most frequent visited listeners should be placed in front.
            gameMap.getNetComm().registerListener(new TankMoveListener(gameMap));
            gameMap.getNetComm().registerListener(new TankStopListener(gameMap));
            gameMap.getNetComm().registerListener(new TankFireListener(gameMap));
            gameMap.getNetComm().registerListener(new ClientKeyOpListener(gameMap));
            gameMap.getNetComm().registerListener(new TerrainDestroyListener(gameMap));
            gameMap.getNetComm().registerListener(new TankHitListener(gameMap));
            gameMap.getNetComm().registerListener(new EnemyGenerateListener(gameMap));
            gameMap.getNetComm().registerListener(new BaseHitListener(gameMap));
            gameMap.getNetComm().registerListener(new PauseAndContinueListener(gameMap));

        }

        this.setLayout(null);
        this.add(gameMap);
    }

    /**
     * Start the game.
     */
    public void start() {

        EnemyTank.ID = 1;

        if (!mainFrame.isOnline() || mainFrame.isServer()) {
            AIEnemyCreationOperator creator = new AIEnemyCreationOperator(gameMap);
            mainFrame.getTimer().registerListener(creator);

            AIOperator aiOperator = new AIOperator(gameMap);
            mainFrame.getTimer().registerListener(aiOperator);
        }

        if (!mainFrame.isOnline() || mainFrame.isServer())
            keyListener = new PlayerKeyListener(gameMap, true);
        else
            keyListener = new ClientKeyListener(gameMap, false);
        //The ONLY possible KeyEvent dispatcher is Frame!
        mainFrame.addKeyListener(keyListener);

        Sound.play("/sounds/start.wav");
        mainFrame.getTimer().start();
    }

    /**
     * End the game and start counting score.
     *
     * @param p1Score   PlayerState for player 1.
     * @param p2Score   PlayerState for player 2. If it doesn't exists, an empty All-Zero counter should be appropriate.
     * @param isVictory if true, ScoreCounterState will call for next level; otherwise return to first state.
     */
    public void endState(PlayerState p1Score, PlayerState p2Score, boolean isVictory) {
        if (gameMap.isOnline())
            gameMap.getNetComm().removeAllListeners();
        mainFrame.getTimer().removeAllListeners();
        mainFrame.removeKeyListener(keyListener);

        ScoreCounterState nextState = new ScoreCounterState(mainFrame, gameMap.getLevel(),
                p1Score, p2Score, isVictory);
        mainFrame.nextState(nextState);

    }
}
