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
    
    private HashMap<MeshEdge,MeshEdgeTriangles> triangles;
    
    private HashMap<Integer,String> edgeAddRecord;
    private HashMap<String,String> vertexAddRecord;
    
    private HashMap<MeshVertex,ArrayList<MeshTriangle>> trianglesByVert;
    private HashMap<String,ArrayList<MeshTriangle>> trianglesByVertString;
    
   public TriangleSet(){
       triangles = new HashMap<MeshEdge,MeshEdgeTriangles>(30000);
       edgeAddRecord = new HashMap<Integer,String>(30000);
       vertexAddRecord = new HashMap<String,String>(30000);
       trianglesByVert = new HashMap<MeshVertex,ArrayList<MeshTriangle>>(30000);
       trianglesByVertString = new HashMap<String,ArrayList<MeshTriangle>>(30000);
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
           
           //DEBUG CODE
           String vertexRecord = String.valueOf(newTri);
           String vertex1Hash = vertex1.toString();
           String vertex2Hash = vertex2.toString();
           String vertex3Hash = vertex3.toString();
           String existing1Record = String.valueOf(vertexAddRecord.get(vertex1Hash));
           String existing2Record = String.valueOf(vertexAddRecord.get(vertex2Hash));
           String existing3Record = String.valueOf(vertexAddRecord.get(vertex3Hash));
           vertexAddRecord.put(vertex1Hash, existing1Record + ";\n" + vertexRecord);
           vertexAddRecord.put(vertex2Hash, existing2Record + ";\n" + vertexRecord);
           vertexAddRecord.put(vertex3Hash, existing3Record + ";\n" + vertexRecord);
           /*String edge12Record = String.valueOf(triangles.get(edge12));
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
           edgeAddRecord.put(edge23hash, existing23Record + ";\n" + edge23Record);*/
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
       
       if(trianglesByVertString.containsKey(vertex.toString())){
           ArrayList<MeshTriangle> theTriangles = trianglesByVertString.get(vertex.toString());
           theTriangles.add(triangle);
       }else{
           ArrayList<MeshTriangle> theTriangles = new ArrayList<MeshTriangle>();
           theTriangles.add(triangle);
           trianglesByVertString.put(vertex.toString(), theTriangles);
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
   
   public void displayNeighbors2(Triangle triangle){
       MeshTriangle currentTri = new MeshTriangle(triangle);
       //ArrayList<MeshTriangle> v1triangles = trianglesByVertString.get(currentTri.getVertex1().toString());
       ArrayList<MeshTriangle> v1triangles = trianglesByVert.get(currentTri.getVertex1());
       
       System.out.println("Vertex 1: " + currentTri.getVertex1());
       //System.out.println("Vertex 1 Record: " + vertexAddRecord.get(currentTri.getVertex1().toString()));
       System.out.println("Vertex 1 Tris: ");
       if(v1triangles != null){
           for(MeshTriangle tri: v1triangles){
                System.out.println(tri);
            }
       }
       System.out.println("-------------------");
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
