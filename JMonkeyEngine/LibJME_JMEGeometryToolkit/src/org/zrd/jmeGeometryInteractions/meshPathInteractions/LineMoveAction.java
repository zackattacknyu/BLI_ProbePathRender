/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryInteractions.meshPathInteractions;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.util.ArrayList;
import org.zrd.geometryToolkit.pointTools.FixedPointPicker;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.meshTraversal.PathOnMeshCalculator;
import org.zrd.geometryToolkit.probeCalibration.ScaleCalibration;
import org.zrd.geometryToolkit.pathDataStructure.RecordedPathSet;
import org.zrd.geometryToolkit.pathDataStructure.SegmentSet;
import org.zrd.geometryToolkit.pathTools.PathTransformHelper;

/**
 * This handles moving a line so its start and end point match
 *      with two points picked on the mesh. It is meant to be used
 *      to move a pre-recorded or imported line between
 *      two points and then show the transformation results.
 *
 * @author BLI
 */
public class LineMoveAction extends PickTwoPointsOnMesh{
    
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
    
    /**
     * This initializes the line move action
     * @param inputManager          application's input manager
     * @param cam                   the application's camera
     * @param shootableMesh         the mesh that contains the two points
     * @param recordedPathSet       the set of recorded paths from the application
     * @param meshInfo              the triangleSet of the mesh
     */
    public LineMoveAction(InputManager inputManager, Camera cam, Node shootableMesh, RecordedPathSet recordedPathSet, TriangleSet meshInfo){
        super(LINE_MOVE_ACTION_NAME,POINT_CLICK_FOR_LINE_MOVE_ACTION_NAME,
                LINE_MOVE_ACTION_KEY,inputManager,cam,shootableMesh,meshInfo,null);
        this.recordedPathSet = recordedPathSet;
    }

    public LineMoveAction(InputManager inputManager, Camera cam, Node shootableMesh, 
            RecordedPathSet recordedPathSet, TriangleSet meshInfo, FixedPointPicker pointPicker) {
        super(LINE_MOVE_TO_FIXED_PT_ACTION_NAME,POINT_CLICK_FOR_LINE_MOVE_TO_FIXED_PT_ACTION_NAME,
                LINE_MOVE_TO_FIXED_PT_ACTION_KEY,inputManager,cam,shootableMesh,meshInfo,pointPicker);
        this.recordedPathSet = recordedPathSet;
    }
    
    /**
     * This returns a string saying that line moving has been enabled
     * @return      string for the console saying line moving enabled
     */
    @Override
    protected String messageUponEnabling() {
        return LINE_MOVE_ENABLING_MESSAGE;
    }

    /**
     * This returns a string saying line moving was cancelled
     * @return      string saying line moving cancelled
     */
    @Override
    protected String messageUponCancelling() {
        return LINE_MOVE_CANCEL_MESSAGE;
    }

    /**
     * This gets called when the start point has been clicked and it
     *      moves the path start point to the clicked point
     * @param startPoint        the clicked point
     */
    @Override
    protected void handleStartPoint(Vector3f startPoint) {
        
        //gets the current pre-recorded path
        currentSegmentSet = recordedPathSet.getCurrentSegment();
        
        //moves the current path so its start point matches the one given
        currentSegmentSet = new SegmentSet(
                PathTransformHelper.movePathStartPoint(currentSegmentSet.getPathVertices(), startPoint),
                currentSegmentSet.getDataAtVertices());
        
        System.out.println("Hit spacebar before selecting end point");
    }

    /**
     * This gets called when the end point has been clicked and it 
     *      sets the current path to the one found in the superclass
     *      that was scaled and rotated. 
     * @param endPoint                  the end point clicked on
     * @param scaleCalib                the scale calibration
     * @param rotCalib                  the rotation calibration
     * @param scaledAndRotatedPath      the scaled and rotated path from the superclass
     */
    @Override
    protected void handleEndPointResult(Vector3f endPoint, 
        ScaleCalibration scaleCalib, 
        PathOnMeshCalculator rotCalib, 
        SegmentSet scaledAndRotatedPath) {
        
        currentSegmentSet = scaledAndRotatedPath;
    }

    /**
     * This returns the current path to the superclass
     * @return      the current path as list of vertices
     */
    @Override
    protected SegmentSet getActivePathAtEndpoint() {
        return currentSegmentSet;
    }
    
    /**
     * returns the current active path as list of vertices
     * @return  current path as list of vertices
     */
    public SegmentSet getCurrentSegment() {
        return currentSegmentSet;
    }
    
}
