/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.graphicsTools.geometry.path;

import com.jme3.material.Material;
import com.jme3.math.Matrix4f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
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
        return PathTransformHelper.transformPathEndpoint(vertices, newEndpoint);
        
    }
    
    public Matrix4f getTransformOfEndpoint(Vector3f newEndpoint){
        return PathTransformHelper.getTransformOfEndpoint(vertices, newEndpoint);
    }
    
    public ArrayList<Vector3f> scaleForNewEndpoint(Vector3f newEndPoint){
        return PathTransformHelper.scalePathForNewEndpoint(vertices, newEndPoint);
    }
    
    public ArrayList<Vector3f> compressPath(){
        return PathTransformHelper.getCompressedPath(vertices, 
                ProgramConstants.MIN_SEGMENT_LENGTH);
    }
    
    
    
}
