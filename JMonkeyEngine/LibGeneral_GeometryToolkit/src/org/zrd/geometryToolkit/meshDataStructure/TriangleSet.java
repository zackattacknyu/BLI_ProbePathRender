/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.meshDataStructure;

import com.jme3.math.Matrix4f;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Zach
 */
public class TriangleSet {
    
    private HashMap<MeshEdge,MeshEdgeTriangles> trianglesByEdge;
    private HashMap<MeshVertex,ArrayList<MeshTriangle>> trianglesByVert;
    private ArrayList<MeshTriangle> triangleList;
    
    private MeshVertex vertexWithMinX;
    private MeshVertex vertexWithMaxX;
    private MeshVertex vertexWithMinY;
    private MeshVertex vertexWithMaxY;
    private MeshVertex vertexWithMinZ;
    private MeshVertex vertexWithMaxZ;
    
    private Matrix4f transform;
    
   public TriangleSet(){
       trianglesByEdge = new HashMap<MeshEdge,MeshEdgeTriangles>(30000);
       trianglesByVert = new HashMap<MeshVertex,ArrayList<MeshTriangle>>(30000);
       triangleList = new ArrayList<MeshTriangle>(30000);
       
       transform = new Matrix4f();
       
       initializeBoundingVertices();
   }

    public void setTransform(Matrix4f transform) {
        this.transform = transform;
    }

    public Matrix4f getTransform() {
        return transform;
    }

    public HashMap<MeshEdge, MeshEdgeTriangles> getTrianglesByEdge() {
        return trianglesByEdge;
    }

    public HashMap<MeshVertex, ArrayList<MeshTriangle>> getTrianglesByVert() {
        return trianglesByVert;
    }
    
    private void initializeBoundingVertices(){
        vertexWithMinX = initMinVertex(); 
        vertexWithMinY = initMinVertex(); 
        vertexWithMinZ = initMinVertex(); 
        
        vertexWithMaxX = initMaxVertex();
        vertexWithMaxY = initMaxVertex();
        vertexWithMaxZ = initMaxVertex();
    }
    
    private MeshVertex initMinVertex(){
        return new MeshVertex(Vector3f.POSITIVE_INFINITY);
    }
    
    private MeshVertex initMaxVertex(){
        return new MeshVertex(Vector3f.NEGATIVE_INFINITY);
    }
   
   public void addTriangle(MeshTriangle newTri){

       MeshEdge edge12, edge23, edge13;
       MeshVertex vertex1,vertex2,vertex3;
           
        edge12 = newTri.getSide12();
        edge13 = newTri.getSide13();
        edge23 = newTri.getSide23();

        vertex1 = newTri.getVertex1();
        vertex2 = newTri.getVertex2();
        vertex3 = newTri.getVertex3();

        addEdgeToTriangleMap(edge12,newTri);
        addEdgeToTriangleMap(edge13,newTri);
        addEdgeToTriangleMap(edge23,newTri);

        addVertexToTriangleMap(vertex1,newTri);
        addVertexToTriangleMap(vertex2,newTri);
        addVertexToTriangleMap(vertex3,newTri);

        triangleList.add(newTri);
   }
   
   public void setBoundaryTriangles(){
       MeshEdgeTriangles currentTriangles;
       for(MeshEdge edge: trianglesByEdge.keySet()){
           currentTriangles = trianglesByEdge.get(edge);
           
           //if boundary triangle, label it as such
           if(currentTriangles.numTriangles() < 2){
               
               //labels triangle 1 as boundary triangle
               currentTriangles.getTriangle1().setBoundaryTriangle(true);
           }
       }
   }

    public ArrayList<MeshTriangle> getTriangleList() {
        return triangleList;
    }
   
   private void addVertexToTriangleMap(MeshVertex vertex, MeshTriangle triangle){
       if(trianglesByVert.containsKey(vertex)){
           ArrayList<MeshTriangle> theTriangles = trianglesByVert.get(vertex);
           theTriangles.add(triangle);
       }else{
           ArrayList<MeshTriangle> theTriangles = new ArrayList<MeshTriangle>();
           theTriangles.add(triangle);
           trianglesByVert.put(vertex, theTriangles);
       }
       checkIfBoundingVertex(vertex);
 
  }
   
