package jbomb.client.listeners.messages;

import com.jme3.network.Client;
import com.jme3.network.Message;
import jbomb.client.messages.task.StartingNewGameTask;
import jbomb.common.messages.StartingNewGameMessage;
import org.apache.log4j.Logger;

public class StartingNewGameListener extends AbstractClientMessageListener<StartingNewGameTask, StartingNewGameMessage> {

    private static final Logger LOGGER = Logger.getLogger(StartingNewGameListener.class);
    
    @Override
    public void messageReceived(Client source, Message m) {
        StartingNewGameMessage message = (StartingNewGameMessage) m;
        LOGGER.debug("StartingNewGameMessage recibido...");
        LOGGER.debug("Ganador: " + message.isWinner());
        doTask(new StartingNewGameTask(), message);
    }
}
