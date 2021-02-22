import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class LoopPlayer {

    Clip clips;
    AudioInputStream inputStream;

    public LoopPlayer(String soundname){
        try {
            clips = AudioSystem.getClip();
            inputStream = AudioSystem.getAudioInputStream(
                    Main.class.getResourceAsStream("resources/sound/" + soundname));
            clips.open(inputStream);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void start(){
        try {
            clips.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    public void stop(){
        try {
            clips.stop();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
