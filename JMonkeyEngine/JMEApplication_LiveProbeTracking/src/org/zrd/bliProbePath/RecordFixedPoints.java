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
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.pointTools.PointsOnMeshTracker;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;

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
    private PointsOnMeshTracker ptsMeshTracker;
    private boolean recording = false;
    private Path dataRecordingPath;
    
    public RecordFixedPoints(InputManager inputManager, PointsOnMeshTracker ptsMeshTracker, Path dataRecordingPath){
        super(inputManager,"recordFixedPts",KeyInput.KEY_T);
        this.ptsMeshTracker = ptsMeshTracker;
        this.dataRecordingPath = dataRecordingPath;
        currentPtsRecording = new ArrayList<Vector3f>();
    }
    
    @Override
    public void actionMethod() {
        currentPt = ptsMeshTracker.getCurrentPointOnMesh();
        
        if(currentPt == null) return;
        
        if(recording){
            
            /*
             * We are recording so check if the point matches the last point,
             *      meaning that T was pressed twice indicating the recording
             *      is done. If they don't match, then put the point
             *      in the array list of points. 
             */
            if(currentPt.equals(lastRecordedPt)){
                
                writeVerticesAndReset();
                
            }else{
                addCurrentPoint();
            }
            
        }else{
            
            //we are now recording and put the first point in the array list
            System.out.println("Now recording points");
            addCurrentPoint();
            recording = true;
            
        }
    }
    
    private void writeVerticesAndReset(){
        System.out.println("Now putting vertices into text file");
        GeometryDataHelper.writeVerticesToDataFile(currentPtsRecording, 
                dataRecordingPath, "fixedPoints");
        resetRecording();
        System.out.println("Finished adding vertices to text file");
    }
    
    private void resetRecording(){
        currentPtsRecording.clear();
        recording = false;
    }
    
    private void addCurrentPoint(){

        MeshTriangle currentTri = ptsMeshTracker.getCurrentTriangleOnMesh();
        
        if(currentTri == null){
            
            System.out.println("Not on Mesh yet. Point Not recorded");
            
        }else{
            
            currentPtsRecording.add(currentPt);
            System.out.println("Point: " + currentPt + " has been added to list");
            currentPtsRecording.add(currentTri.getVertex1().getVertex());
            currentPtsRecording.add(currentTri.getVertex2().getVertex());
            currentPtsRecording.add(currentTri.getVertex3().getVertex());
            lastRecordedPt = currentPt.clone();
            
        }

        
    }
    
}
