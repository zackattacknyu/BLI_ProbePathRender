package org.zrd.recordedPathRendering;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import org.zrd.meshSessionTools.MeshSession;
import org.zrd.cameraTracker.cameraTrackingIO.CameraTrackingIO;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.pathDataStructure.RecordedPathSet;
import org.zrd.geometryToolkit.pathDataStructure.SegmentSet;
import org.zrd.geometryToolkit.pointTools.FixedPointPicker;
import org.zrd.jmeGeometryIO.pathIO.PathRenderHelper;
import org.zrd.jmeGeometryInteractions.meshPathInteractions.LineMoveAction;
import org.zrd.jmeGeometryInteractions.pathInteraction.PathImport;
import org.zrd.jmeUtil.applicationHelp.ApplicationHelper;
import org.zrd.util.fileHelper.FilePathHelper;
import org.zrd.util.fileHelper.MeshInteractionFiles;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    private RecordedPathSet recordedPathSet;
    private LineMoveAction lineMoveAction;
    private LineMoveAction lineMoveActionToFixedPt;
    private PathImport pathImport;
    private SignalProcess signalProcessor;
    
    private Node shootables;
    private Node paths;

    public static void main(String[] args) {
        ApplicationHelper.initializeApplication(new Main());
    }
    

    @Override
    public void simpleInitApp() {
        signalProcessor = new SignalProcess(100,14);
        recordedPathSet = new RecordedPathSet();
        ApplicationHelper.setBackgroundColor(viewPort);
        
        MeshSession currentSession = new MeshSession(assetManager,cam);
        shootables = currentSession.getShootableMesh();
        shootables.setQueueBucket(RenderQueue.Bucket.Sky);
        FixedPointPicker fixedPtsToPick = currentSession.getFixedPtsToPick();
        TriangleSet meshInfo = currentSession.getMeshInfo();
        MeshInteractionFiles meshInterFiles = currentSession.getMeshInterFiles();

        CameraTrackingIO.initializeCameraTrackingIO(inputManager, cam, flyCam, meshInterFiles);
        
        rootNode.attachChild(shootables);
        
        lineMoveAction = new LineMoveAction(inputManager, cam, shootables, recordedPathSet, meshInfo);
        lineMoveActionToFixedPt = new LineMoveAction(inputManager, cam, shootables, recordedPathSet, meshInfo,fixedPtsToPick);
        pathImport = new PathImport(inputManager,recordedPathSet,FilePathHelper.getDefaultInputFolder().toFile());
        
        //rootNode.attachChild(currentSession.getFixedPointNode());
        
        paths = new Node();
        rootNode.attachChild(paths);
        //paths.setQueueBucket(RenderQueue.Bucket.Transparent);
    }

    @Override
    public void simpleUpdate(float tpf) {
        
        if(lineMoveAction.arePointsNewlyPicked()){
            displaySegment(lineMoveAction.getCurrentSegment());
        }
        
        if(lineMoveActionToFixedPt.arePointsNewlyPicked()){
            displaySegment(lineMoveActionToFixedPt.getCurrentSegment());
        }
        
        if(pathImport.isNewPathExists()){
            displaySegment(recordedPathSet.getCurrentSegment());
        }
        
    }
    
    private void displaySegment(SegmentSet segment){
        paths.attachChild(PathRenderHelper.createLineFromVerticesWithData(segment, assetManager, signalProcessor));
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
