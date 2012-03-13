package jbomb.server.listeners;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.ConnectionListener;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;
import com.jme3.network.Server;
import com.jme3.scene.Spatial;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import jbomb.common.game.JBombContext;
import jbomb.common.game.Player;
import jbomb.common.messages.CreatePlayerMessage;
import jbomb.common.messages.NewPlayerMessage;
import jbomb.common.messages.RemovePlayerMessage;
import jbomb.common.messages.StartingNewGameMessage;
import jbomb.server.controls.ScorePlayerControl;
import jbomb.server.game.ServerContext;
import org.apache.log4j.Logger;

public class ClientConnectionListener implements ConnectionListener {

    private static final Logger LOGGER = Logger.getLogger(ClientConnectionListener.class);
    private Vector3f[] positions = {new Vector3f(18f, 1.4f, 18f), new Vector3f(-18f, 1.4f, -18f),
        new Vector3f(18f, 21.4f, 18f), new Vector3f(-18f, 21.4f, -18f)};
    private ColorRGBA[] colors = {ColorRGBA.Blue, ColorRGBA.Red, ColorRGBA.Yellow, ColorRGBA.Green};
    private Map<InternPlayer, Boolean> freeIds = new HashMap<InternPlayer, Boolean>();
    private boolean buzzy;

    public ClientConnectionListener() {
        freeIds.put(new InternPlayer(-1l, 0), true);
        freeIds.put(new InternPlayer(-1l, 1), true);
        freeIds.put(new InternPlayer(-1l, 2), true);
        freeIds.put(new InternPlayer(-1l, 3), true);
    }

    @Override
    public synchronized void connectionAdded(Server server, final HostedConnection conn) {
        try {
            while (isBuzzy())
                wait();
            ServerContext.CONNECTION_LIST.add(conn);
            if (!ServerContext.APP.isRunning()) {
                final int nextId = nextId(conn.getId());
                LOGGER.debug("Jugador #" + conn.getId() + " con Id #" + nextId + " en línea.");
                Vector3f loc = null;
                ColorRGBA color = null;
                Iterator<Long> it = JBombContext.MANAGER.keySet().iterator();
                long key = 0;
                Player p = null;
                Object physicObject = null;
                while (it.hasNext()) {
                    key = it.next();
                    physicObject = JBombContext.MANAGER.getPhysicObject(key);
                    if (physicObject instanceof Player) {
                        p = (Player) physicObject;
                        loc = p.getGeometry().getLocalTranslation();
                        color = p.getColor();
                        ServerContext.SERVER.broadcast(Filters.in(conn),
                                new NewPlayerMessage(loc, color, key));
                    }
                }
                final Vector3f loc2 = nextPosition(nextId);
                final ColorRGBA color2 = nextColor(nextId);
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
                ServerContext.SERVER.broadcast(Filters.in(conn),
                        new CreatePlayerMessage(loc2, color2, nextId));
                ServerContext.SERVER.broadcast(Filters.notEqualTo(conn),
                        new NewPlayerMessage(loc2, color2, nextId));
            }
        } catch (InterruptedException ex) {
            LOGGER.debug("Ha ocurrido un error en la espera para la conexion: " + conn);
        }
    }

    @Override
    public synchronized void connectionRemoved(Server server, final HostedConnection conn) {
        try {
            if (isBuzzy())
                wait();
            ServerContext.CONNECTION_LIST.remove(conn);
            JBombContext.BASE_GAME.enqueue(new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    InternPlayer ip = getInternPlayerByConnId(conn.getId());
                    LOGGER.debug("Jugador #" + ip.getConn() + " con Id #" + ip.getId() + " desconectado.");
                    releaseIn(ip);
                    Player player = (Player) JBombContext.MANAGER.removePhysicObject(ip.getId());
                    if (player != null) 
                        ServerContext.NODE_PLAYERS.detachChild(player.getGeometry());
                    ServerContext.SERVER.broadcast(Filters.notEqualTo(conn), new RemovePlayerMessage(ip.getId()));
                    ServerContext.CONNECTED_PLAYERS--;
                    LOGGER.debug("Cantidad de jugadores: " + ServerContext.CONNECTED_PLAYERS);
                    if (ServerContext.CONNECTED_PLAYERS <= 1) {
                        LOGGER.debug("Comenzando nuevo juego");
                        Vector3f nextPosition = null;
                        if (ServerContext.CONNECTED_PLAYERS == 1 && ServerContext.NODE_PLAYERS.getChildren().size() > 0) {
                            List<Spatial> children = ServerContext.NODE_PLAYERS.getChildren();
                            int idPlayer = ((Integer) children.get(0).getUserData("id"));
                            nextPosition = nextPosition(idPlayer);
                            ServerContext.SERVER.broadcast(new StartingNewGameMessage(nextPosition,
                                    !ServerContext.ROUND_FINISHED));
                        }
                        ServerContext.APP.resetGame();
                        for (Spatial s : JBombContext.NODE_ELEVATORS.getChildren()) {
                            s.setLocalTranslation((Vector3f) s.getUserData("initialLocation"));
                        }
                        List<Spatial> children = ServerContext.NODE_PLAYERS.getChildren();
                        if (children.size() > 0) {
                            for (Spatial s : children) {
                                s.getControl(RigidBodyControl.class).setPhysicsLocation(nextPosition);
                            }
                        }
                    }
                    return null;
                }
            });
        } catch (InterruptedException ex) {
            LOGGER.debug("Ha ocurrido un error en la espera para la desconexion: " + conn);
        }
    }

    public ColorRGBA nextColor(int id) {
        return colors[id];
    }

    public Vector3f nextPosition(int id) {
        return positions[id];
    }

    private synchronized void occupyIn(InternPlayer ip) {
        freeIds.put(ip, false);
        LOGGER.debug("Ocupando en: " + ip.getId());
    }

    public synchronized int nextId(long conn) {
        for (InternPlayer ip : freeIds.keySet()) {
            if (freeIds.get(ip)) {
                occupyIn(ip);
                ip.setConn(conn);
                return ip.getId();
            }
        }
        throw new RuntimeException("Error al obtener id");
    }

    public synchronized void releaseIn(InternPlayer ip) {
        freeIds.put(ip, true);
        LOGGER.debug("Liberando en: " + ip.getId());
    }

    public synchronized InternPlayer getInternPlayerByConnId(int id) {
        for (InternPlayer ip : freeIds.keySet()) {
            if (ip.getConn() == id) {
                return ip;
            }
        }
        return null;
    }
    
    public synchronized InternPlayer getInternPlayerById(int id) {
        for (InternPlayer ip : freeIds.keySet()) {
            if (ip.getId() == id) {
                return ip;
            }
        }
        return null;
    }

    public boolean isBuzzy() {
        return buzzy;
    }

    public synchronized void setBuzzy(boolean buzzy) {
        this.buzzy = buzzy;
        notifyAll();
    }

    private static class InternPlayer {

        private long conn;
        private int id;

        public InternPlayer(long conn, int id) {
            setConn(conn);
            setId(id);
        }

        public long getConn() {
            return conn;
        }

        public void setConn(long conn) {
            this.conn = conn;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
