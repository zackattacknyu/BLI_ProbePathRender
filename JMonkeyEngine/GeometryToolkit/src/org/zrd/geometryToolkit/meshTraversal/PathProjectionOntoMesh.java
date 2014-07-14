/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.meshTraversal;

import com.jme3.math.Matrix3f;
import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.Stack;
import org.zrd.geometryToolkit.geometryUtil.GeneralHelper;
import org.zrd.geometryToolkit.meshDataStructure.MeshEdge;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;

/**
 *
 * @author BLI
 */
public class PathProjectionOntoMesh {

    /*
     * TODO: Rewrite above method so that it is using the difference vectors
     *      at each step.
     * In other words, it will keep track of the current origin point and
     *      and then next entry will be the vector that will move the origin point
     * 
     * The algorithm will be:
     *      1. Take the origin point
     *      2. Look at the vector to move the point
     *      3. Project the vector onto the incident triangles
     *      4. Move the origin point based on the vector projection
     *      5. Back to step 1 with the next entry
     */
    public static ArrayList<Vector3f> findPathProjectionOntoMesh(ArrayList<Vector3f> path, MeshTriangle initTriangle, TriangleSet triangleSet) {
        ArrayList<Vector3f> finalPath = new ArrayList<Vector3f>(path.size());
        
        //gets the diff vector array
        ArrayList<Vector3f> pathAsVectors = new ArrayList<Vector3f>(path.size());
        Vector3f startingPoint = path.get(0);
        Vector3f diffVector;
        for(int index = 1; index < path.size(); index++){
            diffVector = path.get(index).subtract(path.get(index-1));
            pathAsVectors.add(diffVector);
        }
        
        Stack<Vector3f> remainingPath = new Stack<Vector3f>();
        for(int index = pathAsVectors.size()-1; index >= 0; index--){
            remainingPath.add(pathAsVectors.get(index));
        }

        
        Vector3f currentEndPoint;
        Vector3f currentNormal;
        MeshTriangle currentTriangle = initTriangle;
        TriangleLineSegmentIntersection intersection;
        MeshEdge intersectingEdge = null;
        Vector3f newPoint;
        Vector3f oldNormal = new Vector3f();
        Vector3f currentVector;
        Vector3f currentVectorOnPlane;
        Vector3f currentStartPoint = startingPoint.clone();
        Vector3f newDeltaVector;
        while (!remainingPath.empty()) {
            currentNormal = currentTriangle.getNormal();
            
            //this shouldn't happen since we have a smooth surface
            if (oldNormal.dot(currentNormal) < 0) {
                System.out.println("DOT PRODUCT WAS LESS THAN ZERO!!");
                break;
            }
            oldNormal = currentNormal;
            
            currentVector = remainingPath.peek().clone();
            currentVectorOnPlane = MeshTraverseHelper.getVectorRotatedOntoPlane(currentNormal, currentVector);
            
            currentEndPoint = currentStartPoint.add(currentVectorOnPlane);
            
            intersection = new TriangleLineSegmentIntersection(currentTriangle, currentStartPoint, currentEndPoint);
            
            if (intersection.isSegDegenerate()) {
                
                //if segment is degenerate, remove it
                remainingPath.pop();
                continue;
            } else {
                
                //if not degenerate, then start point can be added to final list
                //      and the first vector can be removed
                finalPath.add(currentStartPoint);
                remainingPath.pop();
            }
            
            /*
             * Now we figure out if the segment is in the whole triangle or not
             *      and deal with the cases
             */
            intersectingEdge = intersection.getIntersectionEdge(intersectingEdge);
            if (intersectingEdge != null) {
                
                //segment will go to adjacent triangle
                currentTriangle = triangleSet.getEdgeNeighbor(intersectingEdge, currentTriangle);
                newPoint = intersection.getBreakpoint();
                newDeltaVector = intersection.getDeltaVector();
                currentStartPoint = newPoint.clone();
                
                //the current vector is the part of the first segment that is not in the current triangle
                remainingPath.push(currentVector.subtract(newDeltaVector));
                
                if (currentTriangle == null) {
                    System.out.println("CURRENT TRIANGLE WAS NULL");
                    break;
                }
                
            }else{
                
                //whole segment is in triangle
                currentStartPoint = currentEndPoint.clone();
                
            }
        }
        //finalPath.addAll(remainingPath);
        return finalPath;
    }
    
    /* Here will be the code for following the surface using Triangles:
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
        ArrayList<Vector3f> remainingPath = (ArrayList<Vector3f>) path.clone();
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
        while (remainingPath.size() > 1) {
            initPoint = remainingPath.get(0);
            initEndPoint = remainingPath.get(1);
            currentNormal = currentTriangle.getNormal();
            currentTransform = MeshTraverseHelper.getRotationOntoPlane(currentNormal, initPoint, initEndPoint);
            if (oldNormal.dot(currentNormal) < 0) {
                System.out.println("DOT PRODUCT WAS LESS THAN ZERO!!");
                break;
            }
            oldNormal = currentNormal;
            if (!MeshTraverseHelper.hasNaN(currentTransform)) {
                remainingPath = MeshTraverseHelper.getTransformedVertices(remainingPath, currentTransform);
                initEndPoint = remainingPath.get(1);
            }
            intersection = new TriangleLineSegmentIntersection(currentTriangle, initPoint, initEndPoint);
            if (intersection.isSegDegenerate()) {
                remainingPath.remove(1);
                continue;
            } else {
                finalPath.add(initPoint);
                remainingPath.remove(0);
            }
            intersectingEdge = intersection.getIntersectionEdge(intersectingEdge);
            if (intersectingEdge != null) {
                currentTriangle = triangleSet.getEdgeNeighbor(intersectingEdge, currentTriangle);
                newPoint = intersection.getBreakpoint();
                remainingPath.add(0, newPoint);
                if (currentTriangle == null) {
                    System.out.println("CURRENT TRIANGLE WAS NULL");
                    break;
                }
            }
        }
        finalPath.addAll(remainingPath);
        return finalPath;
    }*/
    
}
