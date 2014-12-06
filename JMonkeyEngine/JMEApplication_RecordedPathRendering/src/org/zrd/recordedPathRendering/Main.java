package org.zrd.recordedPathRendering;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
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
import org.zrd.util.fileHelper.MeshInteractionFiles;

import org.zrd.util.fileHelper.FilePathHelper;
import java.awt.image.*;
import org.zrd.jmeUtil.materials.MaterialHelper;
import org.zrd.util.fileHelper.ImageFileHelper;
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
    
    private Spatial shootableSurface;
    private MeshSession currentMeshSession;
    private BufferedImage currentTextureImage;

    private boolean meshIsFlat = true;
    
    private boolean hideInitialPaths = false;
    
    public static void main(String[] args) {
        ApplicationHelper.initializeApplication(new Main());
    }
    

    @Override
    public void simpleInitApp() {
        signalProcessor = new SignalProcess(100,14);
        recordedPathSet = new RecordedPathSet();
        ApplicationHelper.setBackgroundColor(viewPort);
        
        currentMeshSession = new MeshSession(assetManager,cam);
        shootables = currentMeshSession.getShootableMesh();
        currentTextureImage = currentMeshSession.getTextureImage();
        
        shootableSurface = currentMeshSession.getSurface();
        
        if(meshIsFlat){
            shootables.setQueueBucket(RenderQueue.Bucket.Sky);
        }
        
        FixedPointPicker fixedPtsToPick = currentMeshSession.getFixedPtsToPick();
        TriangleSet meshInfo = currentMeshSession.getMeshInfo();
        MeshInteractionFiles meshInterFiles = currentMeshSession.getMeshInterFiles();

        CameraTrackingIO.initializeCameraTrackingIO(inputManager, cam, flyCam, meshInterFiles);
        
        rootNode.attachChild(shootables);
        
        lineMoveAction = new LineMoveAction(inputManager, cam, shootables, recordedPathSet, meshInfo);
        lineMoveActionToFixedPt = new LineMoveAction(inputManager, cam, shootables, recordedPathSet, meshInfo,fixedPtsToPick);
        pathImport = new PathImport(inputManager,recordedPathSet,FilePathHelper.getDefaultInputFolder().toFile());
        
        //rootNode.attachChild(currentMeshSession.getFixedPointNode());
        
        paths = new Node();
        rootNode.attachChild(paths);
    }

    @Override
    public void simpleUpdate(float tpf) {
        
        if(lineMoveAction.arePointsNewlyPicked()){
            displaySegment(lineMoveAction.getCurrentSegment());
            //attachSegmentToTexture(lineMoveAction.getCurrentSegment());
        }
        
        if(lineMoveActionToFixedPt.arePointsNewlyPicked()){
            displaySegment(lineMoveActionToFixedPt.getCurrentSegment());
            //attachSegmentToTexture(lineMoveActionToFixedPt.getCurrentSegment());
            //attachSegmentAreaToTexture(lineMoveActionToFixedPt.getCurrentSegment());
        }
        
        if(!hideInitialPaths && pathImport.isNewPathExists()){
            displaySegment(recordedPathSet.getCurrentSegment());
        }
        
    }
    
    private void attachSegmentAreaToTexture(SegmentSet segment){
        currentTextureImage = PathRenderHelper.createCirclesOnImage(
                currentTextureImage, segment, signalProcessor);
        ImageFileHelper.writePNGimageToDefaultOutputFile(currentTextureImage);
        Material newMaterial = MaterialHelper.getTextureMaterial(
                assetManager, currentTextureImage);
        shootableSurface.setMaterial(newMaterial);
    }
    
    private void attachSegmentToTexture(SegmentSet segment){
        currentTextureImage = PathRenderHelper.createLineOnImage(
                currentTextureImage, segment, signalProcessor);
        ImageFileHelper.writePNGimageToDefaultOutputFile(currentTextureImage);
        Material newMaterial = MaterialHelper.getTextureMaterial(
                assetManager, currentTextureImage);
        shootableSurface.setMaterial(newMaterial);
    }
    
    private void displaySegment(SegmentSet segment){
        paths.attachChild(PathRenderHelper.createLineFromVerticesWithData(segment, assetManager, signalProcessor));
        signalProcessor.switchRedBlack();
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
