package org.zrd.bliProbePath;

import org.zrd.signalProcessingTools.fftTools.SignalProcessingOutput_Threaded;
import org.zrd.meshSessionTools.MeshSession;
import org.zrd.jmeGeometryInteractions.meshInteraction.RecordMeshSessionInfo;
import org.zrd.jmeGeometryInteractions.meshInteraction.RecordFixedPoints;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import java.nio.file.Path;
import java.util.Properties;
import org.zrd.cameraTracker.cameraTrackingIO.CameraTrackingIO;
import org.zrd.geometryToolkit.geometryUtil.CalibrationProperties;
import org.zrd.geometryToolkit.locationTracking.LocationTracker;
import org.zrd.jmeGeometryIO.renderedObjects.ProbeRepresentation;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.pathDataStructure.RecordedPathSet;
import org.zrd.geometryToolkit.pointTools.FixedPointPicker;
import org.zrd.util.fileHelper.MeshInteractionFiles;
import org.zrd.jmeUtil.applicationHelp.ApplicationHelper;
import org.zrd.jmeUtil.materials.MaterialHelper;
import org.zrd.probeTracking.ProbeTracker;
import org.zrd.probeTrackingRender.ProbeMoveAction;
import org.zrd.probeTrackingRender.ProbeRotationCalibration;
import org.zrd.probeTracking.ProbeTrackerOnSurface;
import org.zrd.util.fileHelper.FilePathHelper;
import org.zrd.util.properties.PropertiesHelper;
import org.zrd.probeTrackingRender.ProbeTrackerRecording;
import org.zrd.probeTrackingRender.ProbeTrackerRender;
import org.zrd.probeTrackingRender.ResetTracker;


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
    private SignalProcessingOutput_Threaded signalTracker;

    private boolean renderPathsDuringRecording;
    
    private boolean enableSignalTracking = true;
    
    public static void main(String[] args) {
        ApplicationHelper.initializeApplication(new Main());
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
        Path defaultOutputPath = FilePathHelper.getDefaultOutputFolder();
        boolean surfaceTrackingOn = PropertiesHelper.getBooleanValueProperty(props, "surfaceTrackingOn");
        boolean showTriangleNormals = PropertiesHelper.getBooleanValueProperty(props, "showTriangleNormals");
        boolean useFixedPointsIfExists = PropertiesHelper.getBooleanValueProperty(props, "useFixedPointsIfExists");
        enableSignalTracking = PropertiesHelper.getBooleanValueProperty(props, "enableSignalTracking");
        
        /*
        * This turns on real-time rendering
        * If it is on, the min arc length determines how long in the virtual world
        *      the path is before it is rendered
        */
        renderPathsDuringRecording=PropertiesHelper.getBooleanValueProperty(props, "realTimeRenderingEnabled");
        float minArcLengthForRender = PropertiesHelper.getFloatValueProperty(props, "minArcLengthForRender");
        
        Material lineMaterial = MaterialHelper.getColorMaterial(assetManager,ColorRGBA.Black);
        recordedPathSet = new RecordedPathSet();
        outputText = new LiveTrackingText(guiNode,assetManager);
        
        //initializes the mesh session variables
        MeshSession currentSession = new MeshSession(assetManager,cam);
        Node shootables = currentSession.getShootableMesh();
        FixedPointPicker fixedPtsToPick = currentSession.getFixedPtsToPick();
        MeshInteractionFiles meshInterFiles = currentSession.getMeshInterFiles();
        TriangleSet meshInfo = currentSession.getMeshInfo();
        
        //initialize tracker
        CalibrationProperties results = currentSession.getAllCalibrationProperties();
        probeTracker = ProbeTracker_BLIProbePath.createNewProbeTracker(inputManager,props,results);
        Node moveableObject = ProbeRepresentation.getProbeRepresentation(
                assetManager,results.getScaleFactorX(),
                results.getScaleFactorY(),results.getScaleFactor());
        
        //initialize the defaults
        viewPort.setBackgroundColor(ApplicationHelper.BACKGROUND_COLOR);
        
        //display default objects
        rootNode.attachChild(currentSession.getFixedPointNode());
        rootNode.attachChild(shootables);
        rootNode.attachChild(moveableObject);
        
        //lines for the normals for verification purposes
        if(showTriangleNormals) rootNode.attachChild(currentSession.getTriangleNormalDisplay());
        
        //initialize camera coordinate actions
        CameraTrackingIO.initializeCameraTrackingIO(inputManager, cam, flyCam, meshInterFiles);
        
        //initialize surface tracker
        probeTrackerOnSurface = new ProbeTrackerOnSurface(probeTracker,meshInfo,FilePathHelper.getDefaultOutputFolder());
        activeTracker = surfaceTrackingOn ? probeTrackerOnSurface : probeTracker;
        
        //makes the signal tracker
        if(enableSignalTracking){
            signalTracker = new SignalProcessingOutput_Threaded(100,14);
            activeTracker.setDataArrayToStringConvertor(signalTracker.getDataTracker());
            activeTracker.setOutputStreaming(signalTracker);
        }
        
        
        //initialize tracker actions
        new ResetTracker(inputManager,probeTracker);
        new ProbeTrackerRecording(inputManager,recordedPathSet,activeTracker);
        
        new ProbeRotationCalibration(inputManager, cam, shootables, probeTracker, meshInfo,
                defaultOutputPath,fixedPtsToPick);
        
        //initialize active tracker actions
        probeMoveAction = ProbeMoveAction.initializeProbeMoveAction(inputManager, 
                cam, shootables, activeTracker, meshInfo, 
                fixedPtsToPick, useFixedPointsIfExists);
        probeTrackerRender = new ProbeTrackerRender(activeTracker,moveableObject,lineMaterial,minArcLengthForRender);
        
        //initialize fixed points actions
        new RecordFixedPoints(inputManager,probeMoveAction,defaultOutputPath,meshInterFiles);
        
        //initialize mesh session recording
        new RecordMeshSessionInfo(inputManager,meshInterFiles);
        
        
        
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
        
        //signalTracker.setData(activeTracker.getCurrentDataStrings());
        if(enableSignalTracking){
           outputText.setDataText(activeTracker.getStreamingOutput()); 
        }
        
    }
}
