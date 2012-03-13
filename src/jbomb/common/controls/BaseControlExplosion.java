package jbomb.common.controls;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.Control;
import jbomb.common.effects.api.Explosion;

public class BaseControlExplosion extends JBombAbstractControl {
    
    private float time;
    private Explosion explosion;

    @Override
    protected Control newInstanceOfMe() {
        return new BaseControlExplosion();
    }

    @Override
    protected void controlUpdate(float tpf) {
        if(time >= explosion.getTimeForDie())
            explosion.stop();
        else
            time += tpf;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {}
    
    public void setExplosion(Explosion explosion) {
        this.explosion = explosion;
    }
}
