package zhou.kunpeng.tank.states;

import zhou.kunpeng.tank.Levels;
import zhou.kunpeng.tank.MainFrame;
import zhou.kunpeng.tank.PlayerState;
import zhou.kunpeng.tank.display.Background;
import zhou.kunpeng.tank.display.GoudyStoutFont;
import zhou.kunpeng.tank.display.ImageComponent;
import zhou.kunpeng.tank.timer.Timeline;
import zhou.kunpeng.tank.timer.TimerListener;

import javax.swing.*;
import java.awt.*;

/**
 * Created by JA on 2017/5/23.
 * <p>
 * A score counter view state after the level. Occurs when victory or game over.
 * </p>
 */
public class CounterState extends JLayeredPane {

    private final MainFrame mainFrame;
    private int level;
    private PlayerState p1State;
    private PlayerState p2State;
    private boolean isVictory;

    private JLabel[][] texts = new JLabel[2][5];

    private JLabel[] totalText = new JLabel[2];

    public static final double COUNTER_INTERVAL = 0.33;
    public static final double WAIT_SEC = 4;
    public static final Integer NONBG_LAYER = 100;

    public void showNextLevel() {

        try {
            Thread.sleep((int)(WAIT_SEC * 1000));
        } catch (InterruptedException e) {
            // Anything to do?
        }

        if(!isVictory || level == Levels.getLevelCount()) {
            //TODO main state

        } else {
            PrepareLevelState prepareState = new PrepareLevelState(mainFrame, level + 1, p1State, p2State);
            mainFrame.nextState(prepareState);
        }
    }

    /**
     * Warning: the counter display effect will start immediately after construction.
     *
     * @param mainFrame the MainFrame instance.
     * @param level level number.
     * @param p1State score of player 1.
     * @param p2State score of player 2.
     * @param isVictory if the level ends with victory. If true, the game will switch to next level,
     *                  otherwise it will return to main screen.
     */
    public CounterState(MainFrame mainFrame, int level, PlayerState p1State, PlayerState p2State, boolean isVictory) {
        super();

        this.mainFrame = mainFrame;
        this.level = level;
        this.p1State = p1State;
        this.p2State = p2State;
        this.isVictory = isVictory;

        if (mainFrame.getTimer() == null)
            mainFrame.setTimer(new Timeline());

        this.setLayout(null);
        this.setSize(mainFrame.getSize());

        initComponents();

        mainFrame.getTimer().registerListener(new CounterStateTimerListener());
    }

    private void initComponents() {

        this.add(new Background(getWidth(), getHeight()), new Integer(0));

        ImageComponent normalTankPic = new ImageComponent("/images/t11.png");
        ImageComponent secondaryTankPic = new ImageComponent("/images/t21.png");
        ImageComponent mobileTankPic = new ImageComponent("/images/ts1.png");
        ImageComponent toughTankPic = new ImageComponent("/images/tt31.png");

        final int p1X = 75;
        final int startY = 50;
        final int intervalY = 100;
        final int p2X = 400;

        initComponent(normalTankPic, true, 0, p1X, startY);
        initComponent(secondaryTankPic, true, 1, p1X, startY + intervalY);
        initComponent(mobileTankPic, true, 2, p1X, startY + intervalY * 2);
        initComponent(toughTankPic, true, 3, p1X, startY + intervalY * 3);

        initComponent(normalTankPic.clone(), false, 0, p2X, startY);
        initComponent(secondaryTankPic.clone(), false, 1, p2X, startY + intervalY);
        initComponent(mobileTankPic.clone(), false, 2, p2X, startY + intervalY * 2);
        initComponent(toughTankPic.clone(), false, 3, p2X, startY + intervalY * 3);

        for (int i = 0; i < 2; i++) {
            totalText[i] = new JLabel(getTotalText(i == 0, -1));
            totalText[i].setFont(GoudyStoutFont.getInstance());
            totalText[i].setBounds(i == 0 ? p1X : p2X,
                    startY + intervalY * 4, 300, 50);
            totalText[i].setForeground(Color.WHITE);
            this.add(totalText[i], NONBG_LAYER);
        }
    }

    private class CounterStateTimerListener implements TimerListener {
        private int timeCounter = 0;
        private int player = 0;
        private int tankType = 0;
        private int totalScore = 0;

        @Override
        public void onTimer() {
            timeCounter++;
            if (timeCounter == (int) (COUNTER_INTERVAL * Timeline.FPS)) {
                timeCounter = 0;

                tryPopUp();
            }
        }

        private void tryPopUp() {
            int count = 0;
            int perTankScore = 0;

            // if count == 0, showing up is unnecessary.
            // keep searching down for others.
            // else loop end; timeCounter starts to count again.
            while (count == 0) {
                PlayerState state = null;

                if (player == 0)
                    state = p1State;
                else if (player == 1)
                    state = p2State;
                else {
                    mainFrame.getTimer().removeListener(this);
                    showNextLevel();
                    return;
                }

                switch (tankType) {
                    case 0:
                        count = state.normalTankCount;
                        perTankScore = PlayerState.NORMAL_TANK_SCORE;
                        break;
                    case 1:
                        count = state.secondaryTankCount;
                        perTankScore = PlayerState.SECONDARY_TANK_SCORE;
                        break;
                    case 2:
                        count = state.mobileTankCount;
                        perTankScore = PlayerState.MOBILE_TANK_SCORE;
                        break;
                    case 3:
                        count = state.toughTankCount;
                        perTankScore = PlayerState.TOUGH_TANK_SCORE;
                        break;
                    case 4:
                        count = -1; //dummy for break loop
                }

                if (tankType != 4)
                    texts[player][tankType].setText(getCounterText(count, count * perTankScore));
                else
                    totalText[player].setText(getTotalText(player == 0, totalScore));

                totalScore += count * perTankScore;

                tankType++;
                if (!(tankType < 5)) {
                    tankType %= 5;
                    player++;
                    totalScore = 0;
                }
            }
        }
    }

    private String getCounterText(int num1, int num2) {
        return " × " + num1 + " = " + num2;
    }

    private String getTotalText(boolean isP1, int totalScore) {
        return "P" + (isP1 ? "1" : "2") + " TOTAL: " + (totalScore == -1 ? "" : totalScore);
    }

    private void initComponent(ImageComponent tankPic, boolean isP1, int type, int x, int y) {
        tankPic.setBounds(x, y, 50, 50);
        this.add(tankPic, NONBG_LAYER);
        JLabel text = new JLabel(getCounterText(0, 0));
        text.setBounds(x + 50, y, 150, 50);
        text.setForeground(Color.WHITE);
        text.setFont(GoudyStoutFont.getInstance());
        this.add(text, NONBG_LAYER);

        texts[isP1 ? 0 : 1][type] = text;
    }

}
