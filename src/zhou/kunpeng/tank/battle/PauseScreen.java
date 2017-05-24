package zhou.kunpeng.tank.battle;

import zhou.kunpeng.tank.display.Background;
import zhou.kunpeng.tank.display.GoudyStoutFont;

import javax.swing.*;
import java.awt.*;

/**
 * Created by JA on 2017/5/24.
 * <p>
 * PauseScreen is the display component shown when the game is paused.
 * </p>
 */
public class PauseScreen extends JLayeredPane {
    public PauseScreen(int width, int height) {
        super();
        this.setSize(width, height);


        Background bg = new Background(width, height,
                new Color(0xCC000000, true));
        this.add(bg, new Integer(0));

        JLabel paused = new JLabel("PAUSED");
        paused.setForeground(Color.WHITE);
        paused.setFont(GoudyStoutFont.getInstance().deriveFont(20.0f));
        paused.setBounds(250, 200, 200, 200);
        this.add(paused, new Integer(1));
    }
}
