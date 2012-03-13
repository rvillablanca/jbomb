package jbomb.server.controls;

import com.jme3.scene.Geometry;
import com.jme3.scene.control.Control;
import jbomb.common.controls.AbstractBombControl;
import jbomb.common.game.JBombContext;
import jbomb.common.messages.ExploitBombMessage;
import jbomb.server.game.ServerContext;

public class BaseBombControl extends AbstractBombControl {

    @Override
    protected void doBeforeExploit() {
        Geometry geometry = (Geometry) getSpatial();
        long id = (Long) geometry.getUserData("id");
        GhostBombControl ghostBombControl = new GhostBombControl();
        bomb.getExplosion().getGeometry().addControl(ghostBombControl);
        JBombContext.PHYSICS_SPACE.add(ghostBombControl);
        ServerContext.SERVER.broadcast(new ExploitBombMessage(id));
    }

    @Override
    protected Control newInstanceOfMe() {
        return new BaseBombControl();
    }
}
