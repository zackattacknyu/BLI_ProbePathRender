/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meshTraversal;

import com.jme3.math.Triangle;
import com.jme3.scene.Mesh;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Zach
 */
public class TriangleSet {
    
    private HashMap<MeshEdge,MeshEdgeTriangles> trianglesByEdge;
    private HashMap<MeshVertex,ArrayList<MeshTriangle>> trianglesByVert;
    
   public TriangleSet(){
       trianglesByEdge = new HashMap<MeshEdge,MeshEdgeTriangles>(30000);
       trianglesByVert = new HashMap<MeshVertex,ArrayList<MeshTriangle>>(30000);
   }
   
   public void addMesh(Mesh mesh){
       Triangle currentTri;
       MeshTriangle newTri;
       MeshEdge edge12, edge23, edge13;
       MeshVertex vertex1,vertex2,vertex3;
       
       //put all triangles into a hash map
       for(int index = 0; index < mesh.getTriangleCount(); index++){
           currentTri = new Triangle();
           mesh.getTriangle(index, currentTri);
           newTri = new MeshTriangle(currentTri);
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
       }
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
   
   public void displayVertexNeighbors(Triangle triangle){
       MeshTriangle currentTri = new MeshTriangle(triangle);
       
       displayVertexTris(1,currentTri.getVertex1(),currentTri);
       displayVertexTris(2,currentTri.getVertex2(),currentTri);
       displayVertexTris(3,currentTri.getVertex3(),currentTri);
   }
   
   private void displayVertexTris(int vertexNum,MeshVertex vertex,MeshTriangle currentTri){
       ArrayList<MeshTriangle> vTriangles = trianglesByVert.get(vertex);
       System.out.println("Vertex " + vertexNum + ": " + vertex);
       System.out.println("Vertex " + vertexNum + " Tris: ");
       if(vTriangles != null){
           for(MeshTriangle tri: vTriangles){
               if(!tri.equals(currentTri)) System.out.println(tri);
            }
       }
       System.out.println("-------------------");
   }
   
   public void displayEdgeNeighbors(Triangle triangle){
       MeshTriangle currentTri = new MeshTriangle(triangle);
       displayOtherTri("12 Tris: ", currentTri.getSide12(),currentTri);
       displayOtherTri("23 Tris: ", currentTri.getSide23(),currentTri);
       displayOtherTri("13 Tris: ", currentTri.getSide13(),currentTri);
   }
   
   private void displayOtherTri(String prefix,MeshEdge edge, MeshTriangle currentTri){
       MeshEdgeTriangles tris = trianglesByEdge.get(edge);
       System.out.println(prefix + tris.getOtherTriangle(currentTri));
   }
    
}
