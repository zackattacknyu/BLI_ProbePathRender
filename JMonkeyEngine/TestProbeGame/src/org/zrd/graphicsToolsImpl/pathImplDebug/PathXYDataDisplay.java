/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.graphicsToolsImpl.pathImplDebug;

import com.jme3.material.Material;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.io.File;
import java.util.ArrayList;
import org.zrd.graphicsTools.geometry.mesh.MeshTriangle;
import org.zrd.graphicsTools.geometry.mesh.TriangleSet;
import org.zrd.graphicsToolsImpl.meshImpl.MeshHelper;
import org.zrd.graphicsToolsImpl.pathImpl.PathDataDisplay;

/**
 *
 * @author BLI
 */
public class PathXYDataDisplay extends PathDataDisplay{

    private ArrayList<Float> xValues,yValues;
    
    private float minX,maxX,minY,maxY;
    
    private float constantZ = 0.0f;
    
    private PathXYDataDisplay(File initDir){
        super(initDir);
    }
    
    public static PathXYDataDisplay obtainXYProbeData(File initDir){
        PathXYDataDisplay data = new PathXYDataDisplay(initDir);
        if(data.isNullReturn()){
            return null;
        }else{
            return data;
        }
    }
    
    protected void generateDisplayValues(){
        
        float currentX = 0.0f;
        float currentY = 0.0f;
        
        minX = -0.5f;
        maxX = 0.5f;
        minY = -0.5f;
        maxY = 0.5f;
        
        displayVertices = new ArrayList<Vector3f>(10000);
        for(int index = 0; index < xValues.size(); index++){
            
            currentX = currentX + (0.00002f)*xValues.get(index);
            currentY = currentY + (0.00002f)*yValues.get(index);
            
            if(currentX < minX){
                minX = currentX;
            }
            if(currentX > maxX){
                maxX = currentX;
            }
            if(currentY < minY){
                minY = currentY;
            }
            if(currentY > maxY){
                maxY = currentY;
            }
            
            displayVertices.add(new Vector3f(currentX, currentY, constantZ));
            
            
            
        }
        
    }


    @Override
    protected void addParts(String[] parts) {
        xValues.add(Float.parseFloat(parts[0]));
        yValues.add(Float.parseFloat(parts[1]));
    }

    @Override
    protected void initializeArrayLists() {
        xValues = new ArrayList<Float>(10000);
        yValues = new ArrayList<Float>(10000);
    }
    
    private void displayMinMax(){
        System.out.println("Min X = " + minX);
        System.out.println("Min Y = " + minY);
        System.out.println("Max X = " + maxX);
        System.out.println("Max Y = " + maxY);
    }

    /**
     * This generates a rectangle with an x in the middle,
     *      so the vertices include the corners of the rectangle
     *      as well as the center point
     * @param mat
     * @return 
     */
    @Override
    public Spatial generateReferenceObject(Material mat) {
        displayMinMax();
        
        float x3 = (minX + maxX)/2.0f; 
        float y3 = (minY + maxY)/2.0f;
        
        Vector3f vert11 = new Vector3f(minX,minY,constantZ);
        Vector3f vert21 = new Vector3f(maxX,minY,constantZ);
        Vector3f vert22 = new Vector3f(maxX,maxY,constantZ);
        Vector3f vert12 = new Vector3f(minX,maxY,constantZ);
        Vector3f vert33 = new Vector3f(x3,y3,constantZ);
        
        MeshTriangle tri1 = new MeshTriangle(new Triangle(vert21,vert33,vert11));
        MeshTriangle tri2 = new MeshTriangle(new Triangle(vert22,vert33,vert21));
        MeshTriangle tri3 = new MeshTriangle(new Triangle(vert12,vert33,vert22));
        MeshTriangle tri4 = new MeshTriangle(new Triangle(vert12,vert11,vert33));
        
        TriangleSet newMesh = new TriangleSet();
        newMesh.addTriangle(tri1);
        newMesh.addTriangle(tri2);
        newMesh.addTriangle(tri3);
        newMesh.addTriangle(tri4);
        
        mat.getAdditionalRenderState().setWireframe(true);
        
        return MeshHelper.createMeshFromTriangles(newMesh, mat);
        
    }
    
    
    
    
    
    
}
