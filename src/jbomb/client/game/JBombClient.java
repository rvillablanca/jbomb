package jbomb.client.game;

import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.renderer.Camera;
import com.jme3.ui.Picture;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import jbomb.client.appstates.ClientManager;
import jbomb.client.appstates.RunningClientAppState;
import jbomb.client.controls.WinnerLoserControl;
import jbomb.client.listeners.BombSecondsListener;
import jbomb.client.listeners.CharacterActionListener;
import jbomb.client.listeners.ServerConnectionListener;
import jbomb.client.listeners.ShootsActionListener;
import jbomb.client.listeners.messages.*;
import jbomb.common.appstates.AbstractManager;
import jbomb.common.game.BaseGame;
import jbomb.common.game.JBombContext;
import jbomb.common.game.Player;
import jbomb.common.messages.*;
import jbomb.common.sounds.api.Sound;
import jbomb.common.sounds.impl.BackgroundSound;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class JBombClient extends BaseGame {

    private static final Logger LOGGER = Logger.getLogger(JBombClient.class);
    private String ip;
    private Client client;
    private Picture bombsPictures = new Picture("bombsPictures");
    private Picture BombsSecondsPictures = new Picture("bombsSecondsPictures");
    private BitmapText winnerMessage;
    private BitmapText youWinner;
    private BitmapText pointer;
    private BitmapText wait;
    private Picture counterPicture = new Picture("counterPicture");
    private Picture winner = new Picture("winner");
    private Picture looser = new Picture("looser");
    private Map<Integer, BitmapText> health;
    private boolean left;
    private boolean right;
    private boolean front;
    private boolean back;
    private Sound backgroundSound;
    
    private ShootsActionListener shootsActionListener = new ShootsActionListener();
    private CharacterActionListener characterActionListener = new CharacterActionListener();
    private BombSecondsListener bombSecondsListener = new BombSecondsListener();
    private ServerConnectionListener connectionListener = new ServerConnectionListener();
    private CreatePlayerListener createPlayerListener = new CreatePlayerListener();
    private NewPlayerListener newPlayerListener = new NewPlayerListener();
    private RemovePlayerListener removePlayerListener = new RemovePlayerListener();
    private StartGameListener startGameListener = new StartGameListener();
    private ThrowBombListener throwBombListener = new ThrowBombListener();
    private ExploitBombListener exploitBombListener = new ExploitBombListener();
    private DamageMessageListener damageMessageListener = new DamageMessageListener();
    private StartingNewGameListener startingNewGameListener = new StartingNewGameListener();
    private CounterListener counterListener = new CounterListener();
    private DeadPlayerListener deadPlayerListener = new DeadPlayerListener();
    private WinnerListener winnerListener = new WinnerListener();
    
    private RunningClientAppState runningClientAppState;

    public JBombClient(String ip) {
        this.ip = ip;
    }
    
    public void resetDirections() {
        left = right = front = back = false;
    }

    public void startGame() {
        if (runningClientAppState != null && stateManager.hasState(runningClientAppState)) {
            runningClientAppState.setEnabled(true);
        } else {
            runningClientAppState = new RunningClientAppState();
            stateManager.attach(runningClientAppState);
        }
    }

    public void resetGame() {
        stateManager.getState(RunningClientAppState.class).setEnabled(false);
    }

    @Override
    public void simpleInitApp() {
        super.simpleInitApp();
        try {
            if (ip == null) {
                ip = "localhost";
            }
            client = Network.connectToServer(ip, 6789);
            addMessageListeners();
            client.addClientStateListener(connectionListener);
            client.start();
        } catch (IOException ex) {
            LOGGER.error("Error al conectar con el servidor", ex);
        }
        guiNode.detachAllChildren();
        backgroundSound = new BackgroundSound("background.ogg", 5f);
        initPictures();
        ClientContext.APP = this;
        ClientContext.CLIENT = client;
    }

    public void initGuiCounter() {
        guiNode.detachAllChildren();
        counterPicture.setImage(assetManager, "jbomb/assets/interfaces/pictures/ready3.png", true);
        counterPicture.setWidth(128f);
        counterPicture.setHeight(128f);
        counterPicture.setLocalTranslation(settings.getWidth() / 2 - 64f, settings.getHeight() / 2 - 64f, 0f);
        guiNode.attachChild(counterPicture);
    }

    public void changeCounter(byte num) {
        counterPicture.setImage(assetManager, "jbomb/assets/interfaces/pictures/ready" + num + ".png", true);
    }

    public void initInterfaces() {
        guiNode.attachChild(pointer);
        guiNode.attachChild(getBombsPictures());
        guiNode.attachChild(getBombsSecondsPictures());
    }

    public void initHealthMarker() {
        enqueue(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                health = new HashMap<Integer, BitmapText>();
                Object o = null;
                Player pl = null;
                BitmapText bmt = null;
                Picture picture = null;
                float up = 0f;
                int id = ClientContext.PLAYER.getIdUserData();
                health.put(id, new BitmapText(guiFont, false));
                picture = new Picture("HealthPlayer" + id);
                picture.setImage(assetManager, "jbomb/assets/interfaces/pictures/" + (id + 1) + ".png", true);
                picture.setWidth(32f);
                picture.setHeight(32f);
                picture.setLocalTranslation(settings.getWidth() - 32f - 5f, 130f + up, 0f);
                guiNode.attachChild(picture);
                bmt = health.get(id);
                bmt.setText("100%");
                bmt.setLocalTranslation(settings.getWidth() - (32f + 10f) * 2, 157f + up, 0f);
                guiNode.attachChild(bmt);
                up += 50f;
                for (long l : JBombContext.MANAGER.keySet()) {
                    o = JBombContext.MANAGER.getPhysicObject(l);
                    if (o instanceof Player) {
                        pl = (Player) o;
                        health.put(pl.getIdUserData(), new BitmapText(guiFont, false));
                    }
                }
                for (Integer i : health.keySet()) {
                    if (i == id)
                        continue;
                    picture = new Picture("HealthPlayer" + i);
                    picture.setImage(assetManager, "jbomb/assets/interfaces/pictures/" + (i + 1) + ".png", true);
                    picture.setWidth(32f);
                    picture.setHeight(32f);
                    picture.setLocalTranslation(settings.getWidth() - 32f - 5f, 130f + up, 0f);
                    guiNode.attachChild(picture);
                    
                    bmt = health.get(i);
                    bmt.setText("100%");
                    bmt.setLocalTranslation(settings.getWidth() - (32f + 10f) * 2, 157f + up, 0f);
                    guiNode.attachChild(bmt);
                    up += 50f;
                }
                return null;
            }
        });
    }

    public void initMappings() {
        inputManager.addMapping("shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Front", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Back", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("one", new KeyTrigger(KeyInput.KEY_1), new KeyTrigger(KeyInput.KEY_NUMPAD1));
        inputManager.addMapping("two", new KeyTrigger(KeyInput.KEY_2), new KeyTrigger(KeyInput.KEY_NUMPAD2));
        inputManager.addMapping("three", new KeyTrigger(KeyInput.KEY_3), new KeyTrigger(KeyInput.KEY_NUMPAD3));
    }

    public void addListeners() {
        inputManager.addListener(shootsActionListener, "shoot");
        inputManager.addListener(characterActionListener, "Left");
        inputManager.addListener(characterActionListener, "Right");
        inputManager.addListener(characterActionListener, "Front");
        inputManager.addListener(characterActionListener, "Back");
        inputManager.addListener(characterActionListener, "Jump");
        inputManager.addListener(bombSecondsListener, "one");
        inputManager.addListener(bombSecondsListener, "two");
        inputManager.addListener(bombSecondsListener, "three");
    }

    public void removeListeners() {
        inputManager.removeListener(shootsActionListener);
        inputManager.removeListener(characterActionListener);
        inputManager.removeListener(bombSecondsListener);
    }

    public Camera getCam() {
        return cam;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setFront(boolean front) {
        this.front = front;
    }

    public void setBack(boolean back) {
        this.back = back;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isFront() {
        return front;
    }

    public boolean isBack() {
        return back;
    }

    public Picture getBombsPictures() {
        return bombsPictures;
    }

    public Picture getBombsSecondsPictures() {
        return BombsSecondsPictures;
    }

    @SuppressWarnings("unchecked")
	@Override
    public void addMessageListeners() {
        client.addMessageListener(createPlayerListener, CreatePlayerMessage.class);
        client.addMessageListener(newPlayerListener, NewPlayerMessage.class);
        client.addMessageListener(removePlayerListener, RemovePlayerMessage.class);
        client.addMessageListener(startGameListener, StartGameMessage.class);
        client.addMessageListener(throwBombListener, ThrowBombMessage.class);
        client.addMessageListener(exploitBombListener, ExploitBombMessage.class);
        client.addMessageListener(damageMessageListener, DamageMessage.class);
        client.addMessageListener(startingNewGameListener, StartingNewGameMessage.class);
        client.addMessageListener(counterListener, CounterMessage.class);
        client.addMessageListener(deadPlayerListener, DeadPlayerMessage.class);
        client.addMessageListener(winnerListener, WinnerMessage.class);
        AbstractManager<Client> m = (AbstractManager<Client>) getManager();
        client.addMessageListener(m, CharacterMovesMessage.class);
        client.addMessageListener(m, CoordinateBombMessage.class);
        client.addMessageListener(m, ElevatorMovesMessage.class);
    }

    @Override
    public void destroy() {
        if (client != null && client.isConnected()) {
            client.close();
        }
        super.destroy();
    }

    @Override
    protected AbstractManager<?> createManager() {
        return new ClientManager();
    }

    @Override
    public void loadLog4jConfig() {
        URL urlConfig = BaseGame.class.getResource("/jbomb/client/config/log4j.xml");
        DOMConfigurator.configure(urlConfig);
    }

    public BitmapText getHealtWithId(int id) {
        return health.get(id);
    }

    public void cleanScreen() {
        guiNode.detachAllChildren();
    }
    
    public void cleanScreenBombs() {
        guiNode.detachChild(bombsPictures);
        guiNode.detachChild(BombsSecondsPictures);
    }
    
    public void youAreLooser() {
        LOGGER.debug("Estableciendo imágen perdedor");
        guiNode.attachChild(looser);
    }
    
    public void youAreWinner() {
        LOGGER.debug("Estableciendo imágen ganador");
        guiNode.attachChild(winner);
        guiNode.detachChild(pointer);
        youWinner.setLocalTranslation(
                settings.getWidth() / 2 - youWinner.getLineWidth() / 2,
                settings.getHeight() / 2 + youWinner.getLineHeight() / 2,
                1);
        guiNode.attachChild(youWinner);
    }
    
    private void initPictures() {
        looser.setImage(assetManager, "jbomb/assets/interfaces/pictures/looser.png", true);
        looser.addControl(new WinnerLoserControl());
        looser.setWidth(324f);
        looser.setHeight(594f);
        looser.setLocalTranslation(settings.getWidth() / 2 - 162f, settings.getHeight() / 2 - 247f, 0f);
        winner.setImage(assetManager, "jbomb/assets/interfaces/pictures/winner.png", true);
        winner.addControl(new WinnerLoserControl());
        winner.setWidth(512f);
        winner.setHeight(512f);
        winner.setLocalTranslation(settings.getWidth() / 2 - 256f, settings.getHeight() / 2 - 256f, 0f);
        getBombsPictures().setImage(assetManager, "jbomb/assets/interfaces/pictures/bomb1.png", true);
        getBombsPictures().setWidth(64f);
        getBombsPictures().setHeight(51f);
        getBombsPictures().setLocalTranslation(settings.getWidth() - 64f - 5f, 0f, 0f);
        getBombsSecondsPictures().setImage(assetManager, "jbomb/assets/interfaces/pictures/glass_numbers_1.png", true);
        getBombsSecondsPictures().setWidth(45f);
        getBombsSecondsPictures().setHeight(45f);
        getBombsSecondsPictures().setLocalTranslation(settings.getWidth() - 45 - 5f, 55f, 0f);
        winnerMessage = new BitmapText(guiFont, false);
        winnerMessage.setSize(guiFont.getCharSet().getRenderedSize() * 1.5f);
        winnerMessage.setText("El ganador es el jugador #");
        pointer = new BitmapText(guiFont, false);
        pointer.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        pointer.setText("+");
        pointer.setLocalTranslation(
                settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
                settings.getHeight() / 2 + pointer.getLineHeight() / 2, 0);
        wait = new BitmapText(guiFont, false);
        wait.setSize(guiFont.getCharSet().getRenderedSize() * 1.5f);
        wait.setText("Espere unos momentos por favor");
        wait.setLocalTranslation(
                settings.getWidth() / 2 - wait.getLineWidth() / 2,
                settings.getHeight() / 2 - 10f,
                1);
        youWinner = new BitmapText(guiFont, false);
        youWinner.setSize(guiFont.getCharSet().getRenderedSize() * 1.5f);
        youWinner.setText("¡Felicitaciones tú eres el ganador!");
    }
    
    public void printWinnerMessage(int id) {
        guiNode.detachChild(pointer);
        String message = winnerMessage.getText();
        String color = null;
        switch(id) {
            case 0:
                color = "azul";
                break;
            case 1:
                color = "rojo";
                break;
            case 2:
                color = "amarillo";
                break;
            case 3:
                color = "verde";
                break;
        }
        message = message.replace("#", color);
        winnerMessage.setText(message);
        winnerMessage.setLocalTranslation(
                settings.getWidth() / 2 - winnerMessage.getLineWidth() / 2,
                settings.getHeight() / 2 + winnerMessage.getLineHeight() / 2,
                1);
        guiNode.attachChild(winnerMessage);
    }
    
    public void waitMoment() {
        guiNode.attachChild(wait);
    }

    public Sound getBackgroundSound() {
        return backgroundSound;
    }
}
