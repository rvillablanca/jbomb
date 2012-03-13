package jbomb.client.listeners.messages;

import com.jme3.network.Message;
import java.util.concurrent.Callable;
import jbomb.client.messages.task.Task;
import jbomb.common.game.JBombContext;

public abstract class AbstractClientMessageListener<T extends Task<U>, U extends Message>
        implements ClientMessageListener<T, U> {

    @Override
    public void doTask(final T task, final U message) {
        JBombContext.BASE_GAME.enqueue(new Callable<Void>(){
            
            @Override
            public Void call() throws Exception {
                task.doThis(message);
                return null;
            }
        });
    }
}
