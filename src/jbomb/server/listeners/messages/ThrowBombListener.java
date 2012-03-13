package jbomb.server.listeners.messages;

import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import jbomb.common.game.JBombContext;
import jbomb.common.game.Player;
import jbomb.common.messages.ThrowBombMessage;
import jbomb.server.controls.BaseBombControl;
import jbomb.server.game.ServerContext;
//import org.apache.log4j.Logger;

public class ThrowBombListener implements MessageListener<HostedConnection> {

//    private static final Logger LOGGER = Logger.getLogger(ThrowBombListener.class);
    @Override
    public void messageReceived(HostedConnection source, Message m) {
//        LOGGER.debug("ThrowBombMessage recibido");
        ThrowBombMessage tbm = (ThrowBombMessage) m;
        byte timeExplosion = tbm.getTimeExplosion();
        long idClient = tbm.getIdClient();
        long idPhysicObject = JBombContext.MANAGER.getRepository().nextFree();
//        LOGGER.debug("Id de nueva bomba: " + idPhysicObject);
        Player player = (Player) JBombContext.MANAGER.getPhysicObject(idClient);
        Vector3f bombLocation = player.getGeometry().getLocalTranslation().add(player.getViewDirection().normalize().mult(1.2f));
        ServerContext.SERVER.broadcast(new ThrowBombMessage(idPhysicObject, idClient, 
                bombLocation, timeExplosion));
        player.throwBomb(bombLocation, idPhysicObject, new BaseBombControl(), timeExplosion, false);
    }
}
