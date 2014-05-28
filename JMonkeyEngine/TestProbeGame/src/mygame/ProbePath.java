/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;

/**
 *
 * @author BLI
 */
public class ProbePath {

    private ArrayList<Vector3f> vertices;
    private Spatial probePath;
    private Material lineMaterial;
    
    public ProbePath(ArrayList<Vector3f> vertices, Material lineMaterial){
        this.vertices = vertices;
        this.lineMaterial = lineMaterial;
        probePath = LineHelper.createLineFromVertices(vertices, lineMaterial);
    }

    public ArrayList<Vector3f> getVertices() {
        return vertices;
    }

    public Spatial getProbePath() {
        return probePath;
    }
    
    public ArrayList<Vector3f> transformEndpoint(Vector3f newEndpoint){
        Vector3f startPoint = vertices.get(0);
        Vector3f oldEndpoint = vertices.get(vertices.size()-1);
        LineTransformation lineMove = new LineTransformation(
                startPoint,newEndpoint,oldEndpoint);
        return lineMove.transformVertices(vertices);
        
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
     *      tangeant1 = rotate N by Pi/2 using rotationAxis;
     *      tangeant2 = rotate N by -Pi/2 using rotationAxis;
     *      if( angle between tangeant1 and v is less than pi/2):
     *          target = tangeant1;
     *      else
     *          target = tangeant2
     *      angle = arccos( dot product between target and v);
     *      return rotation angle about rotationAxis
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
     *          s_1 is entirely inside the triangle at p
     *          s_2 is the rest of the segment
     *          remove s from L and insert s_2
     *          insert s_1 into L'
     */
    
    /*
     * Here will be the code for following the surface using Collisions:
     * 
     * Pseudo-code:
     * The line is an array Vertex where 
     *      Vertex[i] is the i-th vertex in the sequence
     * Copy the array Vertex[i] into currentVertices
     * Initialize array finalVertices that will hold the final results
     * initialize currentNormal to be the normal at the contact point
     * initialize currentPoint to be the contact point
     * let finalVertices[0] be equal to currentPoint
     * for i from 0 to end-1:
     *      transform all of currentVertices so that the following occurs:
     *          currentVertices[0] matches currentPoint
     *      Shoot a ray with the following properties:
     *          origin is currentVertices[1]
     *          direction is currentNormal and negative of currentNormal
     *      When a proper decision result is found:
     *          make currentPoint equal to collision point
     *          make currentNormal equal to collision normal
     *      finalVertices[i] gets set to currentPoint
     *      Delete first element from currentVertices
     */
    
}
