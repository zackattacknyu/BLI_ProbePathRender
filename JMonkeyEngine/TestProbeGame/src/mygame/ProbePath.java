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
    
}
