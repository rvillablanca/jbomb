package jbomb.client.controls;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.Control;
import jbomb.client.game.ClientContext;
import jbomb.common.controls.JBombAbstractControl;

public class WinnerLooserControl extends JBombAbstractControl {
    
    private float time, maxTime = 7f;

    @Override
    protected Control newInstanceOfMe() {
        return new WinnerLooserControl();
    }

    @Override
    protected void controlUpdate(float tpf) {
        time += tpf;
        if (time >= maxTime) {
            time = 0;
            ClientContext.APP.getGuiNode().detachChild(spatial);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {}
    
}
