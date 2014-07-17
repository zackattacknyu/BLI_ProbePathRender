/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTrackingOnSurface;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.util.ArrayList;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.meshTraversal.RotationCalibration;
import org.zrd.geometryToolkit.meshTraversal.ScaleCalibration;
import org.zrd.jmeGeometry.meshPathInteractions.PickTwoPointsOnMesh;
import org.zrd.probeTracking.ProbeTracker;

/**
 *
 * @author BLI
 */
public class ProbeRotationCalibration extends PickTwoPointsOnMesh{

    private boolean rotationCalibrationDone = false;
    private ProbeTracker probeTracker;
    private MeshTriangle currentTriangle;
    private TriangleSet meshInfo;
    
    public ProbeRotationCalibration(InputManager inputManager, Camera cam, Node shootableMesh, ProbeTracker probeTracker, TriangleSet meshInfo){
        super("probeCalibAction","pickPointForProbeCalib",KeyInput.KEY_B,inputManager,cam,shootableMesh,meshInfo);
        this.probeTracker = probeTracker;
    }

    public boolean isRotationCalibrationDone() {
        return rotationCalibrationDone;
    }

    public MeshTriangle getCurrentTriangle() {
        return currentTriangle;
    }

    public TriangleSet getMeshInfo() {
        return meshInfo;
    }

    @Override
    protected String messageUponEnabling() {
        return "Calibration enabled. Click on position of probe, then click again.";
    }

    @Override
    protected String messageUponCancelling() {
        return "Calibration Cancelled";
    }

    @Override
    protected void handleStartPoint(Vector3f startPoint) {
        probeTracker.setCurrentPosition(startPoint);
        probeTracker.startStopRecording();
    }

    @Override
    protected void handleEndPointResult(Vector3f endPoint, ScaleCalibration scaleCalib, RotationCalibration rotCalib, ArrayList<Vector3f> scaledAndRotatedPath) {
        rotationCalibrationDone = true;
        
        probeTracker.startStopRecording();
        //probeTracker.rescaleCoordinates(scaleCalib.getUniformScaleFactor());
        probeTracker.setRotationCalibration(rotCalib.getAggregateRotation());
        probeTracker.setCurrentPosition(endPoint);
    }

    @Override
    protected ArrayList<Vector3f> getActivePathAtEndpoint() {
        return probeTracker.getCurrentPathVertices();
    }
    
}
