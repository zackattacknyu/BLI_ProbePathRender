/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meshTraversal;

import com.jme3.math.Triangle;
import com.jme3.scene.Mesh;
import java.util.HashMap;

/**
 *
 * @author Zach
 */
public class TriangleSet {

    Mesh mesh;
    
    private HashMap<MeshEdge,MeshEdgeTriangles> triangles;
    
   public TriangleSet(Mesh mesh){
       triangles = new HashMap<MeshEdge,MeshEdgeTriangles>(mesh.getTriangleCount());
       Triangle currentTri = new Triangle();
       MeshTriangle newTri;
       MeshEdge edge12, edge23, edge13;
       
       //put all triangles into a hash map
       for(int index = 0; index < mesh.getTriangleCount(); index++){
           mesh.getTriangle(index, currentTri);
           newTri = new MeshTriangle(currentTri);
           edge12 = newTri.getSide12();
           edge13 = newTri.getSide13();
           edge23 = newTri.getSide23();
           
           addEdgeToTriangleMap(edge12,newTri);
           addEdgeToTriangleMap(edge13,newTri);
           addEdgeToTriangleMap(edge23,newTri);
           
       }
   }
   
   private void addEdgeToTriangleMap(MeshEdge edge, MeshTriangle triangle){
       if(triangles.containsKey(edge)){
           triangles.get(edge).addTriangle(triangle);
       }else{
           MeshEdgeTriangles edgeTriangles = new MeshEdgeTriangles();
           edgeTriangles.addTriangle(triangle);
           triangles.put(edge, edgeTriangles);
       }
   }
   
   public void displayNeighbors(Triangle triangle){
       MeshTriangle currentTri = new MeshTriangle(triangle);
       MeshEdgeTriangles v12triangles = triangles.get(currentTri.getSide12());
       MeshEdgeTriangles v23triangles = triangles.get(currentTri.getSide23());
       MeshEdgeTriangles v13triangles = triangles.get(currentTri.getSide13());
       System.out.println("12 Neighbor:");
       System.out.println(v12triangles.getOtherTriangle(currentTri));
       System.out.println("23 Neighbor:");
       System.out.println(v23triangles.getOtherTriangle(currentTri));
       System.out.println("13 Neighbor:");
       System.out.println(v13triangles.getOtherTriangle(currentTri));
   }
    
}
