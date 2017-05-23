package zhou.kunpeng.tank.display;

import javax.swing.*;
import java.awt.*;

/**
 * Created by JA on 2017/5/23.
 * <p>
 *     A normal background JComponent with Color.
 * </p>
 */
public class Background extends JComponent {

    private Color color;

    public Background(int width, int height, Color color) {
        super();
        this.color = color;
        this.setSize(width, height);
    }

    public Background(int width, int height) {
        this(width, height, Color.BLACK);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(color);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
