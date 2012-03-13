package jbomb.client.game;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.control.Control;
import jbomb.common.controls.ThrowBombControl;
import jbomb.common.game.JBombContext;
import jbomb.common.game.Player;
import jbomb.common.weapons.impl.BaseBomb;
import jbomb.common.weapons.impl.GrandBomb;

public class ClientPlayer extends Player {
    
    private BaseBomb[] bomb = new BaseBomb[3];
    private byte seconds = 1;
    private volatile boolean hasBombs = true;
    private CharacterControl control;

    public ClientPlayer(Vector3f location, ColorRGBA color) {
        super(location, color);
        for(int i = 0; i < bomb.length; i++)
            reloadBomb(i);
    }
    
    public void throwBomb(Vector3f location, long idPhysicObject) {
        for (int i = 0; i < bomb.length; i++) {
            if (bomb[i] != null) {
                JBombContext.MANAGER.addPhysicObject(idPhysicObject, bomb[i]);
                bomb[i].getSpatial().setUserData("id", idPhysicObject);
                throwSound.play(control.getPhysicsLocation());
                ThrowBombControl tbc = bomb[i].getSpatial().getControl(ThrowBombControl.class);
                tbc.setPhysicsLocation(location);
                JBombContext.ROOT_NODE.attachChild(bomb[i].getSpatial());
                JBombContext.PHYSICS_SPACE.add(tbc);
                bomb[i] = null;
                if (bomb[0] == null && bomb[1] == null && bomb[2] == null)
                    setHasBombs(false);
                break;
            }
        }
    }
    
    public void setInitialPosition(Vector3f location) {
        control.setPhysicsLocation(location);
    }

    public void setSeconds(byte seconds) {
        this.seconds = seconds;
    }

    public void reloadBomb(int position) {
        bomb[position] = new GrandBomb(true);
        bomb[position].setTimeForExplosion(getSeconds());
        setHasBombs(true);
    }

    public BaseBomb[] getBombs() {
        return bomb;
    }

    public void setHasBombs(boolean hasBombs) {
        this.hasBombs = hasBombs;
    }

    public boolean isHasBombs() {
        return hasBombs;
    }

    public byte getSeconds() {
        return seconds;
    }

    @Override
    protected void setControl(Control control) {
        this.control = (CharacterControl) control;
        geometry.addControl(control);
        this.control.setPhysicsLocation(location);
        JBombContext.PHYSICS_SPACE.add(this.control);
    }

    @Override
    protected Control createControl() {
        return new CharacterControl(new CapsuleCollisionShape(.55f, 1.7f), .45f);
    }

    public void jump() {
        control.jump();
    }

    public CharacterControl getControl() {
        return control;
    }
}
