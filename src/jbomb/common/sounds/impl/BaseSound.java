package jbomb.common.sounds.impl;

import com.jme3.audio.AudioNode;
import com.jme3.math.Vector3f;
import jbomb.common.game.JBombContext;
import jbomb.common.sounds.api.Sound;

public class BaseSound implements Sound {
    
    private AudioNode audioNode;
    private Type type;
    private boolean enabled = true;
    
    public BaseSound(String fileName, Type type, boolean buffer, float volume) {
        audioNode = new AudioNode(JBombContext.ASSET_MANAGER, 
                (type == Type.INSTANCE ? "jbomb/assets/sounds/instance/" : "jbomb/assets/sounds/background/") + fileName, 
                buffer);
        audioNode.setVolume(volume);
        this.type = type;
        if (type == Type.BACKGROUND) {
            audioNode.setLooping(true);
            audioNode.setPositional(false);
        }
    }

    @Override
    public void play(Vector3f location) {
        if (enabled) {
            if (type == Type.INSTANCE) {
                audioNode.setLocalTranslation(location);
                audioNode.playInstance();
            } else
                audioNode.play();
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void stop() {
        audioNode.stop();
    }
    
    public enum Type {
        BACKGROUND, INSTANCE
    }
}
