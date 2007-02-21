package game.gui;

import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;

public abstract class GameDialog extends JDialog implements ActionListener {

    
    public GameDialog(JFrame owner, boolean modal) {
        // Set owner dialog and set modal
        super(owner, modal);
               
        createGUI();
        
    }
    
    protected abstract void createGUI();
    
}
