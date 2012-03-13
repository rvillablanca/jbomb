package jbomb.common.messages;

import com.jme3.network.serializing.Serializable;

@Serializable
public class DamageMessage extends BasePhysicMessage {

    private float damage;

    public DamageMessage() {}

    public DamageMessage(long id, float damage) {
        super(id);
        this.damage = damage;
    }

    public float getDamage() {
        return damage;
    }
}
