package jbomb.server.listeners.messages;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import jbomb.server.game.ServerContext;
import org.apache.log4j.Logger;

public class CreatePlayerListener implements MessageListener<HostedConnection> {
    
    private static final Logger LOGGER = Logger.getLogger(CreatePlayerListener.class);

    @Override
    public void messageReceived(HostedConnection s, Message msg) {
        ServerContext.CONNECTED_PLAYERS++;
        LOGGER.debug("Jugadores conectados: " + ServerContext.CONNECTED_PLAYERS);
        if (ServerContext.CONNECTED_PLAYERS == ServerContext.PLAYERS_COUNT) {
            LOGGER.debug("Iniciando counterAppState");
            ServerContext.APP.initCounter();
        }
    }
}
