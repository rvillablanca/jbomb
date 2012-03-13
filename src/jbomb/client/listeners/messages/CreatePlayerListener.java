    package jbomb.client.listeners.messages;

import com.jme3.network.Client;
import com.jme3.network.Message;
import jbomb.client.messages.task.CreatePlayerTask;
import jbomb.common.messages.CreatePlayerMessage;
import org.apache.log4j.Logger;

public class CreatePlayerListener extends AbstractClientMessageListener<CreatePlayerTask, CreatePlayerMessage> {

    private static final Logger LOGGER = Logger.getLogger(CreatePlayerListener.class);
    @Override
    public void messageReceived(Client source, Message m) {
        CreatePlayerMessage message = (CreatePlayerMessage) m;
        LOGGER.debug("Creando jugador #" + message.getId() + "...");
        doTask(new CreatePlayerTask(), message);
    }
}
