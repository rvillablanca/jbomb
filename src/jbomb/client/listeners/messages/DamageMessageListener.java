package jbomb.client.listeners.messages;

import com.jme3.network.Client;
import com.jme3.network.Message;
import jbomb.client.messages.task.DamageTask;
import jbomb.common.messages.DamageMessage;
//import org.apache.log4j.Logger;

public class DamageMessageListener extends AbstractClientMessageListener<DamageTask, DamageMessage> {

//    private static final Logger LOGGER = Logger.getLogger(DamageMessageListener.class);

    @Override
    public void messageReceived(Client source, Message m) {
        doTask(new DamageTask(), (DamageMessage) m);
    }
}
