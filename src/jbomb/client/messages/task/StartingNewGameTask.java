package jbomb.client.messages.task;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import jbomb.client.game.ClientContext;
import jbomb.common.game.JBombContext;
import jbomb.common.messages.StartingNewGameMessage;
//import org.apache.log4j.Logger;

public class StartingNewGameTask implements Task<StartingNewGameMessage> {

//    private static final Logger LOGGER = Logger.getLogger(StartingNewGameTask.class);
    
    @Override
    public void doThis(StartingNewGameMessage message) {
        ClientContext.APP.resetGame();
        ClientContext.APP.removeListeners();
        ClientContext.APP.cleanScreen();
        for (Spatial s : JBombContext.NODE_ELEVATORS.getChildren()) {
            s.setLocalTranslation((Vector3f) s.getUserData("initialLocation"));
        }
        ClientContext.PLAYER.setInitialPosition(message.getInitialPosition());
        if (message.isWinner())
            ClientContext.APP.youAreWinner();
    }
}
