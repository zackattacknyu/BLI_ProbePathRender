/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryInteractions.meshPathInteractions;

import org.zrd.jmeGeometryInteractions.meshInteraction.PickPointOnMesh;
import org.zrd.geometryToolkit.pointTools.MeshPointHandler;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import org.zrd.geometryToolkit.geometryUtil.GeometryDataHelper;
import org.zrd.geometryToolkit.pointTools.FixedPointPicker;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.pathDataStructure.RecordedPathSet;
import org.zrd.geometryToolkit.pathDataStructure.SegmentSet;
import org.zrd.geometryToolkit.pathTools.PathCompression;
import org.zrd.geometryToolkit.pathTools.PathHelper;
import org.zrd.geometryToolkit.pathTools.PathTransformHelper;
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
public class PickPointOnMeshAndProject extends GeneralKeyboardActionMethod implements MeshPointHandler{
    
    private RecordedPathSet recordedPathSet;
    private SegmentSet currentSegmentSet;
    
    /**
     * Name of the action hitting the keyboard to signify
     *      moving the current line
     */
    public static final String LINE_MOVE_TO_FIXED_PT_ACTION_NAME = "lineMoveToFixedPtAction";
    
    /**
     * Name of the action for hitting the mouse to signify either a start
     *      or end point for the path
     */
    public static final String POINT_CLICK_FOR_LINE_MOVE_TO_FIXED_PT_ACTION_NAME = "pickFixedPtPointForLineMove";
    
    /**
     * The keyboard key to trigger the line move action
     */
    public static final int LINE_MOVE_TO_FIXED_PT_ACTION_KEY = KeyInput.KEY_K;
    
    /**
     * Name of the action hitting the keyboard to signify
     *      moving the current line
     */
    public static final String LINE_MOVE_ACTION_NAME = "lineMoveAction";
    
    /**
     * Name of the action for hitting the mouse to signify either a start
     *      or end point for the path
     */
    public static final String POINT_CLICK_FOR_LINE_MOVE_ACTION_NAME = "pickPointForLineMove";
    
    /**
     * The keyboard key to trigger the line move action
     */
    public static final int LINE_MOVE_ACTION_KEY = KeyInput.KEY_L;
    
    /**
     * This message is displayed to the user to tell them that line moving 
     *      has been enabled
     */
    public static final String LINE_MOVE_ENABLING_MESSAGE = "Last Line will be moved to next 2 points clicked";
    
    /**
     * This message is displayed to the user to tell them that line moving 
     *      has been cancelled
     */
    public static final String LINE_MOVE_CANCEL_MESSAGE = "Line Moving Cancelled";

    private boolean pointPickingEnabled = false;
    private boolean onStartPoint = true;
    private Vector3f lastPointClicked;
    protected MeshTriangle startingTriangle;
    protected MeshTriangle endingTriangle;
    private TriangleSet meshInfo;
    private SpacebarHitImpl spacebarHit;
    
    private boolean pointNewlyPicked = false;
    
    public PickPointOnMeshAndProject(InputManager inputManager, Camera cam, Node shootableMesh, RecordedPathSet recordedPathSet, TriangleSet meshInfo){
        this(LINE_MOVE_ACTION_NAME,POINT_CLICK_FOR_LINE_MOVE_ACTION_NAME,
                LINE_MOVE_ACTION_KEY,inputManager,cam,shootableMesh,meshInfo,null);
        this.recordedPathSet = recordedPathSet;
    }

    public PickPointOnMeshAndProject(InputManager inputManager, Camera cam, Node shootableMesh, 
            RecordedPathSet recordedPathSet, TriangleSet meshInfo, FixedPointPicker pointPicker) {
        this(LINE_MOVE_TO_FIXED_PT_ACTION_NAME,POINT_CLICK_FOR_LINE_MOVE_TO_FIXED_PT_ACTION_NAME,
                LINE_MOVE_TO_FIXED_PT_ACTION_KEY,inputManager,cam,shootableMesh,meshInfo,pointPicker);
        this.recordedPathSet = recordedPathSet;
    }
    
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
    protected PickPointOnMeshAndProject(String actionName, String pointPickName, 
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
        if(!pointPickingEnabled){
            System.out.println(messageUponEnabling());
            pointPickingEnabled = true;
        }else{
            
            System.out.println(messageUponCancelling());
            pointPickingEnabled = false;
        }
    }
    
    /**
     * This is meant to be implemented by a subclass and it is the message
     *      put in the console when enabling the two points clicking action
     * @return      message for the console to say action is enabled
     */
    protected String messageUponEnabling(){
        return "";
    }
    
    /**
     * This is meant to be implemented by a subclass and it is the message
     *      put in the console when cancelling the two points clicking action
     * @return      message for the console to say action is cancelled
     */
    protected String messageUponCancelling(){
        return "";
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
        if(pointPickingEnabled){
            
            //gets the current pre-recorded path
            currentSegmentSet = recordedPathSet.getCurrentSegment();

            //moves the current path so its start point matches the one given
            currentSegmentSet = new SegmentSet(
                    PathTransformHelper.movePathStartPoint(currentSegmentSet.getPathVertices(), pointOnMesh.clone()),
                    currentSegmentSet.getDataAtVertices());
            
            //compresses the path
            currentSegmentSet = PathCompression.getCompressedPath(currentSegmentSet,PathHelper.MIN_SEGMENT_LENGTH);
            
            //projects path onto surface
            //PathOnMeshCalculator currentPathProjectedOntoSurface = 
            //        new PathOnMeshCalculator(currentSegmentSet,triangleOnMesh.clone(),meshInfo);
            //currentSegmentSet = currentPathProjectedOntoSurface.getCurrentSegmentSetOnSurface();

            GeometryDataHelper.writeTexCoordToDefaultOutputFile(currentSegmentSet.getVertexTextureCoords());

            pointPickingEnabled = false;
            pointNewlyPicked = true;
        }
    }
    
    /**
     * This says whether the points were newly chosen. It is meant to only 
     *      return true once for each time a point is chosen, thus once the variable
     *      is true, this method returns true but resets it to false.
     * @return      whether or not points have just been chosen
     */
    public boolean isPointNewlyPicked(){
        if(pointNewlyPicked){
            pointNewlyPicked = false;
            return true;
        }else{
            return false;
        }        
    }
    
}
