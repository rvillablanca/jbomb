package jbomb.server.controls;

import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.GhostControl;
import jbomb.common.game.JBombContext;
//import org.apache.log4j.Logger;

public class GhostBombControl extends GhostControl {

    private float time, maxTime = 1f;
//    private static final Logger LOGGER = Logger.getLogger(GhostBombControl.class);

    public GhostBombControl() {
        super(new SphereCollisionShape(10f));
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        time += tpf;
//        LOGGER.debug("Tiempo Fantasma: " + time);
        if (time >= maxTime)
            JBombContext.PHYSICS_SPACE.remove(this);
    }
}
