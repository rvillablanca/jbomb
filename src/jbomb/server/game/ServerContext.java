package jbomb.server.game;

import com.jme3.network.HostedConnection;
import com.jme3.network.Server;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.List;
import jbomb.common.game.JBombContext;

public class ServerContext extends JBombContext {

    private ServerContext() {}
    
    public static Server SERVER;
    public static Node NODE_PLAYERS;
    public static byte PLAYERS_COUNT;
    public static JBombServer APP;
    public static byte CONNECTED_PLAYERS;
    public static boolean ROUND_FINISHED = false;
    public static List<HostedConnection> CONNECTION_LIST = new ArrayList<HostedConnection>();
}
