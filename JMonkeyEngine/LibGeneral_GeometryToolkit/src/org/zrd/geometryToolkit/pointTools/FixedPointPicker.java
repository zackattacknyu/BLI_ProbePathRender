/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.pointTools;

import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 
 * This class is initialized with a set of points.
 *      When a given point is inputted, it tells which
 *      of the original points it is closest to.
 * Right now, this class just does a simple loop
 *      through all the points but a spatial data
 *      structure may be necessary if the number
 *      of input points grows. 
 *
 * @author BLI
 */
public class FixedPointPicker {
    
    //the data with the input points
    private ArrayList<PointData> inputPointData;
    
    public FixedPointPicker(Collection<PointData> inputPtData){
        inputPointData = new ArrayList<PointData>();
        inputPointData.addAll(inputPtData);
    }
    
    /**
     * This is the code that goes through the input points
     *      and returns the data with the nearest one
     * @param inputPoint    input point
     * @return              nearest point to input point
     */
    public PointData getNearestPointData(Vector3f inputPoint){
        
        float minDistance = Float.MAX_VALUE;
        int minIndex = 0;
        int currIndex = 0;
        float currentDist;
        for(PointData fixedPtData: inputPointData){
            currentDist = fixedPtData.getPointCoords().clone().distance(inputPoint.clone());
            if(currentDist < minDistance){
                minDistance = currentDist;
                minIndex = currIndex;
            }
            currIndex++;
        }
        
        return inputPointData.get(minIndex);
    }
    
    
    
}
