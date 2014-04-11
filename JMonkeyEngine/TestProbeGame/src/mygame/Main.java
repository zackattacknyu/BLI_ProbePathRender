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
    
    private BitmapText yawText, xText, yText, scaleXtext, scaleYtext, readModeText;
    
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
        currentYaw = dataInterpreter.getCurrentYaw() + baselineYaw;
        littleObject.setLocalRotation(LineHelper.getRotationMatrix(currentYaw));
        
        
        //currentX = currentX + dataInterpreter.getDeltaX();
        //currentY = currentY - dataInterpreter.getDeltaY();
        
        boolean useYaw = true;
        
        Vector2f currentDisp;
        
        currentDeltaX = dataInterpreter.getDeltaX() + currentManualDeltaX;
        currentDeltaY = -dataInterpreter.getDeltaY() + currentManualDeltaY;
        
        if(useYaw){
            currentDisp = LineHelper.getXYDisplacement(currentDeltaX,currentDeltaY,currentYaw);
        }else{
            currentDisp = new Vector2f(currentDeltaX,currentDeltaY);
        }
        
        currentManualDeltaX = 0;
        currentManualDeltaY = 0;
        
        currentX = currentX + currentDisp.getX();
        currentY = currentY + currentDisp.getY();
        
        littleObject.setLocalTranslation(currentX, currentY, 0.0f);
        
        /*
         * Around here is where we will want to record the xy path
         */
        if(recordingPath){
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
                + "(Press X to recalibrate): ");
        
        
        scaleYtext = initializeNewText();
        scaleYtext.setText("Virtual Y to real Y scale factor "
                + "(Press Y to recalibrate):"
                + "------------------------- ");
        scaleYtext.setLocalTranslation(
                (cam.getWidth() - scaleYtext.getLineWidth()), 
                (scaleYtext.getLineHeight()*2), 0);
        scaleYtext.setText("Virtual Y to real Y scale factor "
                + "(Press Y to recalibrate):");
        
        readModeText = initializeNewText();
        readModeText.setText("Probe Output Reading (Press V to change): "
                + "---------------------------------------------------------");
        readModeText.setLocalTranslation(
                (cam.getWidth() - readModeText.getLineWidth()), 
                (readModeText.getLineHeight()), 0);
        readModeText.setText("Probe Output Reading (Press V to change):");
        
    }
    
    private BitmapText initializeNewText(){
        
        BitmapText newText = new BitmapText(guiFont,false);
        newText.setSize(guiFont.getCharSet().getRenderedSize());
        guiNode.attachChild(newText);
        return newText;
        
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
        
        inputManager.addMapping("readModeChange", new KeyTrigger(KeyInput.KEY_V));
        
        inputManager.addMapping("moveUp", new KeyTrigger(KeyInput.KEY_NUMPAD8));
        inputManager.addMapping("moveDown", new KeyTrigger(KeyInput.KEY_NUMPAD2));
        
        inputManager.addMapping("moveLeft", new KeyTrigger(KeyInput.KEY_NUMPAD4));
        inputManager.addMapping("moveRight", new KeyTrigger(KeyInput.KEY_NUMPAD6));
        
        inputManager.addMapping("rotateClockwise", new KeyTrigger(KeyInput.KEY_NUMPAD1));
        inputManager.addMapping("rotateCounterClockwise", new KeyTrigger(KeyInput.KEY_NUMPAD3));
        
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
                        float realLastX = 4.0f;
                        float realLastY = 4.0f;
                        //float realLastZ = 0.0f;
                        float lastX = cubePath.getLastX();
                        float lastY = cubePath.getLastY();
                        //float lastZ = 0.0f;
                        currentLine.scale(realLastX/lastX, realLastY/lastY, 0);
                        rootNode.attachChild(currentLine);
                        recordingPath = false;
                    }else{
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
                        scaleXtext.setText("Virtual X to real X scale factor (Press X to recalibrate): #");
                    }else{
                        scaleXtext.setText("Calculating Virtual X to real X scale factor (Press X to stop calibration): ");
                    }
                    
                    calibratingX = !calibratingX;
                    
                }
                
                if(name.equals("recalibrateY") && keyPressed){
                    if(calibratingY){
                        scaleYtext.setText("Virtual Y to real Y scale factor (Press Y to recalibrate): #");
                    }else{
                        scaleYtext.setText("Calculating Virtual Y to real Y scale factor (Press Y to stop calibration): ");
                    }
                    
                    calibratingY = !calibratingY;
                    
                }
                
                if(name.equals("readModeChange") && keyPressed){
                    
                    switch(readMode){
                        case 0:
                            readModeText.setText("Probe Output Reading "
                                    + "(Press V to change): "
                                    + "Raw Output Mode");
                            break;
                            
                        case 1:
                            readModeText.setText("Probe Output Reading "
                                    + "(Press V to change): "
                                    + "Low-Pass Filter Mode");
                            break;
                            
                        case 2:
                            readModeText.setText("Probe Output Reading "
                                    + "(Press V to change): "
                                    + "Mean Error as Threshold Mode (Press B to recalculate)");
                            break;
                    }
                    
                    readMode++;
                    readMode = (short) (readMode%3);
                    
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
                "rotateCounterClockwise");

    }
}
