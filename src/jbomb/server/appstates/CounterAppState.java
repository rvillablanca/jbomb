package jbomb.server.appstates;

import com.jme3.app.state.AbstractAppState;
import jbomb.common.messages.CounterMessage;
import jbomb.common.messages.StartGameMessage;
import jbomb.server.game.ServerContext;
import org.apache.log4j.Logger;

public class CounterAppState extends AbstractAppState {

    private float time;
    private byte[] times = {1, 2, 3};
    private byte currentTime = 0;
    private static final Logger LOGGER = Logger.getLogger(CounterAppState.class);

    @Override
    public void update(float tpf) {
        time += tpf;
        if (currentTime == 3) {
            if (time < 4) {
                return;
            } else {
                LOGGER.debug("Deteniendo counterAppState");
                setEnabled(false);
                ServerContext.APP.startGame();
                ServerContext.SERVER.broadcast(new StartGameMessage());
                LOGGER.debug("Comenzando juego...");
                return;
            }
        }
        if (time >= times[currentTime]) {
            ServerContext.SERVER.broadcast(new CounterMessage((byte) (4 - times[currentTime])));
            currentTime++;
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            currentTime = 0;
            time = 0;
        }
    }
}
