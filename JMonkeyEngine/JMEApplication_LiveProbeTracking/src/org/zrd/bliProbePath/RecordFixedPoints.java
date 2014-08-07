/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.bliProbePath;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.math.Vector3f;
import java.nio.file.Path;
import java.util.ArrayList;
import org.zrd.geometryToolkit.geometryUtil.GeometryDataHelper;
import org.zrd.geometryToolkit.locationTracking.LocationTracker;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;
import org.zrd.util.dataWriting.ProbeDataWriter;
import org.zrd.util.fileHelper.GeneralFileHelper;

/**
 *This records fixed points. If the user presses T, then
 *      the recording starts. 
 * If they hit T after that, the point there gets recorded
 * This continues until T is hit twice for the same point, at which
 *      time the text file gets written.
 * 
 * @author BLI
 */
public class RecordFixedPoints extends GeneralKeyboardActionMethod{
    
    private Vector3f lastRecordedPt;
    private Vector3f currentPt;
    private ArrayList<Vector3f> currentPtsRecording;
    private LocationTracker locTracker;
    private boolean recording = false;
    private Path dataRecordingPath;
    
    public RecordFixedPoints(InputManager inputManager, LocationTracker locTracker, Path dataRecordingPath){
        super(inputManager,"recordFixedPts",KeyInput.KEY_T);
        this.locTracker = locTracker;
        this.dataRecordingPath = dataRecordingPath;
        currentPtsRecording = new ArrayList<Vector3f>();
    }
    
    @Override
    public void actionMethod() {
        currentPt = locTracker.getCurrentPosition();
        
        if(recording){
            
            /*
             * We are recording so check if the point matches the last point,
             *      meaning that T was pressed twice indicating the recording
             *      is done. If they don't match, then put the point
             *      in the array list of points. 
             */
            if(currentPt.equals(lastRecordedPt)){
                GeometryDataHelper.writeVerticesToDataFile(currentPtsRecording, 
                        dataRecordingPath, "fixedPoints");
                resetRecording();
                
            }else{
                addCurrentPoint();
            }
            
        }else{
            
            //we are now recording and put the first point in the array list
            addCurrentPoint();
            recording = true;
            
        }
    }
    
    private void resetRecording(){
        currentPtsRecording.clear();
        recording = false;
    }
    
    private void addCurrentPoint(){
        currentPtsRecording.add(currentPt);
        lastRecordedPt = currentPt.clone();
    }
    
}
