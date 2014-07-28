/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryInteractions.meshInteraction;

import org.zrd.geometryToolkit.geometryUtil.MeshPointHandler;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import org.zrd.jmeUtil.mouseKeyboard.GeneralMouseActionMethod;

/**
 * This is the class that handles the action of selecting a point
 *      on a mesh in the application. It takes in a MeshPointHandler
 *      object which is an interface for any class that takes
 *      in a point being selected on the mesh and processes that.
 *
 * @author BLI
 */
public class PickFixedPointOnMesh extends GeneralMouseActionMethod{
    
    //TODO: make this come from properties file
    public static final Vector3f[] fixedPoints = {
        new Vector3f(-0.51208466f, -2.015924f, -14.905227f),
        new Vector3f(-4.103265f, -2.7216837f, -13.844656f),
        new Vector3f(-0.42267808f, -4.185834f, -17.14265f),
        new Vector3f(-4.0077333f, -4.704981f, -16.033361f)};
    
    private InputManager inputManager;
    private Camera cam;
    private Node shootables;
    private MeshPointHandler pointHandler;
    
    /**
     * This initializes the point picking action
     * @param name              the name of the action
     * @param inputManager      the application's input manager
     * @param cam               the applicatino's camera
     * @param pointHandler      the pointHandler object that will do something with the result
     * @param shootableMesh     the mesh to pick a point on
     */
    public PickFixedPointOnMesh(String name, InputManager inputManager, Camera cam, MeshPointHandler pointHandler, Node shootableMesh){
        super(inputManager,name,MouseInput.BUTTON_LEFT);
        this.inputManager = inputManager;
        this.cam = cam;
        this.pointHandler = pointHandler;
        this.shootables = shootableMesh;
    }

    /**
     * This gets called when the mouse button is clicked and it calls
     *      the methods that care about the mouse being clicked. 
     * It gives the method that cares about the mouse click only
     *      the closest collision point
     */
    @Override
    public void actionMethod() {
        //find the collision results
        CollisionResults results = CollisionHelper.getCurrentCollisions(inputManager, cam, shootables);
        
        //if a result was found, process it
        if(results.size() > 0){
            
            //uses the closes collision as the one that matters
            CollisionPoint point = new CollisionPoint(results.getClosestCollision());
            
            Vector3f currentPoint = point.getContactPoint().clone();
            
            float minDistance = Float.MAX_VALUE;
            int minIndex = 0;
            int currIndex = 0;
            float currentDist;
            for(Vector3f fixedPt: fixedPoints){
                currentDist = fixedPt.clone().distance(currentPoint.clone());
                if(currentDist < minDistance){
                    minDistance = currentDist;
                    minIndex = currIndex;
                }
                currIndex++;
            }
            
            //gives the method the cloest collision point
            pointHandler.handleNewMeshPoint(fixedPoints[minIndex], null);
        }
    }
    
}
