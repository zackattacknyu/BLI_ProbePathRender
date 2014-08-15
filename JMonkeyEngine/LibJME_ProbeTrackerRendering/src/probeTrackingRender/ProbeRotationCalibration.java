/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probeTrackingRender;

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
import org.zrd.geometryToolkit.pointTools.FixedPointPicker;
import org.zrd.geometryToolkit.probeCalibration.ScaleCalibration;
import org.zrd.jmeGeometryIO.oldRenderedObjects.FixedPointsOnLolaMesh;
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
        probeTracker.setCurrentPosition(startPoint);
        probeTracker.startStopRecording();
    }

    @Override
    protected void handleEndPointResult(Vector3f endPoint, ScaleCalibration scaleCalib, PathOnMeshCalculator rotCalib, ArrayList<Vector3f> scaledAndRotatedPath) {
        System.out.println("End Point set at: " + endPoint);
        
        rotationCalibrationDone = true;
        
        calibEndPoint = endPoint.clone();
        
        probeTracker.startStopRecording();
        //probeTracker.rescaleCoordinates(scaleCalib.getUniformScaleFactor());
        //probeTracker.addendRotationCalibration(rotCalib.getAggregateRotation());
        CalibrationHelper.writeCalibrationResults(scaleCalib.getUniformScaleFactor(),
                rotCalib.getAggregateRotation(),probeTracker.getCurrentQualityResults(),resultFilePath);
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
