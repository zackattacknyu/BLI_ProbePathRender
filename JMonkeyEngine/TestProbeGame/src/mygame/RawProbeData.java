/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.io.File;
import java.util.ArrayList;
import render.ObjectHelper;

/**
 *
 * @author BLI
 */
public class RawProbeData {

    private ArrayList<Float> xValues,yValues,yawValues,pitchValues,rollValues;
    
    private ArrayList<Vector3f> xyDisplayValues;
    private ArrayList<Vector3f> yawPitchRollDisplayValues;
    
    private RawProbeData(){
        xValues = new ArrayList<Float>(10000);
        yValues = new ArrayList<Float>(10000);
        yawValues = new ArrayList<Float>(10000);
        pitchValues = new ArrayList<Float>(10000);
        rollValues = new ArrayList<Float>(10000);
    }
    
    public static RawProbeData obtainRawProbeData(File initDir){
        File startingFile = ProbeDataHelper.importPathUsingFileSelector(initDir);
        if(startingFile == null){
            return null;
        }else{
            RawProbeData data = new RawProbeData();
            data.importFromFile(startingFile);
            data.generateDisplayValues();
            return data;
        }
    }
    
    public Node generateSpatialNode(Material lineMat){
        Node rawData = new Node();
        Spatial xyLine = ObjectHelper.createLineFromVertices(xyDisplayValues, lineMat);
        Spatial yawPitchRollLine = ObjectHelper.createLineFromVertices(yawPitchRollDisplayValues, lineMat);
        rawData.attachChild(xyLine);
        rawData.attachChild(yawPitchRollLine);
        return rawData;
        
    }
    
    private void importFromFile(File selectedFile){
        ArrayList<String> lines = ProbeDataHelper.getLinesFromFile(selectedFile);
        for(String line: lines){
            String[] parts = line.split(",");
            xValues.add(Float.parseFloat(parts[1]));
            yValues.add(Float.parseFloat(parts[2]));
            yawValues.add(Float.parseFloat(parts[3]));
            pitchValues.add(Float.parseFloat(parts[4]));
            rollValues.add(Float.parseFloat(parts[5]));
        }
    }
    
    private void generateDisplayValues(){
        
        float currentX = 0.0f;
        float currentY = 0.0f;
        float constantZ = 0.0f;
        
        xyDisplayValues = new ArrayList<Vector3f>(10000);
        for(int index = 0; index < xValues.size(); index++){
            
            currentX = currentX + (0.00002f)*xValues.get(index);
            currentY = currentY + (0.00002f)*yValues.get(index);
            
            xyDisplayValues.add(new Vector3f(currentX, currentY, constantZ));
            
        }
        
        yawPitchRollDisplayValues = new ArrayList<Vector3f>(10000);
        Quaternion currentQuat;
        for(int index = 0; index < yawValues.size(); index++){
            currentQuat = TrackingHelper.getQuarternion(
                    yawValues.get(index), 
                    pitchValues.get(index), 
                    rollValues.get(index));
            yawPitchRollDisplayValues.add(new Vector3f(
                    currentQuat.getX(),
                    currentQuat.getY(),
                    currentQuat.getZ()));
        }
        
    }
    
    
    
    
    
    
}
