package jbomb.common.weapons.impl;

import jbomb.common.effects.impl.WormsExplosion;

public class WormsBomb extends BaseBomb {
    public WormsBomb() {
        setExplosion(new WormsExplosion());
    }
}
