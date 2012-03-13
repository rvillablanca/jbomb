package jbomb.common.messages;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import jbomb.common.game.JBombContext;
import jbomb.common.scene.Elevator;
//import org.apache.log4j.Logger;

@Serializable
public class ElevatorMovesMessage extends BasePhysicMessage {

//    private static final Logger LOGGER = Logger.getLogger(ElevatorMovesMessage.class);
    private Vector3f location = new Vector3f();
    private Vector3f oldPosition = new Vector3f();
    private boolean isFirstInterpolation;
    
    public ElevatorMovesMessage() {}

    public ElevatorMovesMessage(long id, Vector3f location) {
        super(id);
        this.location.set(location);
    }
    
    public Vector3f getLocation() {
        return location;
    }

    @Override
    public void applyData() {
        Elevator e = (Elevator) JBombContext.MANAGER.getPhysicObject(getId());
        if (e != null)
            e.getGeometry().setLocalTranslation(getLocation());
    }

    @Override
    public void interpolate(float percent) {
        Elevator e = (Elevator) JBombContext.MANAGER.getPhysicObject(getId());
        if (e != null) {
            if (!isFirstInterpolation) {
                oldPosition.set(e.getGeometry().getLocalTranslation());
                isFirstInterpolation = true;
            }
            e.getGeometry().setLocalTranslation(FastMath.interpolateLinear(percent, oldPosition, location));
//            LOGGER.debug("Interpolando elevador");
        }
    }
}
