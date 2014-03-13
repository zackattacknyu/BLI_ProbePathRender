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
    private boolean active = false;
    private boolean playing = false;
    private MotionPath path;
    private MotionEvent motionControl;
    private BitmapText wayPointsText;
    private ArduinoDataPoint currentArdData;
    private Quaternion currentRotation;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        String objFileLocation = "Models/textured_mesh.obj";
        String sampleDataLocation = "textFiles/sampleData.txt";
        
        Material ballMat = new Material(assetManager, 
                "Common/MatDefs/Misc/ShowNormals.j3md");
        rootNode.attachChild(ModelHelper.generateModel(
                objFileLocation, ballMat, assetManager));
        initLittleBox(ballMat);
        rootNode.attachChild(littleObject);

        ArrayList<Vector3f> lineVertices = 
                ProbeDataHelper.getVerticesFromFile(sampleDataLocation);
        path = ProbeDataHelper.getMotionPathFromVertices(lineVertices);
        rootNode.attachChild(LineHelper.createLineFromVertices(lineVertices, ballMat)); 
        
        setDefaultCamera();
        enableFlyCam();
        initPathInputs();
        serialThread.start();
    }
    
    Thread serialThread = new Thread(){
        public void run(){
            executeSerialReading();
        }
    };
    
    public void executeSerialReading(){
        try{
            SerialReader serial = new SerialReader();
            serial.beginExecution();
            String previousOutput = "";
            String currentOutput = "";
            ArduinoDataPoint currentData;
            while(true){
                currentOutput = serial.getCurrentOutput();
                if(!currentOutput.equals(previousOutput)){
                    System.out.println(currentOutput);
                    currentArdData = serial.getCurrentData();
                    if(currentArdData != null){
                        currentRotation = currentArdData.getRotation();
                    }
                }
                previousOutput = currentOutput;
            }
        }catch(Throwable e){
            System.out.println("READING SERIAL DATA FAILED!");
                    
        }
    }
    
    
    
    private void initLittleBox(Material ballMat){
        Box b = new Box(0.5f, 0.5f, 0.5f);
        littleObject = new Geometry("Box", b);
        littleObject.setName("Cube");
        littleObject.setLocalScale(1);
        littleObject.setMaterial(ballMat);
    }

    private float lastXangle = 0;
    private float currentXangle = 0;
    @Override
    public void simpleUpdate(float tpf) {
        /*explained here is how the update loop works
         * http://hub.jmonkeyengine.org/wiki/doku.php/jme3:beginner:hello_main_event_loop
         */
        
        //this rotates the little cube depending on the rotation
        if(currentRotation != null){
            try{
                littleObject.rotate(-1*lastXangle, 0, 0);
                currentXangle = currentArdData.getPitch()/100.0f;
                littleObject.rotate(currentXangle, 0, 0);
                lastXangle = currentXangle;
                //littleObject.rotate(currentRotation);
            }catch(Exception e){
                System.out.println(e);
            }
            
        }
        
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
        
        ActionListener acl = new ActionListener() {

            public void onAction(String name, boolean keyPressed, float tpf) {
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


            }
        };

        inputManager.addListener(acl, "display_hidePath", "play_stop", "SwitchPathInterpolation", "tensionUp", "tensionDown");

    }
}
