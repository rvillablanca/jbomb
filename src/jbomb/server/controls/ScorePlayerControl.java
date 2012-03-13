package jbomb.server.controls;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import jbomb.common.controls.JBombAbstractControl;
import jbomb.common.effects.impl.PlayerExplosion;
import jbomb.common.game.JBombContext;
import jbomb.common.messages.DamageMessage;
import jbomb.common.messages.DeadPlayerMessage;
import jbomb.common.messages.WinnerMessage;
import jbomb.server.game.ServerContext;
import org.apache.log4j.Logger;

public class ScorePlayerControl extends JBombAbstractControl {

    private static final Logger LOGGER = Logger.getLogger(ScorePlayerControl.class);
    private volatile float damage;
    private float time, maxTime = 1f;
    private float temporalDamage;
    private int id;
    private float health;

    @Override
    protected Control newInstanceOfMe() {
        return new ScorePlayerControl();
    }

    @Override
    protected void controlUpdate(float tpf) {
        time += tpf;
        if (time >= maxTime) {
            time = 0;
            if (damage > 0) {
                synchronized (this) {
                    temporalDamage = damage;
                    damage = 0;
                }
                health = (Float) spatial.getUserData("health");
                health -= temporalDamage;
                if (health <= 0) {
                    LOGGER.debug("Jugador #" + id + " ha perdido");
                    health = 0;
                    ServerContext.SERVER.broadcast(new DeadPlayerMessage(id, "Player #" + id + " has died!"));
                    ServerContext.NODE_PLAYERS.detachChild(spatial);
                    JBombContext.MANAGER.removePhysicObject(id);
                    ServerContext.APP.releaseId(id);
                    PlayerExplosion explosion = new PlayerExplosion();
                    explosion.setLocation(spatial.getLocalTranslation());
                    explosion.start();
                    if (ServerContext.NODE_PLAYERS.getChildren().size() == 1) {
                        Spatial winner = ServerContext.NODE_PLAYERS.getChildren().iterator().next();
                        int winnerId = (Integer) winner.getUserData("id");
                        LOGGER.debug("Jugador ganador #" + winnerId);
                        ServerContext.SERVER.broadcast(new WinnerMessage(winnerId));
                        ServerContext.ROUND_FINISHED = true;
                        ServerContext.NODE_PLAYERS.detachAllChildren();
                        JBombContext.MANAGER.removePhysicObject(winnerId);
                        ServerContext.APP.releaseId(winnerId);
                        ServerContext.APP.resetGame();
                        for (Spatial s : JBombContext.NODE_ELEVATORS.getChildren()) {
                            s.setLocalTranslation((Vector3f) s.getUserData("initialLocation"));
                        }
                        ServerContext.APP.initWaitingTime();
                    }
                }
                spatial.setUserData("health", health);
                ServerContext.SERVER.broadcast(new DamageMessage((long) id, health));
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public synchronized void sumDamage(float value) {
        damage += value;
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        id = (Integer) spatial.getUserData("id");
    }
}
