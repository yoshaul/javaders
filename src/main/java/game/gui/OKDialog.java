
package game.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.*;


/**
 * The <code>OKDialog</code> is used to display various messages
 * in a blocking dialog with an OK button to close the dialog.
 */
public class OKDialog extends GameDialog {

    private JLabel textLabel;

    /**
     * Construct the dialog.
     * @param owner	Owner frame.
     */
    public OKDialog(JFrame owner) {
        
        super(owner, true);
        
        createGUI();

    }
    
    /**
     * Create the dialog UI.
     */
    public void createGUI() {
        
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        
        textLabel = createLabel("");
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(textLabel, BorderLayout.NORTH);
        
        JPanel buttonsPanel = createPanel(new GridLayout(1, 1));

        JButton okButton = createButton("OK", "", BTN_SMALL_IMAGE);
        buttonsPanel.add(okButton);
        
        contentPane.add(buttonsPanel, BorderLayout.SOUTH);
        
		this.pack();
		
		centralizeOnScreen();
    }
    
    /**
     * Sets the message this dialog displays
     * @param text	Text to display
     */
    public void setText(String text) {
        textLabel.setText(text);
    }
    
    /**
     * Hides the dialog and clears the text.
     */
    public void hideDialog() {
        super.hideDialog();
        textLabel.setText("");
    }
    
    public void popDialog() {
        this.pack();
        super.popDialog();
    }
    
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }
    
    /**
     * Clicked on ok button, hide the dialog
     */
    public void actionPerformed(ActionEvent event) {
        hideDialog();
    }
    
}
