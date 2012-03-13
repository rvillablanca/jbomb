package jbomb.common.messages;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import jbomb.common.game.JBombContext;
import jbomb.common.game.Player;

@Serializable
public class NewPlayerMessage extends CreatePlayerMessage {
    
    public NewPlayerMessage() {}
    
    public NewPlayerMessage(Vector3f location, ColorRGBA color, long id) {
        super(location, color, id);
    }

    @Override
    public void applyData() {
        ColorRGBA color = getColor();
        Vector3f location = getLocation();
        Player player = new Player(location, color);
        player.setIdUserData((int) getId());
        JBombContext.ROOT_NODE.attachChild(player.getGeometry());
        JBombContext.MANAGER.addPhysicObject(getId(), player);
    }
    
    
}
