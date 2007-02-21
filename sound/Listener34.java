package game.sound;

import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class Listener34 implements LineListener {    


    public void update(LineEvent event) {
        
        if (event.getType() == LineEvent.Type.STOP) {
            Line line = event.getLine();
            line.close();
        }
        
    }
}