package jbomb.client.messages.task;

import jbomb.common.messages.RemovePlayerMessage;

public class RemovePlayerTask implements Task<RemovePlayerMessage> {

    @Override
    public void doThis(RemovePlayerMessage message) {
        message.applyData();
    }
    
}
