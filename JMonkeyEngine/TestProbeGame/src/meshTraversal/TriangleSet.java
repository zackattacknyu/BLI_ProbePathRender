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
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Type;
import java.nio.FloatBuffer;
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
   }

    public void setTransform(Matrix4f transform) {
        this.transform = transform;
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
   
   public void addMesh(Mesh mesh){
       Triangle currentTri;
       MeshTriangle newTri;
       MeshEdge edge12, edge23, edge13;
       MeshVertex vertex1,vertex2,vertex3;
       
       initializeBoundingVertices();
       MeshData meshData = new MeshData(mesh);
       ArrayList<Vector2f> textCoords = meshData.getTextureCoordinates();
       System.out.println();
       System.out.println("Sample Texture Coordinates: ");
       for(int index = 0; index < 30; index++){
           System.out.println(textCoords.get(index));
       }
       
       
       //put all triangles into a hash map
       for(int index = 0; index < mesh.getTriangleCount(); index++){
           currentTri = new Triangle();
           mesh.getTriangle(index, currentTri);
           newTri = new MeshTriangle(currentTri,transform);
           
           if(index == 2){
               TriangleTexture texture = meshData.getTriangleTextCoords(index);
               System.out.println();
               System.out.println("Sample Coordinates from Triangle: ");
               System.out.println("Vert 1 Text Coord: " + texture.getVertex1texCoord());
               System.out.println("Vert 2 Text Coord: " + texture.getVertex2texCoord());
               System.out.println("Vert 3 Text Coord: " + texture.getVertex3texCoord());
           }
           
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
       Vector3f initEndPointMod = currentTransform.mult(initEndPoint);
       
       TriangleLineSegmentIntersection intersection = new 
               TriangleLineSegmentIntersection(
               startTriangle,initPoint,initEndPointMod);

       System.out.println("12 Intersect: " + intersection.getIntersectEdge12());
       System.out.println("13 Intersect: " + intersection.getIntersectEdge13());
       System.out.println("23 Intersect: " + intersection.getIntersectEdge23());
       
       
       return MeshHelper.getTransformedVertices(path, currentTransform);
   }
   
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
   public ArrayList<Vector3f> makePathFollowMesh2(ArrayList<Vector3f> path,Triangle initTriangle){
       
       /*
        * IMPORTANT: 
        * Make sure the first step it is doing is to move
        *   the path to the desired end point
        */
       
       ArrayList<Vector3f> remainingPath = (ArrayList<Vector3f>) path.clone();
       ArrayList<Vector3f> finalPath = new ArrayList<Vector3f>(path.size());
       Vector3f initPoint,initEndPoint,initEndPointMod,currentNormal;
       Matrix4f currentTransform;
       MeshTriangle currentTriangle = new MeshTriangle(initTriangle,transform);
       TriangleLineSegmentIntersection intersection;
       MeshEdge intersectingEdge = null;
       Vector3f newPoint;
       Vector3f oldNormal = new Vector3f();
       
       while(remainingPath.size() > 1){
           
           initPoint = remainingPath.get(0);
           initEndPoint = remainingPath.get(1);
           
           
           currentNormal = currentTriangle.getNormal();
            currentTransform = MeshHelper.getRotationOntoPlane(currentNormal, initPoint, initEndPoint);
            
            /*
             * If the mesh has an irregularity then this would occur. For now,
             *      we just want smooth meshes so the differences in normals should be
             *      such that their dot product is greater than zero.
             * We need to figure out how to handle the case of an irregularity 
             *      in the mesh. 
             */
            if(oldNormal.dot(currentNormal) < 0){
                System.out.println("DOT PRODUCT WAS LESS THAN ZERO!!");
                break;
            }
            
            oldNormal = currentNormal;
            
            /*
             * This could happen if initPoint and initEndPoint are already perpendicular to the normal
             *  TODO: Handle this case better
             */
            if(!MeshHelper.hasNaN(currentTransform)){
                remainingPath = MeshHelper.getTransformedVertices(remainingPath, currentTransform);
                initEndPoint = remainingPath.get(1);
            }
          
            intersection = new TriangleLineSegmentIntersection(
                currentTriangle,initPoint,initEndPoint);

            if(intersection.isSegDegenerate()){
                //removes the middle one so a larger segment will be made in
                //      the next step
                remainingPath.remove(1);
                continue;
            }else{
                finalPath.add(initPoint);
                remainingPath.remove(0);
            }
            
            intersectingEdge = intersection.getIntersectionEdge(intersectingEdge);
            if(intersectingEdge != null){
                currentTriangle = getEdgeNeighbor(intersectingEdge,currentTriangle);
                newPoint = intersection.getBreakpoint();
                remainingPath.add(0, newPoint);
                
                if(currentTriangle == null){
                    System.out.println("CURRENT TRIANGLE WAS NULL");
                    break;
                }
                
            }
           
           
           
       }
       
       //adds the last points to the final path
       finalPath.addAll(remainingPath);
       
       return finalPath;
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

