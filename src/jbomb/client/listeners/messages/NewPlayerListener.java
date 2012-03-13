package jbomb.client.listeners.messages;

import com.jme3.network.Client;
import com.jme3.network.Message;
import jbomb.client.messages.task.NewPlayerTask;
import jbomb.common.messages.NewPlayerMessage;
import org.apache.log4j.Logger;

public class NewPlayerListener extends AbstractClientMessageListener<NewPlayerTask, NewPlayerMessage> {

    private static final Logger LOGGER = Logger.getLogger(NewPlayerListener.class);
    
    @Override
    public void messageReceived(Client source, Message m) {
        NewPlayerMessage message = (NewPlayerMessage) m;
        LOGGER.debug("Agregando nuevo jugador # " + message.getId() + "...");
        doTask(new NewPlayerTask(), message);
    }
    
}
