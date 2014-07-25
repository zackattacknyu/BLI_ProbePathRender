/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTrackingOnSurface;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zrd.geometryToolkit.locationTracking.RotationCalibrationTool;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.meshTraversal.CalibrationHelper;
import org.zrd.geometryToolkit.meshTraversal.RotationCalibration;
import org.zrd.geometryToolkit.meshTraversal.ScaleCalibration;
import org.zrd.jmeGeometryInteractions.meshPathInteractions.PickTwoPointsOnMesh;
import org.zrd.probeTracking.ProbeTracker;
import org.zrd.util.dataWriting.ProbeDataWriter;
import org.zrd.util.fileHelper.FileDataHelper;

/**
 * 
 * IMPORTANT: 
 * Idea for future rotation calibration:
 *      1. Do the same procedure but for the 4 paths
 *          between calibration points.
 *      2. Average together the Quaternions and also calculate the
 *          standard deviation
 *      3. If the standard deviation is too high, then order
 *          the user to retry the calibration
 *
 * @author BLI
 */
public class ProbeRotationCalibration extends PickTwoPointsOnMesh implements RotationCalibrationTool{

    private boolean rotationCalibrationDone = false;
    private ProbeTracker probeTracker;
    private Path resultFilePath;
    private TriangleSet meshInfo;
    private Vector3f calibEndPoint;
    
    public ProbeRotationCalibration(InputManager inputManager, Camera cam, 
            Node shootableMesh, ProbeTracker probeTracker, TriangleSet meshInfo,
            Path resultFilePath){
        super("probeCalibAction","pickPointForProbeCalib",KeyInput.KEY_B,inputManager,cam,shootableMesh,meshInfo);
        this.probeTracker = probeTracker;
        this.resultFilePath = resultFilePath;
    }

    public boolean isRotationCalibrationDone() {
        return rotationCalibrationDone;
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
        
        calibEndPoint = endPoint.clone();
        
        probeTracker.startStopRecording();
        //probeTracker.rescaleCoordinates(scaleCalib.getUniformScaleFactor());
        //probeTracker.addendRotationCalibration(rotCalib.getAggregateRotation());
        CalibrationHelper.writeCalibrationResults(scaleCalib.getUniformScaleFactor(),rotCalib.getAggregateRotation(),resultFilePath);
        probeTracker.setCurrentPosition(endPoint);
    }

    @Override
    protected ArrayList<Vector3f> getActivePathAtEndpoint() {
        return probeTracker.getCurrentPathVertices();
    }

    public boolean isCalibrationDone() {
        return rotationCalibrationDone;
    }

    public boolean isCalibrationNewlyFinished() {
        return arePointsNewlyPicked();
    }

    public MeshTriangle getTriangleAtEndPoint() {
        return getEndingTriangle();
    }

    public Vector3f getCalibEndPoint() {
        return calibEndPoint;
    }
    
}
