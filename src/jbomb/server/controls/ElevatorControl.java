package jbomb.server.controls;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.Control;
import jbomb.common.controls.JBombAbstractControl;

public class ElevatorControl extends JBombAbstractControl {

    private float timer = 0, maxY, minY, freezed;
    private State state;

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {}
    
    private enum State {
        MOVING_UP, MOVING_DOWN, WAITING_UP, WAITING_DOWN;
    }
    
    public ElevatorControl() {
        maxY = 1; minY = -1; freezed = 10;
        setEnabled(false);
    }
    
    public ElevatorControl(float maxY, float minY, float freezedSeconds, boolean up) {
        this.maxY = maxY;
        this.minY = minY;
        this.freezed = freezedSeconds;
        if (up)
            state = State.MOVING_UP;
        else
            state = State.MOVING_DOWN;
        setEnabled(false);
    }
    
    @Override
    protected Control newInstanceOfMe() {
        return new ElevatorControl(maxY, minY, freezed, true);
    }

    @Override
    protected void controlUpdate(float tpf) {
        Vector3f currentLocation = spatial.getLocalTranslation();
        if(state == State.MOVING_UP) {
            if (currentLocation.y >= maxY) {
                currentLocation.y = maxY;
                spatial.setLocalTranslation(currentLocation);
                state = State.WAITING_DOWN;
                timer = 0;
            } else {
                spatial.move(0f, tpf * 3, 0f);
            }
        } else if (state == State.MOVING_DOWN) {
            if (currentLocation.y <= minY) {
                currentLocation.y = minY;
                spatial.setLocalTranslation(currentLocation);
                state = State.WAITING_UP;
                timer = 0;
            } else {
                spatial.move(0f, -tpf * 3, 0f);
            }
        } else if (state == State.WAITING_UP) {
            timer += tpf;
            if (timer >= freezed) {
                state = State.MOVING_UP;
            }
        } else {
            timer += tpf;
            if (timer >= freezed) {
                state = State.MOVING_DOWN;
            }
        }
    }
}
