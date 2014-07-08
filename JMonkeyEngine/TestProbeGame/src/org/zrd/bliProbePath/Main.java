package org.zrd.bliProbePath;

import org.zrd.utilImpl.general.CollisionPoint;
import org.zrd.probeTracking.ProbeTracker;
import org.zrd.utilImpl.general.ProgramConstants;
import org.zrd.graphicsTools.geometry.path.ProbePathSet;
import org.zrd.cameraTracker.cameraMoves.CameraTracker;
import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
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
import com.jme3.system.AppSettings;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;
import org.zrd.graphicsTools.geometry.mesh.ConnectedComponent;
import org.zrd.graphicsTools.geometry.meshTraversal.MeshHelper;
import org.zrd.graphicsTools.geometry.mesh.MeshTriangle;
import org.zrd.graphicsTools.geometry.modelVerifier.ModelVerification;
import org.zrd.graphicsTools.geometry.mesh.TriangleSet;
import org.zrd.graphicsTools.geometry.modelVerifier.ModelCorrection;
import org.zrd.graphicsTools.geometry.path.ProbePath;
import org.zrd.graphicsToolsImpl.pathImpl.PathHelper;
import org.zrd.graphicsToolsImpl.pathImplDebug.PathXYDataDisplay;
import org.zrd.graphicsToolsImpl.pathImplDebug.PathYawPitchRollDataDisplay;


