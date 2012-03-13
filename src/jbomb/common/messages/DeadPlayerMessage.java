package jbomb.common.messages;

import com.jme3.network.serializing.Serializable;

@Serializable
public class DeadPlayerMessage extends BasePhysicMessage {
    
    private String message;
    
    public DeadPlayerMessage() {}
    
    public DeadPlayerMessage(long id, String message) {
        super(id);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
