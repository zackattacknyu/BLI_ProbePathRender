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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.JOptionPane;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.jmeGeometryIO.meshIO.MeshInputHelper;
import org.zrd.util.fileHelper.FileDataHelper;

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
    
    public MeshImport(AssetManager assetManager, File importDirectory){
        importMesh(assetManager,importDirectory);
    }
    
    public static void createDirectoryIfNone(Path path){
        if(!Files.exists(path)){
            try {
                Files.createDirectory(path);
            } catch (IOException ex) {
                System.out.println("Error creating path: " + ex);
            }
        }
    }
    
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
    
    private String importAndCopyFile(File importDirectory, Path targetDirectory){
        File fileToCopy = FileDataHelper.importPathUsingFileSelector(importDirectory);
        String fileName = fileToCopy.getName();
        Path copiedFilePath = targetDirectory.resolve(fileName);
        try {
            Files.copy(fileToCopy.toPath(), copiedFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return fileName;
    }

    private void importMesh(AssetManager assetManager, File importDirectory){
        
        Path assetPath = Paths.get("assets");
        createDirectoryIfNone(assetPath);
        
        Path modelAssets = assetPath.resolve("Models");
        createDirectoryIfNone(modelAssets);
        
        JOptionPane.showMessageDialog(null, "Please choose an OBJ File for the 3D Model");
        String objFileName = importAndCopyFile(importDirectory,modelAssets);
        
        Path textureAssets = assetPath.resolve("Textures");
        createDirectoryIfNone(textureAssets);
        
        JOptionPane.showMessageDialog(null, "Please choose an Image file for the texture");
        String textureFileName = importAndCopyFile(importDirectory,textureAssets);
        
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
