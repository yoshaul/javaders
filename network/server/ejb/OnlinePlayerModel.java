package game.network.server.ejb;

import java.io.Serializable;

public class OnlinePlayerModel implements Serializable {

    private Long sessionId;
    private String userName;
    private long sessionStartTime;
    
    public OnlinePlayerModel(Long sessionId, String userName, 
            long sessionStartTime) {
        
        this.sessionId = sessionId;
        this.userName = userName;
        this.sessionStartTime = sessionStartTime;
        
    }
    
    /**
     * @return Returns the sessionId.
     */
    public Long getSessionId() {
        return sessionId;
    }
    /**
     * @param sessionId The sessionId to set.
     */
    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }
    /**
     * @return Returns the sessionStartTime.
     */
    public long getSessionStartTime() {
        return sessionStartTime;
    }
    /**
     * @param sessionStartTime The sessionStartTime to set.
     */
    public void setSessionStartTime(long sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }
    /**
     * @return Returns the userName.
     */
    public String getUserName() {
        return userName;
    }
    /**
     * @param userName The userName to set.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