/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    private Spatial littleObject,background,surface,moveableObject,xAxisLine,yAxisLine,zAxisLine;
    private Material ballMat,boxMat,probeMat,lineMaterial,xMat,yMat,zMat;
    private Material redLineMaterial,orangeLineMaterial;
    
    private BitmapText yawPitchRollText, xyzText, scaleXtext, scaleYtext, 
            readModeText, recordingText, resetProbeText, probeMoveModeText;
    private boolean onStartPoint = true;
    
    private final boolean sphereWireframeOn=false, lolaWireframeOn=false;
    
    private ProbeTracker probeTracker;
    
    private CameraTracker cameraTracker;
    
    //private Spatial lastLine;
    
    private Properties trackerProps;
    private boolean lightVisible;
    private boolean moveProbe = false;
    private boolean moveableObjectIsProbe = true;
    private boolean moveLine = false;
    private Node shootables,probeRep;
    private File initialImportDirectory;
    private ProbePathSet probePathSet;
    
    private Quaternion surfaceRotation;
    private float surfaceScale;
    private Vector3f surfaceLoc;
    private Matrix4f surfaceTransform, sphereTransform;
    private TriangleSet meshInfo;
    private Vector3f lastPointClicked;
    private Triangle startingTriangle;
    
    //this is if we are using the sphere for testing 
    //      instead of lola
    private boolean sphereOn = true;
    
    //if we want to display the raw data instead of 
    //      the sphere or lola mesh. This overrides
    //      the above setting.
    private boolean displayRawDataMode = false;
    
    //to be used if we are only testing using recorded paths
    private boolean showProbeRepLines = true;
    
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
        
        initialImportDirectory = Paths.get("textFiles").toFile();
        
        cameraTracker = new CameraTrackerImpl_ProbePathRender(cam,flyCam,inputManager);
        if(displayRawDataMode){
            cameraTracker.setDefaultCamera((short)2);
        }else if(sphereOn){
            cameraTracker.setDefaultCamera((short)0);
        }else{
            cameraTracker.setDefaultCamera((short)1);
        }
        
        
        //String objFileLocation = "Models/lola_mesh.obj";
        //String objFileLocation = "Models/lola_mesh_simplified_connected.obj";
        String objFileLocation = "Models/lola_mesh_simplePatch2_simplified.obj";
        String sphereLocation = "Models/sphere2.obj";
        //String sphereLocation = "Models/simpleCube.obj";
        viewPort.setBackgroundColor(ProgramConstants.BACKGROUND_COLOR);
        trackerProps = Properties_BLIProbePath.getProperties();
        lightVisible = Boolean.parseBoolean(
                trackerProps.getProperty("lighting.visible"));
        
        if(lightVisible){
            ballMat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
            ballMat.setTexture("DiffuseMap",assetManager.loadTexture("Textures/lola_texture.png"));
        }else{
            //ballMat = new Material(assetManager,"Common/MatDefs/Misc/ShowNormals.j3md");
            ballMat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
            ballMat.setTexture("ColorMap",assetManager.loadTexture("Textures/lola_texture.png"));
            if(lolaWireframeOn) ballMat.getAdditionalRenderState().setWireframe(true);
        }
        
        boxMat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        boxMat.setTexture("ColorMap",assetManager.loadTexture("Textures/table_texture.jpg"));
        
        xMat = makeColorMaterial(ColorRGBA.Red);
        yMat = makeColorMaterial(ColorRGBA.Orange);
        zMat = makeColorMaterial(ColorRGBA.Green);
        lineMaterial = makeColorMaterial(ColorRGBA.Black);
        redLineMaterial = makeColorMaterial(ColorRGBA.Red);
        orangeLineMaterial = makeColorMaterial(ColorRGBA.Orange);
        
        Material lightedSphere = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
        if(sphereWireframeOn) lightedSphere.getAdditionalRenderState().setWireframe(true);
        if(sphereOn){
            surface = org.zrd.graphicsToolsImpl.meshImpl.MeshHelper.generateModel(sphereLocation, lightedSphere, assetManager);
        }else{
            surface = org.zrd.graphicsToolsImpl.meshImpl.MeshHelper.generateModel(
                objFileLocation, ballMat, assetManager);
        }
        
        
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
        
        if(sphereOn){
            //surface.setLocalTranslation(0, 0, 0);
            surface.scale(20f);
            sphereTransform = new Matrix4f();
            sphereTransform.setScale(20f, 20f, 20f);
        }else{
            surface.setLocalRotation(surfaceRotation);
            surface.scale(surfaceScale);
            surface.move(surfaceLoc);
        }
        
        
        xAxisLine = initXLine(xMat);
        yAxisLine = initYLine(yMat);
        zAxisLine = initZLine(zMat);
        probeTracker = new ProbeTracker(inputManager);
        //displayAxisLines();
        
        probeRep = new Node("probeRep");
        probeRep.attachChild(xAxisLine);
        probeRep.attachChild(yAxisLine);
        probeRep.attachChild(zAxisLine);
        if(!displayRawDataMode && showProbeRepLines){
            rootNode.attachChild(probeRep);
        }
        moveableObject = probeRep;
        
        probeMat = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        probeMat.setTexture("ColorMap", assetManager.loadTexture("Textures/plastic_texture.jpg"));
        
        
        
        /* This is a code block to illustrate
         *      lines with varying color
        probeMat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
        probeMat.setTexture("ColorMap", assetManager.loadTexture("Textures/table_texture.jpg"));
        addLineLights();
        */
        
        //littleObject = initLittleBox(probeMat);
        //rootNode.attachChild(littleObject);
        //littleObject = surface;
        //rootNode.attachChild(initLittleBox(probeMat));
        
        background = initBackgroundBox(boxMat, "background");
        
        if(!sphereOn && !displayRawDataMode){
            rootNode.attachChild(background);
        }
        
        if(sphereOn){
            addSphereLights();
            obtainSphereTriangleData();
        }else{
            obtainSurfaceTriangleData();
        }
        
        ConnectedComponent mainComponent = ModelCorrection.getLargestComponent(meshInfo);
        TriangleSet correctedMesh = mainComponent.getComponentTriangleSet();
        correctedMesh = ModelCorrection.getSmoothedTriangleSet(correctedMesh);
        System.out.println("Corrected Mesh has " + correctedMesh.getTriangleList().size() + " triangles ");
        if(sphereOn){
            surface = org.zrd.graphicsToolsImpl.meshImpl.MeshHelper.createMeshFromTriangles(correctedMesh, lightedSphere);
        }else{
            surface = org.zrd.graphicsToolsImpl.meshImpl.MeshHelper.createMeshFromTriangles(correctedMesh, ballMat);
        }
        meshInfo = correctedMesh;
        //Spatial testSurface = ObjectHelper.createMeshFromTriangles(meshInfo, ballMat);
        //rootNode.attachChild(testSurface);
        
        ModelVerification.performModelVerification(correctedMesh);
        
        
        initKeyboardInputs();
        initDebugText();
        
        shootables = new Node("shootables");
        shootables.attachChild(surface);
        
        if(!displayRawDataMode){
            rootNode.attachChild(shootables);
        }
        probePathSet = new ProbePathSet(lineMaterial);
        
        
        //displayNormals(correctedMesh);
        
        
    }
    
    private Material makeColorMaterial(ColorRGBA color){
        Material returnMat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        returnMat.setColor("Color", color);
        return returnMat;
    }
    
    /*
     * This displays each of the normals for all the triangles.
     * It is meant to be used to visually verify that all the normals
     *      point outside the surface
     */
    private void displayNormals(TriangleSet triangles){
        Node normals = new Node();
        Vector3f startPt;
        Vector3f endPt;
        float scaleFactor = 5.0f;
        Spatial currentNormal;
        ArrayList<Vector3f> currentNormalVerts;
        
        for(MeshTriangle tri: triangles.getTriangleList()){
            startPt = tri.getCenter();
            endPt = startPt.add(tri.getNormal().mult(scaleFactor));
            currentNormalVerts = new ArrayList<Vector3f>(2);
            currentNormalVerts.add(startPt); 
            currentNormalVerts.add(endPt);
            currentNormal = PathHelper.createLineFromVertices(currentNormalVerts, lineMaterial);
            normals.attachChild(currentNormal);
        }
        
        rootNode.attachChild(normals);
    }
    
    private void obtainTriangleData(Matrix4f transform){
        meshInfo = new TriangleSet();
        meshInfo.setTransform(transform);
        if(surface instanceof Node){
            System.out.println("**NOTE: Node Triangle Data generated**");
            Node surfaceNode = (Node)surface;
            for(Spatial child: surfaceNode.getChildren()){
                Geometry surfaceGeom = (Geometry)child;
                meshInfo.addMesh(surfaceGeom.getMesh());
            }
        }else if(surface instanceof Geometry){
            System.out.println("**NOTE: Geometry Triangle Data generated**");
            Geometry surfaceGeom = (Geometry)surface;
            meshInfo.addMesh(surfaceGeom.getMesh());
            meshInfo.setBoundaryTriangles();
        }
    }
    
    private void obtainSurfaceTriangleData(){
        obtainTriangleData(surfaceTransform);
        
    }
    
    private void obtainSphereTriangleData(){
        obtainTriangleData(sphereTransform);
        
    }
    
    
    
    private void addLineLights(){
        PointLight ballLight = new PointLight();
        ballLight.setColor(ColorRGBA.Blue);
        ballLight.setRadius(100f);
        ballLight.setPosition(new Vector3f(-4f,-2.7f,-14f));
        
        PointLight probeLight = new PointLight();
        probeLight.setColor(ColorRGBA.Red);
        probeLight.setRadius(50f);
        probeLight.setPosition(new Vector3f(-0.4f,-2f,-15f));
        
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.Green.mult(2.0f));
        
        rootNode.addLight(al);
        
        rootNode.addLight(ballLight);
        rootNode.addLight(probeLight);
    }
    private void addSphereLights(){
        PointLight ballLight = new PointLight();
        ballLight.setColor(ColorRGBA.Blue);
        ballLight.setRadius(100f);
        ballLight.setPosition(probeRep.getLocalTranslation());
        
        PointLight probeLight = new PointLight();
        probeLight.setColor(ColorRGBA.Red);
        probeLight.setRadius(50f);
        probeLight.setPosition(new Vector3f(-0.4f,-1f,-15.5f));
        
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.Green.mult(2.0f));
        
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
    
    private Spatial initBox(Material boxMat){
        Box b = new Box(0.1f, 0.1f, 0.1f);
        Spatial sampleBox = new Geometry("Box", b);
        sampleBox.setName("locationBox");
        sampleBox.setLocalScale(1);
        sampleBox.setMaterial(boxMat);
        return sampleBox;
    }
    
    private void addBoxAtPoint(Vector3f point){
        Spatial currentBox = initBox(redLineMaterial);
        currentBox.setLocalTranslation(point);
        rootNode.attachChild(currentBox);
    }
    
    private Spatial initXLine(Material ballMat){
        ArrayList<Vector3f> xLineVertices = new ArrayList<Vector3f>();
        xLineVertices.add(new Vector3f(4f,0,0));
        xLineVertices.add(new Vector3f(-4f,0,0));
        return PathHelper.createLineFromVertices(xLineVertices,ballMat);
    }
    
    private Spatial initYLine(Material ballMat){
        ArrayList<Vector3f> yLineVertices = new ArrayList<Vector3f>();
        yLineVertices.add(new Vector3f(0,4f,0));
        yLineVertices.add(new Vector3f(0,-4f,0));
        return PathHelper.createLineFromVertices(yLineVertices,ballMat);
    }
    
    private Spatial initZLine(Material ballMat){
        ArrayList<Vector3f> zLineVertices = new ArrayList<Vector3f>();
        zLineVertices.add(new Vector3f(0,0,-4f));
        zLineVertices.add(new Vector3f(0,0,2f));
        return PathHelper.createLineFromVertices(zLineVertices,ballMat);
    }

    @Override
    public void simpleUpdate(float tpf) {
        /*explained here is how the update loop works
         * http://hub.jmonkeyengine.org/wiki/doku.php/jme3:beginner:hello_main_event_loop
         */
        
        probeTracker.updateValues();
        
        moveableObject.setLocalRotation(probeTracker.getLocalRotation());
        
        moveableObject.setLocalTranslation(probeTracker.getCurrentPosition());
        
        xyzText.setText(probeTracker.getXYZtext());
        yawPitchRollText.setText(probeTracker.getYawPitchRollText());
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
        displayPath(probePathSet.getCurrentPathSpatial());
    }
    
    private void displayPath(Spatial path){
        rootNode.attachChild(path);
    }
    
    private void displayCurrentPaths(){
        ArrayList<ProbePath> paths = probePathSet.getPathsToSave();
        for(ProbePath currentPath: paths){
            displayPath(currentPath.getProbePath());
        }
    }
    
    private void displayAxisLines() {
        xAxisLine = makeLineForVector(probeTracker.getCurrentPosition(),probeTracker.getCurrentXAxis(),xMat);
        yAxisLine = makeLineForVector(probeTracker.getCurrentPosition(),probeTracker.getCurrentYAxis(),yMat);
        zAxisLine = makeLineForVector(probeTracker.getCurrentPosition(),probeTracker.getCurrentNormal(),zMat);
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
        rootNode.attachChild(makeLineForVector(point.getContactPoint(),point.getNormal(),lineMaterial));
    }
    
    private Spatial makeLineForVector(Vector3f contactPoint, Vector3f vector,Material mat){
        ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
        vertices.add(contactPoint.clone().add(vector.mult(-0.2f)));
        vertices.add(contactPoint.clone().add(vector.mult(3)));
        return PathHelper.createLineFromVertices(vertices, mat);
    }

    private void initKeyboardInputs() {
        //ChaseCamera chaser = new ChaseCamera(cam, littleObject);
        //chaser.registerWithInput(inputManager);
        
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        
        
        inputManager.addMapping("startStopNewPath", new KeyTrigger(KeyInput.KEY_N));
        
        inputManager.addMapping("recalibrateX", new KeyTrigger(KeyInput.KEY_X));
        inputManager.addMapping("recalibrateY", new KeyTrigger(KeyInput.KEY_Y));
        
        inputManager.addMapping("resetProbe", new KeyTrigger(KeyInput.KEY_H));
        
        inputManager.addMapping("readModeChange", new KeyTrigger(KeyInput.KEY_V));

        inputManager.addMapping("recalibrateProbe", new KeyTrigger(KeyInput.KEY_B));
        
        inputManager.addMapping("pickControlPoint", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

        inputManager.addMapping("changeMoveableObject", new KeyTrigger(KeyInput.KEY_U));
        
        inputManager.addMapping("changeProbeMoveMode", new KeyTrigger(KeyInput.KEY_J));
        
        inputManager.addMapping("changeLineMoveMode", new KeyTrigger(KeyInput.KEY_L));
        
        inputManager.addMapping("importLine", new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("exportLine", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("deleteLine", new KeyTrigger(KeyInput.KEY_O));
        inputManager.addMapping("selectLine", new KeyTrigger(KeyInput.KEY_P));
        
        inputManager.addMapping("rawXYdisplay", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addMapping("rawYawPitchRollDisplay", new KeyTrigger(KeyInput.KEY_G));
        
   
        
        ActionListener acl = new ActionListener() {

            public void onAction(String name, boolean keyPressed, float tpf) {
                
                if(name.equals("rawXYdisplay") && keyPressed){
                    PathXYDataDisplay probeData = PathXYDataDisplay.obtainXYProbeData(initialImportDirectory);
                    rootNode.attachChild(probeData.generateSpatial(orangeLineMaterial));
                    rootNode.attachChild(probeData.generateReferenceObject(zMat));
                }
                
                if(name.equals("rawYawPitchRollDisplay") && keyPressed){
                    PathYawPitchRollDataDisplay probeData = PathYawPitchRollDataDisplay.obtainYawPitchRollProbeData(initialImportDirectory);
                    rootNode.attachChild(probeData.generateSpatial(redLineMaterial));
                    rootNode.attachChild(probeData.generateReferenceObject(zMat));
                }
                
                
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
                    
                    if(moveLine || moveProbe){
                        CollisionResults results = getCollisionResults();
                        if(results.size() > 0){
                            CollisionPoint point = new CollisionPoint(results.getClosestCollision());
                            System.out.println("Contact Point:" + point.getContactPoint());
                            System.out.println("Contact Triangle: " + point.getTriangleInfo());
                            System.out.println("Contact Normal: " + point.getNormal());
                            //meshInfo.displayEdgeNeighbors(point.getTriangle());
                            //meshInfo.displayVertexNeighbors(point.getTriangle());

                            if(moveLine){
                                
                                if(onStartPoint){
                                    
                                    Vector3f endPoint = point.getContactPoint().clone();
                                    if(!endPoint.equals(lastPointClicked)){
                                        
                                        addBoxAtPoint(endPoint);
                                        
                                        lastPointClicked = endPoint;
                                        System.out.println("---------------Above here is line start data------------");
                                        ArrayList<Vector3f> oldPath = probePathSet.getCurrentPath().getVertices();
                                        startingTriangle = point.getTriangle();
                                        Vector3f startPoint = oldPath.get(0);
                                        Vector3f moveVector = endPoint.subtract(startPoint);
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
                                        
                                        addBoxAtPoint(endPoint);
                                        
                                        lastPointClicked = endPoint;
                                        probePathSet.rotateAndProjectCurrentPath(endPoint, startingTriangle, meshInfo,assetManager);
                                        
                                        displayCurrentPaths();

                                        moveLine = false;
                                        onStartPoint = true;
                                    }
                                    
                                }
                                
                                
                            }else if(moveProbe){
                                addLineForNormal(point);
                                probeTracker.setCurrentPosition(point.getContactPoint());
                                displayAxisLines();
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
                
                if(name.equals("resetProbe") && keyPressed){
                    probeTracker.resetProbe();
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
                "pickControlPoint",
                "startStopNewPath",
                "recalibrateX",
                "recalibrateY",
                "readModeChange",
                "resetProbe",
                "recalibrateProbe",
                "changeProbeMoveMode",
                "changeMoveableObject",
                "changeLineMoveMode",
                "importLine",
                "exportLine",
                "deleteLine",
                "selectLine",
                "rawXYdisplay",
                "rawYawPitchRollDisplay");

    }
}
