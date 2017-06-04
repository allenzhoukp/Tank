package zhou.kunpeng.tank.battle;

import zhou.kunpeng.tank.Levels;
import zhou.kunpeng.tank.display.GoudyStoutFont;
import zhou.kunpeng.tank.display.ImageComponent;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

/**
 * Created by JA on 2017/5/21.
 */
class InfoPanel extends JPanel {

    private JPanel enemyCountPanel;
    private Stack<ImageComponent> tankCounters;

    private JLabel p1Score;
    private JLabel p2Score;
    private JPanel p1Info;
    private JPanel p2Info;
    private JLabel p1LifeText;
    private JLabel p2LifeText;
    private JLabel levelLabelText;

    InfoPanel(int enemyTankCount, int p1Life, int p2Life, int level) {
        super();
        this.setLayout(null);
        this.setSize(200, Levels.BATTLE_HEIGHT * GameMap.SLOT_SIZE);

        p1Score = new JLabel("1P: 0");
        p1Score.setBounds(10, 10, 150, 40);
        p1Score.setFont(GoudyStoutFont.getInstance());
        this.add(p1Score);

        p2Score = new JLabel("2P: 0");
        p2Score.setBounds(10, 50, 150, 40);
        p2Score.setFont(GoudyStoutFont.getInstance());
        this.add(p2Score);

        enemyCountPanel = new JPanel();
        GridLayout layout = new GridLayout(10, 2);
        enemyCountPanel.setLayout(layout);
        tankCounters = new Stack<>();
        for (int i = 0; i < enemyTankCount; i++) {
            tankCounters.add(new ImageComponent("/images/tankcounter.png"));
            enemyCountPanel.add(tankCounters.peek());
        }
        enemyCountPanel.setBounds(40, 100, 80, 250);
        this.add(enemyCountPanel);

        p1Info = new JPanel();
        p1Info.setBounds(0, 370, 150, 50);
        p1Info.add(new ImageComponent("/images/1p1.png"));
        JLabel x1 = new JLabel(" × ");
        x1.setFont(GoudyStoutFont.getInstance());
        p1Info.add(x1);
        p1Info.add((p1LifeText = new JLabel("" + p1Life)));
        p1LifeText.setFont(GoudyStoutFont.getInstance());
        this.add(p1Info);

        p2Info = new JPanel();
        p2Info.setBounds(0, 420, 150, 50);
        p2Info.add(new ImageComponent("/images/2p1.png"));
        JLabel x2 = new JLabel(" × ");
        x2.setFont(GoudyStoutFont.getInstance());
        p2Info.add(x2);
        p2Info.add((p2LifeText = new JLabel("" + p2Life)));
        p2LifeText.setFont(GoudyStoutFont.getInstance());
        this.add(p2Info);

        levelLabelText = new JLabel("level " + level);
        levelLabelText.setBounds(20, 470, 150, 50);
        levelLabelText.setFont(GoudyStoutFont.getInstance());
        this.add(levelLabelText);
    }


    void updateEnemyCount(int enemyCount) {
        while (tankCounters.size() > enemyCount)
            enemyCountPanel.remove(tankCounters.pop());
        if (enemyCount % 2 == 0) {
            enemyCountPanel.setSize(80, 250 * enemyCount / 20);
            ((GridLayout) enemyCountPanel.getLayout()).setRows(enemyCount / 2);
            ((GridLayout) enemyCountPanel.getLayout()).setColumns(2);
        }
    }

    void updateLife(int life, boolean isP1) {
        if (isP1)
            p1LifeText.setText(String.valueOf(life));
        else
            p2LifeText.setText(String.valueOf(life));
    }

    void updateScore(int score, boolean isP1) {
        if (isP1)
            p1Score.setText("P1: " + score);
        else
            p2Score.setText("P2: " + score);
    }

}