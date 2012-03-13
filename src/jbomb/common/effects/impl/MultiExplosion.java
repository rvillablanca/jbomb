package jbomb.common.effects.impl;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import java.util.ArrayList;
import java.util.List;
import jbomb.common.effects.api.Explosion;

public abstract class MultiExplosion implements Explosion {
    
    private List<Explosion> explosionList = new ArrayList<Explosion>();
    
    protected void addExplosion(Explosion explosion) {
        explosionList.add(explosion);
    }
    
    public MultiExplosion() {
        config();
    }

    @Override
    public void start() {
        for (Explosion e: explosionList)
            e.start();
    }

    @Override
    public void stop() {
        for (Explosion e: explosionList)
            e.stop();
    }

    @Override
    public void setLocation(Vector3f location) {
        for (Explosion e: explosionList)
            e.setLocation(location);
    }

    @Override
    public float getTimeForDie() {
        float maxTime = 0;
        for(Explosion e: explosionList)
            if (e.getTimeForDie() > maxTime)
                maxTime = e.getTimeForDie();
        return maxTime;
    }

    @Override
    public Geometry getGeometry() {
        return explosionList.get(0).getGeometry();
    }
}
