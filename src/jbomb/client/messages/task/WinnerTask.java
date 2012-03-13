package jbomb.client.messages.task;

import jbomb.client.game.ClientContext;
import jbomb.common.game.JBombContext;
import jbomb.common.game.Player;
import jbomb.common.messages.WinnerMessage;

public class WinnerTask implements Task<WinnerMessage> {

    @Override
    public void doThis(WinnerMessage message) {
        if (ClientContext.PLAYER != null && message.getId() == ClientContext.PLAYER.getIdUserData()) {
            JBombContext.ROOT_NODE.detachChild(ClientContext.PLAYER.getGeometry());
            ClientContext.PLAYER = null;
            ClientContext.APP.resetGame();
            ClientContext.APP.removeListeners();
            ClientContext.APP.cleanScreenBombs();
            ClientContext.APP.youAreWinner();
            ClientContext.APP.waitMoment();
        } else {
            ClientContext.APP.printWinnerMessage((int) message.getId());
            ClientContext.APP.waitMoment();
            Player player = (Player) JBombContext.MANAGER.removePhysicObject(message.getId());
            JBombContext.ROOT_NODE.detachChild(player.getGeometry());
        }
    }
}
