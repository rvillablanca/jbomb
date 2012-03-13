package jbomb.server.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import jbomb.server.game.ServerContext;
import org.apache.log4j.Logger;

public class WaitingAppState extends AbstractAppState {
    
    private float time, maxTime = 10f;
    private static final Logger LOGGER = Logger.getLogger(WaitingAppState.class);

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        LOGGER.debug("Inicializando tiempo de espera: " + maxTime + " seconds");
    }

    @Override
    public void update(float tpf) {
        time += tpf;
        if (time >= maxTime) {
            LOGGER.debug("Finalizando tiempo de espera");
            time = 0;
            setEnabled(false);
            ServerContext.APP.initNewRound();
        }
    }
}
