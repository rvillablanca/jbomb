package jbomb.client.listeners;

import com.jme3.input.controls.ActionListener;
import jbomb.client.game.ClientContext;
import jbomb.client.game.ClientPlayer;
import jbomb.common.game.JBombContext;

public class BombSecondsListener implements ActionListener {

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("one")) {
            setSeconds((byte) 1);
            changeImage(1);
        } else if (name.equals("two")) {
            setSeconds((byte) 2);
            changeImage(2);
        } else {
            setSeconds((byte) 3);
            changeImage(3);
        }
    }

    private void setSeconds(byte seconds) {
        ClientPlayer player = ClientContext.PLAYER;
        player.setSeconds(seconds);
        for (int i = 0; i < player.getBombs().length; i++) {
            if (player.getBombs()[i] != null) {
                player.getBombs()[i].setTimeForExplosion(seconds);
            }
        }
    }
    
    private void changeImage(int value) {
        ClientContext.APP.getBombsSecondsPictures().setImage(JBombContext.ASSET_MANAGER, 
                "jbomb/assets/interfaces/pictures/glass_numbers_" + value + ".png", true);
    }
}
