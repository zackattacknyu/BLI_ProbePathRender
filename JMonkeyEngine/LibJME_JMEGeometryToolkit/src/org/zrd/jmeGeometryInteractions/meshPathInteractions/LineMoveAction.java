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
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.meshTraversal.MeshTraverseHelper;
import org.zrd.geometryToolkit.meshTraversal.RotationCalibration;
import org.zrd.geometryToolkit.meshTraversal.ScaleCalibration;
import org.zrd.geometryToolkit.pathDataStructure.RecordedPathSet;

/**
 * 
 *
 * @author BLI
 */
public class LineMoveAction extends PickTwoPointsOnMesh{
    
    private RecordedPathSet recordedPathSet;
    private ArrayList<Vector3f> currentPath;
    
    public LineMoveAction(InputManager inputManager, Camera cam, Node shootableMesh, RecordedPathSet recordedPathSet, TriangleSet meshInfo){
        super("lineMoveAction","pickPointForLineMove",KeyInput.KEY_L,inputManager,cam,shootableMesh,meshInfo);
        this.recordedPathSet = recordedPathSet;
    }
    
    @Override
    protected String messageUponEnabling() {
        return "Last Line will be moved to next 2 points clicked";
    }

    @Override
    protected String messageUponCancelling() {
        return "Line Moving Cancelled";
    }

    @Override
    protected void handleStartPoint(Vector3f startPoint) {
        currentPath = recordedPathSet.getCurrentPath();
        currentPath = MeshTraverseHelper.movePathStartPoint(currentPath, startPoint);
    }

    @Override
    protected void handleEndPointResult(Vector3f endPoint, ScaleCalibration scaleCalib, RotationCalibration rotCalib, ArrayList<Vector3f> scaledAndRotatedPath) {
        currentPath = scaledAndRotatedPath;
    }

    @Override
    protected ArrayList<Vector3f> getActivePathAtEndpoint() {
        return currentPath;
    }
    
    public ArrayList<Vector3f> getCurrentPath() {
        return currentPath;
    }
    
}
