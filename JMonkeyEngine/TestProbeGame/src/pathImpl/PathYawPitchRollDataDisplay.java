/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pathImpl;

import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.io.File;
import java.util.ArrayList;
import probeTracking.ProbeDataHelper;
import probeTracking.TrackingHelper;

/**
 *
 * @author BLI
 */
public class PathYawPitchRollDataDisplay {

    private ArrayList<Float> yawValues,pitchValues,rollValues;

    private ArrayList<Vector3f> yawPitchRollDisplayValues;
    
    /**
     * This is used to be the start point on the sphere that will get
     *      rotated by each quaternion in the raw data.
     */
    public static final Vector3f START_POINT_ON_SPHERE = new Vector3f(1,0,0);
    
    private PathYawPitchRollDataDisplay(){
        yawValues = new ArrayList<Float>(10000);
        pitchValues = new ArrayList<Float>(10000);
        rollValues = new ArrayList<Float>(10000);
    }
    
    public static PathYawPitchRollDataDisplay obtainRawProbeData(File initDir){
        File startingFile = ProbeDataHelper.importPathUsingFileSelector(initDir);
        if(startingFile == null){
            return null;
        }else{
            PathYawPitchRollDataDisplay data = new PathYawPitchRollDataDisplay();
            data.importFromFile(startingFile);
            data.generateDisplayValues();
            return data;
        }
    }
    
    public Spatial generateSpatial(Material mat){
        return PathHelper.createLineFromVertices(yawPitchRollDisplayValues, mat);
    }
    
    private void importFromFile(File selectedFile){
        ArrayList<String> lines = ProbeDataHelper.getLinesFromFile(selectedFile);
        for(String line: lines){
            String[] parts = line.split(",");
            yawValues.add(Float.parseFloat(parts[0]));
            pitchValues.add(Float.parseFloat(parts[1]));
            rollValues.add(Float.parseFloat(parts[2]));
        }
    }
    
    private void generateDisplayValues(){
        
        yawPitchRollDisplayValues = new ArrayList<Vector3f>(10000);
        Quaternion currentQuat;
        Vector3f currentPt;
        for(int index = 0; index < yawValues.size(); index++){
            currentQuat = TrackingHelper.getQuarternion(
                    yawValues.get(index), 
                    pitchValues.get(index), 
                    rollValues.get(index));
            currentPt = currentQuat.toRotationMatrix().mult(START_POINT_ON_SPHERE);
            yawPitchRollDisplayValues.add(currentPt);
        }
        
    }
    
    
    
    
    
    
}
