/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probeTrackingRender;

import org.zrd.jmeGeometryInteractions.meshInteraction.PickPointOnMesh;
import org.zrd.geometryToolkit.pointTools.MeshPointHandler;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.math.Matrix4f;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import org.zrd.geometryToolkit.locationTracking.LocationTracker;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.pointTools.FixedPointPicker;
import org.zrd.geometryToolkit.pointTools.PointsOnMeshTracker;
import org.zrd.jmeGeometryIO.meshIO.MeshInputHelper;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;

/**
 *
 * @author BLI
 */
public class ProbeMoveAction extends GeneralKeyboardActionMethod implements MeshPointHandler,PointsOnMeshTracker{

    private boolean moveProbeEnabled = false;
    private String probeMoveModeText;
    private LocationTracker activeTracker;
    private MeshTriangle currentPickedTriangle;
    private Vector3f currentPickedPoint;
    private Matrix4f transform;
    private PickPointOnMesh meshPointPicking;
    
    private String enableClickingText;
    private String disableClickingText;
    
    public ProbeMoveAction(InputManager inputManager, Camera cam, Node shootableMesh, LocationTracker probeTracker, Matrix4f transform){
        super(inputManager,"probeMoveAction",KeyInput.KEY_J);
        meshPointPicking = new PickPointOnMesh("pickPointForProbeMove",
                inputManager,cam,this,shootableMesh,null);
        this.transform = transform;
        this.activeTracker = probeTracker;
        
        enableClickingText = "Press J to Enable Probe Movement to Clicked Points on Mesh";
        disableClickingText = "Press J to Disable Probe Movement to Clicked Points on Mesh";
        probeMoveModeText = enableClickingText;
    }
    
    public ProbeMoveAction(InputManager inputManager, Camera cam, Node shootableMesh, LocationTracker probeTracker, FixedPointPicker fixedPtPicker){
        super(inputManager,"probeFixedPointMoveAction",KeyInput.KEY_K);
        meshPointPicking = new PickPointOnMesh("pickFixedPointForProbeMove",
                inputManager,cam,this,shootableMesh,fixedPtPicker);
        this.activeTracker = probeTracker;
        
        enableClickingText = "Press K to Enable Probe Movement to Fixed Points on Mesh";
        disableClickingText = "Press K to Disable Probe Movement to Fixed Points on Mesh";
        probeMoveModeText = enableClickingText;
    }
    
    @Override
    public void actionMethod() {
        moveProbeEnabled = !moveProbeEnabled;
        if(moveProbeEnabled){
            probeMoveModeText = disableClickingText;
        }else{
            probeMoveModeText = enableClickingText;
        }
    }
    
    public void setFixedPointSet(FixedPointPicker fixedPtPicker){
        meshPointPicking.setPtPicker(fixedPtPicker);
    }

    @Override
    public void handleNewMeshPoint(Vector3f pointOnMesh, Triangle triangleOnMesh) {
        handleNewMeshPoint(pointOnMesh,
                MeshInputHelper.convertInputTriangleToMeshTriangle(
                triangleOnMesh, transform));
    }
    
    @Override
    public void handleNewMeshPoint(Vector3f pointOnMesh, MeshTriangle triangleOnMesh) {
        if(moveProbeEnabled){
            currentPickedPoint = pointOnMesh.clone();
            activeTracker.setCurrentPosition(currentPickedPoint);
            currentPickedTriangle = triangleOnMesh.clone();
        }
    }

    public String getProbeMoveModeText() {
        return probeMoveModeText;
    }

    @Override
    public MeshTriangle getCurrentTriangleOnMesh() {
        return currentPickedTriangle;
    }

    @Override
    public Vector3f getCurrentPointOnMesh() {
        return currentPickedPoint;
    }
    
    
    
}
