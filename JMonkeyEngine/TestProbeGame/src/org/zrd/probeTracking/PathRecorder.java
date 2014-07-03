/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTracking;

import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 *
 * @author BLI
 */
public class PathRecorder {

    private ArrayList<Vector3f> vertices;
    
    public PathRecorder(Vector3f startingPosition){
        vertices = new ArrayList<Vector3f>(100);
        vertices.add(startingPosition.clone());
    }
    
    public void addToPath(Vector3f vertex){
        vertices.add(vertex.clone());
    }
    
    public ArrayList<Vector3f> getVertices() {
        return vertices;
    }
    
    
}
