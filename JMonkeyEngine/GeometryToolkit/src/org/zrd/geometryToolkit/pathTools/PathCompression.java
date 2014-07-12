/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.pathTools;

import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 *
 * @author BLI
 */
public class PathCompression {

    public static ArrayList<Vector3f> getCompressedPath(ArrayList<Vector3f> inputPath, float minSegmentLength) {
        Vector3f lastPointAdded = inputPath.get(0);
        ArrayList<Vector3f> newPath = new ArrayList<Vector3f>(inputPath.size());
        float currentLength;
        for (Vector3f vertex : inputPath) {
            currentLength = vertex.distance(lastPointAdded);
            if (currentLength > minSegmentLength) {
                newPath.add(vertex);
                lastPointAdded = vertex;
            }
        }
        return newPath;
    }
    
}
