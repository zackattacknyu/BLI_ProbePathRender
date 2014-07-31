/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.pathTools;

import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 *
 * @author Zach
 */
public class PathHelper {
    //min lenght of individual segment
    public static final float MIN_SEGMENT_LENGTH = 0.001F;

    public static ArrayList<Vector3f> getCopyOfPath(ArrayList<Vector3f> path) {
        ArrayList<Vector3f> returnPath = new ArrayList<Vector3f>(path.size());
        for (Vector3f vertex : path) {
            returnPath.add(vertex.clone());
        }
        return returnPath;
    }
    
    public static Vector3f getFirstPoint(ArrayList<Vector3f> path){
        return path.get(0);
    }
    
    public static Vector3f getSecondPoint(ArrayList<Vector3f> path){
        return path.get(1);
    }
    
    public static Vector3f getLastPoint(ArrayList<Vector3f> path){
        return path.get(path.size()-1);
    }

    public static float getCurrentEndpointDistance(ArrayList<Vector3f> path, Vector3f targetEndpoint) {
        Vector3f actualEndpoint = getLastPoint(path);
        return actualEndpoint.clone().distance(targetEndpoint.clone());
    }
}
