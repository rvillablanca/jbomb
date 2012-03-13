package jbomb.common.controls;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
//import org.apache.log4j.Logger;

public class ThrowBombControl extends RigidBodyControl implements PhysicsTickListener {

    private boolean linear;
    private boolean angular;
    private Vector3f linearVelocity;
    private Vector3f angularVelocity;
//    private static final Logger LOGGER = Logger.getLogger(ThrowBombControl.class);

    public ThrowBombControl(float mass) {
        super(mass);
    }

    @Override
    public void prePhysicsTick(PhysicsSpace ps, float f) {
        if (linear) {
//            LOGGER.debug("Aplicando fuerza linear a bomba");
            super.setLinearVelocity(linearVelocity);
            linear = false;
        }
        if (angular) {
//            LOGGER.debug("Aplicando fuerza angular a bomba");
            super.setAngularVelocity(angularVelocity);
            angular = false;
        }
    }

    @Override
    public void setLinearVelocity(Vector3f vec) {
        linearVelocity = vec;
        linear = true;
    }

    @Override
    public void setAngularVelocity(Vector3f vec) {
        angularVelocity = vec;
        angular = true;
    }
    
    @Override
    public void physicsTick(PhysicsSpace ps, float f) {}

    @Override
    public void setPhysicsSpace(PhysicsSpace space) {
        super.setPhysicsSpace(space);
        space.addTickListener(this);
    }
}
