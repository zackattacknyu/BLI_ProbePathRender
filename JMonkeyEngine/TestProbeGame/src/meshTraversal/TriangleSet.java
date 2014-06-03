/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meshTraversal;

import com.jme3.math.Matrix3f;
import com.jme3.math.Matrix4f;
import com.jme3.math.Triangle;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
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
    
    private Matrix4f transform;
    
   public TriangleSet(){
       trianglesByEdge = new HashMap<MeshEdge,MeshEdgeTriangles>(30000);
       trianglesByVert = new HashMap<MeshVertex,ArrayList<MeshTriangle>>(30000);
       
       transform = new Matrix4f();
   }

    public void setTransform(Matrix4f transform) {
        this.transform = transform;
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
           newTri = new MeshTriangle(currentTri,transform);
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
   /*
     * Here will be the code for following the surface using Triangles:
     * 
     * At each triangle, we will have a normal and a line segment. 
     * We are allowed to rotate around the plane that the two vectors make
     *      but not in any other way because then we will be changing yaw
     * The tangeant vector will be one of the vectors perpendicular to the
     *      normal vector. We want to move our vector so that it is aligned
     *      with the tangenat vector. 
     * We will thus move our vector toward either tangeant vectors in order
     *      to align it with the surface.
     * 
     * Psuedo-code:
     * 
     * getRotation(Normal vector N, our Vector v):
     *      Vector rotationAxis = crossProduct(N,v);
     *      Projection T of vector v onto the plane is given by:
     *          T = v - N*( dot(N,v) )
     *      theta = arccos( dot(E,T) )
     *      return rotation by theta about rotationAxis
     * 
     * list L of to be processed segments, consisting of the path
     * initialize empty list L' of processed segments
     * initialize triangle T to be the triangle at the first point
     * 
     * while L is not empty:
     *      let s equal first segment in L
     *      let p equal first point in L
     *      find normal N to the triangle T
     *      Let R be result of getRotation(N,s)
     *      rotate all of L by R using p as center of rotation
     *      reset s and p to still be the first point and segment
     *      if( s is entirely inside triangle):
     *          remove s and p from L
     *      else:
     *          break up s into s_1 and s_2;
     *          let T be neighboring triangle at the edge
     *          s_1 is entirely inside the triangle at p
     *          s_2 is the rest of the segment
     *          remove s from L and insert s_2
     *          insert s_1 into L'
     */
    
    /*
     * In the end Psuedo code will be the following:
     * 
     * Set startPoint to start point of path
     * Set actualEndPoint to end point of recorded path
     * Set desiredEndPoint to desired calibration point
     * while dist(actualEndPoint,desiredEndPoint)>epsilon:
     *      Make initial guess theta based on startPoint, actualEndPoint, desiredEndPoint
     *      Rotate the curve using theta
     *      Project the curve onto the surface
     *      After projection, change actualEndPoint to the end point of the projected curve
     */
   public ArrayList<Vector3f> makePathFollowMesh(ArrayList<Vector3f> path,Triangle initTriangle, Vector3f initNormal){
       Vector3f initPoint = path.get(0);
       Vector3f initEndPoint = path.get(1);
             
       
       Matrix4f currentTransform = MeshHelper.getRotationOntoPlane(initNormal, initPoint, initEndPoint);
       MeshTriangle startTriangle = new MeshTriangle(initTriangle,transform);
       
       //TODO: Change this later
       Vector3f initEndPointMod = currentTransform.mult(initEndPoint);
       Vector3f vertex1 = startTriangle.getVertex1().getVertex();
       Vector3f vertex2 = startTriangle.getVertex2().getVertex();
       Vector3f vertex3 = startTriangle.getVertex3().getVertex();
       
       System.out.println("Start Triangle: " + startTriangle);
       System.out.println("Contact Point: " + initPoint);
       
       Matrix4f originVertex1 = new Matrix4f();
       originVertex1.setTranslation(vertex1.mult(-1));
       Vector3f vertex2Vec = originVertex1.mult(vertex2);
       Vector3f vertex3Vec = originVertex1.mult(vertex3);
       Vector3f initPointUse = originVertex1.mult(initPoint);
       Vector3f initEndPointModUse = originVertex1.mult(initEndPointMod);
       
       /*
        * This part is necessary due to rounding errors skewing
        *       our results. By multplying, we are ensuring
        *       that rounding errors won't affect the final result.
        */
       float localMultiplier = (float)Math.pow(10, 5);
       vertex2Vec.multLocal(localMultiplier);
       vertex3Vec.multLocal(localMultiplier);
       initPointUse.multLocal(localMultiplier);
       initEndPointModUse.multLocal(localMultiplier);

       System.out.println("Vertex 2: " + vertex2Vec);
       System.out.println("Vertex 3: " + vertex3Vec);
       System.out.println("Start Point: " + initPointUse);
       System.out.println("End Point: " + initEndPointModUse);
       
       Vector3f newZVector = vertex2Vec.clone().cross(vertex3Vec.clone());
       System.out.println("New Z Vector: " + newZVector);
       
       //TODO: START DEBUGGING FROM HERE
       Matrix3f coordMatrix = new Matrix3f();
       float[][] coords = new float[3][3];
       for(int j = 0; j < 3; j++){
            coords[j][0] = vertex2Vec.get(j);
            coords[j][1] = vertex3Vec.get(j);
            coords[j][2] = newZVector.get(j);
        }
       coordMatrix.set(coords);
       System.out.println("Coord Matrix: " + coordMatrix);
       coordMatrix.invertLocal();

       System.out.println("CoordMatrix: " + coordMatrix);
       Vector3f newStart = coordMatrix.mult(initPointUse);
       Vector3f newEnd = coordMatrix.mult(initEndPointModUse);
       System.out.println("Start Point New System: " + newStart);
       System.out.println("End Point New System: " + newEnd);
       Vector3f newDir = newEnd.clone().subtract(newStart);
       float intersect12 = -1*newStart.getY()/newDir.getY();
       float intersect13 = -1*newStart.getX()/newDir.getX();
       System.out.println("12 Intersect: " + intersect12);
       System.out.println("13 Intersect: " + intersect13);
       
       Vector2f intersect23Points = MeshHelper.solveMatrixEqu(
               newDir.getX(), -1, 
               newDir.getY(), 1, 
               -1*newStart.getX(), 1-newStart.getY());
       float intersect23 = intersect23Points.getX();
       System.out.println("23 Intersect: " + intersect23);
       
       
       return MeshHelper.getTransformedVertices(path, currentTransform);
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
       return tris.getOtherTriangle(triangle);
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
   
   public void displayVertexNeighbors(Triangle triangle){
       MeshTriangle currentTri = new MeshTriangle(triangle,transform);
       
       displayVertexTris(1,currentTri.getVertex1(),currentTri);
       displayVertexTris(2,currentTri.getVertex2(),currentTri);
       displayVertexTris(3,currentTri.getVertex3(),currentTri);
   }
   
   private void displayVertexTris(int vertexNum,MeshVertex vertex,MeshTriangle currentTri){
       ArrayList<MeshTriangle> vTriangles = getVertexNeighbors(vertex,currentTri);
       System.out.println("Vertex " + vertexNum + ": " + vertex);
       System.out.println("Vertex " + vertexNum + " Tris: ");
       if(vTriangles != null){
           for(MeshTriangle tri: vTriangles){
               System.out.println(tri);
            }
       }
       System.out.println("-------------------");
   }
   
   public void displayEdgeNeighbors(Triangle triangle){
       MeshTriangle currentTri = new MeshTriangle(triangle,transform);
       System.out.println("12 Tris: " + getEdgeNeighbor(currentTri.getSide12(),currentTri));
       System.out.println("13 Tris: " + getEdgeNeighbor(currentTri.getSide13(),currentTri));
       System.out.println("23 Tris: " + getEdgeNeighbor(currentTri.getSide23(),currentTri));
   }
    
}
