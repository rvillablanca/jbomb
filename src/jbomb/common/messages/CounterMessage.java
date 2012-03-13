package jbomb.common.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

@Serializable
public class CounterMessage extends AbstractMessage {
    
    private byte num;
    
    public CounterMessage() {}
    
    public CounterMessage(byte num) {
        this.num = num;
    }

    public byte getNum() {
        return num;
    }
}
