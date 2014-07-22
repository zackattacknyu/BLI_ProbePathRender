package org.zrd.bliProbePath;

import org.zrd.cameraTracker.presetModes.CameraTrackerImpl_ProbePathRender;
import org.zrd.geometryToolkit.geometryUtil.ProgramConstants;
import org.zrd.cameraTracker.cameraMoves.CameraTracker;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.Properties;
import org.zrd.geometryToolkit.locationTracking.LocationTracker;
import org.zrd.jmeGeometry.renderedObjects.BackgroundBox;
import org.zrd.jmeGeometry.renderedObjects.LolaMesh;
import org.zrd.jmeGeometry.renderedObjects.ProbeRepresentation;
import org.zrd.jmeGeometry.renderedObjects.RenderedMesh;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.pathDataStructure.RecordedPathSet;
import org.zrd.jmeGeometry.renderedObjects.BallMesh;
import org.zrd.jmeUtil.applicationHelp.ApplicationHelper;
import org.zrd.jmeUtil.materials.MaterialHelper;
import org.zrd.probeTracking.ProbeTracker;
import org.zrd.probeTrackingOnSurface.ProbeMoveAction;
import org.zrd.probeTrackingOnSurface.ProbeRotationCalibration;
import org.zrd.probeTrackingOnSurface.ProbeTrackerOnSurface;
import org.zrd.probeTrackingOnSurface.ProbeTrackerRecording;
import org.zrd.probeTrackingOnSurface.ProbeTrackerRender;
import org.zrd.probeTrackingOnSurface.ResetTracker;


/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    public static final boolean SURFACE_TRACKING_ON = false;
    public static final boolean BALL_ON = true;
    
    private Spatial surface,moveableObject;
    private Material lineMaterial;
    
    private ProbeTrackerRender probeTrackerRender;
    private ProbeTracker probeTracker;
    private LocationTracker activeTracker;
    
    private CameraTracker cameraTracker;

    private Node shootables;
    private RecordedPathSet recordedPathSet;

    private TriangleSet meshInfo;

    private ProbeMoveAction probeMoveAction;
    private ProbeTrackerRecording probeRecording;
    private ProbeTrackerOnSurface probeTrackerOnSurface;
    
    private LiveTrackingText outputText;
    
    
    
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
        
        viewPort.setBackgroundColor(ProgramConstants.BACKGROUND_COLOR);
                
        cameraTracker = new CameraTrackerImpl_ProbePathRender(cam,flyCam,inputManager);
        
        int cameraMode = BALL_ON ? 3 : 1;
        cameraTracker.setDefaultCamera((short)cameraMode);
        
        RenderedMesh activeMesh;
        if(BALL_ON){
            activeMesh = new BallMesh(assetManager);
        }else{
            activeMesh = new LolaMesh(assetManager);
        }
        lineMaterial = MaterialHelper.makeColorMaterial(assetManager,ColorRGBA.Black);

        probeTracker = ProbeTracker_BLIProbePath.createNewProbeTracker(inputManager);
        
        moveableObject = ProbeRepresentation.getProbeRepresentation(assetManager);
        rootNode.attachChild(moveableObject);
        
        if(!BALL_ON) rootNode.attachChild(BackgroundBox.getBackgroundBox(assetManager));

        meshInfo = activeMesh.getActiveMeshInfo();
        surface = activeMesh.getSurfaceMesh();
        
        shootables = new Node("shootables");
        shootables.attachChild(surface);
        
        rootNode.attachChild(shootables);
        
        recordedPathSet = new RecordedPathSet();

        probeMoveAction = new ProbeMoveAction(inputManager,cam,shootables,probeTracker);
        probeRecording = new ProbeTrackerRecording(inputManager,recordedPathSet,probeTracker);
        ResetTracker resetTracker = new ResetTracker(inputManager,probeTracker);
        ProbeRotationCalibration rotCalib = new ProbeRotationCalibration(inputManager, cam, shootables, probeTracker, meshInfo);
        probeTrackerOnSurface = new ProbeTrackerOnSurface(probeTracker,rotCalib,meshInfo);
        
        if(SURFACE_TRACKING_ON){
            activeTracker = probeTrackerOnSurface;
        }else{
            activeTracker = probeTracker;
        }
        
        probeTrackerRender = new ProbeTrackerRender(activeTracker,moveableObject,lineMaterial);
        
        outputText = new LiveTrackingText(guiNode,assetManager);
        
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
        
        probeTrackerRender.updateRenderPathInfo();
        if(probeTrackerRender.isNewRenderedPathsExist()){
            rootNode.attachChild(probeTrackerRender.getRenderedPaths());
        }
        
        outputText.setXyzText(activeTracker.getXYZtext());
        outputText.setYawPitchRollText(activeTracker.getYawPitchRollText());
        
        outputText.setProbeMoveModeText(probeMoveAction.getProbeMoveModeText());
    }
}
