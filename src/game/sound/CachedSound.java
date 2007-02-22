
package game.sound;

import javax.sound.sampled.AudioFormat;

/**
 * The <code>CachedSound</code> is used to cache 
 * sound file bytes.
 */
public class CachedSound {

    private byte[] samples;
    private AudioFormat audioFormat;

    /**
     * Construct a new <code>CachedSound</code>
     * @param samples	Sound samples.
     */
    public CachedSound (byte[] samples, AudioFormat audioFormat) {
        this.samples = samples;
        this.audioFormat = audioFormat;
    }


    /**
     * Returns the sound samples byte array.
     * @return The sound samples byte array.
     */
    public byte[] getSamples() {
        return this.samples;
    }
    
    /**
     * Returns the sound audio format.
     * @return The sound audio format.
     */
    public AudioFormat getAudioFormat() {
        return this.audioFormat;
    }

    
}
