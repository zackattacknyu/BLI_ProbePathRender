/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTrackingRender;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.nio.file.Path;
import java.util.ArrayList;
import org.zrd.geometryToolkit.probeCalibration.RotationCalibrationTool;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.probeCalibration.CalibrationHelper;
import org.zrd.geometryToolkit.meshTraversal.PathOnMeshCalculator;
import org.zrd.geometryToolkit.pathDataStructure.SegmentSet;
import org.zrd.geometryToolkit.pointTools.FixedPointPicker;
import org.zrd.geometryToolkit.probeCalibration.ReflectionCalibration;
import org.zrd.geometryToolkit.probeCalibration.ScaleCalibration;
import org.zrd.jmeGeometryInteractions.meshPathInteractions.PickTwoPointsOnMesh;
import org.zrd.probeTracking.ProbeTracker;

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
 * TODO: 
 *      - Have User specify number of points to input
 *      - User will then travel to each of those points
 *          and with each pair will be a new calibration calculation
 *
 * @author BLI
 */
public class ProbeRotationCalibration extends PickTwoPointsOnMesh implements RotationCalibrationTool{

    private boolean rotationCalibrationDone = false;
    private ProbeTracker probeTracker;
    private Path resultFilePath;
    private TriangleSet meshInfo;
    private Vector3f calibEndPoint;
    
    private Vector3f initX,initY,initNormal;
    
    public ProbeRotationCalibration(InputManager inputManager, Camera cam, 
            Node shootableMesh, ProbeTracker probeTracker, TriangleSet meshInfo,
            Path resultFilePath, FixedPointPicker fixedPtPicker){
        super("probeCalibAction","pickPointForProbeCalib",KeyInput.KEY_B,inputManager,cam,shootableMesh,meshInfo,fixedPtPicker);
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
        System.out.println("Start Point set at: " + startPoint);
        System.out.println("Hit space bar before selecting the end point");
        System.out.println("Press B to cancel and only produce the align normal calibration");
        probeTracker.setCurrentPosition(startPoint);
        probeTracker.startStopRecording();

        CalibrationHelper.writeAlignNormalResults(
                probeTracker.getTrackerNormal().clone(),
                startingTriangle.getNormal().clone(), 
                resultFilePath);
        
        initX = probeTracker.getTrackerX().clone().normalize();
        initY = probeTracker.getTrackerY().clone().normalize();
        initNormal = probeTracker.getTrackerNormal().clone().normalize();
    }

    @Override
    protected void handleEndPointResult(Vector3f endPoint,ScaleCalibration scaleCalib, 
        PathOnMeshCalculator rotCalib, SegmentSet scaledAndRotatedPath) {
        System.out.println("End Point set at: " + endPoint);
        
        rotationCalibrationDone = true;
        
        calibEndPoint = endPoint.clone();
        
        probeTracker.startStopRecording();
        ReflectionCalibration reflectionCalib = new ReflectionCalibration(initX,initY,initNormal,rotCalib);
        CalibrationHelper.writeCalibrationResults(
                scaleCalib.getUniformScaleFactor(),
                probeTracker.getRotationCalibration(),
                rotCalib.getAggregateRotation(),
                probeTracker.getCurrentQualityResults(),
                reflectionCalib.getResults(),
                resultFilePath);

        probeTracker.setCurrentPosition(endPoint);
    }

    @Override
    protected SegmentSet getActivePathAtEndpoint() {
        return new SegmentSet(probeTracker.getCurrentPathVertices());
    }

    @Override
    public boolean isCalibrationDone() {
        return rotationCalibrationDone;
    }

    @Override
    public boolean isCalibrationNewlyFinished() {
        return arePointsNewlyPicked();
    }

    @Override
    public MeshTriangle getTriangleAtEndPoint() {
        return getEndingTriangle();
    }

    @Override
    public Vector3f getCalibEndPoint() {
        return calibEndPoint;
    }
    
}
