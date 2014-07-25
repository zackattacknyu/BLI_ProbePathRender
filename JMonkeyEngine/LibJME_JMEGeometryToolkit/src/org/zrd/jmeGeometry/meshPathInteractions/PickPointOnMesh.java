/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometry.meshPathInteractions;

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
import org.zrd.jmeGeometry.meshInteraction.CollisionPoint;

/**
 *
 * @author BLI
 */
public class PickPointOnMesh extends GeneralMouseActionMethod{
    
    private InputManager inputManager;
    private Camera cam;
    private Node shootables;
    private MeshPointHandler pointHandler;
    
    public PickPointOnMesh(InputManager inputManager, Camera cam, MeshPointHandler pointHandler, Node shootableMesh){
        this("pickPointOnMesh",inputManager,cam,pointHandler,shootableMesh);
    }
    
    public PickPointOnMesh(String name, InputManager inputManager, Camera cam, MeshPointHandler pointHandler, Node shootableMesh){
        super(inputManager,name,MouseInput.BUTTON_LEFT);
        this.inputManager = inputManager;
        this.cam = cam;
        this.pointHandler = pointHandler;
        this.shootables = shootableMesh;
    }

    @Override
    public void actionMethod() {
        CollisionResults results = getCollisionResults();
        if(results.size() > 0){
            CollisionPoint point = new CollisionPoint(results.getClosestCollision());
            pointHandler.handleNewMeshPoint(point.getContactPoint(), point.getTriangle());
        }
    }
    
    private CollisionResults getCollisionResults(){
        Vector2f click2d = inputManager.getCursorPosition();
        System.out.println("Mouse Point:" + click2d);

        CollisionResults results = new CollisionResults();
        Vector3f click3d = cam.getWorldCoordinates(
            new Vector2f(click2d.x, click2d.y), 0f).clone();
        Vector3f dir = cam.getWorldCoordinates(
            new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
        Ray ray = new Ray(click3d, dir);
        shootables.collideWith(ray, results);
        return results;
    }
    
}
