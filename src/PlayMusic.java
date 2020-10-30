import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

/**
 * This class is used to read and play .wav format music files for music and sound effects.
 */
abstract class PlayMusic {

    private static Clip clip;
    private static Clip soundClip;
    public static FloatControl gainControl;

    static{
        try{
            soundClip = AudioSystem.getClip();
            clip = AudioSystem.getClip();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * The only method needed just opens up a given file and plays it beginning to end.  If it is the background music,
     * loops a lot of times.  (Can change later if necessary.)
     * @param file - .wav file to be played
     */
    public static void playSound(Game game,File file) {
        try {
            // Gets the audio file and opens it
            if(soundClip.isRunning()){
                soundClip.stop();
            }
            soundClip = AudioSystem.getClip();
            soundClip.open(AudioSystem.getAudioInputStream(file));
            gainControl = (FloatControl)soundClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(game.musicVolume-8);
            gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(game.musicVolume-8);
            if(game.musicVolume>-25){
                soundClip.start();
            }

        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void playBackground(Game game,File file){
        try {
            // Gets the audio file and opens it
            if(clip.isRunning()){
                clip.stop();
            }
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(game.musicVolume-8);
            if(game.musicVolume>-25){
                clip.loop(1000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}