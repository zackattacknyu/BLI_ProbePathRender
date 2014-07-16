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
import org.zrd.jmeGeometry.renderedObjects.BackgroundBox;
import org.zrd.jmeGeometry.renderedObjects.LolaMesh;
import org.zrd.jmeGeometry.renderedObjects.ProbeRepresentation;
import org.zrd.jmeGeometry.renderedObjects.RenderedMesh;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.pathDataStructure.RecordedPathSet;
import org.zrd.graphicsToolsImpl.pathImpl.PathRenderHelper;
import org.zrd.jmeUtil.applicationHelp.ApplicationHelper;
import org.zrd.jmeUtil.materials.MaterialHelper;
import org.zrd.probeTracking.ProbeTracker;
import org.zrd.probeTrackingOnSurface.ProbeMoveAction;
import org.zrd.probeTrackingOnSurface.ProbeRotationCalibration;
import org.zrd.probeTrackingOnSurface.ProbeTrackerOnSurface;
import org.zrd.probeTrackingOnSurface.ProbeTrackerRecording;
import org.zrd.probeTrackingOnSurface.ResetTracker;


/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    private Spatial surface,moveableObject;
    private Material lineMaterial;
    
    private BitmapText yawPitchRollText, xyzText, recordingText, resetProbeText, probeMoveModeText;
    
    private ProbeTracker probeTracker;
    
    private CameraTracker cameraTracker;

    private Node shootables;
    private RecordedPathSet recordedPathSet;

    private TriangleSet meshInfo;

    private ProbeMoveAction probeMoveAction;
    private ProbeTrackerRecording probeRecording;
    private ProbeTrackerOnSurface probeTrackerOnSurface;
    
    public static void main(String[] args) {
        Properties appProps = Properties_BLIProbePath.getProperties();
        ApplicationHelper.initializeApplication(new Main(), appProps);
    }

    @Override
    public void simpleInitApp() {
        
        viewPort.setBackgroundColor(ProgramConstants.BACKGROUND_COLOR);
                
        cameraTracker = new CameraTrackerImpl_ProbePathRender(cam,flyCam,inputManager);
        cameraTracker.setDefaultCamera((short)1);
        
        RenderedMesh activeMesh = new LolaMesh(assetManager);
        lineMaterial = MaterialHelper.makeColorMaterial(assetManager,ColorRGBA.Black);

        probeTracker = ProbeTracker_BLIProbePath.createNewProbeTracker(inputManager);
        
        moveableObject = ProbeRepresentation.getProbeRepresentation(assetManager);
        rootNode.attachChild(moveableObject);
        rootNode.attachChild(BackgroundBox.getBackgroundBox(assetManager));

        meshInfo = activeMesh.getActiveMeshInfo();
        surface = activeMesh.getSurfaceMesh();
        
        
        initDebugText();
        
        shootables = new Node("shootables");
        shootables.attachChild(surface);
        
        rootNode.attachChild(shootables);
        
        recordedPathSet = new RecordedPathSet();

        probeMoveAction = new ProbeMoveAction(inputManager,cam,shootables,probeTracker);
        probeRecording = new ProbeTrackerRecording(inputManager,recordedPathSet,probeTracker);
        ResetTracker resetTracker = new ResetTracker(inputManager,probeTracker);
        ProbeRotationCalibration rotCalib = new ProbeRotationCalibration(inputManager, cam, shootables, probeTracker, meshInfo);
        probeTrackerOnSurface = new ProbeTrackerOnSurface(probeTracker,rotCalib);
    }

    @Override
    public void simpleUpdate(float tpf) {
        /*explained here is how the update loop works
         * http://hub.jmonkeyengine.org/wiki/doku.php/jme3:beginner:hello_main_event_loop
         */
        
        probeTrackerOnSurface.updateData();
        
        moveableObject.setLocalRotation(probeTracker.getLocalRotation());
        
        moveableObject.setLocalTranslation(probeTracker.getCurrentPosition());
        
        xyzText.setText(probeTracker.getXYZtext());
        yawPitchRollText.setText(probeTracker.getYawPitchRollText());
        
        probeMoveModeText.setText(probeMoveAction.getProbeMoveModeText());
        
        if(probeRecording.isNewPathExists()){
            displayPath(PathRenderHelper.createLineFromVertices(recordedPathSet.getCurrentPath(), lineMaterial));
        }
        
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    private void initDebugText(){
        
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        
        float currentStartY = 0.0f;
        
        recordingText = initializeNewText(currentStartY);
        recordingText.setText("Press N to record a new path");
        currentStartY = currentStartY + recordingText.getLineHeight();
        
        resetProbeText = initializeNewText(currentStartY);
        resetProbeText.setText("Press H to reset probe to (0,0)");
        currentStartY = currentStartY + resetProbeText.getLineHeight();
        
        yawPitchRollText = initializeNewText(currentStartY);
        currentStartY = currentStartY + yawPitchRollText.getLineHeight();
        
        xyzText = initializeNewText(currentStartY);
        currentStartY = currentStartY + xyzText.getLineHeight();
        
        probeMoveModeText = initializeNewText(currentStartY);
        probeMoveModeText.setText("Press J to Enable Clicking Probe Movement");
        
    }
    
    private BitmapText initializeNewText(float currentStartY){
        
        BitmapText newText = new BitmapText(guiFont,false);
        newText.setColor(ColorRGBA.Red);
        newText.setSize(guiFont.getCharSet().getRenderedSize());
        newText.setLocalTranslation(0,currentStartY, 0);
        guiNode.attachChild(newText);
        return newText;
        
    }
    
    private void displayPath(Spatial path){
        rootNode.attachChild(path);
    }
}
