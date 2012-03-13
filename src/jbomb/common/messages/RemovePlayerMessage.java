package jbomb.common.messages;

import com.jme3.network.serializing.Serializable;
import jbomb.common.game.JBombContext;
import jbomb.common.game.Player;

@Serializable
public class RemovePlayerMessage extends BasePhysicMessage {
    
    public RemovePlayerMessage() {}
    
    public RemovePlayerMessage(int id) {
        super(id);
    }

    @Override
    public void applyData() {
        Player player = (Player) JBombContext.MANAGER.removePhysicObject(getId());
        if (player != null)
            JBombContext.ROOT_NODE.detachChild(player.getGeometry());
    }
}
