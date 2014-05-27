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
    
    private HashMap<MeshEdge,MeshEdgeTriangles> triangles;
    
    private HashMap<Integer,String> edgeAddRecord;
    
   public TriangleSet(){
       triangles = new HashMap<MeshEdge,MeshEdgeTriangles>(30000);
       edgeAddRecord = new HashMap<Integer,String>(30000);
   }
   
   public void addMesh(Mesh mesh){
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
           
           //DEBUG CODE
           String edge12Record = String.valueOf(triangles.get(edge12));
           String edge13Record = String.valueOf(triangles.get(edge13));
           String edge23Record = String.valueOf(triangles.get(edge23));
           int edge12hash = edge12.hashCode();
           int edge23hash = edge23.hashCode();
           int edge13hash = edge13.hashCode();
           String existing12Record = String.valueOf(edgeAddRecord.get(edge12hash));
           String existing23Record = String.valueOf(edgeAddRecord.get(edge23hash));
           String existing13Record = String.valueOf(edgeAddRecord.get(edge13hash));
           edgeAddRecord.put(edge12hash, existing12Record + ";\n" + edge12Record);
           edgeAddRecord.put(edge13hash, existing13Record + ";\n" + edge13Record);
           edgeAddRecord.put(edge23hash, existing23Record + ";\n" + edge23Record);
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
       //MeshEdgeTriangles v23triangles = triangles.get(currentTri.getSide23());
       //MeshEdgeTriangles v13triangles = triangles.get(currentTri.getSide13());
       
       System.out.println("Edge12 Record:" + 
               edgeAddRecord.get(currentTri.getSide12().hashCode()));
       /*System.out.println("Edge13 Record:" + 
               edgeAddRecord.get(currentTri.getSide13().hashCode()));
       System.out.println("Edge23 Record:" + 
               edgeAddRecord.get(currentTri.getSide23().hashCode()));*/
       
       System.out.println("Edge 12: " + currentTri.getSide12());
       //System.out.println("Edge 23: " + currentTri.getSide23());        
       //System.out.println("Edge 13: " + currentTri.getSide13());
       System.out.println("12 Tris: " + v12triangles);
       //System.out.println("23 Tris: " + v23triangles);
       //System.out.println("13 Tris: " + v13triangles);
   }
    
}
