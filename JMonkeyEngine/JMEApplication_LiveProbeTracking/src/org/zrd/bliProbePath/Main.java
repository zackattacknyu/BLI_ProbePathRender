package org.zrd.bliProbePath;

import org.zrd.jmeGeometryInteractions.meshInteraction.ImportFixedPoints;
import org.zrd.jmeGeometryInteractions.meshInteraction.RecordMeshSessionInfo;
import org.zrd.jmeGeometryInteractions.meshInteraction.RecordFixedPoints;
import org.zrd.cameraTracker.cameraMoves.CameraTracker;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.Properties;
import org.zrd.cameraTracker.cameraCoordIO.CameraCoordIO;
import org.zrd.cameraTracker.cameraMoveImpl.CameraTrackerImpl;
import org.zrd.geometryToolkit.locationTracking.LocationTracker;
import org.zrd.jmeGeometryIO.renderedObjects.ProbeRepresentation;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.pathDataStructure.RecordedPathSet;
import org.zrd.geometryToolkit.pointTools.FixedPointPicker;
import org.zrd.jmeGeometryIO.renderedObjects.FixedPointRender;
import org.zrd.util.fileHelper.MeshInteractionFiles;
import org.zrd.jmeUtil.applicationHelp.ApplicationHelper;
import org.zrd.jmeUtil.materials.MaterialHelper;
import org.zrd.probeTracking.ProbeTracker;
import org.zrd.jmeGeometryInteractions.meshInteraction.PickAndRecordPoint;
import probeTrackingRender.ProbeMoveAction;
import probeTrackingRender.ProbeRotationCalibration;
import org.zrd.probeTracking.ProbeTrackerOnSurface;
import org.zrd.util.properties.PropertiesHelper;
import probeTrackingRender.ProbeTrackerRecording;
import probeTrackingRender.ProbeTrackerRender;
import probeTrackingRender.ResetTracker;


/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    private boolean surfaceTrackingOn = false;
    
    private Spatial moveableObject;
    private Material lineMaterial;
    
    private ProbeTrackerRender probeTrackerRender;
    private ProbeTracker probeTracker;
    private LocationTracker activeTracker;

    private Node shootables;
    private RecordedPathSet recordedPathSet;

    private ProbeMoveAction probeMoveAction;
    private ProbeTrackerOnSurface probeTrackerOnSurface;
    
    private LiveTrackingText outputText;
    
    private boolean renderPathsDuringRecording = false;
    
    private ImportFixedPoints fixedPtsImport;
    private FixedPointPicker fixedPtsToPick;
    
    private Material fixedPtMaterial;
    
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
        
        
        surfaceTrackingOn = PropertiesHelper.getBooleanValueProperty(
                Properties_BLIProbePath.getProperties(), "surfaceTrackingOn");

        new CameraTrackerImpl(cam,flyCam,inputManager);
        
        MeshSession currentSession = new MeshSession(
                Paths_BLIProbePath.MESH_SESSION_PATH,
                Properties_BLIProbePath.getProperties(),
                assetManager,cam);
        rootNode.attachChild(currentSession.getFixedPointNode());
        shootables = currentSession.getShootableMesh();
        rootNode.attachChild(shootables);
        fixedPtsToPick = currentSession.getFixedPtsToPick();
        MeshInteractionFiles meshInterFiles = currentSession.getMeshInterFiles();
        TriangleSet meshInfo = currentSession.getMeshInfo();
        
        new CameraCoordIO(inputManager,cam,Paths_BLIProbePath.PATH_RECORDING_PATH,meshInterFiles);
        
        
        viewPort.setBackgroundColor(ApplicationHelper.BACKGROUND_COLOR);

        lineMaterial = MaterialHelper.getColorMaterial(assetManager,ColorRGBA.Black);

        probeTracker = ProbeTracker_BLIProbePath.createNewProbeTracker(inputManager);
        
        moveableObject = ProbeRepresentation.getProbeRepresentation(assetManager);
        rootNode.attachChild(moveableObject);
        
        recordedPathSet = new RecordedPathSet();

        new ResetTracker(inputManager,probeTracker);
        new ProbeRotationCalibration(
                inputManager, cam, shootables, probeTracker, meshInfo,
                Paths_BLIProbePath.CALIBRATION_RESULTS_PATH);
        probeTrackerOnSurface = new ProbeTrackerOnSurface(probeTracker,meshInfo);
        new PickAndRecordPoint(inputManager,cam,
                shootables,Paths_BLIProbePath.PATH_RECORDING_PATH, meshInfo.getTransform());
        
        
        activeTracker = surfaceTrackingOn ? probeTrackerOnSurface : probeTracker;
        new ProbeTrackerRecording(inputManager,recordedPathSet,probeTracker);
        
        boolean useFixedPoints = false;
        
        
        probeMoveAction = useFixedPoints ? 
                new ProbeMoveAction(inputManager,cam,shootables,activeTracker,fixedPtsToPick) : 
                new ProbeMoveAction(inputManager,cam,shootables,activeTracker,meshInfo.getTransform());
        
        probeTrackerRender = new ProbeTrackerRender(activeTracker,moveableObject,lineMaterial);
        
        new RecordFixedPoints(inputManager,probeMoveAction,Paths_BLIProbePath.CALIBRATION_RESULTS_PATH,meshInterFiles);
        fixedPtsImport = new ImportFixedPoints(inputManager,Paths_BLIProbePath.CALIBRATION_RESULTS_PATH);
        outputText = new LiveTrackingText(guiNode,assetManager);
        
        new RecordMeshSessionInfo(inputManager,meshInterFiles);
        
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
        
        if(fixedPtsImport.isNewPointsImported()){
            
            rootNode.attachChild(FixedPointRender.displayFixedPoints(fixedPtsImport.getImportedPoints(),fixedPtMaterial));
            
            probeMoveAction.setFixedPointSet(fixedPtsImport.getImportedPoints().getFixedPtPicker());
            
        }
    }
}
