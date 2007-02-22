package game.network.client;

import game.gamestate.GameState;
import game.network.packet.Packet;

public class NullGameNetworkManager implements GameNetworkManager {

	public void gatherInput(GameState gameState) {
		// TODO Auto-generated method stub

	}

	public void sendPacket(Packet packet) {
		// TODO Auto-generated method stub

	}

	public void handlePacket(Packet packet) {
		// TODO Auto-generated method stub

	}

	public Packet getNextPacket() {
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

	public void cleanup() {
		// TODO Auto-generated method stub

	}

}
