package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Spline;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Line;
import com.jme3.util.BufferUtils;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    private Spatial littleObject;
    private Spatial startBox;
    private Spatial endBox;
    private boolean active = false;
    private boolean playing = false;
    private boolean pathDisplayed = false;
    private boolean recordingPath = false;
    private ArrayList<Vector3f> pathVertices;
    private MotionPath path;
    private MotionEvent motionControl;
    private BitmapText wayPointsText;
    private Material ballMat,probeMat;
    private ArduinoDataInterpreter dataInterpreter;
    private float currentX = 0, currentY = 0, baselineYaw = 0, currentYaw = 0;
    private float currentManualDeltaX = 0, currentManualDeltaY = 0;
    private float currentDeltaX = 0, currentDeltaY = 0;
    
    private BitmapText yawText, xText, yText, scaleXtext, scaleYtext, readModeText, recordingText, resetProbeText;
    
    private float scaleFactorX = 0.002f,scaleFactorY = 0.002f;
    
    private boolean calibratingX = false, calibratingY = false;
    
    private short readMode = 0;
    
    private PathRecorder cubePath;
    

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        String objFileLocation = "Models/textured_mesh.obj";
        String sampleDataLocation = "textFiles/sampleData.txt";
        
        ballMat = new Material(assetManager, 
                "Common/MatDefs/Misc/ShowNormals.j3md");
        rootNode.attachChild(ModelHelper.generateModel(
                objFileLocation, ballMat, assetManager));
        
        probeMat = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        probeMat.setTexture("ColorMap", assetManager.loadTexture("Textures/plastic_texture.jpg"));
        initLittleBox(probeMat);
        
        rootNode.attachChild(littleObject);
        
        startBox = initSampleBox(ballMat, "startCube");
        endBox = initSampleBox(ballMat, "endCube");
        
        rootNode.attachChild(startBox);
        rootNode.attachChild(endBox);
        
        endBox.move(4.0f, 4.0f, 0.0f);
        
        ArrayList<Vector3f> lineVertices = 
                ProbeDataHelper.getVerticesFromFile(sampleDataLocation);
        path = ProbeDataHelper.getMotionPathFromVertices(lineVertices);
        //rootNode.attachChild(LineHelper.createLineFromVertices(lineVertices, ballMat));
        
        setDefaultCamera();
        enableFlyCam();
        initPathInputs();
        initDebugText();
        
        dataInterpreter = new ArduinoDataInterpreter();
    }
    
    
    
    private void initLittleBox(Material material){
        //Box b = new Box(0.5f, 0.5f, 0.5f);
        cubePath = new PathRecorder(0,0);
        //littleObject = new Geometry("Box", b);
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
        
        //keep this if we just want to test the serial output
        //dataInterpreter.setOnlyDoOutput(true);
        
        dataInterpreter.updateData();
        
        //use this style for displaying the rotation
        if(dataInterpreter.isCalibrated() && readMode > 0){
            currentYaw = dataInterpreter.getOutputYawRadians() + baselineYaw;
        }else{
            currentYaw = baselineYaw;
        }
        
        littleObject.setLocalRotation(LineHelper.getRotationMatrix(currentYaw));
        
        
        //currentX = currentX + dataInterpreter.getDeltaX();
        //currentY = currentY - dataInterpreter.getDeltaY();
        
        boolean useYaw = true;
        
        Vector2f currentDisp;
        
        currentDeltaX = -dataInterpreter.getDeltaX() + currentManualDeltaX;
        currentDeltaY = -dataInterpreter.getDeltaY() + currentManualDeltaY;
        
        if(useYaw){
            currentDisp = LineHelper.getXYDisplacement(currentDeltaX,currentDeltaY,currentYaw);
        }else{
            currentDisp = new Vector2f(currentDeltaX,currentDeltaY);
        }
        
        currentDisp = LineHelper.scaleDisplacement(currentDisp, scaleFactorX, scaleFactorY);
        
        currentManualDeltaX = 0;
        currentManualDeltaY = 0;
        
        currentX = currentX + currentDisp.getX();
        currentY = currentY + currentDisp.getY();
        
        littleObject.setLocalTranslation(currentX, currentY, 0.0f);
        
        /*
         * Around here is where we will want to record the xy path
         */
        if(recordingPath || calibratingX || calibratingY){
            //cubePath.addToPath(dataInterpreter.getDeltaX(), -dataInterpreter.getDeltaY());
            cubePath.addToPath(currentDisp);
        }
        
        xText.setText("current X = " + currentX);
        yText.setText("current Y = " + currentY);
        yawText.setText("current Yaw = " + currentYaw*FastMath.RAD_TO_DEG);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
   
    private void setDefaultCamera(){
        cam.setLocation(new Vector3f(2,2,-20));
        cam.lookAt(new Vector3f(0,0,10), new Vector3f(0,1,0));
    }
    private void enableFlyCam(){
        flyCam.setEnabled(true);
        flyCam.setDragToRotate(true);
        flyCam.setMoveSpeed(10f);
        flyCam.setRotationSpeed(10f);
    }
    
    private void initDebugText(){
        
        yawText = initializeNewText();
        yawText.setLocalTranslation(
                (cam.getWidth() - yawText.getLineWidth()) / 2, 
                cam.getHeight(), 0);       
        
        xText = initializeNewText();
        xText.setLocalTranslation(
                (cam.getWidth() - xText.getLineWidth()) / 2, 
                (cam.getHeight() - xText.getLineHeight()), 0);
        
        
        yText = initializeNewText();
        yText.setLocalTranslation(
                (cam.getWidth() - yText.getLineWidth()) / 2, 
                (cam.getHeight() - yText.getLineHeight()*2), 0);
        
        scaleXtext = initializeNewText();
        scaleXtext.setText("Virtual X to real X scale factor "
                + "(Press X to recalibrate):"
                + "------------------------- ");
        scaleXtext.setLocalTranslation(
                (cam.getWidth() - scaleXtext.getLineWidth()), 
                (scaleXtext.getLineHeight()*3), 0);
        scaleXtext.setText("Virtual X to real X scale factor "
                + "(Press X to recalibrate): " + scaleFactorX);
        
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
                + "(Press Y to recalibrate): " + scaleFactorY);
        
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
        
        switch(readMode){
            case 0:
                readModeText.setText("Probe Output Reading "
                        + "(Press V to change): "
                        + "Only Show Output");
                break;
            case 1:
                readModeText.setText("Probe Output Reading "
                        + "(Press V to change): "
                        + "Raw Output Mode");
                dataInterpreter.setRawSwitch(0);
                break;

            case 2:
                readModeText.setText("Probe Output Reading "
                        + "(Press V to change): "
                        + "Low-Pass Filter Mode");
                dataInterpreter.setUseLowPassFilterData(true);
                dataInterpreter.setRawSwitch(0);
                break;

            case 3:
                readModeText.setText("Probe Output Reading "
                        + "(Press V to change): "
                        + "Mean Error as Threshold Mode");
                dataInterpreter.setUseLowPassFilterData(false);
                dataInterpreter.setRawSwitch(1);
                break;
        }
        
        
    }

    private void initPathInputs() {
        //ChaseCamera chaser = new ChaseCamera(cam, littleObject);
        //chaser.registerWithInput(inputManager);
        
        path.setPathSplineType(Spline.SplineType.Linear);

        motionControl = new MotionEvent(littleObject,path);
        motionControl.setDirectionType(MotionEvent.Direction.PathAndRotation);
        motionControl.setRotation(new Quaternion().fromAngleNormalAxis(-FastMath.HALF_PI, Vector3f.UNIT_Y));
        motionControl.setInitialDuration(10f);
        motionControl.setSpeed(2f);       
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        wayPointsText = new BitmapText(guiFont, false);
        wayPointsText.setSize(guiFont.getCharSet().getRenderedSize());
        wayPointsText.setLocalTranslation(
                (cam.getWidth() - wayPointsText.getLineWidth()) / 2, 
                cam.getHeight(), 0);

        guiNode.attachChild(wayPointsText);
        
        inputManager.addMapping("display_hidePath", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("play_stop", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("tensionUp", new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping("tensionDown", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("SwitchPathInterpolation", new KeyTrigger(KeyInput.KEY_I));
        
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
        
        ActionListener acl = new ActionListener() {

            public void onAction(String name, boolean keyPressed, float tpf) {
                
                if(name.equals("moveInward") && keyPressed){
                    littleObject.move(0, 0, 1.0f/20.0f);
                }
                if(name.equals("moveOutward") && keyPressed){
                    littleObject.move(0,0, -1.0f/20.0f);
                }
                
                if(name.equals("rotateClockwise") && keyPressed){
                    baselineYaw = baselineYaw - 1.0f/20.0f;
                }
                if(name.equals("rotateCounterClockwise") && keyPressed){
                    baselineYaw = baselineYaw + 1.0f/20.0f;
                }
                
                if(name.equals("resetProbe") && keyPressed){
                    currentX = 0;
                    currentY = 0;
                }
                
                if(name.equals("moveUp") && keyPressed){
                    currentManualDeltaY = 1.0f/20.0f;
                }
                
                if(name.equals("moveDown") && keyPressed){
                    currentManualDeltaY = -1.0f/20.0f;
                }
                
                if(name.equals("moveLeft") && keyPressed){
                    currentManualDeltaX = 1.0f/20.0f;
                }
                
                if(name.equals("moveRight") && keyPressed){
                    currentManualDeltaX = -1.0f/20.0f;
                }
                
                if(name.equals("startStopNewPath") && keyPressed){
                    if(recordingPath){
                        System.out.println("Recording New Path Stopped");
                        pathVertices = LineHelper.convertPathRecordingToLineVertices(cubePath);
                        Spatial currentLine = LineHelper.createLineFromVertices(pathVertices, ballMat);
                        
                        recordingText.setText("Press N to record a new path");
                        rootNode.attachChild(currentLine);
                        recordingPath = false;
                    }else{
                        
                        recordingText.setText("Now recording new path (Press N to stop recording)");
                        cubePath = new PathRecorder(currentX,currentY);
                        System.out.println("Now Recording new path");
                        recordingPath = true;
                    }
                }
                
                if (name.equals("display_hidePath") && keyPressed) {
                    if (active) {
                        active = false;
                        path.disableDebugShape();
                    } else {
                        active = true;
                        path.enableDebugShape(assetManager, rootNode);
                    }
                }
                if (name.equals("play_stop") && keyPressed) {
                    if (playing) {
                        playing = false;
                        motionControl.stop();
                        wayPointsText.setText(String.valueOf(motionControl.getCurrentValue()));
                    } else {
                        playing = true;
                        motionControl.play();
                    }
                }
                
                if (name.equals("SwitchPathInterpolation") && keyPressed) {
                    if (path.getPathSplineType() == Spline.SplineType.CatmullRom){
                        path.setPathSplineType(Spline.SplineType.Linear);
                    } else {
                        path.setPathSplineType(Spline.SplineType.CatmullRom);
                    }
                }

                if (name.equals("tensionUp") && keyPressed) {
                    path.setCurveTension(path.getCurveTension() + 0.1f);
                    System.err.println("Tension : " + path.getCurveTension());
                }
                if (name.equals("tensionDown") && keyPressed) {
                    path.setCurveTension(path.getCurveTension() - 0.1f);
                    System.err.println("Tension : " + path.getCurveTension());
                }
                
                if(name.equals("recalibrateX") && keyPressed){
                    if(calibratingX){
                        float lastX = cubePath.getLastX() - cubePath.getFirstX();
                        float realLastX = 8.0f;
                        scaleFactorX = realLastX/lastX;
                        scaleXtext.setText("Virtual X to real X scale factor "
                                + "(Press X to recalibrate): "
                                + scaleFactorX);
                    }else{
                        scaleXtext.setText("Now calibrating. Press X has been moved 8 units right ");
                        cubePath = new PathRecorder(currentX,currentY);
                    }
                    
                    calibratingX = !calibratingX;
                    
                    
                    
                }
                
                if(name.equals("recalibrateY") && keyPressed){
                    if(calibratingY){
                        float lastY = cubePath.getLastY()- cubePath.getFirstY();
                        float realLastY = 8.0f;
                        scaleFactorY = realLastY/lastY;
                        scaleYtext.setText("Virtual X to real X scale factor "
                                + "(Press X to recalibrate): "
                                + scaleFactorY);
                    }else{
                        scaleYtext.setText("Now calibrating. Press Y has been moved 8 units up ");
                        cubePath = new PathRecorder(currentX,currentY);
                    }
                    
                    calibratingY = !calibratingY;
                    
                }
                
                if(name.equals("readModeChange") && keyPressed){
                    
                    readMode++;
                    readMode = (short) (readMode%4);
                    
                    displayReadMode();
                    
                    
                    
                }
                
                if(name.equals("recalibrateProbe") && keyPressed){
                    
                    if(dataInterpreter.isCalibrating()){

                        displayReadMode();

                        dataInterpreter.startStopCalibration();

                    }else{
                        readModeText.setText("Now Recalibrating. Press B again to stop.");
                        dataInterpreter.startStopCalibration();
                    }
                    
                }


            }
        };

        inputManager.addListener(acl, "display_hidePath", 
                "play_stop", 
                "SwitchPathInterpolation", 
                "tensionUp", 
                "tensionDown",
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
                "recalibrateProbe");

    }
}
