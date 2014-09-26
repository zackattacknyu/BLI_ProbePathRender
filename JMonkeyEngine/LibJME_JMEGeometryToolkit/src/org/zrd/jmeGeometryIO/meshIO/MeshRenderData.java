/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryIO.meshIO;

import com.jme3.material.Material;
import com.jme3.math.Matrix4f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import org.zrd.geometryToolkit.meshDataStructure.ConnectedComponent;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.modelTesting.ModelCorrection;
import org.zrd.geometryToolkit.modelTesting.ModelVerification;
import org.zrd.jmeGeometryIO.pathIO.PathRenderHelper;

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
    protected Material meshMaterial;
    protected Vector3f cameraPosition;
    
    private static final float NORMAL_LINE_MAGNITUDE = 0.2f;

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

    public Material getMeshMaterial() {
        return meshMaterial;
    }

    public Vector3f getCameraPosition() {
        return cameraPosition;
    }
    
    public MeshRenderData(){
        
    }
    
    public MeshRenderData(Spatial mesh, Material meshMat, TriangleSet meshInfo){
        renderedMesh = mesh;
        finalMeshInfo = meshInfo;
        meshMaterial = meshMat;
    }
    
    public MeshRenderData(Spatial renderedMesh, Material meshMat){
        this.renderedMesh = renderedMesh;
        this.meshMaterial = meshMat;
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
        
        //do model testing
        ConnectedComponent mainComponent = ModelCorrection.getLargestComponent(finalMeshInfo);
        TriangleSet correctedMesh = mainComponent.getComponentTriangleSet();
        correctedMesh = ModelCorrection.getSmoothedTriangleSet(correctedMesh);
        //ModelCorrection.flattenTrianglesUsingRandomVertices(correctedMesh);
        System.out.println("Corrected Mesh has " + correctedMesh.getTriangleList().size() + " triangles ");
        renderedMesh = MeshRenderHelper.createMeshFromTriangles(correctedMesh, meshMaterial);
        finalMeshInfo = correctedMesh;
        
        ModelVerification.performModelVerification(correctedMesh);
    }
    
    public Node generateNormalLines(Material lineMaterial){
        Node lines = new Node();
        ArrayList<Vector3f> linePoints = new ArrayList<Vector3f>(2);
        Vector3f startPoint;
        Vector3f endPoint;
        Spatial currentLine;        
        for(MeshTriangle tri: finalMeshInfo.getTriangleList()){
            startPoint = tri.getCenter();
            endPoint = startPoint.add(tri.getNormal().clone().mult(NORMAL_LINE_MAGNITUDE));
            linePoints.add(startPoint);
            linePoints.add(endPoint);
            currentLine = PathRenderHelper.createLineFromVertices(linePoints, lineMaterial);
            lines.attachChild(currentLine);
            linePoints.clear();
        }
        return lines;
    }
    
    
    
}
