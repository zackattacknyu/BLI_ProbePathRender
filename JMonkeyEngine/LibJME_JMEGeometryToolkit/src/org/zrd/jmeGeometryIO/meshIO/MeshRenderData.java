/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryIO.meshIO;

import com.jme3.math.Matrix4f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;

/**
 * 
 * This generates and transforms the Lola mesh for use
 *      by JME applications
 *
 * @author Zach
 */
public class MeshRenderData{
    
    protected Spatial renderedMesh;
    protected TriangleSet finalMeshInfo;
    protected Vector3f cameraPosition;

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

    public Spatial getSurfaceMesh() {
        return renderedMesh;
    }

    public TriangleSet getActiveMeshInfo() {
        return finalMeshInfo;
    }

    public Vector3f getCameraPosition() {
        return cameraPosition;
    }
    
    public MeshRenderData(){
        
    }
    
    public MeshRenderData(Spatial mesh, TriangleSet meshInfo){
        renderedMesh = mesh;
        finalMeshInfo = meshInfo;
    }
    
    public MeshRenderData(Spatial renderedMesh){
        this.renderedMesh = renderedMesh;
        
        modifyMesh();
    }
    
    public void modifyMesh(){
        
        float surfaceScale = 80f;
        
        Matrix4f scaleMat = new Matrix4f();
        scaleMat.scale(new Vector3f(surfaceScale,surfaceScale,surfaceScale));
        renderedMesh.scale(surfaceScale);
        
        TriangleSet initMeshInfo = new TriangleSet();
        initMeshInfo = MeshInputHelper.addToTriangleSet(initMeshInfo, renderedMesh, scaleMat);
        Vector3f meshCenter = getCenterPoint(initMeshInfo);
        
        Vector3f meshTranslationVector = meshCenter.clone().negate();
        
        Matrix4f translationMatrix = new Matrix4f();
        translationMatrix.setTranslation(meshTranslationVector);
        renderedMesh.move(meshTranslationVector);

        
        
        Matrix4f finalTransform = translationMatrix.mult(scaleMat);
        finalMeshInfo = new TriangleSet();
        finalMeshInfo.setTransform(finalTransform);
        finalMeshInfo = MeshInputHelper.addToTriangleSet(finalMeshInfo,renderedMesh,finalTransform);
        
        float minZ = finalMeshInfo.getMinZ();
        cameraPosition = new Vector3f(0,0,minZ*1.5f);
        
        /*
         * 
        
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
