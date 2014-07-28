/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryInteractions.meshInteraction;

import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

/**
 *
 * @author BLI
 */
public class CollisionHelper {
    
    
    public static CollisionResults getRayMeshCollisions(Ray ray,Node shootableMesh){
        CollisionResults results = new CollisionResults();
        shootableMesh.collideWith(ray, results);
        return results;
    }
    
    public static Ray getMouseClickRay(Vector2f mousePosition, Camera cam){
        Vector3f click3d = cam.getWorldCoordinates(
            new Vector2f(mousePosition.x, mousePosition.y), 0f).clone();
        Vector3f dir = cam.getWorldCoordinates(
            new Vector2f(mousePosition.x, mousePosition.y), 1f).subtractLocal(click3d).normalizeLocal();
        return new Ray(click3d, dir);
    }
    
    public static Ray getCurrentMouseClickRay(InputManager inputManager, 
            Camera cam){
        return getMouseClickRay(inputManager.getCursorPosition(),cam);
    }
    
    public static CollisionResults getCurrentCollisions(InputManager inputManager, 
            Camera cam, Node shootableMesh){
        Ray currentRay = getCurrentMouseClickRay(inputManager,cam);
        return getRayMeshCollisions(currentRay,shootableMesh);
    }
    
}
