/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.meshTraversal;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.Stack;
import org.zrd.geometryToolkit.geometricCalculations.VectorProjectionHelper;
import org.zrd.geometryToolkit.meshDataStructure.MeshEdge;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.pathDataStructure.SegmentSet;
import org.zrd.geometryToolkit.pathTools.PathHelper;

/**
 * ALGORITHMIC CODE:
 * This implements my algorithm for projecting a path onto a mesh
 *      and preserving the arc length while doing it.
 * 
 * This code is meant to take a path that is close to the mesh and project
 *      its segments onto the mesh itself. The following must be preserved:
 *      1) Arc Length
 *      2) Orientation along the current triangle of the mesh. 
 * 
 * Any path is just a series of segments with a start point and 
 *      a vector saying its magnitude and direction. 
 * 
 * Here is the algorithm for taking a segment and projecting it onto the mesh:
 *      1. Take the normal N at the triangle and the vector V for the segment
 *      2. Find the vector N' that is coplanar to N and V and perpendicular to N
 *      3. Find the vector V' that is the projection of V onto N'
 *      4. Find where V' intersects the triangle
 *      5. If V' goes past the triangle, then make a new segment for the part
 *              that goes past the triangle
 *      6. If a new segment was made, repeat steps 1-6 for the new segment
 *          
 *
 * @author BLI
 */
public class PathProjectionOntoMesh {
    
    private MeshTriangle currentTriangle;
    private TriangleSet triangleSet;
    private Vector3f currentStartPoint;
    
    /**
     * This instantiates the path projection code. The constructor
     *      is meant to be used for probe tracking on the surface
     *      so that the current triangle and current start point
     *      are kept track of using this class. 
     * 
     * @param initTriangle      initial mesh triangle to calculate projection
     * @param startPoint        initial start point on the triangle to calculate projectino
     * @param triangleSet       the mesh to track on
     */
    public PathProjectionOntoMesh(MeshTriangle initTriangle, 
            Vector3f startPoint, TriangleSet triangleSet){
        this.triangleSet = triangleSet;
        this.currentStartPoint = startPoint;
        this.currentTriangle = initTriangle;
    }

    /**
     * This is a static method that is meant to be used by classes
     *      that want to take a single path and mesh and project
     *      the entire path onto the mesh. It takes each segment,
     *      calculates its projection, and aggregates them together
     *      to get the final result
     * 
     * @param path              original input path
     * @param initTriangle      starting mesh triangle
     * @param triangleSet       mesh to project the path onto
     * @return                  new path along the mesh
     */
    public static ArrayList<Vector3f> getPathProjectedOntoMesh(ArrayList<Vector3f> path, 
            MeshTriangle initTriangle, TriangleSet triangleSet) {
        
        return getPathProjectedOntoMesh(new SegmentSet(path),initTriangle,triangleSet).getPathVertices();
    }
    
    public static SegmentSet getPathProjectedOntoMesh(SegmentSet path, 
            MeshTriangle initTriangle, TriangleSet triangleSet) {
        
        //makes a path projection class
        PathProjectionOntoMesh newProjection = new PathProjectionOntoMesh(
                initTriangle,PathHelper.getFirstPoint(path.getPathVertices()),triangleSet);
        
        //returns the path projection
        return newProjection.findCurrentPathProjectionOntoMesh(path);
    }
    
    /**
     * This takes the path and finds each individual segment in it.
     *      It then takes the segments and finds their projections, keeping
     *          track of the triangle we are on. 
     *      It then aggegates the projections together and returns the final
     *          path. 
     * @param path      input path
     * @return          input path projected onto the mesh
     */
    private SegmentSet findCurrentPathProjectionOntoMesh(SegmentSet currentPath){
        ArrayList<Vector3f> finalPath = new ArrayList<Vector3f>(currentPath.getSize());
        ArrayList<String[]> finalPathData = new ArrayList<String[]>(currentPath.getSize());
        ArrayList<Vector2f> finalPathTex = new ArrayList<Vector2f>(currentPath.getSize());
        
        SegmentSet currentProjectedPath;
        int index = 0;
        
        //goes through each segment
        for(Vector3f segmentVec: currentPath.getSegmentVectors()){
            
            //finds the segment projected onto the mesh
            currentProjectedPath = getCurrentProjectedPath(segmentVec);
            
            //adds the projection to the final output path
            finalPath.addAll(currentProjectedPath.getPathVertices());
            finalPathTex.addAll(currentProjectedPath.getVertexTextureCoords());
            
            //repeatedly adds the data for the new points added in projection
            if(currentPath.getDataAtVertices() != null){
                for(int i = 0; i < currentProjectedPath.getPathVertices().size(); i++){
                    finalPathData.add(currentPath.getDataAtVertices().get(index));
                }
                index++;
            }
            
        }
        
        //makes sure the end point gets added
        finalPath.add(currentStartPoint);
        if(currentPath.getDataAtVertices() != null){
            finalPathData.add(currentPath.getDataAtVertices().get(currentPath.getSize()-1));
        }
        
        return new SegmentSet(finalPath,finalPathData,finalPathTex);
    }
    
