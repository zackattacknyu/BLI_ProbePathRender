/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.modelTesting;

import com.jme3.math.Matrix3f;
import com.jme3.math.Matrix4f;
import com.jme3.math.Vector3f;
import java.util.Set;
import java.util.HashSet;
import org.zrd.geometryToolkit.geometricCalculations.TransformHelper;
import org.zrd.geometryToolkit.geometricCalculations.TranslationHelper;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.meshDataStructure.MeshVertex;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;

/**
 * 
 * THIS IS A WORK IN PROGRESS.
 *      DO NOT USE THIS CODE YET
 *
 * @author BLI
 */
public class ModelFlattening {
    
    //vertices used to define plane to flatten on
    private MeshVertex vertex1;
    private MeshVertex vertex2;
    private MeshVertex vertex3;
    
    private Matrix4f totalTransform;

    public TriangleSet getOutputSet() {
        return outputSet;
    }
    
    int numVertices;
    
    private TriangleSet activeTriSet;
    private TriangleSet outputSet;
    
    private Integer[] vertexIndicesToUse = {4084,1662,2192};
    //private Integer[] vertexIndicesToUse;
    
    public ModelFlattening(TriangleSet triangles){
        activeTriSet = triangles;
        obtainTransform();
        modifyOutputSet();
    }
    
    private void modifyOutputSet(){
        
        outputSet = new TriangleSet();
        
        for(MeshTriangle baseTriangle : activeTriSet.getTriangleList()){
            
            Vector3f vert1 = baseTriangle.getVertex1().getVertex();
            Vector3f vert2 = baseTriangle.getVertex2().getVertex();
            Vector3f vert3 = baseTriangle.getVertex3().getVertex();
            
            Vector3f vertex1Trans = totalTransform.mult(vert1.clone());
            Vector3f vertex2Trans = totalTransform.mult(vert2.clone());
            Vector3f vertex3Trans = totalTransform.mult(vert3.clone());
            
            MeshTriangle newTri = new MeshTriangle(vertex1Trans,vertex2Trans,vertex3Trans);
            newTri.setTextureCoords(baseTriangle.getTextureCoords());
            outputSet.addTriangle(newTri);
            
        }
    }
    
    private int getRandomIndex(){
        return (int) ((Math.random())*numVertices);
    }
    
    private Integer[] getIndices(){
        if(vertexIndicesToUse == null){
            
            //gets 3 random, distinct vertex indices
            Set<Integer> indices = new HashSet<Integer>();
            while(indices.size() < 3){
                indices.add(getRandomIndex());
            }
            Integer[] vertexIndices = new Integer[3];
            vertexIndices = indices.toArray(vertexIndices);
            /*for(Integer index: vertexIndices){
                System.out.println("VERTEX INDEX: " + index);
            }*/
            return vertexIndices;
            
        }else{
            return vertexIndicesToUse;
        }
        
    }
    
    private void obtainTransform(){
        //makes the vertex array
        Set<MeshVertex> vertexSet = activeTriSet.getTrianglesByVert().keySet();
        MeshVertex[] vertices = new MeshVertex[vertexSet.size()];
        vertices = vertexSet.toArray(vertices);
        
        numVertices = vertices.length;
        
        Integer[] vertexIndices = getIndices();
        
        //makes the mesh vertices
        vertex1 = vertices[vertexIndices[0]];
        vertex2 = vertices[vertexIndices[1]];
        vertex3 = vertices[vertexIndices[2]];
        
        /*
        * Translates all the points so that
        * vertex1 is now the origin
        */
       Matrix4f originVertex1 = TranslationHelper.getNewOriginTransform(vertex1.getVertex());
       Vector3f seg12Vector = originVertex1.mult(vertex2.getVertex());
       Vector3f seg13Vector = originVertex1.mult(vertex3.getVertex());
       
       //gets the change of coordinate matrix
       Vector3f newZVector = seg12Vector.clone().cross(seg13Vector.clone());
       Matrix3f coordMatrix = TransformHelper.getCoordinateTransformation(seg12Vector, seg13Vector, newZVector);
       Matrix4f coordMatrixToUse = new Matrix4f();
       coordMatrixToUse.toRotationMatrix(coordMatrix);
       
       //gets the matrix that will flatten the coordinates
       Matrix4f flattener = new Matrix4f(
               1,0,0,0,
               0,1,0,0,
               0,0,0,0,
               0,0,0,1);
       
       //combines them all together
       totalTransform = 
               (originVertex1.clone().invert()).
               mult(coordMatrixToUse.clone().invert()).
               mult(flattener).
               mult(coordMatrixToUse).
               mult(originVertex1);
    }
    
}
