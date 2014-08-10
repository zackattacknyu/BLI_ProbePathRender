package org.zrd.bliProbePath;

import org.zrd.cameraTracker.presetModes.CameraTrackerImpl_ProbePathRender;
import org.zrd.cameraTracker.cameraMoves.CameraTracker;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.util.Properties;
import org.zrd.cameraTracker.cameraCoordIO.CameraCoordIO;
import org.zrd.cameraTracker.cameraCoordIO.CameraCoordProperties;
import org.zrd.geometryToolkit.locationTracking.LocationTracker;
import org.zrd.jmeGeometryIO.renderedObjects.BackgroundBox;
import org.zrd.jmeGeometryIO.renderedObjects.LolaMesh;
import org.zrd.jmeGeometryIO.renderedObjects.ProbeRepresentation;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.pathDataStructure.RecordedPathSet;
import org.zrd.geometryToolkit.pointTools.FixedPointPicker;
import org.zrd.geometryToolkit.pointTools.PointData;
import org.zrd.jmeGeometryIO.meshIO.MeshInputHelper;
import static org.zrd.jmeGeometryIO.meshIO.MeshInputHelper.generateRenderData;
import org.zrd.jmeGeometryIO.meshIO.MeshRenderData;
import org.zrd.jmeGeometryIO.renderedObjects.BallMesh;
import org.zrd.jmeGeometryIO.renderedObjects.FixedPointsOnLolaMesh;
import org.zrd.jmeGeometryInteractions.meshInteraction.MeshInteractionFiles;
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
        
        fixedPtMaterial = MaterialHelper.getColorMaterial(1.0f, 0.0f, 0.0f, assetManager);

        ballMeshOn = PropertiesHelper.getBooleanValueProperty(
                Properties_BLIProbePath.getProperties(), "ballOn");
        surfaceTrackingOn = PropertiesHelper.getBooleanValueProperty(
                Properties_BLIProbePath.getProperties(), "surfaceTrackingOn");
        boolean showBackground = PropertiesHelper.getBooleanValueProperty(
                Properties_BLIProbePath.getProperties(), "showBackground");
        
        cameraTracker = new CameraTrackerImpl_ProbePathRender(cam,flyCam,inputManager);
        new CameraCoordIO(inputManager,cam,Paths_BLIProbePath.PATH_RECORDING_PATH);
        
        int cameraMode = ballMeshOn ? 3 : 1;
        cameraTracker.setDefaultCamera((short)cameraMode);
        
        fixedPtsToPick = FixedPointsOnLolaMesh.pointPicker;
        
        MeshInteractionFiles meshInterFiles = MeshInputHelper.obtainAllFiles(Paths_BLIProbePath.LOG_PARENT_PATH.toFile());
        
        MeshRenderData importedMesh;
        if(meshInterFiles == null){
            importedMesh = ballMeshOn ? new BallMesh(assetManager) : new LolaMesh(assetManager);
        }else{
            importedMesh = MeshInputHelper.generateRenderData(meshInterFiles.getDataFiles(),assetManager);
            
            if(meshInterFiles.getCameraCoordFile().exists()){
                CameraCoordProperties.setCameraCoordinatesUsingFile(cam, meshInterFiles.getCameraCoordFile());
            }
            
            if(meshInterFiles.getFixedPointsFile().exists()){
                FixedPointIO fixedPtsImported = FixedPointIO.getPointsFromFile(meshInterFiles.getFixedPointsFile());
                fixedPtsToPick = fixedPtsImported.getFixedPtPicker();
                displayFixedPoints(fixedPtsImported,fixedPtMaterial);
            }
            
        }
        
        viewPort.setBackgroundColor(ApplicationHelper.BACKGROUND_COLOR);

        lineMaterial = MaterialHelper.getColorMaterial(assetManager,ColorRGBA.Black);

        probeTracker = ProbeTracker_BLIProbePath.createNewProbeTracker(inputManager);
        
        moveableObject = ProbeRepresentation.getProbeRepresentation(assetManager);
        rootNode.attachChild(moveableObject);
        
        if(showBackground) rootNode.attachChild(BackgroundBox.getBackgroundBox(assetManager));

        TriangleSet meshInfo = importedMesh.getActiveMeshInfo();
        Spatial surface = importedMesh.getSurfaceMesh();
        
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
                shootables,Paths_BLIProbePath.PATH_RECORDING_PATH, importedMesh.getActiveMeshInfo().getTransform());
        
        
        activeTracker = surfaceTrackingOn ? probeTrackerOnSurface : probeTracker;
        new ProbeTrackerRecording(inputManager,recordedPathSet,probeTracker);
        
        boolean useFixedPoints = true;
        
        
        probeMoveAction = useFixedPoints ? 
                new ProbeMoveAction(inputManager,cam,shootables,activeTracker,fixedPtsToPick) : 
                new ProbeMoveAction(inputManager,cam,shootables,activeTracker,meshInfo.getTransform());
        
        probeTrackerRender = new ProbeTrackerRender(activeTracker,moveableObject,lineMaterial);
        
        new RecordFixedPoints(inputManager,probeMoveAction,Paths_BLIProbePath.CALIBRATION_RESULTS_PATH);
        fixedPtsImport = new ImportFixedPoints(inputManager,Paths_BLIProbePath.CALIBRATION_RESULTS_PATH);
        outputText = new LiveTrackingText(guiNode,assetManager);
        
        new RecordMeshSessionInfo(inputManager);
        
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
            
            displayFixedPoints(fixedPtsImport.getImportedPoints(),fixedPtMaterial);
            
            probeMoveAction.setFixedPointSet(fixedPtsImport.getImportedPoints().getFixedPtPicker());
            
        }
    }
    
    private void displayFixedPoints(FixedPointIO fixedPts,Material ptMaterial){
        for(PointData fixedPt: fixedPts.getFixedPointsOnMesh()){
            rootNode.attachChild(initBox(ptMaterial,fixedPt.getPointCoords()));
        }
    }
    
    private Spatial initBox(Material ballMat, Vector3f position){
        Box b = new Box(0.2f, 0.2f, 0.2f);
        Spatial sampleBox = new Geometry("FixedPoint", b);
        sampleBox.setMaterial(ballMat);
        sampleBox.setLocalTranslation(position);
        return sampleBox;
    }
}