    /**
     * This gets the segment vector and finds its projection onto the mesh.
     *      If we do not have a current triangle then it simply returns
     *          the points without any projection. 
     *      If there is no current triangle, then it was likely that we lost
     *          track of the triangles, which happens when we are trying to 
     *          find a neighbor to a triangle and that neighbor is null. 
     *      The neighbor is null if one of the following is true:
     *          1) We are at the boundary of the mesh
     *          2) There is a "hole" in the mesh that should not be there
     *          
     * @param segmentVector
     * @return 
     */
    public SegmentSet getCurrentProjectedPath(Vector3f segmentVector){
        if(currentTriangle == null){
            
            /*
             * If no triangle, then just return the start point
             *      plus the segment vector
             */
            return new SegmentSet(getPathPoints(segmentVector));
        }else{
            
            /*
             * If we still have a triangle, then project the segment
             *      onto the mesh and return the result
             */
            return findCurrentSegmentProjectionOntoMesh(segmentVector);
        }
    }
    
    /**
     * This simply adds together the current start point with the segment
     *      vector and returns the two points in an array list. It also
     *      updates the current start point
     * @param segmentVector     current segment
     * @return                  startPoint as first point. startPoint+segment as second point
     */
    private ArrayList<Vector3f> getPathPoints(Vector3f segmentVector){
        ArrayList<Vector3f> finalPath = new ArrayList<Vector3f>();
        finalPath.add(currentStartPoint.clone());
        finalPath.add(currentStartPoint.addLocal(segmentVector).clone());
        return finalPath;
    }
    
    /**
     * This takes a segment and returns its projection along the mesh
     *      updating the start point and triangle in the process
     * @param segmentVector     current segment
     * @return                  corresponding path along the mesh for the segment
     */
    private SegmentSet findCurrentSegmentProjectionOntoMesh(Vector3f segmentVector) {
        //the return path
        ArrayList<Vector3f> finalPath = new ArrayList<Vector3f>();
        
        //return path texture coordinates
        ArrayList<Vector2f> finalPathTex = new ArrayList<Vector2f>();
        
        //the segments that still need to be projected onto the mesh
        Stack<Vector3f> remainingSegments = new Stack<Vector3f>();
        
        //adds the first segment
        remainingSegments.add(segmentVector);
        
        Vector3f currentEndPoint;
        Vector3f currentNormal;
        TriangleLineSegmentIntersection intersection;
        MeshEdge intersectingEdge = null;
        Vector3f oldNormal = new Vector3f();
        Vector3f currentVector;
        Vector3f currentVectorOnPlane;
        Vector3f newDeltaVector;
        
        
        finalPath.add(currentStartPoint);
        while (!remainingSegments.empty()) {
            currentNormal = currentTriangle.getNormal();
            
            //this shouldn't happen since we have a smooth surface
            if (oldNormal.dot(currentNormal) < 0) {
                System.out.println("DOT PRODUCT WAS LESS THAN ZERO!!");
                break;
            }
            oldNormal = currentNormal;
            
            /*
             * This takes the top of the stack and finds it projection
             *      onto the plane of the current triangle
             */
            currentVector = remainingSegments.peek().clone();
            currentVectorOnPlane = VectorProjectionHelper.getVectorProjectedOntoPlane(currentNormal, currentVector);
            
            /*
             * This finds the end point of the projected segment and finds
             *      where it intersects the current triangle
             */
            currentEndPoint = currentStartPoint.add(currentVectorOnPlane);
            intersection = new TriangleLineSegmentIntersection(currentTriangle, currentStartPoint, currentEndPoint);
            
            //gets the texture coord of the start point
            TriangleTextureCoord tex = new TriangleTextureCoord(currentTriangle,currentStartPoint);
            finalPathTex.add(tex.getTextureCoordinate());
            System.out.println("Texture Coord: " + tex.getTextureCoordinate());
            
            //now that the intersection was found, remove the top segment
            remainingSegments.pop();
            
            //if segment was degenerate, then move onto the next segment
            if (intersection.isSegDegenerate()) {
                continue;
            } 
            
            /*
             * Now we figure out if the segment is in the whole triangle or not
             *      and deal with the cases
             */
            intersectingEdge = intersection.getIntersectionEdge(intersectingEdge);
            if (intersectingEdge != null) {
                
                //finds correct adjacent triangle
                currentTriangle = triangleSet.getEdgeNeighbor(intersectingEdge, currentTriangle);
                
                //gets vector from start point to intersection along triangle
                newDeltaVector = intersection.getDeltaVector();
                
                //gets the new start point, which is intersection of triangle and segment
                currentStartPoint = intersection.getBreakpoint().clone();
                
                
                /* the current vector is the part of the first segment 
                 *      that is not in the current triangle. It gets pushed
                 *      onto the stack to be processed next
                 */
                remainingSegments.push(currentVector.subtract(newDeltaVector));
                
                /*
                 * If we lost track of the triangles, then we break out of the 
                 *      projection loop and just find the unprojected paths
                 *      for the rest of the segments. 
                 */
                if (currentTriangle == null) {
                    System.out.println("CURRENT TRIANGLE WAS NULL");
                    break;
                }
                
            }else{
                
                //whole segment is in triangle
                currentStartPoint = currentEndPoint.clone();
                
            }
            
            //adds the new start point to the return path
            finalPath.add(currentStartPoint);
        }
        return new SegmentSet(finalPath,null,finalPathTex);
    }
    
