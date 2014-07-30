/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTrackingOnSurface;

import org.zrd.jmeGeometryInteractions.meshInteraction.PickPointOnMesh;
import org.zrd.geometryToolkit.pointTools.MeshPointHandler;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import org.zrd.geometryToolkit.locationTracking.LocationTracker;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.pointTools.FixedPointPicker;
import org.zrd.jmeGeometryIO.renderedObjects.FixedPointsOnLolaMesh;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;
import org.zrd.probeTracking.ProbeTracker;

/**
 *
 * @author BLI
 */
public class ProbeMoveAction extends GeneralKeyboardActionMethod implements MeshPointHandler{

    private boolean moveProbeEnabled = false;
    private String probeMoveModeText;
    private LocationTracker activeTracker;
    
    public ProbeMoveAction(InputManager inputManager, Camera cam, Node shootableMesh, LocationTracker probeTracker){
        super(inputManager,"probeMoveAction",KeyInput.KEY_J);
        new PickPointOnMesh("pickPointForProbeMove",inputManager,cam,this,shootableMesh,null);
        this.activeTracker = probeTracker;
    }
    
    public ProbeMoveAction(InputManager inputManager, Camera cam, Node shootableMesh, LocationTracker probeTracker, FixedPointPicker fixedPtPicker){
        super(inputManager,"probeFixedPointMoveAction",KeyInput.KEY_K);
        new PickPointOnMesh("pickFixedPointForProbeMove",inputManager,cam,this,shootableMesh,fixedPtPicker);
        this.activeTracker = probeTracker;
    }
    
    @Override
    public void actionMethod() {
        moveProbeEnabled = !moveProbeEnabled;
        if(moveProbeEnabled){
            probeMoveModeText = "Press J to Disable Clicking Probe Movement";
        }else{
            probeMoveModeText = "Press J to Enable Clicking Probe Movement";
        }
    }

    public void handleNewMeshPoint(Vector3f pointOnMesh, Triangle triangleOnMesh) {
        if(moveProbeEnabled){
            activeTracker.setCurrentPosition(pointOnMesh);
        }
    }

    public String getProbeMoveModeText() {
        return probeMoveModeText;
    }

    public void handleNewMeshPoint(Vector3f pointOnMesh, MeshTriangle triangleOnMesh) {
        handleNewMeshPoint(pointOnMesh,triangleOnMesh.getTriangleData());
    }
    
    
    
}
