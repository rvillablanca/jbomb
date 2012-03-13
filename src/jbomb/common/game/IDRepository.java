package jbomb.common.game;

import java.util.HashMap;
import java.util.Map;

public class IDRepository {
    
    private Map<Long, Boolean> idMap = new HashMap<Long, Boolean>();
    private long count = 4;
    
    public void occupyIn(long l) {
        idMap.put(l, false);
    }
    
    public long nextFree() {
        for (long l : idMap.keySet())
            if (idMap.get(l)) {
                idMap.put(l, false);
                return l;
            }
        occupyIn(getCount());
        count++;
        return getCount() - 1;
    }
    
    public void releaseIn(long l) {
        if (l < 4)
            return;
        idMap.put(l, true);
    }

    public long getCount() {
        return count;
    }
}
