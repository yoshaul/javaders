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

import game.network.client.NetworkException;
import game.network.client.NetworkManager;
import game.network.packet.InvitationPacket;
import game.util.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The <code>InvitationDialog</code> is displayed when an
 * invitation to play arrives from network player.
 * The user can accept or reject the invitation.
 */
public class InvitationDialog extends GameDialog {

    private JButton okButton, cancelButton;
    private JLabel text;
    private NetworkManager networkManager;
    private InvitationPacket invitationPacket;

    /**
     * Construct the dialog.
     *
     * @param owner          Owner frame.
     * @param modal          True if this is modal dialog.
     * @param networkManager Network manager.
     */
    public InvitationDialog(JFrame owner, boolean modal,
                            NetworkManager networkManager) {

        super(owner, modal);
        this.networkManager = networkManager;

        this.addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        // Cancell the invitation if one exists
                        sendReply(false);
                    }
                }
        );

    }

    /**
     * Create the dialog UI.
     */
    @Override
    public void createGUI() {

        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());

        text = createLabel("");
        text.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(text, BorderLayout.NORTH);

        JPanel buttonsPanel = createPanel(new GridLayout(1, 2));

        okButton = createButton("OK", "", BTN_SMALL_IMAGE);
        buttonsPanel.add(okButton);

        cancelButton = createButton("Cancel", "", BTN_SMALL_IMAGE);
        buttonsPanel.add(cancelButton);

        contentPane.add(buttonsPanel, BorderLayout.SOUTH);

        this.setSize(300, 150);

        centralizeOnScreen();

    }

    /**
     * Invitation arrived from an online player.
     *
     * @param invitationPacket Packet with the invitation details.
     */
    public void invitationArrived(InvitationPacket invitationPacket) {

        this.invitationPacket = invitationPacket;
        text.setText("<html>Invitation yo play arrived from " +
                invitationPacket.userName + ".<br>Do you want to accept?" +
                "</html>");
        super.popDialog();

    }

    /**
     * Previously arrived invitation was cancelled.
     * Display a message.
     */
    public void invitationCancelled() {
        text.setText("Invitation cancelled");
        invitationPacket = null;
        this.validate();
        super.popDialog();
    }

    /**
     * Send reply to an invitation.
     *
     * @param accepted True if user accepts the invitation. False
     *                 if the user rejected it or closed the dialog.
     */
    public void sendReply(boolean accepted) {
        if (invitationPacket != null) {
            try {
                networkManager.sendInvitationReply(
                        invitationPacket, accepted);
            } catch (NetworkException ne) {
                Logger.exception(ne);
                Logger.showErrorDialog(this, "Unable to send " +
                        "invitation reply: " + ne.getMessage());
            }
        }
        hideDialog();
    }

    /**
     * Hides the dialog.
     */
    @Override
    public void hideDialog() {
        text.setText("");
        super.hideDialog();
    }

    /**
     * Respond to user input.
     */
    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == okButton) {
            sendReply(true);
        } else if (event.getSource() == cancelButton) {
            sendReply(false);
        }

    }

}
