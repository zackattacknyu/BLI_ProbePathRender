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
    
    public static ArrayList<Integer> getCompressedPathVertexIndices(ArrayList<Vector3f> inputPath, float minSegmentLength){
        Vector3f lastPointAdded = inputPath.get(0);
        ArrayList<Integer> newPathVertices = new ArrayList<Integer>(inputPath.size());
        newPathVertices.add(0);
        float currentLength;
        Vector3f vertex;
        for(int index = 1; index < inputPath.size(); index++){
            vertex = inputPath.get(index);
            currentLength = vertex.distance(lastPointAdded);
            if (currentLength > minSegmentLength) {
                newPathVertices.add(index);
                lastPointAdded = vertex;
            }
        }
        return newPathVertices;
    }

    public static ArrayList<Vector3f> getCompressedPath(ArrayList<Vector3f> inputPath, float minSegmentLength) {
        ArrayList<Integer> compressedPathVertexIndices = getCompressedPathVertexIndices(inputPath,minSegmentLength);
        ArrayList<Vector3f> newPath = new ArrayList<Vector3f>(inputPath.size());
        for(Integer index: compressedPathVertexIndices){
            newPath.add(inputPath.get(index));
        }
        return newPath;
    }
    
    public static SegmentSet getCompressedPath(SegmentSet inputPath, float minSegmentLength) {
        ArrayList<Integer> compressedPathVertexIndices = 
                getCompressedPathVertexIndices(inputPath.getPathVertices(),minSegmentLength);
        SegmentSet newPath = new SegmentSet(inputPath.getSize());
        for(Integer index: compressedPathVertexIndices){
            newPath.addToSet(inputPath.getDataAtIndex(index));
        }
        return newPath;
    }
    
}
