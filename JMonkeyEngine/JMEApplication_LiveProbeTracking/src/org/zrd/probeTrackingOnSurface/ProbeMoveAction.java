/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTrackingOnSurface;

import org.zrd.jmeGeometryInteractions.meshInteraction.PickPointOnMesh;
import org.zrd.geometryToolkit.geometryUtil.MeshPointHandler;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;
import org.zrd.probeTracking.ProbeTracker;

/**
 *
 * @author BLI
 */
public class ProbeMoveAction extends GeneralKeyboardActionMethod implements MeshPointHandler{

    private boolean moveProbeEnabled = false;
    private String probeMoveModeText;
    private ProbeTracker probeTracker;
    
    public ProbeMoveAction(InputManager inputManager, Camera cam, Node shootableMesh, ProbeTracker probeTracker){
        super(inputManager,"probeMoveAction",KeyInput.KEY_J);
        new PickPointOnMesh("pickPointForProbeMove",inputManager,cam,this,shootableMesh);
        this.probeTracker = probeTracker;
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
            probeTracker.setCurrentPosition(pointOnMesh);
        }
    }

    public String getProbeMoveModeText() {
        return probeMoveModeText;
    }

    public void handleNewMeshPoint(Vector3f pointOnMesh, MeshTriangle triangleOnMesh) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
