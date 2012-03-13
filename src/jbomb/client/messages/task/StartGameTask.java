package jbomb.client.messages.task;

import jbomb.client.game.ClientContext;
import jbomb.common.messages.StartGameMessage;
import org.apache.log4j.Logger;

public class StartGameTask implements Task<StartGameMessage> {
    
    private static final Logger LOGGER = Logger.getLogger(StartGameTask.class);

    @Override
    public void doThis(StartGameMessage message) {
        ClientContext.APP.cleanScreen();
        ClientContext.APP.addListeners();
        ClientContext.APP.initInterfaces();
        ClientContext.APP.initHealthMarker();
        ClientContext.APP.startGame();
        ClientContext.APP.resetDirections();
        LOGGER.debug("Comenzando juego...");
    }
}
