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
import java.util.Arrays;
import org.zrd.geometryToolkit.locationTracking.FixedPointPicker;
import org.zrd.geometryToolkit.locationTracking.PointData;
import org.zrd.geometryToolkit.locationTracking.PointOnMeshData;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
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
    public static final MeshTriangle[] correspondingTriangles = {
        new MeshTriangle(
            new Vector3f(-0.72895795f, -2.0336456f, -14.867519f),
            new Vector3f(-0.47583792f, -1.9271622f, -14.824425f),
            new Vector3f(-0.4999979f, -2.0336838f, -14.926361f)),
        new MeshTriangle(
            new Vector3f(-4.122718f, -2.8099117f, -13.903099f),
            new Vector3f(-4.2539973f, -2.7200909f, -13.752243f),
            new Vector3f(-4.0038376f, -2.677925f, -13.869957f)),
        new MeshTriangle(
            new Vector3f(-0.4999177f, -4.2599983f, -17.215199f),
            new Vector3f(-0.62903774f, -4.1757774f, -17.097366f),
            new Vector3f(-0.37511772f, -4.1455536f, -17.104164f)),
        new MeshTriangle(
            new Vector3f(-3.9985576f, -4.6944504f, -16.025059f),
            new Vector3f(-3.9950378f, -4.770878f, -16.18628f),
            new Vector3f(-4.2582374f, -4.843151f, -15.9549675f))};
    
    public static final PointData[] fixedPointData = {
        new PointOnMeshData(fixedPoints[0],correspondingTriangles[0]),
        new PointOnMeshData(fixedPoints[1],correspondingTriangles[1]),
        new PointOnMeshData(fixedPoints[2],correspondingTriangles[2]),
        new PointOnMeshData(fixedPoints[3],correspondingTriangles[3]),
    };
    
    private InputManager inputManager;
    private Camera cam;
    private Node shootables;
    private MeshPointHandler pointHandler;
    private FixedPointPicker fixedPtPicker;
    
    /**
     * This initializes the point picking action
     * @param name              the name of the action
     * @param inputManager      the application's input manager
     * @param cam               the applicatino's camera
     * @param pointHandler      the pointHandler object that will do something with the result
     * @param shootableMesh     the mesh to pick a point on
     */
    public PickFixedPointOnMesh(String name, InputManager inputManager, 
            Camera cam, MeshPointHandler pointHandler, Node shootableMesh){
        super(inputManager,name,MouseInput.BUTTON_LEFT);
        this.inputManager = inputManager;
        this.cam = cam;
        this.pointHandler = pointHandler;
        this.shootables = shootableMesh;
        fixedPtPicker = new FixedPointPicker(Arrays.asList(fixedPointData));
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
            
            PointOnMeshData ptData = (PointOnMeshData)(fixedPtPicker.getNearestPointData(point.getPointCoords()));

            //gives the method the cloest collision point
            pointHandler.handleNewMeshPoint(ptData.getPointCoords(), ptData.getTriangleContainingPoint());
        }
    }
    
}
