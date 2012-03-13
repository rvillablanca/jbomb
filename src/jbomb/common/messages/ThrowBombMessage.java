package jbomb.common.messages;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

@Serializable
public class ThrowBombMessage extends BasePhysicMessage {
    
    private long idClient = 0;
    private Vector3f location = new Vector3f();
    private byte timeExplosion = 0;
    
    public ThrowBombMessage() {}
    
    public ThrowBombMessage(long idClient, byte timeExplosion) {
        this.timeExplosion = timeExplosion;
        this.idClient = idClient;
    }
    
    public ThrowBombMessage(long id, long idClient, Vector3f location, byte timeExplosion) {
        super(id);
        this.idClient = idClient;
        this.location = location;
        this.timeExplosion = timeExplosion;
    }

    @Override
    public void applyData() {}

    public long getIdClient() {
        return idClient;
    }

    public Vector3f getLocation() {
        return location;
    }

    public byte getTimeExplosion() {
        return timeExplosion;
    }
}
