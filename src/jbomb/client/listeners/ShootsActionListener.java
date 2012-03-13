package jbomb.client.listeners;

import com.jme3.input.controls.ActionListener;
import jbomb.client.game.ClientContext;
import jbomb.common.messages.ThrowBombMessage;

public class ShootsActionListener implements ActionListener {
    
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isPressed && ClientContext.PLAYER.isHasBombs())
            ClientContext.CLIENT.send(new ThrowBombMessage(ClientContext.PLAYER.getIdUserData(),
                    ClientContext.PLAYER.getSeconds()));
    }
    
}
