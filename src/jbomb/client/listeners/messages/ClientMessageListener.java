package jbomb.client.listeners.messages;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import jbomb.client.messages.task.Task;

public interface ClientMessageListener<T extends Task<U>, U extends Message> 
extends MessageListener<Client> {
    
    void doTask(final T task, final U message);
}
