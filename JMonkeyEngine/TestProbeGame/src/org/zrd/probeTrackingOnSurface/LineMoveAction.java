/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTrackingOnSurface;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.math.Matrix4f;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.util.ArrayList;
import org.zrd.geometryToolkit.geometryUtil.ProgramConstants;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.meshTraversal.MeshTraverseHelper;
import org.zrd.geometryToolkit.meshTraversal.RotationCalibration;
import org.zrd.geometryToolkit.pathTools.PathCompression;
import org.zrd.geometryToolkit.pathTools.PathTransformHelper;
import org.zrd.graphicsTools.geometry.path.ProbePathSet;
import org.zrd.graphicsToolsImpl.meshImpl.MeshHelper;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;

/**
 *
 * @author BLI
 */
public class LineMoveAction extends GeneralKeyboardActionMethod implements MeshPointHandler{

    private boolean moveLineEnabled = false;
    private boolean onStartPoint = true;
    private Vector3f lastPointClicked;
    private ProbePathSet probePathSet;
    private MeshTriangle startingTriangle;
    private TriangleSet meshInfo;
    private ArrayList<Vector3f> currentPath;
    
    private boolean newLineConstructed = false;
    
    public LineMoveAction(InputManager inputManager, Camera cam, Node shootableMesh, ProbePathSet probePathSet, TriangleSet meshInfo){
        super(inputManager,"lineMoveAction",KeyInput.KEY_L);
        new PickPointOnMesh(inputManager,cam,this,shootableMesh);
        this.probePathSet = probePathSet;
        this.meshInfo = meshInfo;
    }
    
    @Override
    public void actionMethod() {
        if(!moveLineEnabled){
            System.out.println("Last Line will be moved to next 2 points clicked");
            moveLineEnabled = true;
        }else{
            System.out.println("Line Moving Cancelled");
            moveLineEnabled = false;
        }
    }

    public void handleNewMeshPoint(Vector3f pointOnMesh, Triangle triangleOnMesh) {
        if(moveLineEnabled){
                                
            if(onStartPoint){

                Vector3f endPoint = pointOnMesh.clone();
                currentPath = probePathSet.getCurrentPath().getVertices();
                if(!endPoint.equals(lastPointClicked)){

                    lastPointClicked = endPoint;
                    startingTriangle = MeshHelper.convertInputTriangleToMeshTriangle(triangleOnMesh, meshInfo.getTransform());
                    currentPath = MeshTraverseHelper.movePathStartPoint(currentPath, endPoint);
                    onStartPoint = false;
                }

            }else{

                Vector3f endPoint = pointOnMesh.clone();
                if(!endPoint.equals(lastPointClicked)){

                    lastPointClicked = endPoint;
                    currentPath = PathTransformHelper.scalePathForNewEndpoint(currentPath, endPoint);
                    currentPath = PathCompression.getCompressedPath(currentPath,ProgramConstants.MIN_SEGMENT_LENGTH);
                    RotationCalibration newCalibration = 
                            new RotationCalibration(currentPath,endPoint,startingTriangle,meshInfo);
                    currentPath = newCalibration.getCurrentPathOnSurface();
                    
                    moveLineEnabled = false;
                    onStartPoint = true;
                    newLineConstructed = true;
                }

            }
        }
    }

    public ArrayList<Vector3f> getCurrentPath() {
        return currentPath;
    }
    
    
    
    public boolean hasNewLine(){
        if(newLineConstructed){
            newLineConstructed = false;
            return true;
        }else{
            return false;
        }        
    }
    
}
