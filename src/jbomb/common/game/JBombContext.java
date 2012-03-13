package jbomb.common.game;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.scene.Node;
import jbomb.common.appstates.AbstractManager;

public class JBombContext {

    protected JBombContext() {}

    public static AssetManager ASSET_MANAGER;
    public static Node ROOT_NODE;
    public static PhysicsSpace PHYSICS_SPACE;
    public static BaseGame BASE_GAME;
    public static AbstractManager<?> MANAGER;
    public static float MESSAGES_PER_SECOND = 20f;
    public static Node NODE_ELEVATORS;
}
