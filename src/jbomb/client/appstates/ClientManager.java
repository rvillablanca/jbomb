package jbomb.client.appstates;

import com.jme3.network.Client;
import jbomb.common.appstates.AbstractManager;
import jbomb.common.messages.BasePhysicMessage;

public class ClientManager extends AbstractManager<Client> {

    @Override
    public void doOnUpdate(float tpf, BasePhysicMessage message) {
        message.applyData();
    }
}
