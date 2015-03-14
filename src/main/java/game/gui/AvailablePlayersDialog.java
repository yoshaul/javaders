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

import game.GameMenu;
import game.network.client.NetworkException;
import game.network.server.ejb.OnlinePlayerModel;
import game.util.Logger;
import game.util.ResourceManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.List;

/**
 * The <code>AvailablePlayersDialog</code> is a dialog which displays
 * the available online players and enables the player to invite
 * available player from the list.
 */
public class AvailablePlayersDialog extends GameDialog {

    private GameMenu gameMenu;

    private JButton inviteButton, refreshButton, closeButton;
    private JLabel statusLabel;
    private JTable playersTable;
    private DefaultTableModel tableModel;

    private List<OnlinePlayerModel> availablePlayers;
    private boolean invitationSent;

    /**
     * Construct the dialog.
     *
     * @param game Reference to the game menu.
     */
    public AvailablePlayersDialog(GameMenu game) {

        super(game, true);

        this.gameMenu = game;

        this.setTitle("Online Players");

    }

    /**
     * Create the dialog UI.
     */
    protected void createGUI() {

        Container contentPane = this.getContentPane();

        String[] columnsNames =
                new String[]{"Player Name", "Session Start Time"};
        tableModel = new MyTableModel(columnsNames, 0);

        playersTable = new JTable(tableModel);

        playersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        playersTable.setBackground(Color.BLACK);
        playersTable.setForeground(Color.WHITE);
        playersTable.setFont(ResourceManager.getFont(14));
        playersTable.getTableHeader().setBackground(Color.BLACK);
        playersTable.getTableHeader().setForeground(Color.WHITE);
        playersTable.getTableHeader().setFont(ResourceManager.getFont(16));

        // Resize the table columns
        TableCellRenderer headerRenderer =
                playersTable.getTableHeader().getDefaultRenderer();

        for (int i = 0; i < columnsNames.length; i++) {
            TableColumn column = playersTable.getColumnModel().getColumn(i);

            Component comp = headerRenderer.getTableCellRendererComponent(
                    null, column.getHeaderValue(), false, false, 0, 0);

            int headerWidth = comp.getPreferredSize().width;

            comp = playersTable.getDefaultRenderer(tableModel.getColumnClass(i)).
                    getTableCellRendererComponent(
                            playersTable, columnsNames[i],
                            false, false, 0, i);
            int cellWidth = comp.getPreferredSize().width;

            column.setPreferredWidth(Math.max(headerWidth, cellWidth));
        }

        contentPane.add(new JScrollPane(playersTable), BorderLayout.CENTER);

        JPanel buttonsPanel = createPanel(new GridLayout(1, 3));

        inviteButton = createButton("Invite",
                "Invite selected player to play", BTN_SMALL_IMAGE);
        buttonsPanel.add(inviteButton);

        refreshButton = createButton("Refresh",
                "Refresh the players list", BTN_SMALL_IMAGE);
        buttonsPanel.add(refreshButton);

        closeButton = createButton("Close",
                "Close the dialog", BTN_SMALL_IMAGE);
        buttonsPanel.add(closeButton);

        JPanel statusPanel = createPanel(new FlowLayout());
        statusLabel = createLabel("Select player to play with");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusLabel.setForeground(new Color(50, 98, 200));
        statusPanel.add(statusLabel);

        JPanel southPanel = createPanel(new GridLayout(2, 1));
        southPanel.add(buttonsPanel);
        southPanel.add(statusPanel);

        contentPane.add(southPanel, BorderLayout.SOUTH);

        this.setSize(400, 300);

        centralizeOnScreen();

    }

    /**
     * Refresh the online players list and show the dialog.
     */
    public void popDialog() {
        this.refresh();
        super.popDialog();
    }

    /**
     * Hide the dialog.
     */
    public void hideDialog() {
        this.setVisible(false);
    }

    /**
     * Obtains and updates the list of online players.
     */
    private void refresh() {
        try {
            // Clear previous data
            tableModel.setRowCount(0);

            // Get the available players from the server
            availablePlayers =
                    gameMenu.getNetworkManager().getAvailablePlayers();

            // Add the players to the table
            for (OnlinePlayerModel playerModel : availablePlayers) {
                Date startDate = new Date(playerModel.getSessionStartTime());

                tableModel.addRow(new String[]{playerModel.getUserName(),
                        startDate.toString()});
            }
        } catch (NetworkException ne) {
            Logger.exception(ne);
            Logger.showErrorDialog(this, ne.getMessage());
        }

    }

    /**
     * Invite the selected player for online game.
     */
    private void invitePlayer() {
        try {
            // Get the selected row
            int selectedRow = playersTable.getSelectedRow();
            if (selectedRow > -1) {
                // Get the player's session id and send invitation
                OnlinePlayerModel selectedPlayer = (OnlinePlayerModel)
                        availablePlayers.get(selectedRow);

                Long sessionId = selectedPlayer.getSessionId();
                gameMenu.getNetworkManager().sendInvitation(sessionId);
                gameMenu.getNetworkManager().acceptInvitations(false);

                setStatusText("Invitation " +
                        "sent to " + selectedPlayer.getUserName() +
                        ". Waiting for reply...");

                invitationSent = true;
                // Change the invite button to cancel button
                customizeButton(inviteButton, "Cancel", BTN_SMALL_IMAGE);
            }
        } catch (NetworkException ne) {
            Logger.exception(ne);
            Logger.showErrorDialog(this, ne.getMessage());
        }
    }

    /**
     * Cancel a previously sent invitation by changing the status
     * and sending cancellation packet.
     */
    public void cancelInvitation() {
        try {
            gameMenu.getNetworkManager().cancelInvitation();
            reset();
        } catch (NetworkException ne) {
            Logger.exception(ne);
            Logger.showErrorDialog(this, ne.getMessage());
        }
    }

    /**
     * Reset the invitation sent status and button.
     */
    public void reset() {
        invitationSent = false;
        customizeButton(inviteButton, "Invite", BTN_SMALL_IMAGE);
    }

    /**
     * Sets the status of the invitation.
     *
     * @param text Status text.
     */
    public void setStatusText(String text) {
        statusLabel.setText(text);
    }

    /**
     * React to the user input.
     */
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == inviteButton) {
            if (invitationSent) {
                cancelInvitation();
            } else {
                invitePlayer();
            }
        } else if (event.getSource() == refreshButton) {
            refresh();
        } else if (event.getSource() == closeButton) {
            hideDialog();
        }

    }

    /**
     * This inner class is used only to set the editable state
     * of the players to false (the default is true).
     */
    private class MyTableModel extends DefaultTableModel {

        public MyTableModel(String[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        public boolean isCellEditable(int row, int col) {
            return false;
        }
    }
}
