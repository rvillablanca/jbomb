package jbomb.common.effects.impl;

public class GrandExplosion extends MultiExplosion {

    @Override
    public void config() {
        addExplosion(new WormsExplosion());
        addExplosion(new RoundsparkExplosion());
        addExplosion(new StarExplosion());
    }
    
}
