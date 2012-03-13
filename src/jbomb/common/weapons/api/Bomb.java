package jbomb.common.weapons.api;

import jbomb.common.controls.AbstractBombControl;
import jbomb.common.effects.api.Explosion;

public interface Bomb {
    
    void exploit();
    
    void setTimeForExplosion(float seconds);
    
    float getTimeForExplosion();
    
    void setControl(AbstractBombControl control);

    Explosion getExplosion();
}
