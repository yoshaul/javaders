package game.network.client;

import game.GameMenu;
import game.highscore.HighScore;
import game.network.InvalidLoginException;
import game.network.packet.InvitationPacket;
import game.network.packet.Packet;

import java.util.List;

public class NullNetworkManager implements NetworkManager {

	public NullNetworkManager(GameMenu gameMenu) {
		
	}
	
	public void sendPacket(Packet packet) {
		// TODO Auto-generated method stub

	}

	public void handlePacket(Packet packet) {
		// TODO Auto-generated method stub

	}

	public Long login(String user, String password) throws NetworkException,
			InvalidLoginException {
		// TODO Auto-generated method stub
		return null;
	}

	public void logout() throws NetworkException {
		// TODO Auto-generated method stub

	}

	public void signup(String user, String password, String email)
			throws NetworkException {
		// TODO Auto-generated method stub

	}

	public void sendInvitation(Long sessionId) throws NetworkException {
		// TODO Auto-generated method stub

	}

	public void cancelInvitation() throws NetworkException {
		// TODO Auto-generated method stub

	}

	public void acceptInvitations(boolean accept) throws NetworkException {
		// TODO Auto-generated method stub

	}

	public void sendInvitationReply(InvitationPacket originalInvitation,
			boolean accepted) throws NetworkException {
		// TODO Auto-generated method stub

	}

	public List getAvailablePlayers() throws NetworkException {
		// TODO Auto-generated method stub
		return null;
	}

	public Long getSenderId() {
		// TODO Auto-generated method stub
		return null;
	}

	public Long getReceiverId() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isInviter() {
		// TODO Auto-generated method stub
		return false;
	}

	public void postHighScore(HighScore score) throws NetworkException {
		// TODO Auto-generated method stub

	}

	public HighScore[] getTopTenScores() throws NetworkException {
		// TODO Auto-generated method stub
		return null;
	}

	public HighScore[] getHighScores(int fromRank, int toRank)
			throws NetworkException {
		// TODO Auto-generated method stub
		return null;
	}

	public GameNetworkManager getGameNetworkManager() throws NetworkException {
		// TODO Auto-generated method stub
		return null;
	}

}
