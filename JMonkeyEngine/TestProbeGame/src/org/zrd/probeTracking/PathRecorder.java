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

    private ArrayList<Vector3f> vertices = new ArrayList<Vector3f>(100);
    
    public PathRecorder(Vector3f startingPosition){
        vertices.add(startingPosition);
    }
    
    public void addToPath(Vector3f vertex){
        vertices.add(vertex);
    }
    
    public ArrayList<Vector3f> getVertices() {
        return vertices;
    }
    
    
}
