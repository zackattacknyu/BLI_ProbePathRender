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
 * This contains general helper methods for handling ray-mesh collisions.
 *      For now, it calls the code that calculates the ray-mesh collision
 *      when the user clicks a point on the mesh. 
 *
 * @author BLI
 */
public class CollisionHelper {
    
    /**
     * This gets the collision points between a ray and a mesh
     * @param ray               the ray being shot
     * @param shootableMesh     the mesh that it is supposed to hit
     * @return                  the collision results
     */
    public static CollisionResults getRayMeshCollisions(Ray ray,Node shootableMesh){
        CollisionResults results = new CollisionResults();
        shootableMesh.collideWith(ray, results);
        return results;
    }
    
    /**
     * This takes in a mouse position and camera object and returns the ray
     *      that results from that mouse position as the starting point
     *      and the direction being the direction that the camera is pointing
     *      in at that point
     * @param mousePosition     cursor position on screen
     * @param cam               camera object for current camer
     * @return                  ray being shot from the cursor position
     */
    public static Ray getMouseClickRay(Vector2f mousePosition, Camera cam){
        Vector3f click3d = cam.getWorldCoordinates(
            new Vector2f(mousePosition.x, mousePosition.y), 0f).clone();
        Vector3f dir = cam.getWorldCoordinates(
            new Vector2f(mousePosition.x, mousePosition.y), 1f).subtractLocal(click3d).normalizeLocal();
        return new Ray(click3d, dir);
    }
    
    /**
     * This is meant to be called when the user clicks the mouse as it gets
     *      the current ray that is being shot from the mouse position
     *      in the direction of the camera
     * @param inputManager      the application's input manager which knows the mouse position
     * @param cam               the application's camera
     * @return                  the ray currently being shot from the mouse cursor in the camera direction
     */
    public static Ray getCurrentMouseClickRay(InputManager inputManager, 
            Camera cam){
        return getMouseClickRay(inputManager.getCursorPosition(),cam);
    }
    
    /**
     * This is meant to be called when the user clicks the mosue as it gets
     *      the points on the mesh that the user clicked on
     * @param inputManager      the application's input manager which knows the mouse position
     * @param cam               the application's camera
     * @param shootableMesh     the mesh to shoot at
     * @return                  the results of the collision of the ray from the mouse click and the mesh
     */
    public static CollisionResults getCurrentCollisions(InputManager inputManager, 
            Camera cam, Node shootableMesh){
        Ray currentRay = getCurrentMouseClickRay(inputManager,cam);
        return getRayMeshCollisions(currentRay,shootableMesh);
    }
    
}
