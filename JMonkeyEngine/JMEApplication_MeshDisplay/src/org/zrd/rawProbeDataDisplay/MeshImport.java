/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.rawProbeDataDisplay;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Matrix4f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JOptionPane;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.jmeGeometryIO.meshIO.MeshInputHelper;
import org.zrd.util.fileHelper.GeneralFileHelper;

/**
 * 
 * This generates and transforms the Lola mesh for use
 *      by JME applications
 *
 * @author Zach
 */
public class MeshImport{
    
    private Spatial finalMesh;
    private TriangleSet finalMeshInfo;
    private Vector3f cameraCenter;
    private String objFileName;
    private String textureFileName;

    public static Vector3f getCenterPoint(TriangleSet triSet){
        
        float minX = triSet.getMinX();
        float maxX = triSet.getMaxX();
        float minY = triSet.getMinY();
        float maxY = triSet.getMaxY();
        float minZ = triSet.getMinZ();
        float maxZ = triSet.getMaxZ();
        float avgX = (minX+maxX)/2;
        float avgY = (minY+maxY)/2;
        float avgZ = (minZ+maxZ)/2;
        
        return new Vector3f(avgX, avgY, avgZ);
        
    }

    public Spatial getFinalMesh() {
        return finalMesh;
    }

    public TriangleSet getFinalMeshInfo() {
        return finalMeshInfo;
    }

    public Vector3f getCameraCenter() {
        return cameraCenter;
    }
    

    
    public void obtainFiles(File initImportDirectory){
        Path assetPath = Paths.get("assets");
        GeneralFileHelper.createDirectoryIfNone(assetPath);
        
        Path modelAssets = assetPath.resolve("Models");
        GeneralFileHelper.createDirectoryIfNone(modelAssets);
        
        JOptionPane.showMessageDialog(null, "Please choose an OBJ File for the 3D Model");
        objFileName = GeneralFileHelper.importAndCopyFile(initImportDirectory,modelAssets);
        
        Path textureAssets = assetPath.resolve("Textures");
        GeneralFileHelper.createDirectoryIfNone(textureAssets);
        
        JOptionPane.showMessageDialog(null, "Please choose an Image file for the texture");
        textureFileName = GeneralFileHelper.importAndCopyFile(initImportDirectory,textureAssets);
    }

    /**
     * TODO:
     * This needs to be improved as when it imports a completely 
     *      new mesh, an AssetNotFoundException gets thrown
     * This seems to be because the assetManager loads all the assets
     *      upon the application start
     * Two solutions could be done:
     *      1) Have the asset files loaded separately 
     *          and then restart the application
     *      2) Figure out how to get the asset manager to rebuild all the file keys
     * 
     * This is what I will try right now:
     *      1) Run a barebones application
     *      2) Have the import file code run and copy the files
     *          into the assets directory
     *      3) Destroy the simple application from 1
     *      4) Start the real application
     * 
     * @param assetManager
     * @param importDirectory 
     */
    public void importMeshAndTextureChosen(AssetManager assetManager){

        Material objectMaterial = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        objectMaterial.setTexture("ColorMap",assetManager.loadTexture("Textures/" + textureFileName));
        //if(wireframeOn) objectMaterial.getAdditionalRenderState().setWireframe(true);
        
        finalMesh = MeshInputHelper.generateModel("Models/" + objFileName, objectMaterial, assetManager);
        
        float surfaceScale = 80f;
        finalMesh.scale(80f);
        Matrix4f scaleMat = new Matrix4f();
        scaleMat.scale(new Vector3f(surfaceScale,surfaceScale,surfaceScale));
        
        finalMeshInfo = new TriangleSet();
        finalMeshInfo.setTransform(scaleMat);
        finalMeshInfo = MeshInputHelper.addToTriangleSet(finalMeshInfo,finalMesh,scaleMat);

        Vector3f centerPt = getCenterPoint(finalMeshInfo);
        
        finalMesh.move(centerPt.clone().negate());
        
        float minZ = finalMeshInfo.getMinZ();
        cameraCenter = new Vector3f(0,0,minZ*1.5f);
        
        /*
         * 
        Quaternion yaw = new Quaternion();
        yaw.fromAngleAxis(180*FastMath.DEG_TO_RAD, Vector3f.UNIT_Z);
        Quaternion pitch = new Quaternion();
        pitch.fromAngleAxis(-20*FastMath.DEG_TO_RAD, Vector3f.UNIT_X);
        
        Quaternion surfaceRotation = yaw.mult(pitch);
        float surfaceScale = 80f;
        Vector3f surfaceLoc = new Vector3f(0,22,-53);
        
        Matrix4f surfaceRot = new Matrix4f();
        Matrix4f scaleMat = new Matrix4f();
        Matrix4f moveMatrix = new Matrix4f();
        
        scaleMat.scale(new Vector3f(surfaceScale,surfaceScale,surfaceScale));
        surfaceRotation.toRotationMatrix(surfaceRot);
        moveMatrix.setTranslation(surfaceLoc);
        Matrix4f surfaceTransform = moveMatrix.mult(scaleMat).mult(surfaceRot);
        
        surfaceMesh.setLocalRotation(surfaceRotation);
        surfaceMesh.scale(surfaceScale);
        surfaceMesh.move(surfaceLoc);
        
        activeMeshInfo = new TriangleSet();
        activeMeshInfo.setTransform(surfaceTransform);
        activeMeshInfo = MeshInputHelper.addToTriangleSet(activeMeshInfo,surfaceMesh,surfaceTransform);
        
        ConnectedComponent mainComponent = ModelCorrection.getLargestComponent(activeMeshInfo);
        TriangleSet correctedMesh = mainComponent.getComponentTriangleSet();
        correctedMesh = ModelCorrection.getSmoothedTriangleSet(correctedMesh);
        System.out.println("Corrected Mesh has " + correctedMesh.getTriangleList().size() + " triangles ");
        surfaceMesh = MeshRenderHelper.createMeshFromTriangles(correctedMesh, objectMaterial);
        activeMeshInfo = correctedMesh;
        
        ModelVerification.performModelVerification(correctedMesh);
        * */
    }
    
    
    
}
