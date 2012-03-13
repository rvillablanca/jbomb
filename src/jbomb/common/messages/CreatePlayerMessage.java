package jbomb.common.messages;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

@Serializable
public class CreatePlayerMessage extends BasePhysicMessage {

    private Vector3f location = new Vector3f();
    private ColorRGBA color = ColorRGBA.White;
    private boolean newPlayer = true;

    public CreatePlayerMessage() {}

    public CreatePlayerMessage(Vector3f location, ColorRGBA color, long id) {
        super(id);
        this.color = color;
        this.location.set(location);
    }

    public Vector3f getLocation() {
        return location;
    }

    public void setLocation(Vector3f location) {
        this.location = location;
    }

    public ColorRGBA getColor() {
        return color;
    }

    public void setColor(ColorRGBA color) {
        this.color = color;
    }

    @Override
    public void applyData() {
        
    }

    public boolean isNewPlayer() {
        return newPlayer;
    }

    public void setNewPlayer(boolean newPlayer) {
        this.newPlayer = newPlayer;
    }
}
