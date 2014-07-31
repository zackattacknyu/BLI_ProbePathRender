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
import org.zrd.geometryToolkit.geometricCalculations.TranslationHelper;
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
    
    public static void createDirectoryIfNone(Path path){
        if(!Files.exists(path)){
            try {
                Files.createDirectory(path);
            } catch (IOException ex) {
                System.out.println("Error creating path: " + ex);
            }
        }
    }

    public static Spatial importMesh(AssetManager assetManager, File importDirectory){
        
        Path assetPath = Paths.get("assets");
        createDirectoryIfNone(assetPath);
        
        Path modelAssets = assetPath.resolve("Models");
        createDirectoryIfNone(modelAssets);
        
        File objFile = FileDataHelper.importPathUsingFileSelector(importDirectory);
        String objFileName = objFile.getName();
        Path newObjFilePath = modelAssets.resolve(objFileName);
        try {
            Files.copy(objFile.toPath(), newObjFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        
        Path textureAssets = assetPath.resolve("Textures");
        createDirectoryIfNone(textureAssets);
        
        File textureFile = FileDataHelper.importPathUsingFileSelector(importDirectory);
        String textureFileName = textureFile.getName();
        Path textureFilePath = textureAssets.resolve(textureFileName);
        try {
            Files.copy(textureFile.toPath(), textureFilePath,StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        
        Material lolaMaterial = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        lolaMaterial.setTexture("ColorMap",assetManager.loadTexture("Textures/" + textureFileName));
        //if(wireframeOn) lolaMaterial.getAdditionalRenderState().setWireframe(true);
        
        Spatial returnMesh = MeshInputHelper.generateModel("Models/" + objFile.getName(), lolaMaterial, assetManager);
        
        float surfaceScale = 80f;
        returnMesh.scale(80f);
        Matrix4f scaleMat = new Matrix4f();
        scaleMat.scale(new Vector3f(surfaceScale,surfaceScale,surfaceScale));
        Matrix4f surfaceTransform = scaleMat;
        
        TriangleSet activeMeshInfo = new TriangleSet();
        activeMeshInfo.setTransform(surfaceTransform);
        activeMeshInfo = MeshInputHelper.addToTriangleSet(activeMeshInfo,returnMesh,surfaceTransform);
        
        float minX = activeMeshInfo.getMinX();
        float maxX = activeMeshInfo.getMaxX();
        float minY = activeMeshInfo.getMinY();
        float maxY = activeMeshInfo.getMaxY();
        float minZ = activeMeshInfo.getMinZ();
        float maxZ = activeMeshInfo.getMaxZ();
        float avgX = (minX+maxX)/2;
        float avgY = (minY+maxY)/2;
        float avgZ = (minZ+maxZ)/2;
        
        Matrix4f translateTransform = TranslationHelper.getNewOriginTransform(new Vector3f(avgX,avgY,avgZ));
        returnMesh.move(-avgX, -avgY, -avgZ);
        
        return returnMesh;
        
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
        surfaceMesh = MeshRenderHelper.createMeshFromTriangles(correctedMesh, lolaMaterial);
        activeMeshInfo = correctedMesh;
        
        ModelVerification.performModelVerification(correctedMesh);
        * */
    }
    
    
    
}
