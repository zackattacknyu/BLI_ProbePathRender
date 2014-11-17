/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryInteractions.meshPathInteractions;

import org.zrd.jmeGeometryInteractions.meshInteraction.PickPointOnMesh;
import org.zrd.geometryToolkit.pointTools.MeshPointHandler;
import com.jme3.input.InputManager;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.util.ArrayList;
import org.zrd.geometryToolkit.geometryUtil.GeometryDataHelper;
import org.zrd.geometryToolkit.pointTools.FixedPointPicker;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.meshTraversal.PathOnMeshCalculator;
import org.zrd.geometryToolkit.pathDataStructure.SegmentSet;
import org.zrd.geometryToolkit.probeCalibration.ScaleCalibration;
import org.zrd.geometryToolkit.pathTools.PathCompression;
import org.zrd.geometryToolkit.pathTools.PathHelper;
import org.zrd.jmeGeometryIO.meshIO.MeshInputHelper;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;
import org.zrd.jmeUtil.mouseKeyboard.SpacebarHitImpl;

/**
 * 
 * This is an abstract class which handles picking two points on the mesh
 *      which correspond to desired start and end points for a path.
 * It handles calling the rotation, scaling, and translation methods
 *      required for the transformation which the subclasses 
 *      are in charge of getting the path in the first place and handling
 *      the transformed path
 *
 * @author BLI
 */
public abstract class PickTwoPointsOnMesh extends GeneralKeyboardActionMethod implements MeshPointHandler{

    private boolean twoPointPickEnabled = false;
    private boolean onStartPoint = true;
    private Vector3f lastPointClicked;
    protected MeshTriangle startingTriangle;
    protected MeshTriangle endingTriangle;
    private TriangleSet meshInfo;
    private SpacebarHitImpl spacebarHit;
    
    private boolean pointsNewlyPicked = false;
    
    /**
     * This initializes the class that picks two points on the mesh
     * @param actionName            name of the keyboard action that requires picking two points
     * @param pointPickName         name of the mouse action that picks a point
     * @param keyTrigger            the keyboard trigger to start the action
     * @param inputManager          the application's input manager
     * @param cam                   the application's camera
     * @param shootableMesh         the mesh where the points will occur on
     * @param meshInfo              the TriangleSet from Geometry toolkit for the mesh
     * @param fixedPtPicker         the fixed point picker object
     */
    protected PickTwoPointsOnMesh(String actionName, String pointPickName, 
            int keyTrigger, InputManager inputManager, Camera cam, 
            Node shootableMesh, TriangleSet meshInfo, FixedPointPicker fixedPtPicker){
        super(inputManager,actionName,keyTrigger);
        new PickPointOnMesh(pointPickName,inputManager,cam,this,shootableMesh,fixedPtPicker);
        this.meshInfo = meshInfo;
        this.spacebarHit = new SpacebarHitImpl(inputManager);
    }
    
    /**
     * This gets called when the keyboard key has been pressed saying
     *      that the two point clicking has been enabled
     */
    @Override
    public void actionMethod() {
        
        //enables the clicking of two points
        if(!twoPointPickEnabled){
            System.out.println(messageUponEnabling());
            twoPointPickEnabled = true;
        }else{
            
            //if key is pressed before two points are picked, then it cancels the action
            System.out.println(messageUponCancelling());
            twoPointPickEnabled = false;
            onStartPoint = true;
        }
    }
    
    /**
     * This is meant to be implemented by a subclass and it is the message
     *      put in the console when enabling the two points clicking action
     * @return      message for the console to say action is enabled
     */
    protected abstract String messageUponEnabling();
    
    /**
     * This is meant to be implemented by a subclass and it is the message
     *      put in the console when cancelling the two points clicking action
     * @return      message for the console to say action is cancelled
     */
    protected abstract String messageUponCancelling();

    /**
     * This is meant to be called by a subclass and it is passed the start
     *      point that is clicked and it is supposed to do whatever
     *      action is necessary depending on that start point
     * @param startPoint    the initial point clicked
     */
    protected abstract void handleStartPoint(Vector3f startPoint);
    
