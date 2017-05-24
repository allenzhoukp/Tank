package zhou.kunpeng.tank;

import java.applet.Applet;
import java.applet.AudioClip;

/**
 * Created by JA on 2017/5/24.
 * <p>
 * Sound class is for sound play.
 * </p>
 */
public class Sound {
    /**
     * Play the sound in the given resource path.
     * If something wrong occurs, it will keep silent.
     * @param path Resource path.
     */
    public static void play(String path) {
        try {
            AudioClip aau;
            aau = Applet.newAudioClip(Sound.class.getResource(path));
            aau.play();
        } catch (Exception e) {
            //Do nothing. Source file may not exist
        }

    }
}
