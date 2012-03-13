package jbomb.client.listeners.messages;

import com.jme3.network.Client;
import com.jme3.network.Message;
import jbomb.client.messages.task.WinnerTask;
import jbomb.common.messages.WinnerMessage;
//import org.apache.log4j.Logger;


public class WinnerListener extends AbstractClientMessageListener<WinnerTask, WinnerMessage> {
    
//    private static final Logger LOGGER = Logger.getLogger(WinnerListener.class);

    @Override
    public void messageReceived(Client source, Message m) {
        doTask(new WinnerTask(), (WinnerMessage) m);
    }
}
