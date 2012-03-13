package jbomb.client.listeners.messages;

import com.jme3.network.Client;
import com.jme3.network.Message;
import jbomb.client.messages.task.DeadPlayerTask;
import jbomb.common.messages.DeadPlayerMessage;
import org.apache.log4j.Logger;

public class DeadPlayerListener extends AbstractClientMessageListener<DeadPlayerTask, DeadPlayerMessage> {

    private static final Logger LOGGER = Logger.getLogger(DeadPlayerListener.class);
    
    @Override
    public void messageReceived(Client source, Message m) {
        LOGGER.debug("DeadPlayerMessage recibido");
        doTask(new DeadPlayerTask(), (DeadPlayerMessage) m);
    }
}
