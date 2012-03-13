package jbomb.client.listeners;

import com.jme3.input.controls.ActionListener;
import jbomb.client.game.ClientContext;

public class CharacterActionListener implements ActionListener {
        
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("Left"))
            if (isPressed) ClientContext.APP.setLeft(true); else ClientContext.APP.setLeft(false);
        else if (name.equals("Right"))
            if (isPressed) ClientContext.APP.setRight(true); else ClientContext.APP.setRight(false);
        else if (name.equals("Front"))
            if (isPressed) ClientContext.APP.setFront(true); else ClientContext.APP.setFront(false);
        else if (name.equals("Back")) 
            if (isPressed) ClientContext.APP.setBack(true); else ClientContext.APP.setBack(false);
        else
            ClientContext.PLAYER.jump();
    }
}
