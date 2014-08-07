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
    protected Vector3f meshCenterPoint;

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

    public Vector3f getMeshCenterPoint() {
        return meshCenterPoint;
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
        renderedMesh.scale(80f);
        Matrix4f scaleMat = new Matrix4f();
        scaleMat.scale(new Vector3f(surfaceScale,surfaceScale,surfaceScale));
        
        finalMeshInfo = new TriangleSet();
        finalMeshInfo.setTransform(scaleMat);
        finalMeshInfo = MeshInputHelper.addToTriangleSet(finalMeshInfo,renderedMesh,scaleMat);

        meshCenterPoint = getCenterPoint(finalMeshInfo);
        
        //renderedMesh.move(centerPt.clone().negate());
        
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
