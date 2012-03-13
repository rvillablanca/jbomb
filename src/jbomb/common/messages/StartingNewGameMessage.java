package jbomb.common.messages;

import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

@Serializable
public class StartingNewGameMessage extends AbstractMessage {
    
    private Vector3f initialPosition = new Vector3f();
    private boolean winner = false;
    
    public StartingNewGameMessage() {}
    
    public StartingNewGameMessage(Vector3f initialPosition, boolean winner) {
        this.initialPosition.set(initialPosition);
        this.winner = winner;
    }

    public Vector3f getInitialPosition() {
        return initialPosition;
    }

    public void setInitialPosition(Vector3f initialPosition) {
        this.initialPosition = initialPosition;
    }

    public boolean isWinner() {
        return winner;
    }
}
