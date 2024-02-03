import javax.sound.sampled.*;
import java.io.File;

public class Sounds{

    static String hintergrundmusikFile = "Sounds/hintergrundmusik.wav";
    static Clip hintergrundmusik;

    public static void play(String filename)
    {
        try
        {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(filename)));
            clip.start();
        }
        catch (Exception exc)
        {
            exc.printStackTrace(System.out);
        }
    }

    public static void hintergrundmusik(){
        try {
            hintergrundmusik = AudioSystem.getClip();
            hintergrundmusik.open(AudioSystem.getAudioInputStream(new File(hintergrundmusikFile)));
            hintergrundmusik.loop(hintergrundmusik.LOOP_CONTINUOUSLY);
        }
        catch (Exception exc){
            exc.printStackTrace(System.out);
        }

    }

    public static void stopHintergrundmusik(){
        hintergrundmusik.stop();
    }

}
