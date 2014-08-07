package org.zrd.bliProbePath;

import org.zrd.cameraTracker.presetModes.CameraTrackerImpl_ProbePathRender;
import org.zrd.cameraTracker.cameraMoves.CameraTracker;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.Properties;
import org.zrd.cameraTracker.cameraCoordIO.CameraCoordIO;
import org.zrd.geometryToolkit.locationTracking.LocationTracker;
import org.zrd.jmeGeometryIO.renderedObjects.BackgroundBox;
import org.zrd.jmeGeometryIO.renderedObjects.LolaMesh;
import org.zrd.jmeGeometryIO.renderedObjects.ProbeRepresentation;
import org.zrd.jmeGeometryIO.meshIO.RenderedMesh;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.pathDataStructure.RecordedPathSet;
import org.zrd.jmeGeometryIO.meshIO.MeshInputHelper;
import org.zrd.jmeGeometryIO.meshIO.MeshRenderData;
import org.zrd.jmeGeometryIO.renderedObjects.BallMesh;
import org.zrd.jmeGeometryIO.renderedObjects.FixedPointsOnLolaMesh;
import org.zrd.jmeUtil.applicationHelp.ApplicationHelper;
import org.zrd.jmeUtil.materials.MaterialHelper;
import org.zrd.probeTracking.ProbeTracker;
import org.zrd.jmeGeometryInteractions.meshInteraction.PickAndRecordPoint;
import probeTrackingRender.ProbeMoveAction;
import probeTrackingRender.ProbeRotationCalibration;
import org.zrd.probeTracking.ProbeTrackerOnSurface;
import probeTrackingRender.ProbeTrackerRecording;
import probeTrackingRender.ProbeTrackerRender;
import probeTrackingRender.ResetTracker;


