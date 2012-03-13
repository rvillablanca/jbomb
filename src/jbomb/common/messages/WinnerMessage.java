package jbomb.common.messages;

import com.jme3.network.serializing.Serializable;

@Serializable
public class WinnerMessage extends BasePhysicMessage {
    
    public WinnerMessage() {}
    
    public WinnerMessage(long id) {
        super(id);
    }
}
