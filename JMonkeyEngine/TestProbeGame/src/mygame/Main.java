package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Triangle;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.LightControl;
import com.jme3.system.AppSettings;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;
import meshTraversal.MeshHelper;
import meshTraversal.TriangleSet;


/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    private Spatial littleObject,background,surface,moveableObject,xAxisBox,yAxisBox,zAxisBox;
    private Material ballMat,boxMat,probeMat,lineMaterial,xMat,yMat,zMat;
    
    private BitmapText yawPitchRollText, xyzText, scaleXtext, scaleYtext, 
            readModeText, recordingText, resetProbeText, probeMoveModeText;
    private boolean onStartPoint = true;
    
    private ProbeTracker probeTracker;
    
    //private Spatial lastLine;
    
    private Properties trackerProps;
    private boolean lightVisible;
    private boolean moveProbe = false;
    private boolean moveableObjectIsProbe = true;
    private boolean moveLine = false;
    private Node shootables,probeRep;
    private File initialImportDirectory;
    private ProbePathSet probePathSet;
    private boolean mousePressedDown = false;
    
    private Quaternion surfaceRotation;
    private float surfaceScale;
    private Vector3f surfaceLoc;
    private Matrix4f surfaceTransform;
    private TriangleSet meshInfo;
    private Vector3f lookAtCenter = Vector3f.ZERO;
    private Vector3f lastPointClicked;
    private Triangle startingTriangle;
    private Vector3f startingNormal;

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
        
        initialImportDirectory = Paths.get("textFiles").toFile();
        
        String objFileLocation = "Models/lola_mesh.obj";
        viewPort.setBackgroundColor(Constants.BACKGROUND_COLOR);
        trackerProps = PropertiesHelper.getProperties();
        lightVisible = Boolean.parseBoolean(
                trackerProps.getProperty("lighting.visible"));
        
        if(lightVisible){
            ballMat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
            ballMat.setTexture("DiffuseMap",assetManager.loadTexture("Textures/lola_texture.png"));
        }else{
            ballMat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
            ballMat.setTexture("ColorMap",assetManager.loadTexture("Textures/lola_texture.png"));
        }
        
        boxMat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        boxMat.setTexture("ColorMap",assetManager.loadTexture("Textures/table_texture.jpg"));
        
        xMat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        xMat.setColor("Color", ColorRGBA.Red);
        yMat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        yMat.setColor("Color", ColorRGBA.Orange);
        zMat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        zMat.setColor("Color", ColorRGBA.Green);
        
        surface = ModelHelper.generateModel(
                objFileLocation, ballMat, assetManager);
        
        Quaternion yaw = new Quaternion();
        yaw.fromAngleAxis(180*FastMath.DEG_TO_RAD, Vector3f.UNIT_Z);
        Quaternion pitch = new Quaternion();
        pitch.fromAngleAxis(-20*FastMath.DEG_TO_RAD, Vector3f.UNIT_X);
        
        surfaceRotation = yaw.mult(pitch);
        surfaceScale = 80f;
        surfaceLoc = new Vector3f(0,22,-53);
        
        Matrix4f surfaceRot = new Matrix4f();
        Matrix4f scaleMat = new Matrix4f();
        Matrix4f moveMatrix = new Matrix4f();
        
        scaleMat.scale(new Vector3f(surfaceScale,surfaceScale,surfaceScale));
        surfaceRotation.toRotationMatrix(surfaceRot);
        moveMatrix.setTranslation(surfaceLoc);
        surfaceTransform = moveMatrix.mult(scaleMat).mult(surfaceRot);
        
        surface.setLocalRotation(surfaceRotation);
        surface.scale(surfaceScale);
        surface.move(surfaceLoc);
        
        xAxisBox = initXLine(xMat);
        yAxisBox = initYLine(yMat);
        zAxisBox = initZLine(zMat);
        
        probeRep = new Node("probeRep");
        probeRep.attachChild(xAxisBox);
        probeRep.attachChild(yAxisBox);
        probeRep.attachChild(zAxisBox);
        //rootNode.attachChild(probeRep);
        moveableObject = probeRep;
        
        probeMat = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        probeMat.setTexture("ColorMap", assetManager.loadTexture("Textures/plastic_texture.jpg"));
        
        lineMaterial = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        lineMaterial.setColor("Color", ColorRGBA.Black);
        
        //littleObject = initLittleBox(probeMat);
        //rootNode.attachChild(littleObject);
        //littleObject = surface;
        //rootNode.attachChild(initLittleBox(probeMat));

        if(lightVisible) addLighting();
        
        background = initBackgroundBox(boxMat, "background");
        
        rootNode.attachChild(background);
        
        setDefaultCamera();
        enableFlyCam();
        initKeyboardInputs();
        
        probeTracker = new ProbeTracker();
        
        initDebugText();
        
        shootables = new Node("shootables");
        shootables.attachChild(surface);
        
        rootNode.attachChild(shootables);
        probePathSet = new ProbePathSet(lineMaterial);
        
        displaySurfaceTriangles();
        
    }
    
    private void displaySurfaceTriangles(){
        
        Node surfaceNode = (Node)surface;
        meshInfo = new TriangleSet();
        meshInfo.setTransform(surfaceTransform);
        for(Spatial child: surfaceNode.getChildren()){
            Geometry surfaceGeom = (Geometry)child;
            meshInfo.addMesh(surfaceGeom.getMesh());
        }
        
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
    
    private Spatial initBackgroundBox(Material ballMat, String name){
        Box b = new Box(30f, 30f, 2f);
        Spatial sampleBox = new Geometry("Background", b);
        sampleBox.setCullHint(Spatial.CullHint.Never);
        sampleBox.setName(name);
        sampleBox.setLocalScale(1);
        sampleBox.setMaterial(ballMat);
        sampleBox.setLocalTranslation(10.0f, -10.0f, 0.0f);
        return sampleBox;
    }
    
    private Spatial initXLine(Material ballMat){
        ArrayList<Vector3f> xLineVertices = new ArrayList<Vector3f>();
        xLineVertices.add(new Vector3f(0,0,4f));
        xLineVertices.add(new Vector3f(0,0,-4f));
        return LineHelper.createLineFromVertices(xLineVertices,ballMat);
    }
    
    private Spatial initYLine(Material ballMat){
        Box b = new Box(0.2f, 3f, 0.2f);
        ArrayList<Vector3f> yLineVertices = new ArrayList<Vector3f>();
        yLineVertices.add(new Vector3f(0,4f,0));
        yLineVertices.add(new Vector3f(0,-4f,0));
        return LineHelper.createLineFromVertices(yLineVertices,ballMat);
    }
    
    private Spatial initZLine(Material ballMat){
        ArrayList<Vector3f> zLineVertices = new ArrayList<Vector3f>();
        zLineVertices.add(new Vector3f(-4f,0,0));
        zLineVertices.add(new Vector3f(2f,0,0));
        return LineHelper.createLineFromVertices(zLineVertices,ballMat);
    }

    @Override
    public void simpleUpdate(float tpf) {
        /*explained here is how the update loop works
         * http://hub.jmonkeyengine.org/wiki/doku.php/jme3:beginner:hello_main_event_loop
         */
        
        probeTracker.updateValues();
        
        moveableObject.setLocalRotation(probeTracker.getDisplayRotation());
        
        moveableObject.setLocalTranslation(probeTracker.getLocalTranslation());
        //zAxisBox.move(zAxisBoxInitLocation);
        
        xyzText.setText(probeTracker.getXYZtext());
        yawPitchRollText.setText(probeTracker.getYawPitchRollText());
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
   
    private void setDefaultCamera(){
        cam.setLocation(new Vector3f(-16.928802f, 23.251862f, -54.489124f));
        cam.setRotation(new Quaternion(0.20308718f, 0.20007013f, -0.042432234f, 0.9575631f));
    }
    private void enableFlyCam(){
        flyCam.setEnabled(true);
        flyCam.setDragToRotate(true);
        flyCam.setMoveSpeed(10f);
        flyCam.setRotationSpeed(0f);
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
    
    private void displayReadMode(){
        
        readModeText.setText(probeTracker.getReadModeText());
                
    }
    
    private void displayCurrentPath(){
        rootNode.attachChild(probePathSet.getCurrentPathSpatial());
    }
    
    private CollisionResults getCollisionResults(){
        Vector2f click2d = inputManager.getCursorPosition();
        System.out.println("Mouse Point:" + click2d);

        CollisionResults results = new CollisionResults();
        Vector3f click3d = cam.getWorldCoordinates(
            new Vector2f(click2d.x, click2d.y), 0f).clone();
        Vector3f dir = cam.getWorldCoordinates(
            new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
        Ray ray = new Ray(click3d, dir);
        shootables.collideWith(ray, results);
        return results;
    }
    
    private void addLineForNormal(CollisionPoint point){
        ArrayList<Vector3f> normalVertices = new ArrayList<Vector3f>();
        normalVertices.add(point.getContactPoint());
        normalVertices.add(point.getContactPoint().add(point.getNormal().mult(3)));
        Spatial controlPointNormal = 
                LineHelper.createLineFromVertices(
                normalVertices, lineMaterial);
        rootNode.attachChild(controlPointNormal);
    }
    
    private void moveCamera(boolean inward){
        float moveAmount = 1.0f/5.0f;
        if(inward){
            moveAmount = -1*moveAmount;
        }
        Vector3f currentLoc = cam.getLocation();
        Vector3f direction = currentLoc.subtract(lookAtCenter);
        direction.normalizeLocal();
        Vector3f moveVector = direction.mult(moveAmount);
        cam.setLocation(currentLoc.add(moveVector));
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
        
        inputManager.addMapping("pickControlPoint", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        
        inputManager.addMapping("rotCameraLeft", new MouseAxisTrigger(MouseInput.AXIS_X,false));
        inputManager.addMapping("rotCameraRight", new MouseAxisTrigger(MouseInput.AXIS_X,true));
        inputManager.addMapping("rotCameraUp", new MouseAxisTrigger(MouseInput.AXIS_Y,false));
        inputManager.addMapping("rotCameraDown", new MouseAxisTrigger(MouseInput.AXIS_Y,true));

        inputManager.addMapping("changeMoveableObject", new KeyTrigger(KeyInput.KEY_U));
        
        inputManager.addMapping("changeProbeMoveMode", new KeyTrigger(KeyInput.KEY_J));
        
        inputManager.addMapping("changeLineMoveMode", new KeyTrigger(KeyInput.KEY_L));
        
        inputManager.addMapping("importLine", new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("exportLine", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("deleteLine", new KeyTrigger(KeyInput.KEY_O));
        inputManager.addMapping("selectLine", new KeyTrigger(KeyInput.KEY_P));
        
        final Matrix3f rotLeftMatrix = TrackingHelper.getRotationMatrix(-1.0f/20.0f, Vector3f.UNIT_Y);
        final Matrix3f rotRightMatrix = TrackingHelper.getRotationMatrix(1.0f/20.0f, Vector3f.UNIT_Y);
        final Matrix3f rotUpMatrix = TrackingHelper.getRotationMatrix(-1.0f/20.0f, Vector3f.UNIT_X);
        final Matrix3f rotDownMatrix = TrackingHelper.getRotationMatrix(1.0f/20.0f, Vector3f.UNIT_X);
        
        AnalogListener anl = new AnalogListener(){

            public void onAnalog(String name, float value, float tpf) {
                if(name.equals("rotCameraLeft") && mousePressedDown){
                    cam.setLocation(rotLeftMatrix.mult(cam.getLocation()));
                    cam.lookAt(lookAtCenter, cam.getUp());
                }
                if(name.equals("rotCameraRight") && mousePressedDown){
                    cam.setLocation(rotRightMatrix.mult(cam.getLocation()));
                    cam.lookAt(lookAtCenter, cam.getUp());
                }
                if(name.equals("rotCameraUp") && mousePressedDown){
                    cam.setLocation(rotUpMatrix.mult(cam.getLocation()));
                    cam.lookAt(lookAtCenter, cam.getUp());
                }
                if(name.equals("rotCameraDown") && mousePressedDown){
                    cam.setLocation(rotDownMatrix.mult(cam.getLocation()));
                    cam.lookAt(lookAtCenter, cam.getUp());
                }
            }
        };        
        
        ActionListener acl = new ActionListener() {

            public void onAction(String name, boolean keyPressed, float tpf) {
                
                if(name.equals("importLine") && keyPressed){
                    boolean chosen = probePathSet.importPathUsingFileSelector(initialImportDirectory);
                    if(chosen) displayCurrentPath();
                }
                
                if(name.equals("exportLine") && keyPressed){
                    System.out.println("Export Line Chosen");
                }
                
                if(name.equals("deleteLine") && keyPressed){
                    System.out.println("Delete Line Chosen");
                }
                
                if(name.equals("selectLine") && keyPressed){
                    System.out.println("Select Line Chosen");
                }
                
                if(name.equals("changeMoveableObject") && keyPressed){
                    moveableObjectIsProbe = !moveableObjectIsProbe;
                    if(moveableObjectIsProbe){
                        moveableObject = probeRep;
                    }else{
                        //moveableObject = currentLineNode;
                        //moveableObject = currentLine;
                    }
                }
                
                if(name.equals("changeProbeMoveMode") && keyPressed){
                    moveProbe = !moveProbe;
                    if(moveProbe){
                        probeMoveModeText.setText("Press J to Disable Clicking Probe Movement");
                    }else{
                        probeMoveModeText.setText("Press J to Enable Clicking Probe Movement");
                    }
                }
                
                
                
                if(name.equals("pickControlPoint")){
                    
                    //IMPORTANT: 
                    // used when I want to register dragging above
                    mousePressedDown = !mousePressedDown;
                    
                    if(moveLine || moveProbe){
                        CollisionResults results = getCollisionResults();
                        if(results.size() == 1){
                            CollisionPoint point = new CollisionPoint(results.getCollision(0));
                            System.out.println("Contact Point:" + point.getContactPoint());
                            System.out.println("Contact Triangle: " + point.getTriangleInfo());
                            System.out.println("Contact Normal: " + point.getNormal());
                            meshInfo.displayEdgeNeighbors(point.getTriangle());
                            //meshInfo.displayVertexNeighbors(point.getTriangle());

                            if(moveLine){
                                
                                if(onStartPoint){
                                    
                                    Vector3f endPoint = point.getContactPoint().clone();
                                    if(!endPoint.equals(lastPointClicked)){
                                        lastPointClicked = endPoint;
                                        System.out.println("---------------Above here is line start data------------");
                                        ArrayList<Vector3f> oldPath = probePathSet.getCurrentPath().getVertices();
                                        startingTriangle = point.getTriangle();
                                        Vector3f startPoint = oldPath.get(0);
                                        Vector3f moveVector = endPoint.subtract(startPoint);
                                        startingNormal = point.getNormal();
                                        Matrix4f moveTransform = new Matrix4f();
                                        moveTransform.setTranslation(moveVector);
                                        ArrayList<Vector3f> newPath = MeshHelper.getTransformedVertices(oldPath, moveTransform);
                                        probePathSet.addPath(newPath);
                                        displayCurrentPath();
                                        onStartPoint = false;
                                    }

                                }else{
                                    
                                    Vector3f endPoint = point.getContactPoint().clone();
                                    if(!endPoint.equals(lastPointClicked)){
                                        lastPointClicked = endPoint;
                                        probePathSet.transformCurrentPathEndpoint(endPoint);
                                        //displayCurrentPath();

                                        ArrayList<Vector3f> oldPath = probePathSet.getCurrentPath().getVertices();
                                        ArrayList<Vector3f> newPath = meshInfo.makePathFollowMesh(oldPath,startingTriangle,startingNormal);
                                        probePathSet.addPath(newPath);
                                        displayCurrentPath();

                                        moveLine = false;
                                        onStartPoint = true;
                                    }
                                    
                                }
                                
                                
                            }else if(moveProbe){
                                //addLineForNormal(point);
                                probeTracker.setBaselineRotation(point.getRotation());
                                probeTracker.setCurrentPosition(point.getContactPoint());
                            }
                        }
                    }
                }
                
                if(name.equals("changeLineMoveMode") && keyPressed){
                    if(!moveLine){
                        System.out.println("Last Line will be moved "
                            + "to next 2 points clicked");
                        moveLine = true;
                    }else{
                        System.out.println("Line Moving Cancelled");
                        moveLine = false;
                    }
                }
                
                if(name.equals("startStopNewPath") && keyPressed){
                     
                     probeTracker.updatePathRecording();
                     recordingText.setText(probeTracker.getRecordingText());
                     if(probeTracker.isNewPathExists()){
                         probePathSet.addPath(probeTracker.getCurrentPathVertices());
                         displayCurrentPath();
                     }
                     
                 }
 
                if(name.equals("moveInward")){
                    moveCamera(true);
                }
                if(name.equals("moveOutward")){
                    moveCamera(false);
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
        
        inputManager.addListener(anl, 
                "rotCameraLeft",
                "rotCameraRight",
                "rotCameraUp",
                "rotCameraDown");
        inputManager.addListener(acl,
                "rotCameraLeft",
                "rotCameraRight",
                "rotCameraUp",
                "rotCameraDown",
                "pickControlPoint",
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
                "rollForward",
                "changeProbeMoveMode",
                "changeMoveableObject",
                "changeLineMoveMode",
                "importLine",
                "exportLine",
                "deleteLine",
                "selectLine");

    }
}
