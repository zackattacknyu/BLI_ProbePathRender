package org.zrd.bliProbePath;

import org.zrd.geometryToolkit.geometryUtil.ProgramConstants;
import org.zrd.cameraTracker.cameraMoves.CameraTracker;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import java.io.File;
import java.nio.file.Paths;
import java.util.Properties;
import org.zrd.bliProbePath.renderedObjects.BackgroundBox;
import org.zrd.bliProbePath.renderedObjects.LolaMesh;
import org.zrd.bliProbePath.renderedObjects.ProbeRepresentation;
import org.zrd.bliProbePath.renderedObjects.RenderedMesh;
import org.zrd.bliProbePath.renderedObjects.SphereMesh;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.pathDataStructure.RecordedPathSet;
import org.zrd.graphicsToolsImpl.pathImpl.PathRenderHelper;
import org.zrd.graphicsToolsImpl.pathImplDebug.PathXYDataDisplay;
import org.zrd.graphicsToolsImpl.pathImplDebug.PathYawPitchRollDataDisplay;
import org.zrd.jmeUtil.materials.MaterialHelper;
import org.zrd.pathInteractions.PathImport;
import org.zrd.probeTracking.ProbeTracker;
import org.zrd.probeTrackingOnSurface.LineMoveAction;
import org.zrd.probeTrackingOnSurface.ProbeMoveAction;
import org.zrd.probeTrackingOnSurface.ProbeTrackerRecording;
import org.zrd.probeTrackingOnSurface.ResetTracker;


