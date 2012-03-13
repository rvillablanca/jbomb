package jbomb.server.game;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.scene.Node;
import com.jme3.scene.control.Control;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Callable;
import jbomb.common.appstates.AbstractManager;
import jbomb.common.game.BaseGame;
import jbomb.common.game.JBombContext;
import jbomb.common.game.Player;
import jbomb.common.messages.CharacterMovesMessage;
import jbomb.common.messages.CreatePlayerMessage;
import jbomb.common.messages.NewPlayerMessage;
import jbomb.common.messages.ThrowBombMessage;
import jbomb.server.appstates.CounterAppState;
import jbomb.server.appstates.RunningServerAppState;
import jbomb.server.appstates.ServerManager;
import jbomb.server.appstates.WaitingAppState;
import jbomb.server.controls.ElevatorControl;
import jbomb.server.controls.ScorePlayerControl;
import jbomb.server.listeners.BombCollisionListener;
import jbomb.server.listeners.ClientConnectionListener;
import jbomb.server.listeners.messages.CreatePlayerListener;
import jbomb.server.listeners.messages.ThrowBombListener;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class JBombServer extends BaseGame {

    private static final Logger LOGGER = Logger.getLogger(JBombServer.class);
    private Server server;
    private ClientConnectionListener connectionListener = new ClientConnectionListener();
    private ThrowBombListener throwBombListener = new ThrowBombListener();
    private CreatePlayerListener createPlayerListener = new CreatePlayerListener();
    private CounterAppState counterAppState = new CounterAppState();
    private WaitingAppState waitingAppState = new WaitingAppState();

    public JBombServer(String playersNumber) {
        ServerContext.PLAYERS_COUNT = setPlayersCount(playersNumber);
        LOGGER.debug("Cantidad de jugadores: " + ServerContext.PLAYERS_COUNT);
    }

    @Override
    public void simpleInitApp() {
        super.simpleInitApp();
        guiNode.detachAllChildren();
        JBombContext.PHYSICS_SPACE.addCollisionListener(new BombCollisionListener());
        ServerContext.NODE_PLAYERS = new Node("Node_players");
        JBombContext.ROOT_NODE.attachChild(ServerContext.NODE_PLAYERS);
        try {
            server = Network.createServer(6789);
            addMessageListeners();
            server.addConnectionListener(connectionListener);
            server.start();
        } catch (IOException ex) {
            LOGGER.error("Error al iniciar el servidor", ex);
        }
        ServerContext.APP = this;
        ServerContext.SERVER = server;
    }

    @Override
    public void destroy() {
        if (server != null && server.isRunning()) {
            server.close();
        }
        super.destroy();
    }

    public void initWaitingTime() {
        if (!stateManager.hasState(waitingAppState)) {
            stateManager.attach(waitingAppState);
        } else {
            waitingAppState.setEnabled(true);
        }
    }

    public void initNewRound() {
        connectionListener.setBuzzy(true);
        for (HostedConnection conn2 : ServerContext.CONNECTION_LIST) {
            final int nextId = connectionListener.nextId(conn2.getId());
            final Vector3f loc2 = connectionListener.nextPosition(nextId);
            final ColorRGBA color2 = connectionListener.nextColor(nextId);
            final HostedConnection conn = conn2;
            JBombContext.BASE_GAME.enqueue(new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    Player player = new Player(loc2, color2);
                    player.getGeometry().setUserData("id", nextId);
                    player.getGeometry().setName("Player(" + conn.getId() + ", " + nextId + ")");
                    player.getGeometry().setUserData("health", 100f);
                    player.getGeometry().setUserData("conn", conn.getId());
                    player.getGeometry().addControl(new ScorePlayerControl());
                    JBombContext.MANAGER.addPhysicObject(nextId, player);
                    ServerContext.NODE_PLAYERS.attachChild(player.getGeometry());
                    return null;
                }
            });
            CreatePlayerMessage newPlayer = new CreatePlayerMessage(loc2, color2, nextId);
            newPlayer.setNewPlayer(false);
            ServerContext.SERVER.broadcast(Filters.in(conn), newPlayer);
            ServerContext.SERVER.broadcast(Filters.notEqualTo(conn),
                    new NewPlayerMessage(loc2, color2, nextId));
        }
        if (ServerContext.CONNECTED_PLAYERS == ServerContext.PLAYERS_COUNT) {
            LOGGER.debug("Iniciando counterAppState");
            ServerContext.APP.initCounter();
        }
        connectionListener.setBuzzy(false);
    }

    public void releaseId(int id) {
        connectionListener.releaseIn(connectionListener.getInternPlayerById(id));
    }

    @Override
    protected void initStateManager() {
        super.initStateManager();
    }

    public void initCounter() {
        if (!stateManager.hasState(counterAppState)) {
            stateManager.attach(counterAppState);
        } else {
            counterAppState.setEnabled(true);
        }
    }

    public void startGame() {
        stateManager.attach(new RunningServerAppState());
        ServerContext.ROUND_FINISHED = false;
    }

    public void resetGame() {
        RunningServerAppState state = stateManager.getState(RunningServerAppState.class);
        if (state != null)
                state.setEnabled(false);
    }

    public boolean isRunning() {
        RunningServerAppState state = stateManager.getState(RunningServerAppState.class);
        if (state != null) {
            return state.isEnabled();
        } else {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
	@Override
    public void addMessageListeners() {
        AbstractManager<HostedConnection> m = (AbstractManager<HostedConnection>) getManager();
        server.addMessageListener(m, CharacterMovesMessage.class);
        server.addMessageListener(throwBombListener, ThrowBombMessage.class);
        server.addMessageListener(createPlayerListener, CreatePlayerMessage.class);
    }

    @Override
    protected AbstractManager<?> createManager() {
        return new ServerManager();
    }

    private byte setPlayersCount(String playersNumber) {
        if ("two".equals(playersNumber)) {
            return 2;
        } else if ("three".equals(playersNumber)) {
            return 3;
        } else {
            return 4;
        }
    }

    @Override
    public void loadLog4jConfig() {
        URL urlConfig = BaseGame.class.getResource("/jbomb/server/config/log4j.xml");
        DOMConfigurator.configure(urlConfig);
    }

    @Override
    protected boolean getElevatorServerControlled() {
        return true;
    }

    @Override
    public Control createElevatorControl(float upY, float downY, float freezedSeconds, boolean up) {
        return new ElevatorControl(upY, downY, freezedSeconds, up);
    }
}
