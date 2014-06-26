/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.graphicsTools.geometry.path;

import com.jme3.material.Material;
import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import org.zrd.graphicsTools.geometry.meshTraversal.MeshHelper;
import org.zrd.graphicsToolsImpl.pathImpl.PathHelper;
import org.zrd.utilImpl.general.ProgramConstants;

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
        probePath = PathHelper.createLineFromVertices(vertices, lineMaterial);
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
        Matrix4f wholeTransformation = MeshHelper.getRotationAroundPoint(startPoint, newEndpoint, oldEndpoint);
        displayAngleOfRotation(wholeTransformation);
        return MeshHelper.getTransformedVertices(vertices, wholeTransformation);
        
    }
    
    public void displayAngleOfRotation(Matrix4f transform){
        Quaternion rotation = transform.toRotationQuat();
        AngleAxisRotation rot = new AngleAxisRotation(rotation);
        System.out.println("Angle is: " + rot.getAngle() + " radians");
    }
    
    public ArrayList<Vector3f> scaleForNewEndpoint(Vector3f newEndPoint){
        Vector3f startPoint = vertices.get(0);
        Vector3f oldEndpoint = vertices.get(vertices.size()-1);
        float currentLength = oldEndpoint.subtract(startPoint).length();
        float desiredLength = newEndPoint.subtract(startPoint).length();
        float scaleFactor = desiredLength/currentLength;
        System.out.println("Scale Factor was: " + scaleFactor);
        Matrix4f transform = MeshHelper.getScaleAroundPoint(startPoint, scaleFactor);
        return MeshHelper.getTransformedVertices(vertices, transform);
    }
    
    public ArrayList<Vector3f> compressPath(){
        Vector3f lastPointAdded = vertices.get(0);
        ArrayList<Vector3f> newPath = new ArrayList<Vector3f>(vertices.size());
        float currentLength;
        for(Vector3f vertex: vertices){
            currentLength = vertex.distance(lastPointAdded);
            if(currentLength > ProgramConstants.MIN_SEGMENT_LENGTH){
                newPath.add(vertex);
                lastPointAdded = vertex;
            }
        }
        return newPath;
    }
    
    
    
}
