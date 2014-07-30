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

    public static ArrayList<Vector3f> getCopyOfPath(ArrayList<Vector3f> path) {
        ArrayList<Vector3f> returnPath = new ArrayList<Vector3f>(path.size());
        for (Vector3f vertex : path) {
            returnPath.add(vertex.clone());
        }
        return returnPath;
    }
    
}