    /**
     * Returns the current triangle for debugging
     * @return      current triangle being tracked on
     */
    public MeshTriangle getCurrentTriangle() {
        return currentTriangle;
    }
    
    /* 
     * ********IMPORTANT NOTE*****************
     * UNUSED CODE BELOW HERE
     * PLEASE KEEP THOUGH FOR POTENTIAL
     *      FUTURE USES
     * ***************************************
     *
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
     * 
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
    /*public static ArrayList<Vector3f> findPathProjectionOntoMesh(ArrayList<Vector3f> path, MeshTriangle initTriangle, TriangleSet triangleSet) {
        ArrayList<Vector3f> remainingSegments = (ArrayList<Vector3f>) path.clone();
        ArrayList<Vector3f> finalPath = new ArrayList<Vector3f>(path.size());
        Vector3f initPoint;
        Vector3f initEndPoint;
        Vector3f initEndPointMod;
        Vector3f currentNormal;
        Matrix4f currentTransform;
        MeshTriangle currentTriangle = initTriangle;
        TriangleLineSegmentIntersection intersection;
        MeshEdge intersectingEdge = null;
        Vector3f newPoint;
        Vector3f oldNormal = new Vector3f();
        while (remainingSegments.size() > 1) {
            initPoint = remainingSegments.get(0);
            initEndPoint = remainingSegments.get(1);
            currentNormal = currentTriangle.getNormal();
            currentTransform = MeshTraverseHelper.getRotationOntoPlane(currentNormal, initPoint, initEndPoint);
            if (oldNormal.dot(currentNormal) < 0) {
                System.out.println("DOT PRODUCT WAS LESS THAN ZERO!!");
                break;
            }
            oldNormal = currentNormal;
            if (!MeshTraverseHelper.hasNaN(currentTransform)) {
                remainingSegments = MeshTraverseHelper.getTransformedVertices(remainingSegments, currentTransform);
                initEndPoint = remainingSegments.get(1);
            }
            intersection = new TriangleLineSegmentIntersection(currentTriangle, initPoint, initEndPoint);
            if (intersection.isSegDegenerate()) {
                remainingSegments.remove(1);
                continue;
            } else {
                finalPath.add(initPoint);
                remainingSegments.remove(0);
            }
            intersectingEdge = intersection.getIntersectionEdge(intersectingEdge);
            if (intersectingEdge != null) {
                currentTriangle = triangleSet.getEdgeNeighbor(intersectingEdge, currentTriangle);
                newPoint = intersection.getBreakpoint();
                remainingSegments.add(0, newPoint);
                if (currentTriangle == null) {
                    System.out.println("CURRENT TRIANGLE WAS NULL");
                    break;
                }
            }
        }
        finalPath.addAll(remainingSegments);
        return finalPath;
    }*/

    

    
    
}
