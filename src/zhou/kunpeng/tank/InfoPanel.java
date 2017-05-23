package zhou.kunpeng.tank;

import zhou.kunpeng.tank.display.GoudyStoutFont;
import zhou.kunpeng.tank.display.ImageComponent;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

/**
 * Created by JA on 2017/5/21.
 */
public class InfoPanel extends JPanel {

    private JPanel enemyCountPanel;
    private Stack<ImageComponent> tankCounters;

    private JPanel p1Info;
    private JPanel p2Info;
    private JLabel p1LifeText;
    private JLabel p2LifeText;
    private JLabel levelLabelText;

    public InfoPanel(int enemyTankCount, int p1Life, int p2Life, int level) {
        super();
        this.setLayout(null);
        this.setSize(200, GameMap.BATTLE_HEIGHT * GameMap.SLOT_SIZE);

        enemyCountPanel = new JPanel();
        GridLayout layout = new GridLayout(10, 2);
        enemyCountPanel.setLayout(layout);
        tankCounters = new Stack<>();
        for (int i = 0; i < enemyTankCount; i++) {
            tankCounters.add(new ImageComponent("/images/tankcounter.png"));
            enemyCountPanel.add(tankCounters.peek());
        }
        enemyCountPanel.setBounds(40, 20, 80, 300);
        this.add(enemyCountPanel);

        p1Info = new JPanel();
        p1Info.setBounds(0, 320, 150, 50);
        p1Info.add(new ImageComponent("/images/1p1.png"));
        JLabel x1 = new JLabel(" × ");
        x1.setFont(GoudyStoutFont.getInstance());
        p1Info.add(x1);
        p1Info.add((p1LifeText = new JLabel("" + p1Life)));
        p1LifeText.setFont(GoudyStoutFont.getInstance());
        this.add(p1Info);

        p2Info = new JPanel();
        p2Info.setBounds(0, 370, 150, 50);
        p2Info.add(new ImageComponent("/images/2p1.png"));
        JLabel x2 = new JLabel(" × ");
        x2.setFont(GoudyStoutFont.getInstance());
        p2Info.add(x2);
        p2Info.add((p2LifeText = new JLabel("" + p2Life)));
        p2LifeText.setFont(GoudyStoutFont.getInstance());
        this.add(p2Info);

        levelLabelText = new JLabel("level " + level);
        levelLabelText.setBounds(20, 420, 150, 50);
        levelLabelText.setFont(GoudyStoutFont.getInstance());
        this.add(levelLabelText);
    }

    public InfoPanel(int enemyTankCount, int p1Life, int p2Life) {
        this(enemyTankCount, p1Life, p2Life, 1);
    }

    public InfoPanel() {
        this(GameMap.INIT_ENEMY, PlayerState.INIT_LIFE, PlayerState.INIT_LIFE);
    }

    public void updateEnemyCount(int enemyCount) {
        while (tankCounters.size() > enemyCount)
            enemyCountPanel.remove(tankCounters.pop());
    }

    public void updateLife(int life, boolean isP1) {
        if (isP1)
            p1LifeText.setText(String.valueOf(life));
        else
            p2LifeText.setText(String.valueOf(life));
    }

}