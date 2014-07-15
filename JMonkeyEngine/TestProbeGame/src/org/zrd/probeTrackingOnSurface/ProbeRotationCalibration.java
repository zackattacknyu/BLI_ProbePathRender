/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTrackingOnSurface;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.util.ArrayList;
import org.zrd.geometryToolkit.geometryUtil.ProgramConstants;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.meshTraversal.RotationCalibration;
import org.zrd.geometryToolkit.meshTraversal.ScaleCalibration;
import org.zrd.geometryToolkit.pathTools.PathCompression;
import org.zrd.graphicsToolsImpl.meshImpl.MeshHelper;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;
import org.zrd.probeTracking.ProbeTracker;

/**
 *
 * @author BLI
 */
public class ProbeRotationCalibration extends GeneralKeyboardActionMethod implements MeshPointHandler{

    private boolean calibrationEnabled = false;
    private boolean onStartPoint = true;
    private Vector3f lastPointClicked;
    private ProbeTracker probeTracker;
    private MeshTriangle startingTriangle;
    private TriangleSet meshInfo;
    private ArrayList<Vector3f> currentPath;
    
    public ProbeRotationCalibration(InputManager inputManager, Camera cam, Node shootableMesh, ProbeTracker probeTracker, TriangleSet meshInfo){
        super(inputManager,"calibrationAction",KeyInput.KEY_B);
        new PickPointOnMesh("pickPointForRotCalibration",inputManager,cam,this,shootableMesh);
        this.probeTracker = probeTracker;
        this.meshInfo = meshInfo;
    }
    
    @Override
    public void actionMethod() {
        if(!calibrationEnabled){
            System.out.println("Calibration enabled. Click on position of probe, then click again.");
            calibrationEnabled = true;
        }else{
            System.out.println("Calibration Cancelled");
            calibrationEnabled = false;
        }
    }

    public void handleNewMeshPoint(Vector3f pointOnMesh, Triangle triangleOnMesh) {
        if(calibrationEnabled){
                                
            if(onStartPoint){

                Vector3f startPoint = pointOnMesh.clone();
                if(!startPoint.equals(lastPointClicked)){

                    lastPointClicked = startPoint;
                    probeTracker.setCurrentPosition(startPoint);
                    probeTracker.startStopRecording();
                    startingTriangle = MeshHelper.convertInputTriangleToMeshTriangle(triangleOnMesh, meshInfo.getTransform());
                    onStartPoint = false;
                }

            }else{

                Vector3f endPoint = pointOnMesh.clone();
                if(!endPoint.equals(lastPointClicked)){

                    lastPointClicked = endPoint;
                    probeTracker.startStopRecording();
                    currentPath = probeTracker.getCurrentPathVertices();
                    ScaleCalibration currentScaleCalib = new ScaleCalibration(currentPath,endPoint);
                    currentPath = currentScaleCalib.getScaledPath();
                    probeTracker.rescaleCoordinates(currentScaleCalib.getUniformScaleFactor());
                    currentPath = PathCompression.getCompressedPath(currentPath,ProgramConstants.MIN_SEGMENT_LENGTH);
                    RotationCalibration newCalibration = 
                            new RotationCalibration(currentPath,endPoint,startingTriangle,meshInfo);
                    probeTracker.setRotationCalibration(newCalibration.getAggregateRotation());
                    currentPath = newCalibration.getCurrentPathOnSurface();
                    probeTracker.setCurrentPosition(currentPath.get(currentPath.size()-1));
                    
                    calibrationEnabled = false;
                    onStartPoint = true;
                }

            }
        }
    }

    public ArrayList<Vector3f> getCurrentPath() {
        return currentPath;
    }
    
}
