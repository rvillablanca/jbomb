package jbomb.client.listeners.messages;

import com.jme3.network.Client;
import com.jme3.network.Message;
import jbomb.client.messages.task.RemovePlayerTask;
import jbomb.common.messages.RemovePlayerMessage;
import org.apache.log4j.Logger;

public class RemovePlayerListener extends AbstractClientMessageListener<RemovePlayerTask, RemovePlayerMessage> {

    private static final Logger LOGGER = Logger.getLogger(RemovePlayerListener.class);
    
    @Override
    public void messageReceived(Client source, Message m) {
        RemovePlayerMessage message = (RemovePlayerMessage) m;
        LOGGER.debug("Eliminando jugador #" + message.getId());
        doTask(new RemovePlayerTask(), message);
    }
    
}