   private void checkIfBoundingVertex(MeshVertex vertex){
       vertexWithMinX = getMinVertex(vertexWithMinX,vertex,0);
       vertexWithMinY = getMinVertex(vertexWithMinY,vertex,1);
       vertexWithMinZ = getMinVertex(vertexWithMinZ,vertex,2);
       
       vertexWithMaxX = getMaxVertex(vertexWithMaxX,vertex,0);
       vertexWithMaxY = getMaxVertex(vertexWithMaxY,vertex,1);
       vertexWithMaxZ = getMaxVertex(vertexWithMaxZ,vertex,2);
   }
   
    public float getMinX(){
        return vertexWithMinX.getVertex().get(0);
    }
    public float getMinY(){
        return vertexWithMinY.getVertex().get(1);
    }
    public float getMinZ(){
        return vertexWithMinZ.getVertex().get(2);
    }
    
    public float getMaxX(){
        return vertexWithMaxX.getVertex().get(0);
    }
    public float getMaxY(){
        return vertexWithMaxY.getVertex().get(1);
    }
    public float getMaxZ(){
        return vertexWithMaxZ.getVertex().get(2);
    }

    public MeshVertex getVertexWithMinX() {
        return vertexWithMinX;
    }

    public MeshVertex getVertexWithMaxX() {
        return vertexWithMaxX;
    }

    public MeshVertex getVertexWithMinY() {
        return vertexWithMinY;
    }

    public MeshVertex getVertexWithMaxY() {
        return vertexWithMaxY;
    }

    public MeshVertex getVertexWithMinZ() {
        return vertexWithMinZ;
    }

    public MeshVertex getVertexWithMaxZ() {
        return vertexWithMaxZ;
    }
   
    public Vector3f getNormalAtVertex(MeshVertex vertex){
        ArrayList<MeshTriangle> triangles = trianglesByVert.get(vertex);
        Vector3f normal = new Vector3f();
        for(MeshTriangle triangle: triangles){
            normal.addLocal(triangle.getNormal());
        }
        normal.divideLocal(triangles.size());
        return normal;
    }
    
   private MeshVertex getMinVertex(MeshVertex currentMin, MeshVertex vertex, int coordNum){
       if(vertex.getVertex().get(coordNum) < currentMin.getVertex().get(coordNum)){
           return vertex;
       }else{
           return currentMin;
       }
   }
   private MeshVertex getMaxVertex(MeshVertex currentMax, MeshVertex vertex, int coordNum){
       if(vertex.getVertex().get(coordNum) > currentMax.getVertex().get(coordNum)){
           return vertex;
       }else{
           return currentMax;
       }
   }
   
   private void addEdgeToTriangleMap(MeshEdge edge, MeshTriangle triangle){
       if(trianglesByEdge.containsKey(edge)){
           trianglesByEdge.get(edge).addTriangle(triangle);
       }else{
           MeshEdgeTriangles edgeTriangles = new MeshEdgeTriangles();
           edgeTriangles.addTriangle(triangle);
           trianglesByEdge.put(edge, edgeTriangles);
       }
   }
   
   public MeshTriangle getEdgeNeighbor(MeshEdge edge,MeshTriangle triangle){
       MeshEdgeTriangles tris = trianglesByEdge.get(edge);
       if(tris == null) return null;
       return tris.getOtherTriangle(triangle);
   }
   
   public ArrayList<MeshTriangle> getEdgeNeighbors(MeshTriangle triangle){
       ArrayList<MeshTriangle> result = new ArrayList<MeshTriangle>(3);
       
       MeshTriangle edge12Neighbor = getEdgeNeighbor(triangle.getSide12(),triangle);
       MeshTriangle edge13Neighbor = getEdgeNeighbor(triangle.getSide13(),triangle);
       MeshTriangle edge23Neighbor = getEdgeNeighbor(triangle.getSide23(),triangle);
       
       if(edge12Neighbor != null) result.add(edge12Neighbor);
       if(edge13Neighbor != null) result.add(edge13Neighbor);
       if(edge23Neighbor != null) result.add(edge23Neighbor);
       
       return result;
   }
   
   public ArrayList<MeshTriangle> getVertexNeighbors(MeshVertex vertex, MeshTriangle triangle){
       ArrayList<MeshTriangle> vTriangles = trianglesByVert.get(vertex);
       ArrayList<MeshTriangle> vertexNeighbors = new ArrayList<MeshTriangle>(vTriangles.size()-1);
       for(MeshTriangle neighbor: vTriangles){
           if(!neighbor.equals(triangle)){
               vertexNeighbors.add(neighbor);
           }
       }
       return vertexNeighbors;
   }
    
}