/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    private Spatial surface,moveableObject;
    private Material lineMaterial,redMaterial,orangeMaterial,greenMaterial;
    
    private BitmapText yawPitchRollText, xyzText, recordingText, resetProbeText, probeMoveModeText;
    
    private ProbeTracker probeTracker;
    
    private CameraTracker cameraTracker;

    private Node shootables;
    private File initialImportDirectory;
    private RecordedPathSet recordedPathSet;

    private TriangleSet meshInfo;
    
    //this is if we are using the sphere for testing 
    //      instead of lola
    private boolean sphereOn = false;
    
    //if we want to display the raw data instead of 
    //      the sphere or lola mesh. This overrides
    //      the above setting.
    private boolean displayRawDataMode = true;
    
    //to be used if we are only testing using recorded paths
    private boolean showProbeRepLines = true;
    
    private LineMoveAction lineMoveAction;
    private ProbeMoveAction probeMoveAction;
    private PathImport pathImport;
    private ProbeTrackerRecording probeRecording;
    
    public static void main(String[] args) {
        
        /*
         * Refer to these two web pages to find out about start settings:
         *      http://hub.jmonkeyengine.org/wiki/doku.php/jme3:intermediate:appsettings
         * 
         *      http://hub.jmonkeyengine.org/wiki/doku.php/jme3:intermediate:simpleapplication
         * 
         * 
         */
        Properties appProps = Properties_BLIProbePath.getProperties();
        AppSettings settings = new AppSettings(true);
        
        //adjusts title shown on window
        settings.setTitle(appProps.getProperty("settings.title"));
        
        //adjusts resolution
        int xRes = Integer.parseInt(appProps.getProperty("settings.xResolution"));
        int yRes = Integer.parseInt(appProps.getProperty("settings.yResolution"));
        settings.setResolution(xRes, yRes);
        
        Main app = new Main();
        app.setSettings(settings); //disables the setting screen at start-up
        app.setShowSettings(false); //shows the above settings
        app.setDisplayFps(false); //makes sure the fps text is not displayed
        app.setDisplayStatView(false); //makes sure the stat view is not displayed
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        viewPort.setBackgroundColor(ProgramConstants.BACKGROUND_COLOR);
        
        initialImportDirectory = Paths.get("textFiles").toFile();
        
        cameraTracker = new CameraTrackerImpl_ProbePathRender(cam,flyCam,inputManager);
        if(displayRawDataMode){
            cameraTracker.setDefaultCamera((short)2);
        }else if(sphereOn){
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
        
        redMaterial = MaterialHelper.makeColorMaterial(assetManager,ColorRGBA.Red);
        orangeMaterial = MaterialHelper.makeColorMaterial(assetManager,ColorRGBA.Orange);
        greenMaterial = MaterialHelper.makeColorMaterial(assetManager,ColorRGBA.Green);
        lineMaterial = MaterialHelper.makeColorMaterial(assetManager,ColorRGBA.Black);

        probeTracker = ProbeTracker_BLIProbePath.createNewProbeTracker(inputManager);
        
        moveableObject = ProbeRepresentation.getProbeRepresentation(assetManager);
        if(!displayRawDataMode && showProbeRepLines){
            rootNode.attachChild(moveableObject);
        }
        
        if(!sphereOn && !displayRawDataMode){
            rootNode.attachChild(BackgroundBox.getBackgroundBox(assetManager));
        }

        meshInfo = activeMesh.getActiveMeshInfo();
        surface = activeMesh.getSurfaceMesh();
        
        
        initKeyboardInputs();
        initDebugText();
        
        shootables = new Node("shootables");
        shootables.attachChild(surface);
        
        if(!displayRawDataMode){
            rootNode.attachChild(shootables);
        }
        recordedPathSet = new RecordedPathSet();
        
        lineMoveAction = new LineMoveAction(inputManager, cam, shootables, recordedPathSet, meshInfo);
        probeMoveAction = new ProbeMoveAction(inputManager,cam,shootables,probeTracker);
        pathImport = new PathImport(inputManager,recordedPathSet,initialImportDirectory);
        probeRecording = new ProbeTrackerRecording(inputManager,recordedPathSet,probeTracker);
        ResetTracker resetTracker = new ResetTracker(inputManager,probeTracker);
    }

    @Override
    public void simpleUpdate(float tpf) {
        /*explained here is how the update loop works
         * http://hub.jmonkeyengine.org/wiki/doku.php/jme3:beginner:hello_main_event_loop
         */
        
        probeTracker.updateData();
        
        moveableObject.setLocalRotation(probeTracker.getLocalRotation());
        
        moveableObject.setLocalTranslation(probeTracker.getCurrentPosition());
        
        xyzText.setText(probeTracker.getXYZtext());
        yawPitchRollText.setText(probeTracker.getYawPitchRollText());
        
        if(lineMoveAction.hasNewLine()){
            displayPath(PathRenderHelper.createLineFromVertices(lineMoveAction.getCurrentPath(), lineMaterial));
        }
        
        probeMoveModeText.setText(probeMoveAction.getProbeMoveModeText());
        
        if(pathImport.isNewPathExists() || probeRecording.isNewPathExists()){
            displayPath(PathRenderHelper.createLineFromVertices(recordedPathSet.getCurrentPath(), lineMaterial));
        }
        
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    private void initDebugText(){
        
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
    
    private void initKeyboardInputs() {
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        
        inputManager.addMapping("rawXYdisplay", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addMapping("rawYawPitchRollDisplay", new KeyTrigger(KeyInput.KEY_G));

        ActionListener acl = new ActionListener() {

            public void onAction(String name, boolean keyPressed, float tpf) {
                
                if(name.equals("rawXYdisplay") && keyPressed){
                    PathXYDataDisplay probeData = PathXYDataDisplay.obtainXYProbeData(initialImportDirectory);
                    rootNode.attachChild(probeData.generateSpatial(orangeMaterial));
                    rootNode.attachChild(probeData.generateReferenceObject(greenMaterial));
                }
                
                if(name.equals("rawYawPitchRollDisplay") && keyPressed){
                    PathYawPitchRollDataDisplay probeData = PathYawPitchRollDataDisplay.obtainYawPitchRollProbeData(initialImportDirectory);
                    rootNode.attachChild(probeData.generateSpatial(redMaterial));
                    rootNode.attachChild(probeData.generateReferenceObject(greenMaterial));
                }
            }

            
        };

        inputManager.addListener(acl,
                "rawXYdisplay",
                "rawYawPitchRollDisplay");

    }
}
