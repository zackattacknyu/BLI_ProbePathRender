package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.LightControl;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import java.util.Properties;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    private Spatial littleObject,background,surface;
    private Material ballMat,boxMat,probeMat,lineMaterial;
    
    private BitmapText yawPitchRollText, xyzText, scaleXtext, scaleYtext, readModeText, recordingText, resetProbeText;
    
    private ProbeTracker probeTracker;
    
    private Properties trackerProps;
    private boolean lightVisible;
    

    public static void main(String[] args) {
        
        /*
         * Refer to these two web pages to find out about start settings:
         *      http://hub.jmonkeyengine.org/wiki/doku.php/jme3:intermediate:appsettings
         * 
         *      http://hub.jmonkeyengine.org/wiki/doku.php/jme3:intermediate:simpleapplication
         * 
         * 
         */
        Properties appProps = PropertiesHelper.getProperties();
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
        
        
        
        String objFileLocation = "Models/textured_mesh3.obj";
        viewPort.setBackgroundColor(ColorRGBA.White);
        trackerProps = PropertiesHelper.getProperties();
        lightVisible = Boolean.parseBoolean(
                trackerProps.getProperty("lighting.visible"));
        
        if(lightVisible){
            ballMat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
            ballMat.setTexture("DiffuseMap",assetManager.loadTexture("Textures/ball_texture_2.png"));
        }else{
            ballMat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
            ballMat.setTexture("ColorMap",assetManager.loadTexture("Textures/ball_texture_2.png"));
        }
        
        boxMat = new Material(assetManager,"Common/MatDefs/Misc/ShowNormals.j3md");
        
        surface = ModelHelper.generateModel(
                objFileLocation, ballMat, assetManager);
        
        //moves the front of the ball to the (0,0,0) location
        surface.move(0, 0, -16.5f);
        
        //makes the scale better
        surface.scale(0.75f);
        
        rootNode.attachChild(surface);
        
        
        probeMat = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        probeMat.setTexture("ColorMap", assetManager.loadTexture("Textures/plastic_texture.jpg"));
        
        lineMaterial = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        lineMaterial.setColor("Color", ColorRGBA.Red);
        
        initLittleBox(probeMat);
        
        rootNode.attachChild(littleObject);
        
        
        
        if(lightVisible) addLighting();
        
        background = initBackgroundBox(boxMat, "background");
        
        rootNode.attachChild(background);
        
        setDefaultCamera();
        enableFlyCam();
        initKeyboardInputs();
        
        probeTracker = new ProbeTracker();
        
        initDebugText();
        
        
    }
    
    private void addLighting(){
        PointLight ballLight = new PointLight();
        ballLight.setColor(ColorRGBA.White);
        ballLight.setRadius(100f);
        ballLight.setPosition(new Vector3f(0,0,-5f));
        
        PointLight probeLight = new PointLight();
        probeLight.setColor(ColorRGBA.White);
        probeLight.setRadius(20f);
        probeLight.setPosition(littleObject.getLocalTranslation());
        LightControl ballLightPos = new LightControl(probeLight);
        littleObject.addControl(ballLightPos);
        
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(2.0f));
        
        rootNode.addLight(al);
        
        rootNode.addLight(ballLight);
        rootNode.addLight(probeLight);
    }
    
    private void initLittleBox(Material material){
        littleObject = ModelHelper.generateModel("Models/ultrasoundProbe2.obj", material, assetManager);
        littleObject.setName("Probe");
        littleObject.setLocalScale(1.0f/25.0f);
        littleObject.setLocalTranslation(0.0f, 0.0f, 0.0f);
        littleObject.setMaterial(material);
    }
    
    private Spatial initBackgroundBox(Material ballMat, String name){
        Box b = new Box(10f, 10f, 10f);
        Quad q = new Quad(10f,10f);
        Spatial sampleBox = new Geometry("Background", q);
        sampleBox.setCullHint(Spatial.CullHint.Never);
        sampleBox.setName(name);
        sampleBox.setLocalScale(1);
        sampleBox.setMaterial(ballMat);
        sampleBox.setLocalTranslation(0.0f, 0.0f, 0.0f);
        return sampleBox;
    }

    @Override
    public void simpleUpdate(float tpf) {
        /*explained here is how the update loop works
         * http://hub.jmonkeyengine.org/wiki/doku.php/jme3:beginner:hello_main_event_loop
         */
        
        probeTracker.updateValues();
        
        littleObject.setLocalRotation(probeTracker.getDisplayRotation());
        
        littleObject.setLocalTranslation(probeTracker.getLocalTranslation());

        xyzText.setText("(X,Y,Z) = (" + probeTracker.getCurrentX() + ","
                + probeTracker.getCurrentY() + "," 
                + probeTracker.getCurrentZ() + ")");
        yawPitchRollText.setText("(Yaw,Pitch,Roll) = (" + 
                probeTracker.getCurrentYaw()*FastMath.RAD_TO_DEG + "," + 
                probeTracker.getCurrentPitch()*FastMath.RAD_TO_DEG + "," + 
                probeTracker.getCurrentRoll()*FastMath.RAD_TO_DEG + ")");
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
   
    private void setDefaultCamera(){
        cam.setLocation(new Vector3f(1.9081001f, 6.6795464f, -16.626781f));
        cam.setRotation(new Quaternion(0.17346787f, -0.07118106f, 0.012571785f, 0.98218334f));
    }
    private void enableFlyCam(){
        flyCam.setEnabled(true);
        flyCam.setDragToRotate(true);
        flyCam.setMoveSpeed(10f);
        flyCam.setRotationSpeed(10f);
    }
    
    private void initDebugText(){
        
        float currentStartY = 0.0f;
        
        recordingText = initializeNewText(currentStartY);
        recordingText.setText("Press N to record a new path");
        currentStartY = currentStartY + recordingText.getLineHeight();
        
        resetProbeText = initializeNewText(currentStartY);
        resetProbeText.setText("Press H to reset probe to (0,0)");
        currentStartY = currentStartY + resetProbeText.getLineHeight();
        
        readModeText = initializeNewText(currentStartY);
        currentStartY = currentStartY + readModeText.getLineHeight();
        readModeText.setText("Probe B to start calibrating the probe");
        
        scaleYtext = initializeNewText(currentStartY);
        currentStartY = currentStartY + scaleYtext.getLineHeight();
        scaleYtext.setText("Virtual Y to real Y scale factor "
                + "(Press Y to recalibrate): " + probeTracker.getScaleFactorY());
        
        scaleXtext = initializeNewText(currentStartY);
        currentStartY = currentStartY + scaleXtext.getLineHeight();
        scaleXtext.setText("Virtual X to real X scale factor "
                + "(Press X to recalibrate): " + probeTracker.getScaleFactorX());
        
        yawPitchRollText = initializeNewText(currentStartY);
        currentStartY = currentStartY + yawPitchRollText.getLineHeight();
        
        xyzText = initializeNewText(currentStartY);
        //currentStartY = currentStartY + xyzText.getLineHeight();
        
    }
    
    private BitmapText initializeNewText(float currentStartY){
        
        BitmapText newText = new BitmapText(guiFont,false);
        newText.setColor(ColorRGBA.Red);
        newText.setSize(guiFont.getCharSet().getRenderedSize());
        newText.setLocalTranslation(0,currentStartY, 0);
        guiNode.attachChild(newText);
        return newText;
        
    }
    
    private void displayReadMode(){
        
        readModeText.setText(probeTracker.getReadModeText());
        
        
    }

    private void initKeyboardInputs() {
        //ChaseCamera chaser = new ChaseCamera(cam, littleObject);
        //chaser.registerWithInput(inputManager);
        
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        
        inputManager.addMapping("moveInward", new KeyTrigger(KeyInput.KEY_R));
        inputManager.addMapping("moveOutward", new KeyTrigger(KeyInput.KEY_F));
        
        inputManager.addMapping("startStopNewPath", new KeyTrigger(KeyInput.KEY_N));
        
        inputManager.addMapping("recalibrateX", new KeyTrigger(KeyInput.KEY_X));
        inputManager.addMapping("recalibrateY", new KeyTrigger(KeyInput.KEY_Y));
        
        inputManager.addMapping("resetProbe", new KeyTrigger(KeyInput.KEY_H));
        
        inputManager.addMapping("readModeChange", new KeyTrigger(KeyInput.KEY_V));
        
        inputManager.addMapping("moveUp", new KeyTrigger(KeyInput.KEY_NUMPAD8));
        inputManager.addMapping("moveDown", new KeyTrigger(KeyInput.KEY_NUMPAD2));
        
        inputManager.addMapping("moveLeft", new KeyTrigger(KeyInput.KEY_NUMPAD4));
        inputManager.addMapping("moveRight", new KeyTrigger(KeyInput.KEY_NUMPAD6));
        
        inputManager.addMapping("rotateClockwise", new KeyTrigger(KeyInput.KEY_NUMPAD1));
        inputManager.addMapping("rotateCounterClockwise", new KeyTrigger(KeyInput.KEY_NUMPAD3));
        
        inputManager.addMapping("recalibrateProbe", new KeyTrigger(KeyInput.KEY_B));
        
        inputManager.addMapping("pitchLeft", new KeyTrigger(KeyInput.KEY_NUMPAD7));
        inputManager.addMapping("pitchRight", new KeyTrigger(KeyInput.KEY_NUMPAD9));
        
        inputManager.addMapping("rollForward", new KeyTrigger(KeyInput.KEY_NUMPAD5));
        inputManager.addMapping("rollBackward", new KeyTrigger(KeyInput.KEY_NUMPAD0));
        
        inputManager.addMapping("startStopNewPath", new KeyTrigger(KeyInput.KEY_N));
        
        ActionListener acl = new ActionListener() {

            public void onAction(String name, boolean keyPressed, float tpf) {
                
                if(name.equals("startStopNewPath") && keyPressed){
                     
                     probeTracker.updatePathRecording();
                     recordingText.setText(probeTracker.getRecordingText());
                     if(probeTracker.isNewPathExists()){
                         Spatial currentLine = 
                                 LineHelper.createLineFromVertices(
                                 probeTracker.getCurrentPathVertices(), 
                                 ballMat);
                         currentLine.setMaterial(lineMaterial);
                         rootNode.attachChild(currentLine);
                     }
                     
                 }
                
                if(name.equals("moveInward") && keyPressed){
                    //littleObject.move(0, 0, 1.0f/20.0f);
                }
                if(name.equals("moveOutward") && keyPressed){
                    //littleObject.move(0,0, -1.0f/20.0f);
                }
                
                if(name.equals("rotateClockwise") && keyPressed){
                    probeTracker.rotateClockwise();
                }
                if(name.equals("rotateCounterClockwise") && keyPressed){
                    probeTracker.rotateCounterClockwise();
                }
                
                if(name.equals("resetProbe") && keyPressed){
                    probeTracker.resetProbe();
                }
                
                if(name.equals("moveUp") && keyPressed){
                    probeTracker.moveUp();
                }
                
                if(name.equals("moveDown") && keyPressed){
                    probeTracker.moveDown();
                }
                
                if(name.equals("pitchLeft") && keyPressed){
                    probeTracker.pitchLeft();
                }
                
                if(name.equals("pitchRight") && keyPressed){
                    probeTracker.pitchRight();
                }
                
                if(name.equals("rollForward") && keyPressed){
                    probeTracker.rollForward();
                }
                
                if(name.equals("rollBackward") && keyPressed){
                    probeTracker.rollBackward();
                }
                
                if(name.equals("moveLeft") && keyPressed){
                    probeTracker.moveLeft();
                }
                
                if(name.equals("moveRight") && keyPressed){
                    probeTracker.moveRight();
                }
                
                
                if(name.equals("recalibrateX") && keyPressed){
                    probeTracker.updateXcalibration();
                    scaleXtext.setText(probeTracker.getScaleXtext());
                }
                
                if(name.equals("recalibrateY") && keyPressed){
                    probeTracker.updateYcalibration();
                    scaleYtext.setText(probeTracker.getScaleYtext());
                }
                
                if(name.equals("readModeChange") && keyPressed){
                    probeTracker.incrementReadMode();
                    displayReadMode();
                }
                
                if(name.equals("recalibrateProbe") && keyPressed){
                    probeTracker.updateProbeCalibration();
                    displayReadMode();
                }
            }
        };

        inputManager.addListener(acl, 
                "moveInward",
                "moveOutward",
                "startStopNewPath",
                "recalibrateX",
                "recalibrateY",
                "readModeChange",
                "moveUp",
                "moveDown",
                "moveLeft",
                "moveRight",
                "rotateClockwise",
                "rotateCounterClockwise",
                "resetProbe",
                "recalibrateProbe",
                "pitchRight",
                "pitchLeft",
                "rollBackward",
                "rollForward");

    }
}