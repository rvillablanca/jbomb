package jbomb.common.effects.api;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

public interface Explosion {
    
    void config();
    
    void start();
    
    void stop();
    
    void setLocation(Vector3f location);
    
    float getTimeForDie();

    Geometry getGeometry();
}
