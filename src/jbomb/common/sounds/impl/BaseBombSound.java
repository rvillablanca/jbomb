package jbomb.common.sounds.impl;

public class BaseBombSound extends InstanceSound {
    
    public BaseBombSound(String fileName, float volume) {
        super(fileName, volume);
    }
    
    public BaseBombSound(String fileName) {
        this(fileName, 6f);
    }
    
    public BaseBombSound() {
        this("bomb/explode1.wav", 3f);
    }
}
