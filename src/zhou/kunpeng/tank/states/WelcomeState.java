package zhou.kunpeng.tank.states;

import zhou.kunpeng.tank.MainFrame;
import zhou.kunpeng.tank.PlayerState;
import zhou.kunpeng.tank.Sound;
import zhou.kunpeng.tank.display.Background;
import zhou.kunpeng.tank.display.GoudyStoutFont;
import zhou.kunpeng.tank.display.ImageComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JA on 2017/5/24.
 * <p>
 * WelcomeState is the first state of the game. <br>
 * The user can choose <br>
 * (1) Single player; <br>
 * (2) Online game as Player 1 (server); <br>
 * (3) Online game as Player 2 (client). <br>
 * If single player game is chosen, switch to PrepareLevelState directly;
 * otherwise switch to ConfigNetCommState.
 * </p>
 */
public class WelcomeState extends JLayeredPane implements KeyListener {

    private static final Integer NONBG_LAYER = 100;

    private MainFrame mainFrame;

    private List<JLabel> selectList = new ArrayList<>();
    private int cursor;

    public WelcomeState(MainFrame mainFrame) {
        super();
        this.mainFrame = mainFrame;
        this.setSize(mainFrame.getSize());

        Background bg = new Background(this.getWidth(), this.getHeight());
        this.add(bg, new Integer(0));

        ImageComponent title = new ImageComponent("/images/title.png", 75, 75);
        this.add(title, NONBG_LAYER);

        JLabel onePlayer = new JLabel("1 PLAYER");
        onePlayer.setFont(GoudyStoutFont.getInstance());
        onePlayer.setForeground(Color.WHITE);
        onePlayer.setBounds(200, 300, 400, 50);
        this.add(onePlayer, NONBG_LAYER);
        selectList.add(onePlayer);

        JLabel twoPlayer = new JLabel("2 PLAYERS");
        twoPlayer.setFont(GoudyStoutFont.getInstance());
        twoPlayer.setForeground(Color.WHITE);
        twoPlayer.setBounds(200, 340, 400, 50);
        this.add(twoPlayer, NONBG_LAYER);

        JLabel p1 = new JLabel("PLAY AS PLAYER 1");
        p1.setFont(GoudyStoutFont.getInstance());
        p1.setForeground(Color.WHITE);
        p1.setBounds(250, 380, 400, 50);
        this.add(p1, NONBG_LAYER);
        selectList.add(p1);

        JLabel p2 = new JLabel("PLAY AS PLAYER 2");
        p2.setFont(GoudyStoutFont.getInstance());
        p2.setForeground(Color.WHITE);
        p2.setBounds(250, 420, 400, 50);
        this.add(p2, NONBG_LAYER);
        selectList.add(p2);

        cursor = 0;
        updateColor();
        mainFrame.addKeyListener(this);
    }

    private void updateColor() {
        synchronized (this) {
            for (JLabel label : selectList)
                label.setForeground(Color.WHITE);
            selectList.get(cursor).setForeground(Color.ORANGE);
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                cursor--;
                if (cursor < 0)
                    cursor = 0;
                updateColor();
                break;

            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                cursor++;
                if (cursor >= selectList.size())
                    cursor = selectList.size() - 1;
                updateColor();
                break;

            case KeyEvent.VK_F:
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_SPACE:
                Sound.play("/sounds/count.wav");
                mainFrame.removeKeyListener(this);
                nextFrame();
                break;
        }
    }

    private void nextFrame() {
        if (cursor == 0) {
            PrepareLevelState prepareLevelState = new PrepareLevelState
                    (mainFrame, 1, new PlayerState(), new PlayerState());
            mainFrame.nextState(prepareLevelState);
        } else {
            ConfigNetCommState configNetCommState = new ConfigNetCommState
                    (mainFrame, cursor == 1);
            mainFrame.nextState(configNetCommState);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //Do nothing
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //Do nothing
    }
}
