
package zhou.kunpeng.tank;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
    /**
     * Play the sound in the given resource path.
     * If something wrong occurs, it will keep silent.
     *
     * @param path Resource path.
     */


    public static void play(String path) {
        new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            Sound.class.getResource(path));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();

    }
}
//
//import javax.sound.sampled.*;
//import java.io.IOException;
//
///**
// * Play the sound. Source: http://blog.csdn.net/al_assad/article/details/53209031
// *
// * @author Al_assad yulinying_1994@outlook.com
// * @version V1.0
// *          Description: Simple audio player without JMF (supports au, ra and wav)
// */
//public class Sound {
//    /**
//     * Play the sound in the given resource path.
//     * If something wrong occurs, it will keep silent.
//     *
//     * @param path Resource path.
//     */
//    public static void play(String path) {
//        /*
//        try {
//            AudioClip aau;
//            aau = Applet.newAudioClip(Sound.class.getResource(path));
//            aau.play();
//        } catch (Exception e) {
//            //Do nothing. Source file may not exist
//        }*/
//        new Sound(path).start(false);
//
//    }
//
//    ;
//
//
//    private String musicPath; //file resource path
//    private volatile boolean run = true;  //the music is playing or not
//    private Thread mainThread;   //music player thread
//
//    private AudioInputStream audioStream;
//    private AudioFormat audioFormat;
//    private SourceDataLine sourceDataLine;
//
//    public Sound(String musicPath) {
//        this.musicPath = musicPath;
//        prefetch();
//    }
//
//    //prepare data
//    private void prefetch() {
//        try {
//
//            audioStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(musicPath));
//
//            audioFormat = audioStream.getFormat();
//
//            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class,
//                    audioFormat, AudioSystem.NOT_SPECIFIED);
//
//            //create data line as source, using Info from the process of decoding.
//            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
//
//            sourceDataLine.open(audioFormat);
//            sourceDataLine.start();
//
//        } catch (Exception ex) {
//            //Do nothing
//        }
//
//    }
//
//    //destructor
//    protected void finalize() throws Throwable {
//        super.finalize();
//        sourceDataLine.drain();
//        sourceDataLine.close();
//        audioStream.close();
//    }
//
//    //use loop to set whether the music will be played in loop
//    private void playMusic(boolean loop) throws InterruptedException {
//        try {
//            if (loop) {
//                while (true) {
//                    playMusic();
//                }
//            } else {
//                playMusic();
//                //clear the data line and close
//                sourceDataLine.drain();
//                sourceDataLine.close();
//                audioStream.close();
//            }
//
//        } catch (Exception ex) {
//            //Do nothing
//        }
//
//
//    }
//
//    private void playMusic() {
//        try {
//            synchronized (this) {
//                run = true;
//            }
//            //get audio stream from data line, and send it to mixer
//            //data flow: AudioInputStream -> SourceDataLine;
//            audioStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(musicPath));
//            int count;
//            byte tempBuff[] = new byte[1024];
//
//            while ((count = audioStream.read(tempBuff, 0, tempBuff.length)) != -1) {
//                synchronized (this) {
//                    while (!run)
//                        wait();
//                }
//                sourceDataLine.write(tempBuff, 0, count);
//
//            }
//
//        } catch (Exception ex) {
//            //Do nothing
//        }
//
//    }
//
//
//
//    private void stopMusic() {
//        synchronized (this) {
//            run = false;
//            notifyAll();
//        }
//    }
//
//
//    private void continueMusic() {
//        synchronized (this) {
//            run = true;
//            notifyAll();
//        }
//    }
//
//
//    public void start(boolean loop) {
//        mainThread = new Thread(new Runnable() {
//            public void run() {
//                try {
//                    playMusic(loop);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        mainThread.start();
//    }
//
//    public void stop() {
//        new Thread(new Runnable() {
//            public void run() {
//                stopMusic();
//
//            }
//        }).start();
//    }
//
//    public void continues() {
//        new Thread(new Runnable() {
//            public void run() {
//                continueMusic();
//            }
//        }).start();
//    }
//
//}
