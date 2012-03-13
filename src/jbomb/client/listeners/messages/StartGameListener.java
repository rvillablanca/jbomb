package jbomb.client.listeners.messages;

import com.jme3.network.Client;
import com.jme3.network.Message;
import jbomb.client.messages.task.StartGameTask;
import jbomb.common.messages.StartGameMessage;
//import org.apache.log4j.Logger;

public class StartGameListener extends AbstractClientMessageListener<StartGameTask, StartGameMessage> {

//    private static final Logger LOGGER = Logger.getLogger(StartGameListener.class);
    
    @Override
    public void messageReceived(Client source, Message m) {
        doTask(new StartGameTask(), (StartGameMessage) m);
    }
    
}
