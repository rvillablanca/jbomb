package jbomb.client.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.Listener;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import jbomb.client.game.ClientContext;
import jbomb.common.appstates.RunningAppState;
import jbomb.common.game.JBombContext;
import jbomb.common.messages.CharacterMovesMessage;
import jbomb.common.weapons.impl.BaseBomb;
//import org.apache.log4j.Logger;

public class RunningClientAppState extends RunningAppState {

    private float[] timer = new float[]{0, 0, 0};
    private float time;
    private float maxTime = 1f / JBombContext.MESSAGES_PER_SECOND;
//    private static final Logger LOGGER = Logger.getLogger(RunningClientAppState.class);

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        ClientContext.APP.initMappings();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    @Override
    public void update(float tpf) {
        moveCam(tpf);
        hearingSounds();
        reloadBombs(tpf);
        loadInterface();
    }

    private void reloadBombs(float tpf) {
        for (int i = 0; i < timer.length; i++) {
            if (ClientContext.PLAYER.getBombs()[i] == null) {
                if (timer[i] >= 5f) {
                    timer[i] = 0;
                    ClientContext.PLAYER.reloadBomb(i);
                } else {
                    timer[i] += tpf;
                }
            }
        }
    }

    private void moveCam(float tpf) {
        time += tpf;

        Camera cam = ClientContext.APP.getCam();
        Vector3f camDir = cam.getDirection().clone().multLocal(0.2f);
        Vector3f camLeft = cam.getLeft().clone().multLocal(0.1f);
        camDir.y = 0;
        camLeft.y = 0;
        Vector3f walk = new Vector3f();
        walk.set(0, 0, 0);

        if (ClientContext.APP.isLeft()) {
            walk.addLocal(camLeft);
        }
        if (ClientContext.APP.isRight()) {
            walk.addLocal(camLeft.negate());
        }
        if (ClientContext.APP.isFront()) {
            walk.addLocal(camDir);
        }
        if (ClientContext.APP.isBack()) {
            walk.addLocal(camDir.negate());
        }

        CharacterControl c = ClientContext.PLAYER.getControl();
        c.setViewDirection(cam.getDirection());
        if (time >= maxTime) {
            time = 0;
            ClientContext.CLIENT.send(
                    new CharacterMovesMessage(ClientContext.PLAYER.getIdUserData(), c, walk));
        }
        c.setWalkDirection(walk);
        ClientContext.APP.getCam().setLocation(c.getPhysicsLocation());
    }

    private void hearingSounds() {
        Camera cam = ClientContext.APP.getCam();
        Listener listener = JBombContext.BASE_GAME.getListener();
        listener.setLocation(cam.getLocation());
        listener.setRotation(cam.getRotation());
    }

    private void loadInterface() {
        BaseBomb[] bombs = ClientContext.PLAYER.getBombs();
        byte bombsCount = 0;
        for (BaseBomb b : bombs) {
            if (b != null) {
                bombsCount++;
            }
        }
        ClientContext.APP.getBombsPictures().setImage(JBombContext.ASSET_MANAGER, "interfaces/pictures/bomb" + bombsCount + ".png", true);
    }
}
