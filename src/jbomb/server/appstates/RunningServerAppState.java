package jbomb.server.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.List;
import jbomb.common.appstates.RunningAppState;
import jbomb.common.game.JBombContext;
import jbomb.common.messages.CoordinateBombMessage;
import jbomb.common.messages.ElevatorMovesMessage;
import jbomb.common.scene.Elevator;
import jbomb.common.weapons.impl.GrandBomb;
import jbomb.server.controls.ElevatorControl;
import jbomb.server.game.ServerContext;
import org.apache.log4j.Logger;

public class RunningServerAppState extends RunningAppState {

    private static final Logger LOGGER = Logger.getLogger(RunningServerAppState.class);
    private float time, maxTime = 1 / JBombContext.MESSAGES_PER_SECOND;
    private List<Spatial> elevators;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        elevators = JBombContext.NODE_ELEVATORS.getChildren();
        LOGGER.debug("Tamaño lista elevador: " + elevators.size());
        for (Spatial s : elevators)
            s.getControl(ElevatorControl.class).setEnabled(true);
        LOGGER.debug("¡RunningServerAppState inicializado!");
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (Spatial s : elevators)
            s.getControl(ElevatorControl.class).setEnabled(enabled);
    }

    @Override
    public void update(float tpf) {
        time += tpf;
        if (time >= maxTime) {
            time = 0;
            coordinateBombs();
            coordinateElevators();
        }
    }

    private void coordinateBombs() {
        Object o = null;
        GrandBomb gb = null;
        for (long l : JBombContext.MANAGER.keySet()) {
            o = JBombContext.MANAGER.getPhysicObject(l);
            if (o instanceof GrandBomb) {
                gb = (GrandBomb) o;
                CoordinateBombMessage cbm = new CoordinateBombMessage(l,
                        gb.getSpatial().getControl(RigidBodyControl.class));
                ServerContext.SERVER.broadcast(cbm);
//                LOGGER.debug("Enviando mensaje de coordinateBomb");
            }
        }
    }

    private void coordinateElevators() {
        synchronized (JBombContext.MANAGER.getPhysicsObjects()) {
            for (long l : JBombContext.MANAGER.keySet()) {
                Object o = JBombContext.MANAGER.getPhysicObject(l);
                if (o instanceof Elevator) {
                    Elevator e = (Elevator) o;
                    Vector3f location = e.getGeometry().getControl(RigidBodyControl.class).getPhysicsLocation();
                    ServerContext.SERVER.broadcast(new ElevatorMovesMessage(e.getId(), location));
//                LOGGER.debug("Enviando mensaje de elevador");
                }
            }
        }
    }
}
