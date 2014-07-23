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
public class SegmentSet {
    
    /**
     * This is the x,y,z vertices of the path
     */
    private ArrayList<Vector3f> pathVertices;
    
    /**
     * This says the data at each point
     * Each segment will be rendered with the data
     *      at the starting point of the segment
     */
    private ArrayList<Float> dataAtVertices;
    
    /**
     * This is the same path, but with a start point
     *      and vectors for each of the following segments
     */
    private Vector3f startPoint;
    private ArrayList<Vector3f> segmentVectors;
    
    //arc length of segment
    private float arcLength = 0;
    
    public SegmentSet(ArrayList<Vector3f> pathVertices){
        this.pathVertices = pathVertices;
        constructSegmentList();
    }
    
    public SegmentSet(ArrayList<Vector3f> pathVertices, ArrayList<Float> dataAtVertices){
        this(pathVertices);
        this.dataAtVertices = dataAtVertices;
    }
    
    private void constructSegmentList(){
        //gets the diff vector array
        segmentVectors = new ArrayList<Vector3f>(pathVertices.size());
        Vector3f diffVector;
        for(int index = 1; index < pathVertices.size(); index++){
            diffVector = pathVertices.get(index).subtract(pathVertices.get(index-1));
            segmentVectors.add(diffVector);
            arcLength = arcLength + diffVector.length();
        }
    }

    public float getArcLength() {
        return arcLength;
    }

    public ArrayList<Vector3f> getPathVertices() {
        return pathVertices;
    }

    public Vector3f getStartPoint() {
        return startPoint;
    }

    public ArrayList<Vector3f> getSegmentVectors() {
        return segmentVectors;
    }

    public ArrayList<Float> getDataAtVertices() {
        return dataAtVertices;
    }
    
    
    
    
}
