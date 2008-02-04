package game.gui;

import game.GameConstants;
import game.ScreenManager;
import game.graphic.GraphicsHelper;
import game.util.ResourceManager;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;

/**
 * The <code>InGameDialog</code> is used as the base class for all
 * the in game dialogs (dialogs when the users plays the game).
 * This class extends <code>JPanel</code> and uses a customized look -
 * image as background, special buttons, border, etc.
 */
public abstract class InGameDialog extends JPanel implements ActionListener {

    public static final String DEFAULT_BG_IMAGE = "indialogbg.jpg";
    public static final String DEFAULT_BTN_IMAGE = "indialogbtn1.png";
    
    protected ScreenManager screenManager;
    protected Image bgImage;

    /**
     * Construct a new dialog.
     * @param screenManager	A screen manager
     * @param imageName		Name of the background image for the dialog.
     */
    public InGameDialog(ScreenManager screenManager, String imageName) {
        this.screenManager = screenManager;
        
        if (imageName != null && !imageName.equals("")) {
            bgImage = ResourceManager.loadImage(
                    GameConstants.IMAGES_DIR + imageName);            
        }
        
        initDialog();
    }
    
    /**
     * Init the dialog properties.
     */
    protected void initDialog() {

      this.setLayout(new BorderLayout());
	  this.setVisible(false);
      this.setOpaque(false);
      this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
      
    }
    
    /**
     * Creates and returns a customized button.
     * @param buttonText	Text to display on the button
     * @param imageName		Name of the button image.
     * @return	Customized button.
     */
    protected JButton createButton(String buttonText, String imageName) {
        JButton button = new JButton();
        
        // Load the image for the button
		Image srcImage = ResourceManager.loadImage(
		        GameConstants.IMAGES_DIR + imageName);
		
		int width = srcImage.getWidth(null);
		int height = srcImage.getHeight(null);
		
		// Get a compatible translucent image
		Image image = screenManager.getCompatibleImage(
		        width, height, Transparency.TRANSLUCENT);
		
        // Draw the source image and the button text 
		// on the tranlucent image with alpha composite of 0.5
		Graphics2D g = (Graphics2D)image.getGraphics();
        Composite alpha = AlphaComposite.getInstance(
            AlphaComposite.SRC_OVER, 0.5f);
        g.setComposite(alpha);
        g.drawImage(srcImage, 0, 0, null);
        g.setFont(ResourceManager.getFont(16));
        GraphicsHelper.drawInMiddle(g, image, buttonText);
        g.dispose();
        
        // Create an image icon for the default button image
        ImageIcon iconDefault = new ImageIcon(image);
        
        // Create a pressed image
        image = screenManager.getCompatibleImage(
		        width, height, Transparency.TRANSLUCENT);
        g = (Graphics2D)image.getGraphics();
        alpha = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 0.7f);
            g.setComposite(alpha);
        g.drawImage(srcImage, 2, 2, null);
        g.setFont(ResourceManager.getFont(16));
        GraphicsHelper.drawInMiddle(g, image, buttonText);
        g.dispose();
        ImageIcon iconPressed = new ImageIcon(image);
		
        button.setOpaque(false);
        button.setFocusPainted(false);
        button.setFocusable(false);
        button.setContentAreaFilled(false);
      	button.setBorderPainted(false);
      	button.setMargin(new Insets(0, 0, 0, 0));
        button.setIcon(iconDefault);
        button.setPressedIcon(iconPressed);
        button.setSize(button.getPreferredSize());
        button.addActionListener(this);
        
        return button;
    }
    
    /**
     * Center the dialog on the game frame.
     */
    protected void centralizeOnScreen() {
        Dimension screenSize = 
            screenManager.getScreenDimension();
        Dimension dialogSize = this.getSize();
        this.setLocation(
                Math.max(0,(screenSize.width - dialogSize.width) / 2), 
                Math.max(0,(screenSize.height - dialogSize.height) / 2));
    }

    /**
     * Paint the dialog and the background image.
     */
    public void paint(Graphics g) {
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);            
        }
        super.paint(g);
    }
    
}
