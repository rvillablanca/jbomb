package jbomb.common.effects.impl;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.Control;
import jbomb.common.controls.BaseControlExplosion;
import jbomb.common.effects.api.Explosion;
import jbomb.common.game.JBombContext;
import jbomb.common.utils.MatDefs;

public class BaseExplosion implements Explosion {
    
    protected ParticleEmitter emitter;
    private int x, y;
    private Control control;
    
    public BaseExplosion(String name, BaseControlExplosion baseControlExplosion, int numParticles, String fileName, int x, int y) {
        this.control = baseControlExplosion;
        baseControlExplosion.setExplosion(this);
        emitter = new ParticleEmitter(name, ParticleMesh.Type.Triangle, numParticles);
        Material m = new Material(JBombContext.ASSET_MANAGER, MatDefs.PARTICLE);
        m.setTexture("Texture", JBombContext.ASSET_MANAGER.loadTexture("jbomb/assets/textures/explosion/" + fileName));
        emitter.setMaterial(m);
        emitter.addControl(control);
        this.x = x;
        this.y = y;
        config();
    }
    
    public BaseExplosion(String name, int numParticles, String fileName, int x, int y) {
        this(name, new BaseControlExplosion(), numParticles, fileName, x, y);
    }
    
    public BaseExplosion(String name, String fileName, int x, int y) {
        this(name, new BaseControlExplosion(), 30, fileName, x, y);
    }
    
    public BaseExplosion(String name, String fileName) {
        this(name, new BaseControlExplosion(), 30, fileName, 1, 1);
    }
    
    public BaseExplosion(String name, int numParticles, String fileName) {
        this(name, new BaseControlExplosion(), numParticles, fileName, 1, 1);
    }
    
    public BaseExplosion() {
        this("defaultBaseExplosion", new BaseControlExplosion(), 30, "shockwave.png", 2, 2);
    }

    @Override
    public void config() {
        emitter.setImagesX(x);
        emitter.setImagesY(y);
        emitter.setStartColor(ColorRGBA.Red);
        emitter.setEndColor(ColorRGBA.Yellow);
        emitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0, 0));
        emitter.setStartSize(1.5f);
        emitter.setEndSize(0.1f);
        emitter.setGravity(0, 0, 0);
        emitter.setLowLife(1f);
        emitter.setHighLife(3f);
        emitter.getParticleInfluencer().setVelocityVariation(0.3f);
    }

    @Override
    public void start() {
        JBombContext.ROOT_NODE.attachChild(emitter);
    }

    @Override
    public void stop() {
        JBombContext.ROOT_NODE.detachChild(emitter);
    }
    
    @Override
    public void setLocation(Vector3f location) {
        emitter.setLocalTranslation(location);
    }

    @Override
    public float getTimeForDie() {
        return 1f;
    }

    @Override
    public Geometry getGeometry() {
        return emitter;
    }
}
