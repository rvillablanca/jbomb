package jbomb.client.messages.task;

import jbomb.common.messages.NewPlayerMessage;

public class NewPlayerTask implements Task<NewPlayerMessage> {

    @Override
    public void doThis(NewPlayerMessage message) {
        message.applyData();
    }
}
