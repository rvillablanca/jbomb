package jbomb.common.sounds.api;

import com.jme3.math.Vector3f;

public interface Sound {
    
    void play(Vector3f location);
    
    void setEnabled(boolean enabled);
    
    void stop();
}
