/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.pathDataStructure;

import com.jme3.math.Vector3f;
import java.util.ArrayList;
import org.zrd.util.dataHelp.OutputHelper;

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
    
    public static Vector3f getFirstSegment(ArrayList<Vector3f> pathVertices){
        SegmentSet set = new SegmentSet(pathVertices);
        return set.getSegmentVectors().get(0);
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
    
    /**
     * This returns the Euclidean distance between the start and end points
     *      of the path
     * @return 
     */
    public float getStartToEndDistance(){
        return pathVertices.get(0).distance(pathVertices.get(pathVertices.size()-1));
    }
    
    public ArrayList<String> getResultStrings(){
        ArrayList<String> resultStrings = new ArrayList<String>(10);
        resultStrings.add(OutputHelper.EMPTY_LINE_STRING);
        resultStrings.add("Arc Length of Recorded Path: ");
        resultStrings.add(String.valueOf(arcLength));
        resultStrings.add(OutputHelper.EMPTY_LINE_STRING);
        resultStrings.add("Length between start and end points");
        resultStrings.add(String.valueOf(getStartToEndDistance()));
        resultStrings.add(OutputHelper.EMPTY_LINE_STRING);
        return resultStrings;
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
