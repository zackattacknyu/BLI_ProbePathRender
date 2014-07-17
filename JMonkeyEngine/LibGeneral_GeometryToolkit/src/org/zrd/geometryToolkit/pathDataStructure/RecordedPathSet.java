/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.pathDataStructure;

import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 *
 * @author BLI
 */
public class RecordedPathSet {
    
    private ArrayList<SegmentSet> pathSet;
    private SegmentSet currentSegment;
    
    public RecordedPathSet(){
        pathSet = new ArrayList<SegmentSet>();
    }
    
    public void addPath(ArrayList<Vector3f> path){
        currentSegment = new SegmentSet(path);
        pathSet.add(currentSegment);
    }
    
    public ArrayList<Vector3f> getCurrentPath(){
        return currentSegment.getPathVertices();
    }
    
}
