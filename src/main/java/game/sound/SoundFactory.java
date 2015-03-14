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

import game.GameConstants;
import game.util.Logger;
import game.util.ResourceManager;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The <code>SoundFactory</code> class is used to play sounds in the game.
 */
public class SoundFactory {
    private static LineListener lineListener = new SimpleLineListener();

    /**
     * Play the sound file. This method is not blocking.
     *
     * @param fileName File name with the sound (we search in the sounds folder).
     */
    public static void playSound(String fileName) {
        try {
            InputStream is = ResourceManager.getResourceAsStream(
                    GameConstants.SOUNDS_DIR + fileName);
            BufferedInputStream bis = new BufferedInputStream(is);
            AudioInputStream ais = AudioSystem.getAudioInputStream(bis);
            AudioFormat audioFormat = ais.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
            // Obtain a line 
            Clip clip = (Clip) AudioSystem.getLine(info);
            // Open the line (acquire resources)
            clip.open(ais);
            // Set the line event's listener
            clip.addLineListener(lineListener);
            // Start playing the playback
            clip.start();

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            Logger.exception(e);
        }
    }
}


