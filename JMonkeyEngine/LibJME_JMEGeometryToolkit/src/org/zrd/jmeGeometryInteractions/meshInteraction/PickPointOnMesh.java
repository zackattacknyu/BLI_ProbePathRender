/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryInteractions.meshInteraction;

import org.zrd.geometryToolkit.geometryUtil.MeshPointHandler;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import org.zrd.jmeUtil.mouseKeyboard.GeneralMouseActionMethod;

/**
 * This is the class that handles the action of selecting a point
 *      on a mesh in the application
 *
 * @author BLI
 */
public class PickPointOnMesh extends GeneralMouseActionMethod{
    
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
    public PickPointOnMesh(String name, InputManager inputManager, Camera cam, MeshPointHandler pointHandler, Node shootableMesh){
        super(inputManager,name,MouseInput.BUTTON_LEFT);
        this.inputManager = inputManager;
        this.cam = cam;
        this.pointHandler = pointHandler;
        this.shootables = shootableMesh;
    }

    @Override
    public void actionMethod() {
        CollisionResults results = CollisionHelper.getCurrentCollisions(inputManager, cam, shootables);
        if(results.size() > 0){
            CollisionPoint point = new CollisionPoint(results.getClosestCollision());
            pointHandler.handleNewMeshPoint(point.getContactPoint(), point.getTriangle());
        }
    }
    
}
