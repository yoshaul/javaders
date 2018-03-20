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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


/**
 * The <code>OKDialog</code> is used to display various messages
 * in a blocking dialog with an OK button to close the dialog.
 */
public class OKDialog extends GameDialog {

    private JLabel textLabel;

    /**
     * Construct the dialog.
     *
     * @param owner Owner frame.
     */
    public OKDialog(JFrame owner) {

        super(owner, true);

        createGUI();

    }

    /**
     * Create the dialog UI.
     */
    @Override
    protected void createGUI() {

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
     *
     * @param text Text to display
     */
    public void setText(String text) {
        textLabel.setText(text);
    }

    /**
     * Hides the dialog and clears the text.
     */
    @Override
    public void hideDialog() {
        super.hideDialog();
        textLabel.setText("");
    }

    @Override
    public void popDialog() {
        this.pack();
        super.popDialog();
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    /**
     * Clicked on ok button, hide the dialog
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        hideDialog();
    }

}
