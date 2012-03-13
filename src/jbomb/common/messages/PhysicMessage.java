package jbomb.common.messages;

import com.jme3.network.Message;
import com.jme3.network.serializing.Serializable;

@Serializable
public interface PhysicMessage extends Message {

    long getId();

    void setId(long id);

    void applyData();
    
    void interpolate(float percent);
}
