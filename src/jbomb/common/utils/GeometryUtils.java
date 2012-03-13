package jbomb.common.utils;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import jbomb.common.game.JBombContext;

public class GeometryUtils {

    public static Geometry makeCube(float x, float y, float z, String name, String texturePath, Vector3f localTranslation, Vector2f scaleTexture, boolean transparent, Node node) {
        Box box = new Box(Vector3f.ZERO, x, y, z);
        Geometry geometry = new Geometry(name, box);
        Material material = new Material(JBombContext.ASSET_MANAGER, MatDefs.UNSHADED);
        Texture texture = JBombContext.ASSET_MANAGER.loadTexture(texturePath);
        material.setTexture("ColorMap", texture);
        geometry.setMaterial(material);
        geometry.setLocalTranslation(localTranslation);
        if (scaleTexture != null) {
            box.scaleTextureCoordinates(scaleTexture.mult(2));
            texture.setWrap(Texture.WrapMode.Repeat);
        }
        if (transparent) {
            geometry.getMaterial().getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            geometry.setQueueBucket(RenderQueue.Bucket.Transparent);
        }
        RigidBodyControl physics = new RigidBodyControl(0f);
        geometry.addControl(physics); 
        JBombContext.PHYSICS_SPACE.add(physics);
        if (node == null)
            JBombContext.ROOT_NODE.attachChild(geometry);
        else
            node.attachChild(geometry);
        return geometry;
    }
    
    public static Geometry makeCube(float x, float y, float z, String name, String texturePath, Vector3f localTranslation, Vector2f scaleTexture, boolean transparent) {
        return makeCube(x, y, z, name, texturePath, localTranslation, scaleTexture, transparent, null);
    }
    
    public static Geometry makeCube(float x, float y, float z, String name, String texturePath, Vector3f localTranslation, Vector2f scaleTexture) {
        return makeCube(x, y, z, name, texturePath, localTranslation, scaleTexture, false, null);
    }
    
    public static Geometry makeCube(float x, float y, float z, String name, String texturePath, Vector3f localTranslation, boolean transparent) {
        return makeCube(x, y, z, name, texturePath, localTranslation, null, transparent, null);
    }

    public static Geometry makeCube(float x, float y, float z, String name, String texturePath, Vector3f localTranslation) {
        return makeCube(x, y, z, name, texturePath, localTranslation, null, false, null);
    }    
    
    public static Geometry makePlane(float x, float y, String name, String texturePath, Vector3f localTranslation, Quaternion quaternion, Vector2f scaleTexture, boolean transparent) {
        Quad quad = new Quad(x, y);
        Geometry geometry = new Geometry(name, quad);
        Material material = new Material(JBombContext.ASSET_MANAGER, MatDefs.UNSHADED);
        Texture texture = JBombContext.ASSET_MANAGER.loadTexture(texturePath);
        material.setTexture("ColorMap", texture);
        geometry.setMaterial(material);
        geometry.setLocalTranslation(localTranslation);
        if (quaternion != null)
            geometry.setLocalRotation(quaternion);
        if (scaleTexture != null) {
            quad.scaleTextureCoordinates(scaleTexture);
            texture.setWrap(Texture.WrapMode.Repeat);
        }
        if (transparent) {
            geometry.getMaterial().getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            geometry.setQueueBucket(RenderQueue.Bucket.Transparent);
        }
        RigidBodyControl physics = new RigidBodyControl(0f);
        geometry.addControl(physics); 
        JBombContext.PHYSICS_SPACE.add(physics);
        JBombContext.ROOT_NODE.attachChild(geometry);
        return geometry;
    }
    
    public static Geometry makePlane(float x, float y, String name, String texturePath, Vector3f localTranslation, Quaternion quaternion, Vector2f scaleTexture) {
        return makePlane(x, y, name, texturePath, localTranslation, quaternion, scaleTexture, false);
    }
    
    public static Geometry makePlane(float x, float y, String name, String texturePath, Vector3f localTranslation, Quaternion quaternion, boolean transparent) {
        return makePlane(x, y, name, texturePath, localTranslation, quaternion, null, transparent);
    }

    public static Geometry makePlane(float x, float y, String name, String texturePath, Quaternion quaternion, Vector3f localTranslation) {
        return makePlane(x, y, name, texturePath, localTranslation, quaternion, null, false);
    }  
    
    public static Geometry makePlane(float x, float y, String name, String texturePath, Vector3f localTranslation) {
        return makePlane(x, y, name, texturePath, localTranslation, null, null, false);
    } 
    
    public static Geometry makePlane(float x, float y, String name, String texturePath, Vector3f localTranslation, Vector2f scaleTexture) {
        return makePlane(x, y, name, texturePath, localTranslation, null, scaleTexture, false);
    }
    
    public static Geometry makePlane(float x, float y, String name, String texturePath, Vector3f localTranslation, boolean transparent) {
        return makePlane(x, y, name, texturePath, localTranslation, null, null, transparent);
    }
    
    public static Geometry makePlane(float x, float y, String name, String texturePath, Vector3f localTranslation, Vector2f scaleTexture, boolean transparent) {
        return makePlane(x, y, name, texturePath, localTranslation, null, scaleTexture, transparent);
    }
}
