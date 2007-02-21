package game.gui;

import game.Game;
import game.network.server.ejb.OnlinePlayerModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class AvailablePlayersDialog extends GameDialog {
    
    private Game game;
    
    private JButton inviteButton, refreshButton;
    private JTable playersTable;
    private DefaultTableModel tableModel;
    
    private java.util.List availablePlayers;
    
    public AvailablePlayersDialog(Game game) {
        
        super(game, true);
        
        this.game = game;
        
        this.setTitle("Online Players");
        
    }
    
    protected void createGUI() {
		
        Container contentPane = this.getContentPane();

		String[] columnsNames = 
		    new String[]{"Player Name", "Session Start Time"};
		tableModel = new MyTableModel(columnsNames, 0);
		
		playersTable = new JTable(tableModel);

		playersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		/** XXX: has no affect? */
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
		
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));
        
        inviteButton = new JButton("Invite");
        inviteButton.addActionListener(this);
        buttonsPanel.add(inviteButton);
        
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(this);
        buttonsPanel.add(refreshButton);

		contentPane.add(buttonsPanel, BorderLayout.SOUTH);
		
		
		this.pack();
		
        // Center the dialog on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dialogSize = this.getSize();
        this.setLocation(
                Math.max(0,(screenSize.width - dialogSize.width) / 2), 
                Math.max(0,(screenSize.height - dialogSize.height) / 2));
		
    }
    
    public void refresh() {
        // Clear previous data
        tableModel.setRowCount(0);
        
        availablePlayers = 
            game.getNetworkManager().getAvailablePlayers();
        
        Iterator itr = availablePlayers.iterator();
        while (itr.hasNext()) {
            OnlinePlayerModel playerModel = (OnlinePlayerModel) itr.next();

            Date startDate = new Date(playerModel.getSessionStartTime());
            
            tableModel.addRow(new String[]{playerModel.getUserName(),
                    startDate.toString()});
        }
        
    }
    
    private void invitePlayer() {
        
        int selectedRow = playersTable.getSelectedRow();
        if (selectedRow > -1) {
            OnlinePlayerModel selectedPlayer = (OnlinePlayerModel)
            	availablePlayers.get(selectedRow);
            
            Long sessionId = selectedPlayer.getSessionId();
            System.out.println("sending invitation to " + sessionId);
            game.getNetworkManager().sendInvitation(sessionId);
            
        }
        
    }
	
	public void actionPerformed(ActionEvent event) {
	    
	    if (event.getSource() == inviteButton) {
	        invitePlayer();
	    } else if (event.getSource() == refreshButton) {
	        refresh();
	    }
	    
	}
	
	private class MyTableModel extends DefaultTableModel {
	    
	    public MyTableModel(String[] columnNames, int rowCount) {
	        super(columnNames, rowCount);
	    }
	    
	    public boolean isCellEditable(int row, int col) {
	        return false;
	    }
	}
}
