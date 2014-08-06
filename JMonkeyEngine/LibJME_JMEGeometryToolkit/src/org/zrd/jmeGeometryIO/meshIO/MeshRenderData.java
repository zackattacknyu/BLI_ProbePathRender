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
    
    private Spatial renderedMesh;
    private TriangleSet finalMeshInfo;
    private Vector3f cameraCenter;

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
        return renderedMesh;
    }

    public TriangleSet getFinalMeshInfo() {
        return finalMeshInfo;
    }

    public Vector3f getCameraCenter() {
        return cameraCenter;
    }
    
    public MeshRenderData(Spatial renderedMesh){
        this.renderedMesh = renderedMesh;
        
        modifyMesh();
    }
    
    public void modifyMesh(){

        float surfaceScale = 80f;
        renderedMesh.scale(80f);
        Matrix4f scaleMat = new Matrix4f();
        scaleMat.scale(new Vector3f(surfaceScale,surfaceScale,surfaceScale));
        
        finalMeshInfo = new TriangleSet();
        finalMeshInfo.setTransform(scaleMat);
        finalMeshInfo = MeshInputHelper.addToTriangleSet(finalMeshInfo,renderedMesh,scaleMat);

        Vector3f centerPt = getCenterPoint(finalMeshInfo);
        
        renderedMesh.move(centerPt.clone().negate());
        
        float minZ = finalMeshInfo.getMinZ();
        cameraCenter = new Vector3f(0,0,minZ*1.5f);
        
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
