/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.meshTraversal;

import com.jme3.math.Matrix4f;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import org.zrd.geometryToolkit.meshDataStructure.MeshEdge;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;

/**
 *
 * @author BLI
 */
public class PathProjectionOntoMesh {

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
    public static ArrayList<Vector3f> findPathProjectionOntoMesh(ArrayList<Vector3f> path, MeshTriangle initTriangle, TriangleSet triangleSet) {
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
    }
    
}
