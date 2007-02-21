
package game.sound;

import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

/**
 * This class is used as sound line listener. When a stop
 * line event arrives the line is closed.
 */
public class SimpleLineListener implements LineListener {    

    public void update(LineEvent event) {
        if (event.getType() == LineEvent.Type.STOP) {
            Line line = event.getLine();
            line.close();
        }
    }
}