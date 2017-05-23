package zhou.kunpeng.tank.states;

import zhou.kunpeng.tank.MainFrame;
import zhou.kunpeng.tank.PlayerState;
import zhou.kunpeng.tank.display.Background;
import zhou.kunpeng.tank.display.GoudyStoutFont;
import zhou.kunpeng.tank.messages.StartLevelListener;
import zhou.kunpeng.tank.messages.StartLevelMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by JA on 2017/5/23.
 * <p>
 * PrepareLevelState is a state that shows the level info.
 * The player shall press any key to start the level. <br>
 * On the other hand, if online gaming, the level will only start
 * when both player press any key in this state.
 * </p>
 */
public class PrepareLevelState extends JLayeredPane implements KeyListener {

    private MainFrame mainFrame;
    private int level;

    private JLabel levelIndicator;
    private JLabel info;

    private boolean p1Started;
    private boolean p2Started;

    private StartLevelListener startLevelListener;

    private BattleState battleState;

    public static final Integer NONBG_LAYER = 100;
    public static final double WAIT_TIME = 3;

    /**
     * Warning: Once created, this class will starts listening to partner's StartLevelMessage, if online.
     *
     * @param mainFrame The MainFrame instance.
     * @param level     level number.
     */
    public PrepareLevelState(MainFrame mainFrame, int level, PlayerState p1State, PlayerState p2State) {
        super();
        this.mainFrame = mainFrame;
        this.level = level;
        this.setSize(mainFrame.getSize());

        this.add(new Background(getWidth(), getHeight()), new Integer(0));

        levelIndicator = new JLabel("Level " + level);
        levelIndicator.setBounds(level >= 10 ? 225 : 240, 200, 400, 50);
        levelIndicator.setFont(GoudyStoutFont.getInstance().deriveFont(25.0f));
        levelIndicator.setForeground(Color.WHITE);
        this.add(levelIndicator, NONBG_LAYER);

        // Preload BattleState. It is SOOOOOO Slow!
        battleState = new BattleState(mainFrame, level, p1State, p2State);

        info = new JLabel("Press any key to start");
        info.setHorizontalTextPosition(SwingConstants.CENTER);
        info.setFont(GoudyStoutFont.getInstance());
        info.setForeground(Color.WHITE);
        info.setBounds(170, 300, 400, 50);
        this.add(info, NONBG_LAYER);

        if (mainFrame.isOnline()) {
            startLevelListener = new StartLevelListener(this);
            mainFrame.getNetComm().registerListener(startLevelListener);
        }

        mainFrame.addKeyListener(this);

    }

    public void partnerStarts() {
        if (mainFrame.isServer())
            p2Started = true; // partner.
        else
            p1Started = true;

        if (p1Started && p2Started)
            startCountdown();
    }


    private void setWaitInfo() {
        info.setText("Waiting for partner...");
    }


    @Override
    public void keyTyped(KeyEvent e) {

        mainFrame.removeKeyListener(this);

        if (mainFrame.isOnline()) {
            mainFrame.getNetComm().send(new StartLevelMessage());
            if (mainFrame.isServer())
                p1Started = true;
            else
                p2Started = true;

            if (p1Started && p2Started)
                startCountdown();
            else
                setWaitInfo();

        } else {
            startCountdown();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //Do nothing
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //Do nothing
    }

    private void startCountdown() {
        if(startLevelListener != null)
            mainFrame.getNetComm().removeListener(startLevelListener);

        mainFrame.nextState(battleState);
        battleState.start();
    }
}
