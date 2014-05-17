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
     * Here will be the code for following the surface. 
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
