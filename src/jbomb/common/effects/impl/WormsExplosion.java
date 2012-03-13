package jbomb.common.effects.impl;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class WormsExplosion extends BaseExplosion {
    public WormsExplosion() {
        super("wormsExplosion", 6, "worms.png");
    }

    @Override
    public void config() {
        emitter.setStartColor(ColorRGBA.DarkGray);
        emitter.setEndColor(ColorRGBA.Blue);
        emitter.setStartSize(3f);
        emitter.setEndSize(6f);
        emitter.setRandomAngle(true);
        emitter.getParticleInfluencer().setInitialVelocity(Vector3f.UNIT_X);
        emitter.getParticleInfluencer().setVelocityVariation(1f);
    }
    
    @Override
    public float getTimeForDie() {
        return 3.3f;
    }
}
