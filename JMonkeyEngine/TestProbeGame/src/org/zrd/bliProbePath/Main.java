package org.zrd.bliProbePath;

import org.zrd.utilImpl.general.CollisionPoint;
import org.zrd.geometryToolkit.geometryUtil.ProgramConstants;
import org.zrd.cameraTracker.cameraMoves.CameraTracker;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
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
import org.zrd.geometryToolkit.meshDataStructure.ConnectedComponent;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.modelTesting.ModelVerification;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.modelTesting.ModelCorrection;
import org.zrd.geometryToolkit.pathDataStructure.RecordedPathSet;
import org.zrd.graphicsToolsImpl.meshImpl.MeshHelper;
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
    
    private Spatial background,surface,moveableObject,xAxisLine,yAxisLine,zAxisLine;
    private Material lolaMaterial,backgroundBoxMaterial,probeMat,lineMaterial,xMat,yMat,zMat;
    private Material redLineMaterial,orangeLineMaterial;
    
    private BitmapText yawPitchRollText, xyzText, recordingText, resetProbeText, probeMoveModeText;
    
    private final boolean sphereWireframeOn=false, lolaWireframeOn=false;
    
    private ProbeTracker probeTracker;
    
    private CameraTracker cameraTracker;
    
    //private Spatial lastLine;
    
    private Properties trackerProps;
    private boolean lightVisible;
    private Node shootables,probeRep;
    private File initialImportDirectory;
    private RecordedPathSet recordedPathSet;
    
    private Quaternion surfaceRotation;
    private float surfaceScale;
    private Vector3f surfaceLoc;
    private Matrix4f surfaceTransform, sphereTransform;
    private TriangleSet meshInfo;
    
    //this is if we are using the sphere for testing 
    //      instead of lola
    private boolean sphereOn = true;
    
    //if we want to display the raw data instead of 
    //      the sphere or lola mesh. This overrides
    //      the above setting.
    private boolean displayRawDataMode = false;
    
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
        
        
        //String objFileLocation = "Models/lola_mesh.obj";
        //String objFileLocation = "Models/lola_mesh_simplified_connected.obj";
        String objFileLocation = "Models/lola_mesh_simplePatch2_simplified.obj";
        String sphereLocation = "Models/sphere2.obj";
        //String sphereLocation = "Models/simpleCube.obj";
        
        trackerProps = Properties_BLIProbePath.getProperties();
        
        lolaMaterial = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        lolaMaterial.setTexture("ColorMap",assetManager.loadTexture("Textures/lola_texture.png"));
        if(lolaWireframeOn) lolaMaterial.getAdditionalRenderState().setWireframe(true);
        
        backgroundBoxMaterial = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        backgroundBoxMaterial.setTexture("ColorMap",assetManager.loadTexture("Textures/table_texture.jpg"));
        
        xMat = MaterialHelper.makeColorMaterial(assetManager,ColorRGBA.Red);
        yMat = MaterialHelper.makeColorMaterial(assetManager,ColorRGBA.Orange);
        zMat = MaterialHelper.makeColorMaterial(assetManager,ColorRGBA.Green);
        lineMaterial = MaterialHelper.makeColorMaterial(assetManager,ColorRGBA.Black);
        redLineMaterial = MaterialHelper.makeColorMaterial(assetManager,ColorRGBA.Red);
        orangeLineMaterial = MaterialHelper.makeColorMaterial(assetManager,ColorRGBA.Orange);
        
        Material lightedSphere = backgroundBoxMaterial.clone();
        if(sphereWireframeOn) lightedSphere.getAdditionalRenderState().setWireframe(true);
        if(sphereOn){
            surface = org.zrd.graphicsToolsImpl.meshImpl.MeshHelper.generateModel(sphereLocation, lightedSphere, assetManager);
        }else{
            surface = org.zrd.graphicsToolsImpl.meshImpl.MeshHelper.generateModel(
                objFileLocation, lolaMaterial, assetManager);
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
        probeTracker = ProbeTracker_BLIProbePath.createNewProbeTracker(inputManager);
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
        
        background = initBackgroundBox(backgroundBoxMaterial, "background");
        
        if(!sphereOn && !displayRawDataMode){
            rootNode.attachChild(background);
        }
        
        if(sphereOn){
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
            surface = org.zrd.graphicsToolsImpl.meshImpl.MeshHelper.createMeshFromTriangles(correctedMesh, lolaMaterial);
        }
        meshInfo = correctedMesh;
        
        ModelVerification.performModelVerification(correctedMesh);
        
        
        initKeyboardInputs();
        initDebugText();
        
        shootables = new Node("shootables");
        shootables.attachChild(surface);
        
        if(!displayRawDataMode){
            rootNode.attachChild(shootables);
        }
        recordedPathSet = new RecordedPathSet();
        
        //RotationTesting.doRotationTesting();
        
        //displayNormals(correctedMesh);
        
        lineMoveAction = new LineMoveAction(inputManager, cam, shootables, recordedPathSet, meshInfo);
        probeMoveAction = new ProbeMoveAction(inputManager,cam,shootables,probeTracker);
        pathImport = new PathImport(inputManager,recordedPathSet,initialImportDirectory);
        probeRecording = new ProbeTrackerRecording(inputManager,recordedPathSet,probeTracker);
        ResetTracker resetTracker = new ResetTracker(inputManager,probeTracker);
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
            currentNormal = PathRenderHelper.createLineFromVertices(currentNormalVerts, lineMaterial);
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
                meshInfo = MeshHelper.addMeshToTriangleSet(
                        surfaceGeom.getMesh(),transform,meshInfo);
            }
        }else if(surface instanceof Geometry){
            System.out.println("**NOTE: Geometry Triangle Data generated**");
            Geometry surfaceGeom = (Geometry)surface;
            meshInfo = MeshHelper.addMeshToTriangleSet(
                    surfaceGeom.getMesh(),transform,meshInfo);
            meshInfo.setBoundaryTriangles();
        }
    }
    
    private void obtainSurfaceTriangleData(){
        obtainTriangleData(surfaceTransform);
        
    }
    
    private void obtainSphereTriangleData(){
        obtainTriangleData(sphereTransform);
        
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
        return PathRenderHelper.createLineFromVertices(xLineVertices,ballMat);
    }
    
    private Spatial initYLine(Material ballMat){
        ArrayList<Vector3f> yLineVertices = new ArrayList<Vector3f>();
        yLineVertices.add(new Vector3f(0,4f,0));
        yLineVertices.add(new Vector3f(0,-4f,0));
        return PathRenderHelper.createLineFromVertices(yLineVertices,ballMat);
    }
    
    private Spatial initZLine(Material ballMat){
        ArrayList<Vector3f> zLineVertices = new ArrayList<Vector3f>();
        zLineVertices.add(new Vector3f(0,0,-4f));
        zLineVertices.add(new Vector3f(0,0,2f));
        return PathRenderHelper.createLineFromVertices(zLineVertices,ballMat);
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
                    rootNode.attachChild(probeData.generateSpatial(orangeLineMaterial));
                    rootNode.attachChild(probeData.generateReferenceObject(zMat));
                }
                
                if(name.equals("rawYawPitchRollDisplay") && keyPressed){
                    PathYawPitchRollDataDisplay probeData = PathYawPitchRollDataDisplay.obtainYawPitchRollProbeData(initialImportDirectory);
                    rootNode.attachChild(probeData.generateSpatial(redLineMaterial));
                    rootNode.attachChild(probeData.generateReferenceObject(zMat));
                }
            }

            
        };

        inputManager.addListener(acl,
                "rawXYdisplay",
                "rawYawPitchRollDisplay");

    }
}
