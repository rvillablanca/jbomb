package jbomb.client.messages.task;

import com.jme3.math.Vector3f;
import jbomb.client.game.ClientContext;
import jbomb.common.effects.impl.PlayerExplosion;
import jbomb.common.game.JBombContext;
import jbomb.common.game.Player;
import jbomb.common.messages.DeadPlayerMessage;
import org.apache.log4j.Logger;

public class DeadPlayerTask implements Task<DeadPlayerMessage> {

    private static final Logger LOGGER = Logger.getLogger(DeadPlayerTask.class);

    @Override
    public void doThis(DeadPlayerMessage message) {
        Vector3f location = null;
        if (message.getId() == ClientContext.PLAYER.getIdUserData()) {
            LOGGER.debug("He perdido");
            location = ClientContext.PLAYER.getGeometry().getLocalTranslation();
            JBombContext.ROOT_NODE.detachChild(ClientContext.PLAYER.getGeometry());
            ClientContext.PLAYER = null;
            ClientContext.APP.resetGame();
            ClientContext.APP.removeListeners();
            ClientContext.APP.cleanScreenBombs();
            ClientContext.APP.youAreLooser();
        } else {
            LOGGER.debug("Jugador #" + message.getId() + " perdió");
            Player player = (Player) JBombContext.MANAGER.removePhysicObject(message.getId());
            location = player.getGeometry().getLocalTranslation();
            JBombContext.ROOT_NODE.detachChild(player.getGeometry());
        }
        PlayerExplosion explosion = new PlayerExplosion();
        explosion.setLocation(location);
        explosion.start();
    }
}
