package zhou.kunpeng.tank.display;

import zhou.kunpeng.tank.timer.TimerListener;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JA on 2017/5/19.
 * <p>
 * Designed for components with motions. <br>
 * Different images will be displayed one by one, and loop.
 * </p>
 * <p>
 * However, only with this component, the clip will not animate itself. <br>
 * It needs to be registered as a TimerListener in Timeline.
 * </p>
 * <p>
 * The sequence is an image sequence, and Clip itself only serves as a container.
 * Thus, the x and y of the components should be set relative to Clip's x and y.
 * </p>
 */
public class Clip extends JPanel implements TimerListener {

    private boolean inPlay = true;
    private int frame = 0;

    private List<ImageComponent> sequence = new ArrayList<>();

    public Clip(List<ImageComponent> seq, int sx, int sy, int width, int height) {
        super();
        this.setOpaque(false);
        this.setLayout(null);
        this.setBounds(sx, sy, width, height);
        sequence = seq;
        if (sequence != null && sequence.size() != 0)
            this.add(seq.get(0));
    }

    public Clip(List<ImageComponent> seq, int sx, int sy) {
        this(seq, sx, sy, 0, 0);
        updateSize();
    }

    public Clip(List<ImageComponent> seq) {
        this(seq, 0, 0);
    }

    public Clip() {
        this(new ArrayList<>(), 0, 0);
    }


    private void updateSize() {
        int maxWidth = 0, maxHeight = 0;
        for (ImageComponent comp : sequence) {
            maxWidth = Math.max(comp.getWidth(), maxWidth);
            maxHeight = Math.max(comp.getHeight(), maxHeight);
        }
        this.setSize(maxWidth, maxHeight);
    }

    public void stop() {
        inPlay = false;
    }

    public void play() {
        inPlay = true;
    }

    public boolean isInPlay() {
        return inPlay;
    }

    public void gotoAndPlay(int frame) {
        if (frame >= 0 && frame < sequence.size())
            this.frame = frame;
        this.inPlay = true;
    }

    public void gotoAndStop(int frame) {
        if (frame >= 0 && frame < sequence.size())
            this.frame = frame;
        this.inPlay = false;
    }

    @Override
    public void onTimer() {
        // Sequence may change. Check the boundary!
        if (frame >= sequence.size())
            frame = 0;

        // Stopped
        if (!inPlay)
            return;

        this.removeAll();
        frame++;
        if (frame >= sequence.size())
            frame = 0;
        this.add(sequence.get(frame));
        this.repaint();

    }


    public List<ImageComponent> getSequence() {
        return sequence;
    }

    public void setSequence(List<ImageComponent> sequence) {
        this.sequence = sequence;
        updateSize();
        frame = 0;
    }

    public int getFrame() {
        return frame;
    }

    public int getFrameCount() {
        return sequence.size();
    }
}
