
package game.sound;

import game.GameConstants;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.*;

import javax.sound.sampled.*;

/**
 * The <code>SoundFactory</code> class is used to play sounds
 * in the game.
 */
public class SoundFactory {
    
    private static LineListener lineListener = new SimpleLineListener();
    
    /**
     * Play the sound file. This method is not blocking.
     * @param fileName	File name with the sound (we serach in the
     * sounds folder).
     */
    public static void playSound(String fileName) {

        try {
            
            File soundFile = new File(GameConstants.SOUNDS_DIR + fileName);
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
            AudioFormat audioFormat = ais.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
            // Obtain a line 
            Clip clip = (Clip) AudioSystem.getLine(info);
            // Open the line (aquire resources)
            clip.open(ais);
            // Set the line event's listener
            clip.addLineListener(lineListener);
            // Start playing the playback
            clip.start();
            
        }
        
        catch (UnsupportedAudioFileException uafe) {
            uafe.printStackTrace();
        }
        catch (LineUnavailableException lue) {
            lue.printStackTrace();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        
    }
    
    /**
     * Play an audio clip in the "Applet" way (i.e., using the 
     * <code>Applet</code> class). This method blocks so we don't
     * use it in the game.
     * @param fileName	Name of the audio clip file.
     */
    public static void playAppletClip(String fileName) {
        try {
	        File file = new File(GameConstants.SOUNDS_DIR + fileName);
	        AudioClip clip = Applet.newAudioClip(file.toURL());
	        clip.play();
        }
        catch (Exception e) {}
    }

    
}


