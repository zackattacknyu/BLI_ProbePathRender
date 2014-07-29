package org.zrd.recordedPathRendering;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.io.File;
import java.nio.file.Paths;
import java.util.Properties;
import org.zrd.cameraTracker.cameraMoves.CameraTracker;
import org.zrd.cameraTracker.presetModes.CameraTrackerImpl_ProbePathRender;
import org.zrd.geometryToolkit.geometryUtil.ProgramConstants;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.pathDataStructure.RecordedPathSet;
import org.zrd.jmeGeometryIO.pathIO.PathRenderHelper;
import org.zrd.jmeGeometryInteractions.meshPathInteractions.LineMoveAction;
import org.zrd.jmeGeometryInteractions.pathInteraction.PathImport;
import org.zrd.jmeGeometryIO.renderedObjects.BackgroundBox;
import org.zrd.jmeGeometryIO.renderedObjects.LolaMesh;
import org.zrd.jmeGeometryIO.renderedObjects.RenderedMesh;
import org.zrd.jmeGeometryIO.renderedObjects.SphereMesh;
import org.zrd.jmeGeometryIO.renderedObjects.FixedPointsOnLolaMesh;
import org.zrd.jmeUtil.applicationHelp.ApplicationHelper;
import org.zrd.jmeUtil.materials.MaterialHelper;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    private boolean sphereOn = false;
    private Spatial surface;
    private TriangleSet meshInfo;
    private Node shootables;
    private RecordedPathSet recordedPathSet;
    private Material lineMaterial;
    private LineMoveAction lineMoveAction;
    private LineMoveAction lineMoveActionToFixedPt;
    private PathImport pathImport;

    public static void main(String[] args) {
        Properties appProps = Properties_RecordedPathRendering.getProperties();
        ApplicationHelper.initializeApplication(new Main(), appProps);
    }
    

    @Override
    public void simpleInitApp() {
        recordedPathSet = new RecordedPathSet();
        viewPort.setBackgroundColor(ProgramConstants.BACKGROUND_COLOR);
        
        File initialImportDirectory = Paths.get("C:\\Users\\BLI\\Desktop\\BLI_ProbePathRender\\sampleTextFiles").toFile();
        
        CameraTracker cameraTracker = new CameraTrackerImpl_ProbePathRender(cam,flyCam,inputManager);
        if(sphereOn){
            cameraTracker.setDefaultCamera((short)0);
        }else{
            cameraTracker.setDefaultCamera((short)1);
        }
        
        RenderedMesh activeMesh;
        if(sphereOn){
            activeMesh = new SphereMesh(assetManager);
        }else{
            activeMesh = new LolaMesh(assetManager);
        }
        
        if(!sphereOn){
            rootNode.attachChild(BackgroundBox.getBackgroundBox(assetManager));
        }

        meshInfo = activeMesh.getActiveMeshInfo();
        surface = activeMesh.getSurfaceMesh();
        
        shootables = new Node("shootables");
        shootables.attachChild(surface);
        
        rootNode.attachChild(shootables);
        
        lineMoveAction = new LineMoveAction(inputManager, cam, shootables, recordedPathSet, meshInfo);
        lineMoveActionToFixedPt = new LineMoveAction(inputManager, cam, shootables, recordedPathSet, meshInfo,FixedPointsOnLolaMesh.pointPicker);
        pathImport = new PathImport(inputManager,recordedPathSet,initialImportDirectory);
        lineMaterial = MaterialHelper.getColorMaterial(assetManager,ColorRGBA.Black);
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
            displayPath(PathRenderHelper.createLineFromVertices(recordedPathSet.getCurrentPath(), lineMaterial));
            
            rootNode.attachChild(TestPathWithData.getPathSpatialWithSampleData(recordedPathSet.getCurrentPath(), assetManager));
            
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
