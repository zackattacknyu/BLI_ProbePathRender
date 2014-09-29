/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.pathTools;

import com.jme3.math.Vector3f;
import java.util.ArrayList;
import org.zrd.geometryToolkit.pathDataStructure.SegmentSet;

/**
 *
 * @author BLI
 */
public class PathCompression {

    public static ArrayList<Vector3f> getCompressedPath(ArrayList<Vector3f> inputPath, float minSegmentLength) {
        Vector3f lastPointAdded = inputPath.get(0);
        ArrayList<Vector3f> newPath = new ArrayList<Vector3f>(inputPath.size());
        newPath.add(lastPointAdded);
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
    
    public static SegmentSet getCompressedPath(SegmentSet inputPath, float minSegmentLength) {
        Vector3f lastPointAdded = inputPath.getDataAtIndex(0).getVertex();
        SegmentSet newPath = new SegmentSet(inputPath.getSize());
        newPath.addToSet(inputPath.getDataAtIndex(0));
        float currentLength;
        Vector3f vertex;
        for(int index = 1; index < inputPath.getSize(); index++){
            vertex = inputPath.getDataAtIndex(index).getVertex();
            currentLength = vertex.distance(lastPointAdded);
            if (currentLength > minSegmentLength) {
                newPath.addToSet(inputPath.getDataAtIndex(index));
                lastPointAdded = vertex;
            }
        }
        return newPath;
    }
    
}
