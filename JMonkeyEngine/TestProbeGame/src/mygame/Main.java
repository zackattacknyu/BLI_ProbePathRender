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
import java.util.Properties;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    private Spatial littleObject,startBox,endBox,surface;
    private Material ballMat,boxMat,probeMat,lineMaterial;
    
    private BitmapText yawPitchRollText, xyzText, scaleXtext, scaleYtext, readModeText, recordingText, resetProbeText;
    
    private ProbeTracker probeTracker;
    
    private Properties trackerProps;
    private boolean lightVisible;
    

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        String objFileLocation = "Models/textured_mesh.obj";
        
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
        
        startBox = initSampleBox(boxMat, "startCube");
        endBox = initSampleBox(boxMat, "endCube");
        
        //rootNode.attachChild(startBox);
        //rootNode.attachChild(endBox);
        
        endBox.move(4.0f, 4.0f, 0.0f);
        
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
        littleObject.setLocalScale(1.0f/40.0f);
        littleObject.setLocalTranslation(0.0f, 0.0f, 0.0f);
        littleObject.setMaterial(material);
    }
    
    private Spatial initSampleBox(Material ballMat, String name){
        Box b = new Box(0.5f, 0.5f, 0.5f);
        Spatial sampleBox = new Geometry("Box", b);
        sampleBox.setName(name);
        sampleBox.setLocalScale(1);
        sampleBox.setMaterial(ballMat);
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
        
        yawPitchRollText = initializeNewText();
        yawPitchRollText.setLocalTranslation(
                (cam.getWidth()) / 2, 
                cam.getHeight(), 0);       
        
        xyzText = initializeNewText();
        xyzText.setLocalTranslation(
                (cam.getWidth()) / 2, 
                (cam.getHeight() - xyzText.getLineHeight()), 0);
        
        scaleXtext = initializeNewText();
        scaleXtext.setText("Virtual X to real X scale factor "
                + "(Press X to recalibrate):"
                + "------------------------- ");
        scaleXtext.setLocalTranslation(
                (cam.getWidth() - scaleXtext.getLineWidth()), 
                (scaleXtext.getLineHeight()*3), 0);
        scaleXtext.setText("Virtual X to real X scale factor "
                + "(Press X to recalibrate): " + probeTracker.getScaleFactorX());
        
        recordingText = initializeNewText();
        recordingText.setText("Press N to record a new path");
        recordingText.setLocalTranslation(
                (cam.getWidth() - recordingText.getLineWidth()) / 2, 
                (cam.getHeight() - recordingText.getLineHeight()*3), 0);
        
        resetProbeText = initializeNewText();
        resetProbeText.setText("Press H to reset probe to (0,0)");
        resetProbeText.setLocalTranslation(
                (cam.getWidth() - resetProbeText.getLineWidth()) / 2, 
                (cam.getHeight() - resetProbeText.getLineHeight()*4), 0);
        
        scaleYtext = initializeNewText();
        scaleYtext.setText("Virtual Y to real Y scale factor "
                + "(Press Y to recalibrate):"
                + "------------------------- ");
        scaleYtext.setLocalTranslation(
                (cam.getWidth() - scaleYtext.getLineWidth()), 
                (scaleYtext.getLineHeight()*2), 0);
        scaleYtext.setText("Virtual Y to real Y scale factor "
                + "(Press Y to recalibrate): " + probeTracker.getScaleFactorY());
        
        readModeText = initializeNewText();
        readModeText.setText("Probe Output Reading (Press V to change): "
                + "---------------------------------------"
                + "------------------------------------------------");
        readModeText.setLocalTranslation(
                (cam.getWidth() - readModeText.getLineWidth()), 
                (readModeText.getLineHeight()), 0);
        readModeText.setText("Probe B to start calibrating the probe");
        
        
        
    }
    
    private BitmapText initializeNewText(){
        
        BitmapText newText = new BitmapText(guiFont,false);
        newText.setSize(guiFont.getCharSet().getRenderedSize());
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
