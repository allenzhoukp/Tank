package zhou.kunpeng.tank;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JA on 2017/5/19.
 * <p> Controls the Clips' animation in a certain dps. </p>
 * <p> Clips should be appended to its clipList to animate. </p>
 */
public class ClipManager {
    private int fps;

    private List<Clip> clipList;

    private class ClipAnimator extends Thread {
        private boolean running = true;

        @Override
        public void run() {
            while (running) {
                try {
                    sleep(1000 / fps);
                } catch (InterruptedException e) {
                    //Do nothing. Will there be any problems?
                }
                for (Clip c : clipList)
                    c.nextFrame();
            }
        }

        public void stopAnimate() {
            running = false;
        }

    }

    private ClipAnimator clipAnimator = new ClipAnimator();

    public ClipManager(int fps, List<Clip> clipList) {
        this.fps = fps;
        this.clipList = clipList;
    }

    public ClipManager() {
        this(24, new ArrayList<>());
    }

    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public List<Clip> getClipList() {
        return clipList;
    }

    public void setClipList(List<Clip> clipList) {
        this.clipList = clipList;
    }

    public void start() {
        clipAnimator.start();
    }

    public void stop() {
        clipAnimator.stopAnimate();
    }
}
