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

package game.gui;

import game.GameConstants;
import game.graphic.GraphicsHelper;
import game.util.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The <code>GameDialog</code> class extends <code>JDialog</code> and
 * serves as the base class for all the game menu dialogs (
 * the dialogs before starting a game).
 *
 * @see javax.swing.JDialog
 */
public abstract class GameDialog extends JDialog
        implements ActionListener {

    public static final String BTN_BIG_IMAGE = "button_green.png";
    public static final String BTN_SMALL_IMAGE = "btn_small_green.png";

    /**
     * Constructs a ew <code>GameDialog</code>
     *
     * @param owner Owner frame.
     * @param modal True if it's a modal dialog.
     */
    public GameDialog(JFrame owner, boolean modal) {
        // Set owner dialog and set modal
        super(owner, modal);
        getContentPane().setBackground(Color.BLACK);
        setUndecorated(true);
        createGUI();
    }

    /**
     * This method is called from the constructor. In this method
     * the dialog should build its UI.
     */
    protected abstract void createGUI();

    /**
     * Centers the dialog on the screen. This method should be
     * called only after setting the size of the dialog.
     */
    protected void centralizeOnScreen() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dialogSize = this.getSize();
        this.setLocation(
                Math.max(0, (screenSize.width - dialogSize.width) / 2),
                Math.max(0, (screenSize.height - dialogSize.height) / 2));
    }

    /**
     * Show the dialog on the screen.
     */
    public void popDialog() {
        this.setVisible(true);
    }

    /**
     * Hides the dialog.
     */
    public void hideDialog() {
        this.setVisible(false);
    }

    /**
     * Creates and returns a customized button. Adds the dialog
     * as the button action listener.
     *
     * @param buttonText  Text to display on the button
     * @param toolTipText Button tooltip text
     * @param buttonImage Name of the button image.
     * @return Customized button.
     */
    public JButton createButton(String buttonText,
                                String toolTipText, String buttonImage) {
        JButton button = new JButton();
        button.setToolTipText(toolTipText);

        customizeButton(button, buttonText, buttonImage);

        button.addActionListener(this);

        return button;
    }

    /**
     * Customize the look and text of the button.
     *
     * @param button      JButton to customize
     * @param buttonText  Text for the button
     * @param buttonImage Image for the button
     */
    protected void customizeButton(JButton button, String buttonText,
                                   String buttonImage) {

        // Load the base image for the button
        Image srcImage = ResourceManager.loadImage(
                GameConstants.IMAGES_DIR + buttonImage);

        int width = srcImage.getWidth(null);
        int height = srcImage.getHeight(null);

        Font menuFont = ResourceManager.getFont(16);

        // Get a compatible translucent image
        Image image = GraphicsHelper.getCompatibleImage(this,
                width, height, Transparency.TRANSLUCENT);
        // Draw the source image and the button text 
        // on the tranlucent image with alpha composite of 0.8
        Graphics2D g = (Graphics2D) image.getGraphics();
        Composite alpha = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 0.8f);
        g.setComposite(alpha);
        g.drawImage(srcImage, 0, 0, null);
        g.setFont(menuFont);
        GraphicsHelper.drawInMiddle(g, image, buttonText);
        g.dispose();

        // Create an image icon for the default button image
        ImageIcon iconDefault = new ImageIcon(image);

        // Create a pressed image
        image = GraphicsHelper.getCompatibleImage(this,
                width, height, Transparency.TRANSLUCENT);
        g = (Graphics2D) image.getGraphics();
        alpha = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 0.9f);
        g.setComposite(alpha);
        // a bit lowered and to the right button
        g.drawImage(srcImage, 2, 2, null);
        g.setFont(menuFont);
        GraphicsHelper.drawInMiddle(g, image, buttonText);
        g.dispose();
        ImageIcon iconPressed = new ImageIcon(image);

        // Create disabled button image
        image = GraphicsHelper.getCompatibleImage(this,
                width, height, Transparency.TRANSLUCENT);
        g = (Graphics2D) image.getGraphics();
        alpha = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 0.3f);
        g.setComposite(alpha);
        g.drawImage(srcImage, 0, 0, null);
        g.setFont(menuFont);
        GraphicsHelper.drawInMiddle(g, image, buttonText);
        g.dispose();
        ImageIcon iconDisabled = new ImageIcon(image);

        button.setOpaque(false);
        button.setFocusPainted(false);
        button.setFocusable(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setIcon(iconDefault);
        button.setPressedIcon(iconPressed);
        button.setDisabledIcon(iconDisabled);
        button.setSize(button.getPreferredSize());

    }

    /**
     * Returns a <code>JPanel</code> with black background.
     *
     * @param lm Layout manager for the panel.
     */
    protected JPanel createPanel(LayoutManager lm) {
        JPanel panel = new JPanel(lm);
        panel.setBackground(Color.BLACK);
        return panel;
    }

    /**
     * Returns a <code>JLabel</code> with white game font.
     *
     * @param text Text for the label
     */
    protected JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(ResourceManager.getFont(12));
        return label;
    }

    /**
     * Returns <code>JTextField</code> with black background
     * and white foreground.
     */
    protected JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(Color.BLACK);
        textField.setForeground(Color.WHITE);
        return textField;
    }

    /**
     * Returns <code>JPasswordField</code> with black background
     * and white foreground.
     */
    protected JPasswordField createPasswordField() {
        JPasswordField passField = new JPasswordField();
        passField.setBackground(Color.BLACK);
        passField.setForeground(Color.WHITE);
        return passField;
    }

}
