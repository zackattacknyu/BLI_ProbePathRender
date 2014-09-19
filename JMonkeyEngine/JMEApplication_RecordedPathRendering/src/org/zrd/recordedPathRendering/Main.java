package org.zrd.recordedPathRendering;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import org.zrd.meshSessionTools.MeshSession;
import org.zrd.cameraTracker.cameraTrackingIO.CameraTrackingIO;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.pathDataStructure.RecordedPathSet;
import org.zrd.geometryToolkit.pointTools.FixedPointPicker;
import org.zrd.jmeGeometryIO.pathIO.PathRenderHelper;
import org.zrd.jmeGeometryInteractions.meshPathInteractions.LineMoveAction;
import org.zrd.jmeGeometryInteractions.pathInteraction.PathImport;
import org.zrd.jmeUtil.applicationHelp.ApplicationHelper;
import org.zrd.jmeUtil.materials.MaterialHelper;
import org.zrd.util.fileHelper.FilePathHelper;
import org.zrd.util.fileHelper.MeshInteractionFiles;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    private RecordedPathSet recordedPathSet;
    private Material lineMaterial;
    private LineMoveAction lineMoveAction;
    private LineMoveAction lineMoveActionToFixedPt;
    private PathImport pathImport;
    private SignalProcess signalProcessor;

    public static void main(String[] args) {
        ApplicationHelper.initializeApplication(new Main());
    }
    

    @Override
    public void simpleInitApp() {
        signalProcessor = new SignalProcess(100,14);
        recordedPathSet = new RecordedPathSet();
        ApplicationHelper.setBackgroundColor(viewPort);
        
        MeshSession currentSession = new MeshSession(assetManager,cam);
        Node shootables = currentSession.getShootableMesh();
        FixedPointPicker fixedPtsToPick = currentSession.getFixedPtsToPick();
        TriangleSet meshInfo = currentSession.getMeshInfo();
        MeshInteractionFiles meshInterFiles = currentSession.getMeshInterFiles();

        CameraTrackingIO.initializeCameraTrackingIO(inputManager, cam, flyCam, meshInterFiles);
        
        rootNode.attachChild(shootables);
        
        lineMoveAction = new LineMoveAction(inputManager, cam, shootables, recordedPathSet, meshInfo);
        lineMoveActionToFixedPt = new LineMoveAction(inputManager, cam, shootables, recordedPathSet, meshInfo,fixedPtsToPick);
        pathImport = new PathImport(inputManager,recordedPathSet,FilePathHelper.getDefaultInputFolder().toFile());
        lineMaterial = MaterialHelper.getColorMaterial(assetManager,ColorRGBA.Black);
        
        rootNode.attachChild(currentSession.getFixedPointNode());
    }

    @Override
    public void simpleUpdate(float tpf) {
        
        if(lineMoveAction.arePointsNewlyPicked()){
            rootNode.attachChild(TestPathWithData.getPathSpatialWithSampleData(lineMoveAction.getCurrentPath(), assetManager));
            //displayPath(PathRenderHelper.createLineFromVertices(lineMoveAction.getCurrentPath(), lineMaterial));
        }
        
        if(lineMoveActionToFixedPt.arePointsNewlyPicked()){
            rootNode.attachChild(TestPathWithData.getPathSpatialWithSampleData(lineMoveActionToFixedPt.getCurrentPath(), assetManager));
            //displayPath(PathRenderHelper.createLineFromVertices(lineMoveAction.getCurrentPath(), lineMaterial));
        }
        
        if(pathImport.isNewPathExists()){
            //displayPath(PathRenderHelper.createLineFromVertices(recordedPathSet.getCurrentPath(), lineMaterial));
            //rootNode.attachChild(TestPathWithData.getPathSpatialWithSampleData(recordedPathSet.getCurrentPath(), assetManager));
            rootNode.attachChild(PathRenderHelper.createLineFromVerticesWithData(
                    recordedPathSet.getCurrentSegment(), assetManager, signalProcessor));
        }
        
    }
    
    private void displayPath(Spatial path){
        rootNode.attachChild(path);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
