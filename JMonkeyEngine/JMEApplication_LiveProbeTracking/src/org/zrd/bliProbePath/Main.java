package org.zrd.bliProbePath;

import meshSessionTools.MeshSession;
import org.zrd.jmeGeometryInteractions.meshInteraction.ImportFixedPoints;
import org.zrd.jmeGeometryInteractions.meshInteraction.RecordMeshSessionInfo;
import org.zrd.jmeGeometryInteractions.meshInteraction.RecordFixedPoints;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import java.nio.file.Path;
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
import org.zrd.util.fileHelper.PathHelper;
import org.zrd.util.properties.PropertiesHelper;
import probeTrackingRender.ProbeTrackerRecording;
import probeTrackingRender.ProbeTrackerRender;
import probeTrackingRender.ResetTracker;


/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    private ProbeTrackerRender probeTrackerRender;
    private ProbeTracker probeTracker;
    private LocationTracker activeTracker;
    private RecordedPathSet recordedPathSet;

    private ProbeMoveAction probeMoveAction;
    private ProbeTrackerOnSurface probeTrackerOnSurface;
    
    private LiveTrackingText outputText;
    
    private boolean renderPathsDuringRecording = false;
    
    private ImportFixedPoints fixedPtsImport;
    
    private Material fixedPtMaterial;
    
    public static void main(String[] args) {
        Properties appProps = PropertiesHelper.getDefaultProperties();
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
        
        //initializes the variables
        Properties props = PropertiesHelper.getDefaultProperties();
        Path defaultOutputPath = PathHelper.getDefaultOutputFolder();
        boolean surfaceTrackingOn = PropertiesHelper.getBooleanValueProperty(props, "surfaceTrackingOn");
        Material lineMaterial = MaterialHelper.getColorMaterial(assetManager,ColorRGBA.Black);
        recordedPathSet = new RecordedPathSet();
        Node moveableObject = ProbeRepresentation.getProbeRepresentation(assetManager);
        boolean useFixedPoints = false;
        outputText = new LiveTrackingText(guiNode,assetManager);

        //initializes the mesh session variables
        MeshSession currentSession = new MeshSession(
                PathHelper.getDefaultInputFolder(),
                props,assetManager,cam);
        Node shootables = currentSession.getShootableMesh();
        FixedPointPicker fixedPtsToPick = currentSession.getFixedPtsToPick();
        MeshInteractionFiles meshInterFiles = currentSession.getMeshInterFiles();
        TriangleSet meshInfo = currentSession.getMeshInfo();
        
        //initialize the defaults
        viewPort.setBackgroundColor(ApplicationHelper.BACKGROUND_COLOR);
        
        //display default objects
        rootNode.attachChild(currentSession.getFixedPointNode());
        rootNode.attachChild(shootables);
        rootNode.attachChild(moveableObject);
        
        //initialize camera coordinate actions
        new CameraCoordIO(inputManager,cam,defaultOutputPath,meshInterFiles);
        new CameraTrackerImpl(cam,flyCam,inputManager);
        
        //initialize trackers
        probeTracker = ProbeTracker_BLIProbePath.createNewProbeTracker(inputManager);
        probeTrackerOnSurface = new ProbeTrackerOnSurface(probeTracker,meshInfo);
        activeTracker = surfaceTrackingOn ? probeTrackerOnSurface : probeTracker;
        
        //initialize tracker actions
        new ResetTracker(inputManager,probeTracker);
        new ProbeRotationCalibration(
                inputManager, cam, shootables, probeTracker, meshInfo,
                defaultOutputPath);
        new PickAndRecordPoint(inputManager,cam,
                shootables,defaultOutputPath, meshInfo.getTransform());
        new ProbeTrackerRecording(inputManager,recordedPathSet,probeTracker);
        
        //initialize active tracker actions
        probeMoveAction = useFixedPoints ? 
                new ProbeMoveAction(inputManager,cam,shootables,activeTracker,fixedPtsToPick) : 
                new ProbeMoveAction(inputManager,cam,shootables,activeTracker,meshInfo.getTransform());
        probeTrackerRender = new ProbeTrackerRender(activeTracker,moveableObject,lineMaterial);
        
        //initialize fixed points actions
        new RecordFixedPoints(inputManager,probeMoveAction,defaultOutputPath,meshInterFiles);
        fixedPtsImport = new ImportFixedPoints(inputManager,defaultOutputPath);
        
        //initialize mesh session recording
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
