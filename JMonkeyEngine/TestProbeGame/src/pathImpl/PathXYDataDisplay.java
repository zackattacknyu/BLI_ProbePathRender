/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pathImpl;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.io.File;
import java.util.ArrayList;
import probeTracking.ProbeDataHelper;

/**
 *
 * @author BLI
 */
public class PathXYDataDisplay {

    private ArrayList<Float> xValues,yValues;
    
    private ArrayList<Vector3f> xyDisplayValues;
    
    private PathXYDataDisplay(){
        xValues = new ArrayList<Float>(10000);
        yValues = new ArrayList<Float>(10000);
    }
    
    public static PathXYDataDisplay obtainRawProbeData(File initDir){
        File startingFile = ProbeDataHelper.importPathUsingFileSelector(initDir);
        if(startingFile == null){
            return null;
        }else{
            PathXYDataDisplay data = new PathXYDataDisplay();
            data.importFromFile(startingFile);
            data.generateDisplayValues();
            return data;
        }
    }
    
    public Spatial generateSpatial(Material mat){
        return PathHelper.createLineFromVertices(xyDisplayValues, mat);
    }
    
    private void importFromFile(File selectedFile){
        ArrayList<String> lines = ProbeDataHelper.getLinesFromFile(selectedFile);
        for(String line: lines){
            String[] parts = line.split(",");
            xValues.add(Float.parseFloat(parts[0]));
            yValues.add(Float.parseFloat(parts[1]));
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
        
    }
    
    
    
    
    
    
}
