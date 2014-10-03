/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.pathDataStructure;

import com.jme3.math.Vector3f;
import java.util.ArrayList;
import org.zrd.geometryToolkit.pathTools.PathHelper;
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
    private ArrayList<String[]> dataAtVertices;
    
    /**
     * This is the same path, but with a start point
     *      and vectors for each of the following segments
     */
    private Vector3f startPoint;
    private ArrayList<Vector3f> segmentVectors;
    
    private ArrayList<Long> timestamps;

    public ArrayList<Long> getTimestamps() {
        return timestamps;
    }
    
    //arc length of segment
    private float arcLength = 0;
    
    public SegmentSet(int estimatedSize){
        pathVertices = new ArrayList<Vector3f>(estimatedSize);
        dataAtVertices = new ArrayList<String[]>(estimatedSize);
        timestamps = new ArrayList<Long>(estimatedSize);
    }
    public void addToSet(Vector3f vertex){
        pathVertices.add(vertex);
    }
    public void addToSet(Vector3f vertex,String[] data,long timestamp){
        pathVertices.add(vertex);
        dataAtVertices.add(data);
        timestamps.add(timestamp);
    }
    public void addToSet(long timestamp){
        timestamps.add(timestamp);
    }
    public void addToSet(String[] data){
        dataAtVertices.add(data);
    }
    public void addToSet(SegmentData data){
        addToSet(data.getVertex(),data.getData(),data.getTimestamp());
    }
    public void finalizeSegment(){
        constructSegmentList();
    }
    public int getSize(){
        return pathVertices.size();
    }
    public SegmentData getDataAtIndex(int index){
        if(dataAtVertices != null && dataAtVertices.size() > 0){
            
            if(timestamps != null && timestamps.size() > 0){
                return new SegmentData(pathVertices.get(index),dataAtVertices.get(index),timestamps.get(index));
            }else{
                return new SegmentData(pathVertices.get(index),dataAtVertices.get(index));
            }
        }
        else{
            return new SegmentData(pathVertices.get(index),null);
        }
    }
    
    public SegmentSet(ArrayList<Vector3f> pathVertices){
        this.pathVertices = pathVertices;
        timestamps = new ArrayList<Long>(pathVertices.size());
        constructSegmentList();
    }
    
    public SegmentSet(ArrayList<Vector3f> pathVertices, ArrayList<String[]> dataAtVertices){
        this(pathVertices);
        this.dataAtVertices = dataAtVertices;
    }
    
    public static Vector3f getStartToEndUnitVector(ArrayList<Vector3f> pathVertices){
        return getStartToEndVector(pathVertices).clone().normalize();
    }
    
    public static Vector3f getStartToEndVector(ArrayList<Vector3f> pathVertices){
        return PathHelper.getLastPoint(pathVertices).subtract(PathHelper.getFirstPoint(pathVertices));
    }
    
    public static Vector3f getFirstSegmentUnitVector(ArrayList<Vector3f> pathVertices){
        return getFirstSegment(pathVertices).normalize();
    }
    
    public static Vector3f getFirstSegment(ArrayList<Vector3f> pathVertices){
        return PathHelper.getSecondPoint(pathVertices).subtract(PathHelper.getFirstPoint(pathVertices));
    }
    
    /**
     * This returns the Euclidean distance between the start and end points
     *      of the path
     * @return 
     */
    public static float getStartToEndDistance(ArrayList<Vector3f> pathVertices){
        return getStartToEndVector(pathVertices).length();
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
    
    
    
    public ArrayList<String> getResultStrings(){
        ArrayList<String> resultStrings = new ArrayList<String>(10);
        resultStrings.add(OutputHelper.EMPTY_LINE_STRING);
        resultStrings.add("Arc Length of Recorded Path: ");
        resultStrings.add(String.valueOf(arcLength));
        resultStrings.add(OutputHelper.EMPTY_LINE_STRING);
        resultStrings.add("Length between start and end points");
        resultStrings.add(String.valueOf(getStartToEndDistance(pathVertices)));
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

    public ArrayList<String[]> getDataAtVertices() {
        return dataAtVertices;
    }
    
    
    
    
}