/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    private boolean surfaceTrackingOn = false;
    private boolean ballMeshOn = false;
    
    private Spatial moveableObject;
    private Material lineMaterial;
    
    private ProbeTrackerRender probeTrackerRender;
    private ProbeTracker probeTracker;
    private LocationTracker activeTracker;
    
    private CameraTracker cameraTracker;

    private Node shootables;
    private RecordedPathSet recordedPathSet;

    private ProbeMoveAction probeMoveAction;
    private ProbeTrackerOnSurface probeTrackerOnSurface;
    
    private LiveTrackingText outputText;
    
    private boolean renderPathsDuringRecording = false;
    
    
    
    public static void main(String[] args) {
        Properties appProps = Properties_BLIProbePath.getProperties();
        ApplicationHelper.initializeApplication(new Main(), appProps);
    }

    @Override
    public void simpleInitApp() {
        
        /*
         * This has details on capturing video:
         *  http://www.aurellem.com/cortex/html/capture-video.html
         * 
         * Blog Entries:
         *  http://www.aurellem.com/
         * 
         * Javadoc for the below class:
         *  http://www.aurellem.com/jmeCapture/docs/
         * 
         * 
        Path videoFilePath = Paths.get("C:\\Users\\BLI\\Desktop\\BLI_ProbePathRender\\videoFiles");
        File recordedVideoFile = videoFilePath.resolve("LiveTrackingRun_" + TimeHelper.getTimestampSuffix()).toFile();
        this.setTimer(new IsoTimer(30));
        try {
            Capture.captureVideo(this, recordedVideoFile);
        } catch (IOException ex) {
            System.out.println("Error trying to capture video: " + ex);
        }*/

        ballMeshOn = Boolean.parseBoolean(
                Properties_BLIProbePath.getProperties().
                getProperty("ballOn"));
        surfaceTrackingOn = Boolean.parseBoolean(
                Properties_BLIProbePath.getProperties().
                getProperty("surfaceTrackingOn"));
        
        RenderedMesh activeMesh;
        MeshRenderData importedMesh = MeshInputHelper.selectFilesAndGenerateRenderData(
                Paths_BLIProbePath.LOG_PARENT_PATH.toFile(),assetManager);
        
        if(importedMesh == null){
            activeMesh = ballMeshOn ? new BallMesh(assetManager) : new LolaMesh(assetManager);
        }else{
            activeMesh = new RenderedMesh(importedMesh.getFinalMesh(),
                    importedMesh.getFinalMeshInfo());
        }
        
        viewPort.setBackgroundColor(ApplicationHelper.BACKGROUND_COLOR);
                
        cameraTracker = new CameraTrackerImpl_ProbePathRender(cam,flyCam,inputManager);
        new CameraCoordIO(inputManager,cam,Paths_BLIProbePath.PATH_RECORDING_PATH);
        
        int cameraMode = ballMeshOn ? 3 : 1;
        cameraTracker.setDefaultCamera((short)cameraMode);
        
        lineMaterial = MaterialHelper.getColorMaterial(assetManager,ColorRGBA.Black);

        probeTracker = ProbeTracker_BLIProbePath.createNewProbeTracker(inputManager);
        
        moveableObject = ProbeRepresentation.getProbeRepresentation(assetManager);
        rootNode.attachChild(moveableObject);
        
        if(!ballMeshOn) rootNode.attachChild(BackgroundBox.getBackgroundBox(assetManager));

        TriangleSet meshInfo = activeMesh.getActiveMeshInfo();
        Spatial surface = activeMesh.getSurfaceMesh();
        
        shootables = new Node("shootables");
        shootables.attachChild(surface);
        
        rootNode.attachChild(shootables);
        
        recordedPathSet = new RecordedPathSet();

        ResetTracker resetTracker = new ResetTracker(inputManager,probeTracker);
        ProbeRotationCalibration rotCalib = new ProbeRotationCalibration(
                inputManager, cam, shootables, probeTracker, meshInfo,
                Paths_BLIProbePath.CALIBRATION_RESULTS_PATH);
        probeTrackerOnSurface = new ProbeTrackerOnSurface(probeTracker,meshInfo);
        PickAndRecordPoint pointRecorder = new PickAndRecordPoint(inputManager,cam,
                shootables,Paths_BLIProbePath.PATH_RECORDING_PATH, activeMesh.getActiveMeshInfo().getTransform());
        
        
        activeTracker = surfaceTrackingOn ? probeTrackerOnSurface : probeTracker;
        new ProbeTrackerRecording(inputManager,recordedPathSet,probeTracker);
        
        boolean useFixedPoints = false;
        
        probeMoveAction = useFixedPoints ? 
                new ProbeMoveAction(inputManager,cam,shootables,activeTracker,
                    FixedPointsOnLolaMesh.pointPicker) : 
                new ProbeMoveAction(inputManager,cam,shootables,activeTracker);
        
        probeTrackerRender = new ProbeTrackerRender(activeTracker,moveableObject,lineMaterial);
        
        
        
        outputText = new LiveTrackingText(guiNode,assetManager);
        
        //RotationTesting.doRotationTesting();
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        /*explained here is how the update loop works
         * http://hub.jmonkeyengine.org/wiki/doku.php/jme3:beginner:hello_main_event_loop
         */
        
        probeTrackerRender.updateInfo();
    }

    @Override
    public void simpleRender(RenderManager rm) {
        probeTrackerRender.updateRenderObjectInfo();
        
        if(renderPathsDuringRecording){
            probeTrackerRender.updateRenderPathInfo();
            if(probeTrackerRender.isNewRenderedPathsExist()){
                rootNode.attachChild(probeTrackerRender.getRenderedPaths());
            }
        }
        
        
        outputText.setXyzText(activeTracker.getXYZtext());
        outputText.setYawPitchRollText(activeTracker.getYawPitchRollText());
        
        outputText.setProbeMoveModeText(probeMoveAction.getProbeMoveModeText());
    }
}
