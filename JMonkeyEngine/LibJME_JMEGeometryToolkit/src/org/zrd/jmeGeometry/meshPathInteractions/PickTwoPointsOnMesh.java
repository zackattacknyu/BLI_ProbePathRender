/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometry.meshPathInteractions;

import org.zrd.geometryToolkit.geometryUtil.MeshPointHandler;
import com.jme3.input.InputManager;
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

/**
 *
 * @author BLI
 */
public abstract class PickTwoPointsOnMesh extends GeneralKeyboardActionMethod implements MeshPointHandler{

    private boolean twoPointPickEnabled = false;
    private boolean onStartPoint = true;
    private Vector3f lastPointClicked;
    private MeshTriangle startingTriangle;
    private MeshTriangle endingTriangle;
    private TriangleSet meshInfo;
    
    private boolean pointsNewlyPicked = false;
    
    public PickTwoPointsOnMesh(String actionName, String pointPickName, int keyTrigger, InputManager inputManager, Camera cam, Node shootableMesh, TriangleSet meshInfo){
        super(inputManager,actionName,keyTrigger);
        new PickPointOnMesh(pointPickName,inputManager,cam,this,shootableMesh);
        this.meshInfo = meshInfo;
    }
    
    @Override
    public void actionMethod() {
        if(!twoPointPickEnabled){
            System.out.println(messageUponEnabling());
            twoPointPickEnabled = true;
        }else{
            System.out.println(messageUponEnabling());
            twoPointPickEnabled = false;
        }
    }
    
    protected abstract String messageUponEnabling();
    protected abstract String messageUponCancelling();

    protected abstract void handleStartPoint(Vector3f startPoint);
    protected abstract void handleEndPointResult(Vector3f endPoint,ScaleCalibration scaleCalib, RotationCalibration rotCalib, ArrayList<Vector3f> scaledAndRotatedPath);
    protected abstract ArrayList<Vector3f> getActivePathAtEndpoint();
    
    public MeshTriangle getEndingTriangle(){
        return endingTriangle;
    }

    @Override
    public void handleNewMeshPoint(Vector3f pointOnMesh, Triangle triangleOnMesh) {
        if(twoPointPickEnabled){
                                
            if(onStartPoint){

                Vector3f startPoint = pointOnMesh.clone();
                if(!startPoint.equals(lastPointClicked)){

                    lastPointClicked = startPoint;
                    startingTriangle = MeshHelper.convertInputTriangleToMeshTriangle(triangleOnMesh, meshInfo.getTransform());
                    handleStartPoint(startPoint);
                    onStartPoint = false;
                }

            }else{

                Vector3f endPoint = pointOnMesh.clone();
                if(!endPoint.equals(lastPointClicked)){

                    lastPointClicked = endPoint;
                    
                    endingTriangle = MeshHelper.convertInputTriangleToMeshTriangle(triangleOnMesh, meshInfo.getTransform());
                    
                    ArrayList<Vector3f> activePath = getActivePathAtEndpoint();
                    
                    ScaleCalibration currentScaleCalib = new ScaleCalibration(activePath,endPoint);
                    activePath = currentScaleCalib.getScaledPath();
                    activePath = PathCompression.getCompressedPath(activePath,ProgramConstants.MIN_SEGMENT_LENGTH);
                    RotationCalibration currentRotCalib = 
                            new RotationCalibration(activePath,endPoint,startingTriangle,meshInfo);
                    activePath = currentRotCalib.getCurrentPathOnSurface();
                    
                    handleEndPointResult(endPoint,currentScaleCalib,currentRotCalib,activePath);
                    
                    twoPointPickEnabled = false;
                    onStartPoint = true;
                    pointsNewlyPicked = true;
                }

            }
        }
    }
    
    public boolean arePointsNewlyPicked(){
        if(pointsNewlyPicked){
            pointsNewlyPicked = false;
            return true;
        }else{
            return false;
        }        
    }
    
}
