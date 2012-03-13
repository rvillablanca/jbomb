package jbomb.client.messages.task;

import com.jme3.network.Message;

public interface Task<T extends Message> {

     void doThis(T message);
}
