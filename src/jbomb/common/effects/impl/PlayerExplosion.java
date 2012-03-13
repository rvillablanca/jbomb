package jbomb.common.effects.impl;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class PlayerExplosion extends BaseExplosion {
    
    public PlayerExplosion() {
        super("playerRoundsparkExplosion", 60, "roundspark.png");
    }

    @Override
    public void config() {
        emitter.setStartColor(ColorRGBA.Red);
        emitter.setEndColor(ColorRGBA.Yellow);
        emitter.setStartSize(.3f);
        emitter.setEndSize(.5f);
        emitter.setRandomAngle(true);
        emitter.getParticleInfluencer().setInitialVelocity(Vector3f.UNIT_Y.multLocal(.3f));
        emitter.getParticleInfluencer().setVelocityVariation(1f);
    }

    @Override
    public float getTimeForDie() {
        return 10f;
    }
}