    /**
     * This is meant to be called by a subclass and it is passed the end
     *      point that is clicked as well as the rotation and scale
     *      calibration object results. It does whatever action is necessary
     *      based on those results
     * @param endPoint                  the end point clicked
     * @param scaleCalib                the scale calibration object
     * @param rotCalib                  the rotation calibration object
     * @param scaledAndRotatedPath      the transformed path
     */
    protected abstract void handleEndPointResult(Vector3f endPoint,
            ScaleCalibration scaleCalib, 
            PathOnMeshCalculator rotCalib, 
            SegmentSet scaledAndRotatedPath);
    
    /**
     * This must be called by a subclass and it gets whatever path
     *      is the one we are about when we click the endpoint
     * @return 
     */
    protected abstract SegmentSet getActivePathAtEndpoint();
    
    /**
     * This gets the MeshTriangle at the end point
     * @return  meshTriangle at end point
     */
    public MeshTriangle getEndingTriangle(){
        return endingTriangle;
    }
    
    @Override
     public void handleNewMeshPoint(Vector3f pointOnMesh, Triangle triangleOnMesh){
         handleNewMeshPoint(pointOnMesh,
                 MeshInputHelper.convertInputTriangleToMeshTriangle(
                 triangleOnMesh, meshInfo.getTransform()));
     }

    /**
     * This takes in the point on the mesh and the triangle at that point
     *      and does various actions depending on whether it is the start or end
     *      point. At the start point, it calls the methods that handle the start
     *      point. At the end point, it does the calibrations and handles the 
     *      results. 
     * 
     * @param pointOnMesh       the point on the mesh
     * @param triangleOnMesh    the triangle at that point
     */
    @Override
    public void handleNewMeshPoint(Vector3f pointOnMesh, MeshTriangle triangleOnMesh) {
        
        //makes sure the action has been enabled
        if(twoPointPickEnabled){
            
            //we are at the start point
            if(onStartPoint){

                Vector3f startPoint = pointOnMesh.clone();
                if(!startPoint.equals(lastPointClicked)){

                    lastPointClicked = startPoint;
                    
                    //registers the starting triangle
                    startingTriangle = triangleOnMesh.clone();
                    
                    //calls the action that handles the start point
                    handleStartPoint(startPoint);
                    
                    onStartPoint = false;
                    spacebarHit.resetSpacebarHit();
                }

            }else{
                
                

                Vector3f endPoint = pointOnMesh.clone();
                if(!endPoint.equals(lastPointClicked) && spacebarHit.wasSpacebarHit()){
                    
                    //registers the ending triangle
                    endingTriangle = triangleOnMesh.clone();
                    
                    //gets the active pre-transformed path
                    SegmentSet activeSegmentSet = getActivePathAtEndpoint();
                    
                    //gets the calibrations
                    ScaleCalibration currentScaleCalib = new ScaleCalibration(activeSegmentSet.getPathVertices(),endPoint);
                    //activeSegmentSet = new SegmentSet(currentScaleCalib.getScaledPath(),activeSegmentSet.getDataAtVertices());
                    activeSegmentSet = PathCompression.getCompressedPath(activeSegmentSet,PathHelper.MIN_SEGMENT_LENGTH);
                    PathOnMeshCalculator currentRotCalib = 
                            new PathOnMeshCalculator(activeSegmentSet,endPoint,startingTriangle,meshInfo);
                    activeSegmentSet = currentRotCalib.getCurrentSegmentSetOnSurface();
                    
                    //DEBUG CODE. COMMENT THIS WHEN DONE
                    GeometryDataHelper.writeTexCoordToDefaultOutputFile(activeSegmentSet.getVertexTextureCoords());
                    
                    //calls the method that handles the result
                    handleEndPointResult(endPoint,currentScaleCalib,currentRotCalib,activeSegmentSet);
                    
                    twoPointPickEnabled = false;
                    onStartPoint = true;
                    pointsNewlyPicked = true;
                    
                    /*
                     * This must be set to null so that the same point
                     *      can be used as the end of one path
                     *      and the start of the next path
                     */
                    lastPointClicked = null;
                }

            }
        }
    }
    
    /**
     * This says whether the points were newly chosen. It is meant to only 
     *      return true once for each time a point is chosen, thus once the variable
     *      is true, this method returns true but resets it to false.
     * @return      whether or not points have just been chosen
     */
    public boolean arePointsNewlyPicked(){
        if(pointsNewlyPicked){
            pointsNewlyPicked = false;
            return true;
        }else{
            return false;
        }        
    }
    
}
