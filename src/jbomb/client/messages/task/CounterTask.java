package jbomb.client.messages.task;

import com.jme3.math.Vector3f;
import jbomb.client.game.ClientContext;
import jbomb.common.messages.CounterMessage;
import org.apache.log4j.Logger;

public class CounterTask implements Task<CounterMessage> {
    
    private static final Logger LOGGER = Logger.getLogger(CounterTask.class);

    @Override
    public void doThis(CounterMessage message) {
        LOGGER.debug("¿Listo?: " + message.getNum());
        if (message.getNum() == 3)
            ClientContext.APP.initGuiCounter();
        else
            ClientContext.APP.changeCounter(message.getNum());
        if (message.getNum() == 1)
            ClientContext.APP.getBackgroundSound().play(Vector3f.ZERO);
    }
}
