package jbomb.common.weapons.impl;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import jbomb.common.controls.AbstractBombControl;
import jbomb.common.controls.ThrowBombControl;
import jbomb.common.effects.api.Explosion;
import jbomb.common.effects.impl.BaseExplosion;
import jbomb.common.game.JBombContext;
import jbomb.common.sounds.api.Sound;
import jbomb.common.sounds.impl.BaseBombSound;
import jbomb.common.utils.MatDefs;
import jbomb.common.weapons.api.Bomb;

public class BaseBomb implements Bomb {

    private Geometry geometry;
    protected Sound sound;
    private Explosion explosion;
    private float timeForExplosion;
    private float radius;

    public BaseBomb(String fileName, float radius, Explosion explosion, Sound sound, float timeForExplosion, float mass) {
        this.timeForExplosion = timeForExplosion;
        this.explosion = explosion;
        this.sound = sound;
        Sphere sphere = new Sphere(32, 32, radius);
        sphere.setTextureMode(Sphere.TextureMode.Projected);
        geometry = new Geometry("bomb", sphere);
        Material material = new Material(JBombContext.ASSET_MANAGER, MatDefs.UNSHADED);
        material.setTexture("ColorMap", JBombContext.ASSET_MANAGER.loadTexture("jbomb/assets/textures/bomb/" + fileName));
        geometry.setMaterial(material);
        ThrowBombControl tbc = new ThrowBombControl(mass);
        geometry.addControl(tbc);
        this.radius = radius;
    }

    public BaseBomb(String fileName, float radius, Explosion explosion, float timeForExplosion) {
        this(fileName, radius, explosion, new BaseBombSound(), timeForExplosion, 1f);
    }

    public BaseBomb(String fileName, float radius, float timeForExplosion) {
        this(fileName, radius, new BaseExplosion(), new BaseBombSound(), timeForExplosion, 1f);
    }

    public BaseBomb() {
        this("bomb.png", 0.4f, 1f);
    }

    @Override
    public void exploit() {
        Vector3f location = geometry.getLocalTranslation();
        getExplosion().setLocation(location);
        geometry.removeControl(ThrowBombControl.class);
        getExplosion().start();
        sound.play(location);
        JBombContext.ROOT_NODE.detachChild(geometry);
        long id = ((Long) geometry.getUserData("id"));
        JBombContext.MANAGER.removePhysicObject(id);
    }

    @Override
    public float getTimeForExplosion() {
        return timeForExplosion;
    }

    public Spatial getSpatial() {
        return geometry;
    }

    public void setExplosion(Explosion explosion) {
        this.explosion = explosion;
    }

    public void setLocalTranslation(Vector3f localTranslation) {
        geometry.setLocalTranslation(localTranslation);
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public void setTimeForExplosion(float seconds) {
        timeForExplosion = seconds;
    }

    @Override
    public void setControl(AbstractBombControl control) {
        geometry.addControl(control);
        control.setBomb(this);
    }
    
    @Override
    public Explosion getExplosion() {
        return explosion;
    }
}
