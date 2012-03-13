package jbomb.client.game;

import com.jme3.network.Client;
import jbomb.common.game.JBombContext;

public class ClientContext extends JBombContext {

    private ClientContext() {}

    public static ClientPlayer PLAYER;
    public static JBombClient APP;
    public static Client CLIENT;
}
