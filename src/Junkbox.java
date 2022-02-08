import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Junkbox {

    Boolean mute = false;

    AudioInputStream AudioSource;
    Clip clip = AudioSystem.getClip();

    public Junkbox() throws UnsupportedAudioFileException, IOException, LineUnavailableException{

    }

    public void PlaySound(File in) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        AudioSource = AudioSystem.getAudioInputStream(in);
        clip = AudioSystem.getClip();
        clip.open(AudioSource);
        if(!mute){
            if(!clip.isRunning()){
                clip.setFramePosition(0);
                clip.start();
            }
        }
        FloatControl audioGain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        audioGain.setValue(-1.0f);
    }
    
}
