package jbomb.common.messages;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import jbomb.common.controls.ThrowBombControl;
import jbomb.common.game.JBombContext;
import jbomb.common.weapons.impl.GrandBomb;

@Serializable
public class CoordinateBombMessage extends BasePhysicMessage {

    private Matrix3f rotation;
    private Vector3f location;
    private Vector3f linearVelocity;
    private Vector3f angularVelocity;

    public CoordinateBombMessage() {}

    public CoordinateBombMessage(long id, RigidBodyControl control) {
        super(id);
        location = control.getPhysicsLocation(new Vector3f());
        rotation = control.getPhysicsRotationMatrix(new Matrix3f());
        linearVelocity = new Vector3f();
        control.getLinearVelocity(linearVelocity);
        angularVelocity = new Vector3f();
        control.getAngularVelocity(angularVelocity);
    }

    @Override
    public void applyData() {
        GrandBomb gb = (GrandBomb) JBombContext.MANAGER.getPhysicObject(getId());
        if (gb != null) {
            ThrowBombControl control = gb.getSpatial().getControl(ThrowBombControl.class);
            control.setPhysicsLocation(location);
            control.setPhysicsRotation(rotation);
            control.setLinearVelocity(linearVelocity);
            control.setAngularVelocity(angularVelocity);
        }
    }
}
