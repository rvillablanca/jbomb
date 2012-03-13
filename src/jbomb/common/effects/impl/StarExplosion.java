package jbomb.common.effects.impl;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class StarExplosion extends BaseExplosion {

    public StarExplosion() {
        super("starExplosion", 5, "stars.png", 2, 2);
    }

    @Override
    public void config() {
        emitter.setStartColor(ColorRGBA.White);
        emitter.setEndColor(ColorRGBA.Yellow);
        emitter.setStartSize(2f);
        emitter.setEndSize(4f);
        emitter.setImagesX(2);
        emitter.setImagesY(2);
        emitter.setRandomAngle(true);
        emitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0f, 1f, 0f));
        emitter.getParticleInfluencer().setVelocityVariation(.5f);
        emitter.setSelectRandomImage(true);
    }

    @Override
    public float getTimeForDie() {
        return 3.1f;
    }
}
