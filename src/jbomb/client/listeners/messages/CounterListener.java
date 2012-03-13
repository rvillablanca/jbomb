package jbomb.client.listeners.messages;

import com.jme3.network.Client;
import com.jme3.network.Message;
import jbomb.client.messages.task.CounterTask;
import jbomb.common.messages.CounterMessage;

public class CounterListener extends AbstractClientMessageListener<CounterTask, CounterMessage> {

    @Override
    public void messageReceived(Client source, Message m) {
        doTask(new CounterTask(), (CounterMessage) m);
    }
    
}
