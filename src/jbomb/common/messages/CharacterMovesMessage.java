package jbomb.common.messages;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import jbomb.common.game.JBombContext;
import jbomb.common.game.Player;
//import org.apache.log4j.Logger;

@Serializable
public class CharacterMovesMessage extends BasePhysicMessage {

//    private static final Logger LOGGER = Logger.getLogger(CharacterMovesMessage.class);
    private Vector3f viewDirection = new Vector3f();
    private Vector3f location = new Vector3f();
    private boolean isFirstInterpolation;
    private Vector3f oldPosition = new Vector3f();

    public CharacterMovesMessage() {}

    public CharacterMovesMessage(long id, CharacterControl character, Vector3f walk) {
        super(id);
        viewDirection.set(character.getViewDirection());
        location.set(character.getPhysicsLocation());
    }

    @Override
    public void applyData() {
        Player p = (Player) JBombContext.MANAGER.getPhysicObject(getId());
        if (p != null) {
            p.getGeometry().setLocalTranslation(location);
            p.setViewDirection(viewDirection);
        }
    }

    @Override
    public void interpolate(float percent) {
        Player p = (Player) JBombContext.MANAGER.getPhysicObject(getId());
        if (p != null) {
            if (!isFirstInterpolation) {
                oldPosition.set(p.getGeometry().getLocalTranslation());
                isFirstInterpolation = true;
            }
            p.getGeometry().setLocalTranslation(FastMath.interpolateLinear(percent, oldPosition, location));
//            LOGGER.debug("Interpolado a: " + FastMath.interpolateLinear(percent, oldPosition, location));
        }
    }
}