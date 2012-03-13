package jbomb.common.weapons.impl;

import jbomb.common.effects.impl.GrandExplosion;
import jbomb.common.sounds.impl.BaseSound;

public class GrandBomb extends BaseBomb {
    
    public GrandBomb(boolean enableSound) {
        super("bomb.png", .4f, new GrandExplosion(), 1.5f);
        ((BaseSound) sound).setEnabled(enableSound);
    }
}
