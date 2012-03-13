package jbomb.common.scene;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.Control;
import jbomb.common.game.JBombContext;
import jbomb.common.utils.GeometryUtils;
import org.apache.log4j.Logger;

public class Elevator {

    private static final Logger LOGGER = Logger.getLogger(Elevator.class);
    private Geometry geometry;
    private Control control;
    private long id;

    public Elevator(String name, String texture, Vector3f localTraslation, float upY, float downY, float freezedSeconds, boolean up, boolean transparency,
            boolean serverControlled) {
        geometry = GeometryUtils.makeCube(1f, .1f, 1f, name, texture, localTraslation, new Vector2f(1f, 1f), transparency, JBombContext.NODE_ELEVATORS);
        geometry.setUserData("initialLocation", localTraslation);
        id = JBombContext.MANAGER.getRepository().nextFree();
        LOGGER.debug("Creando elevador: " + id);
        LOGGER.debug("Posicion Inicial: " + geometry.getUserData("initialLocation"));
        JBombContext.MANAGER.addPhysicObject(id, this);
        geometry.getControl(RigidBodyControl.class).setMass(1f);
        geometry.getControl(RigidBodyControl.class).setKinematic(true);
        if (serverControlled) {
            control = JBombContext.BASE_GAME.createElevatorControl(upY, downY, freezedSeconds, up);
            geometry.addControl(control);
        }
    }

    public Elevator(String name, String texture, Vector3f localTraslation, float upY, float downY, float freezedSeconds, boolean up, boolean serverControlled) {
        this(name, texture, localTraslation, upY, downY, freezedSeconds, up, false, serverControlled);
    }

    public Elevator(Vector3f localTraslation, float upY, float downY, float freezedSeconds, boolean up, boolean serverControlled) {
        this("elevator", "textures/boxes/w_darkgray.png", localTraslation, upY, downY, freezedSeconds, up, serverControlled);
    }

    public Geometry getGeometry() {
        return geometry;
    }
    
    public long getId() {
        return id;
    }
}
