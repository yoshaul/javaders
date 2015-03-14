/*
 * This file is part of Javaders.
 * Copyright (c) Yossi Shaul
 *
 * Javaders is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Javaders is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Javaders.  If not, see <http://www.gnu.org/licenses/>.
 */

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
     *
     * @param samples Sound samples.
     */
    public CachedSound(byte[] samples, AudioFormat audioFormat) {
        this.samples = samples;
        this.audioFormat = audioFormat;
    }


    /**
     * Returns the sound samples byte array.
     *
     * @return The sound samples byte array.
     */
    public byte[] getSamples() {
        return this.samples;
    }

    /**
     * Returns the sound audio format.
     *
     * @return The sound audio format.
     */
    public AudioFormat getAudioFormat() {
        return this.audioFormat;
    }


}
