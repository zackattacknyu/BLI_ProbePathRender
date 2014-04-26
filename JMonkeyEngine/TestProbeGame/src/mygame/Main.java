package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.Spatial;
import java.util.ArrayList;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    private Spatial littleObject;
    private Spatial startBox;
    private Spatial endBox;
    private Material ballMat,boxMat,probeMat;
    
    private BitmapText yawPitchRollText, xyzText, scaleXtext, scaleYtext, readModeText, recordingText, resetProbeText;
    
    private ProbeTracker probeTracker;
    

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        String objFileLocation = "Models/textured_mesh.obj";
        String sampleDataLocation = "textFiles/sampleData.txt";
        
        ballMat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        boxMat = new Material(assetManager,"Common/MatDefs/Misc/ShowNormals.j3md");
        ballMat.setTexture("ColorMap",assetManager.loadTexture("Textures/ball_texture_2.png"));
        rootNode.attachChild(ModelHelper.generateModel(
                objFileLocation, ballMat, assetManager));
        
        probeMat = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        probeMat.setTexture("ColorMap", assetManager.loadTexture("Textures/plastic_texture.jpg"));
        initLittleBox(probeMat);
        
        rootNode.attachChild(littleObject);
        
        startBox = initSampleBox(boxMat, "startCube");
        endBox = initSampleBox(boxMat, "endCube");
        
        rootNode.attachChild(startBox);
        rootNode.attachChild(endBox);
        
        endBox.move(4.0f, 4.0f, 0.0f);
        
        setDefaultCamera();
        enableFlyCam();
        initKeyboardInputs();
        
        probeTracker = new ProbeTracker();
        
        initDebugText();
        
        
    }
    
    
    
    private void initLittleBox(Material material){
        littleObject = ModelHelper.generateModel("Models/ultrasoundProbe2.obj", material, assetManager);
        littleObject.setName("Probe");
        littleObject.setLocalScale(1.0f/50.0f);
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
        cam.setLocation(new Vector3f(-10.831136f, 4.06632f, -9.481096f));
        cam.setRotation(new Quaternion(0.065406695f, 0.23731168f, -0.01601674f, 0.9690968f));
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
        
        ActionListener acl = new ActionListener() {

            public void onAction(String name, boolean keyPressed, float tpf) {
                
                if(name.equals("moveInward") && keyPressed){
                    littleObject.move(0, 0, 1.0f/20.0f);
                }
                if(name.equals("moveOutward") && keyPressed){
                    littleObject.move(0,0, -1.0f/20.0f);
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
