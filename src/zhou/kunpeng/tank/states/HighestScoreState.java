package zhou.kunpeng.tank.states;

import zhou.kunpeng.tank.MainFrame;
import zhou.kunpeng.tank.display.Background;
import zhou.kunpeng.tank.display.GoudyStoutFont;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by JA on 2017/5/24.
 * <p>
 * This state is for showing Highest Scores, once finishing all levels or game over. <br>
 * After this state, the game will return to WelcomeState.
 * </p>
 */
public class HighestScoreState extends JLayeredPane implements KeyListener {

    private MainFrame mainFrame;

    public static final String HIGH_SCORE_FILE_NAME = "D:/Tank_Highest_Score.txt";

    public HighestScoreState(MainFrame mainFrame, int score, boolean isOnline) {
        super();
        this.mainFrame = mainFrame;

        this.setSize(mainFrame.getSize());

        this.add(new Background(getWidth(), getHeight()), new Integer(0));

        List<Integer> spList = new ArrayList<>();
        List<Integer> mpList = new ArrayList<>();

        // Load current highest scores
        try {
            Scanner in = new Scanner(new FileInputStream(HIGH_SCORE_FILE_NAME));
            for (int i = 0; i < 5 && in.hasNext(); i++)
                spList.add(in.nextInt());
            for (int i = 0; i < 5 && in.hasNext(); i++)
                mpList.add(in.nextInt());
            in.close();
        } catch (Exception e) {
            spList.clear();
            mpList.clear();
        }
        for (int i = spList.size(); i < 5; i++)
            spList.add(0);
        for (int i = mpList.size(); i < 5; i++)
            mpList.add(0);

        // Add current score to highest score list
        if (!isOnline)
            spList.add(score);
        else
            mpList.add(score);

        // Sort from highest to lowest
        spList.sort((o1, o2) -> o2 - o1);
        mpList.sort((o1, o2) -> o2 - o1);

        // get text to show
        StringBuilder show = new StringBuilder().append("<html>Single Player Highest Scores:<br><br>");
        for (int i = 0; i < 5; i++)
            show = show.append("&nbsp;&nbsp;&nbsp;&nbsp;").append(i + 1).append(":&nbsp;&nbsp;").
                    append(spList.get(i)).append("<br>");
        show = show.append("<br>Multi Player Highest Scores:<br><br>");
        for (int i = 0; i < 5; i++)
            show = show.append("&nbsp;&nbsp;&nbsp;&nbsp;").append(i + 1).append(":&nbsp;&nbsp;").
                    append(mpList.get(i)).append("<br>");
        show = show.append("<br>Press any key to continue");

        // Show the text on screen using JLabel
        JLabel label = new JLabel(show.toString());
        label.setBounds(50, 0, getWidth() - 100, getHeight() - 100);
        label.setForeground(Color.WHITE);
        label.setFont(GoudyStoutFont.getInstance());
        this.add(label, new Integer(1));

        // Write the new Highest Score list to file
        try {
            FileWriter out = new FileWriter(HIGH_SCORE_FILE_NAME);
            for (int i = 0; i < 5; i++)
                out.write("" + spList.get(i) + "\r\n");
            for (int i = 0; i < 5; i++)
                out.write("" + mpList.get(i) + "\r\n");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Press any key to continue...
        mainFrame.addKeyListener(this);

    }


    @Override
    public void keyTyped(KeyEvent e) {
        //Do nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        mainFrame.removeKeyListener(this);

        if (mainFrame.isOnline())
            mainFrame.getNetComm().close();

        WelcomeState welcomeState = new WelcomeState(mainFrame);
        mainFrame.nextState(welcomeState);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //Do nothing
    }

}
