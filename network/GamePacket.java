package game.network;

import java.io.Serializable;

public class GamePacket implements Serializable {

    private int invokerID;
    private int actionCode;
    
    public GamePacket(int invokerID, int actionCode) {
        this.invokerID = invokerID;
        this.actionCode = actionCode;
    }
    
    public int getInvokerID() {
        return invokerID;
    }
    
    public int getActionCode() {
        return actionCode;
    }
    
}
