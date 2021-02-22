import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundPlayer {
    public static synchronized void playAsync(final String name) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Clip clips = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            Main.class.getResourceAsStream("resources/sound/" + name));
                    clips.open(inputStream);
                    clips.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    public static void play(final String name) {
        try {
            Clip clips = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                    Main.class.getResourceAsStream("resources/sound/" + name));
            clips.open(inputStream);
            clips.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void startSiren(){
        try {
            Clip clips = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                    Main.class.getResourceAsStream("resources/sound/siren.wav"));
            clips.open(inputStream);
            clips.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


}