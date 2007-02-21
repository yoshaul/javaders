
package game.sound;

import game.GameConstants;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.*;

import javax.sound.sampled.*;

import sun.awt.image.ByteInterleavedRaster;

public class SoundFactory {
    
    private static LineListener listener = new Listener34();
    
    private static Clip cachedClip = initCache();
    private static AudioInputStream stream;
    
    public SoundFactory() {
        
    }
    
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
//clip.addLineListener(listener);            
            // Start playing the playback
            clip.start();
//            // Block untill all data has been played
//            clip.drain();
//            // Stop the clip
//            clip.stop();
//            // Close the line (free resources)
//            clip.close();
            
        }
        
        catch (UnsupportedAudioFileException uafe) {
            //
        }
        catch (LineUnavailableException lue) {
            //
        }
        catch (IOException ioe) {
            //
        }
        
    }
    
    
    public static void playDataSource(String fileName) {
        
        try {
            File soundFile = new File(GameConstants.SOUNDS_DIR + fileName);
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
            AudioFormat audioFormat = ais.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            
            // Open the line (aquire resources)
            line.open(audioFormat);
            // Start playing the playback

	        // get the number of bytes to read
	        int length = (int)(ais.getFrameLength() * audioFormat.getFrameSize());
	  
	        // read the entire stream
	        byte[] samples = new byte[length];
	          
	        DataInputStream is = new DataInputStream(ais);
	        is.readFully(samples);
	        
            line.start();
            
            line.write(samples, 0, length);
            
            line.drain();
            line.stop();
            line.close();
            
        }
        
        catch (UnsupportedAudioFileException uafe) {
            //
        }
        catch (LineUnavailableException lue) {
            //
        }
        catch (IOException ioe) {
            //
        }
        
    }
    
    
    public static void playCachedSound() {
        try {
        cachedClip.open(stream);
        cachedClip.start();

        } catch (Exception e){}
    }

    private static Clip initCache() {
        Clip clip = null;
        try {
        File soundFile = new File(GameConstants.SOUNDS_DIR + "laser1.wav");
        stream = AudioSystem.getAudioInputStream(soundFile);
        AudioFormat audioFormat = stream.getFormat();
        DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
        // Obtain a line 
        clip = (Clip) AudioSystem.getLine(info);
        clip.addLineListener(listener); 
        }
        catch (Exception e) {
            //
        }
        return clip;
    }
    
    public static void playAppletClip(String fileName) {
        try {
        File file = new File(GameConstants.SOUNDS_DIR + fileName);
        AudioClip clip = Applet.newAudioClip(file.toURL());
        clip.play();
        }
        catch (Exception e) {}
    }
    
}





//package sound;
//
//import java.io.*;
//import javax.sound.sampled.*;
//
///**
//    The SoundFactory encapsulates a sound that can be opened
//    from the file system and later played.
//*/
//public class SoundFactory  extends Thread {
//
//    private AudioFormat format;
//    private byte[] samples;
//    
//    public static void main(String[] args) {
//        // load a sound
//        SoundFactory sound =
//            new SoundFactory("../sounds/voice.wav");
//
//        // create the stream to play
//        InputStream stream =
//            new ByteArrayInputStream(sound.getSamples());
//
//        // play the sound
//        sound.play(stream);
//
//        // exit
//        System.exit(0);
//    }
//
//
//    /**
//        Opens a sound from a file.
//    */
//    public SoundFactory(String filename) {
//        try {
//            // open the audio input stream
//            AudioInputStream stream =
//                AudioSystem.getAudioInputStream(
//                new File(filename));
//
//            format = stream.getFormat();
//
//            // get the audio samples
//            samples = getSamples(stream);
//        }
//        catch (UnsupportedAudioFileException ex) {
//            ex.printStackTrace();
//        }
//        catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//
//public void run() {
//    
////    Mixer.Info[] info = AudioSystem.getMixerInfo();
////    for(int i = 0; i < info.length; i++) {
////        System.out.println("\n" + i + "****************");
////        System.out.println("Name: " + info[i].getName());
////        System.out.println("Description: " + info[i].getDescription());
////        System.out.println("Vendor: " + info[i].getVendor());
////        System.out.println("Version: " + info[i].getVersion());
////        System.out.println("***************************");
////    }
//    
//    this.play(new ByteArrayInputStream(this.samples));
//}
//    
//    
//    /**
//        Gets the samples of this sound as a byte array.
//    */
//    public byte[] getSamples() {
//        return samples;
//    }
//
//
//    /**
//        Gets the samples from an AudioInputStream as an array
//        of bytes.
//    */
//    private byte[] getSamples(AudioInputStream audioStream) {
//        // get the number of bytes to read
//        int length = (int)(audioStream.getFrameLength() *
//            format.getFrameSize());
//
//        // read the entire stream
//        byte[] samples = new byte[length];
//        DataInputStream is = new DataInputStream(audioStream);
//        try {
//            is.readFully(samples);
//        }
//        catch (IOException ex) {
//            ex.printStackTrace();
//        }
//
//        // return the samples
//        return samples;
//    }
//
//
//    /**
//        Plays a stream. This method blocks (doesn't return) until
//        the sound is finished playing.
//    */
//    public void play(InputStream source) {
//
//        // use a short, 100ms (1/10th sec) buffer for real-time
//        // change to the sound stream
//        int bufferSize = format.getFrameSize() *
//            Math.round(format.getSampleRate() / 10);
//        byte[] buffer = new byte[bufferSize];
//
//        // create a line to play to
//        SourceDataLine line;
//        try {
//            DataLine.Info info =
//                new DataLine.Info(SourceDataLine.class, format);
//            line = (SourceDataLine)AudioSystem.getLine(info);
//            line.open(format, bufferSize);
//        }
//        catch (LineUnavailableException ex) {
//            ex.printStackTrace();
//            return;
//        }
//
//        // start the line
//        line.start();
//
//        // copy data to the line
//        try {
//            int numBytesRead = 0;
//            while (numBytesRead != -1) {
//                numBytesRead =
//                    source.read(buffer, 0, buffer.length);
//                if (numBytesRead != -1) {
//                   line.write(buffer, 0, numBytesRead);
//                }
//            }
//        }
//        catch (IOException ex) {
//            ex.printStackTrace();
//        }
//
//        // wait until all data is played
//        line.drain();
//
//        // close the line
//        line.close();
//
//    }
//
//}
