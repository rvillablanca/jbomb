package jbomb.client.messages.task;

import jbomb.client.game.ClientContext;
import jbomb.common.game.JBombContext;
import jbomb.common.game.Player;
import jbomb.common.messages.ThrowBombMessage;

public class ThrowBombTask implements Task<ThrowBombMessage> {

    @Override
    public void doThis(ThrowBombMessage message) {
        long idClient = message.getIdClient();
        byte timeExplosion = message.getTimeExplosion();
        Player player = (Player) JBombContext.MANAGER.getPhysicObject(idClient);
        if (player != null) {
            player.throwBomb(message.getLocation(), message.getId(), null, timeExplosion, true);
        } else {
            ClientContext.PLAYER.throwBomb(message.getLocation(), message.getId());
        }
    }
}
