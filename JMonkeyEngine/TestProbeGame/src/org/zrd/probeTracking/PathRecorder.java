/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTracking;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.zrd.util.dataWriting.ProbeDataWriter;

/**
 *
 * @author BLI
 */
public class PathRecorder {

    private ArrayList<Vector3f> vertices;
    private ProbeDataWriter xyzVertexWriter;
    private ProbeDataWriter xyVertexWriter;
    private ProbeDataWriter yawPitchRollWriter;
    
    public PathRecorder(Vector3f startingPosition){
        vertices = new ArrayList<Vector3f>(100);
        Path pathRecordingFilePath = Paths.get("textFiles").
                resolve("logs").resolve("paths");
        xyzVertexWriter = ProbeDataWriter.getNewWriter(
                pathRecordingFilePath, "pathVertices");
        xyVertexWriter = ProbeDataWriter.getNewWriter(
                pathRecordingFilePath, 
                "pathXYvertices");
        yawPitchRollWriter = ProbeDataWriter.getNewWriter(
                pathRecordingFilePath, "yawPitchRollData");
        vertices.add(startingPosition.clone());
    }

    public static String getPositionOutputText(Vector3f position){
        return position.getX() + "," + position.getY() + "," + position.getZ();
    }
    public static String getPositionOutputText(Vector2f position){
        return position.getX() + "," + position.getY();
    }
    public static String getOrientationOutputString(float yaw, float pitch, float roll){
        return yaw + "," + pitch + "," + roll;
    }
    public ArrayList<Vector3f> getVertices() {
        return vertices;
    }
    
    public void closeRecording(){
        ProbeDataWriter.closeWriter(xyzVertexWriter);
    }

    void addToPath(Vector3f currentPosition, 
            Vector2f currentXYPosition, 
            float currentYaw, float currentPitch, float currentRoll) {
        
        ProbeDataWriter.writeLineInWriter(
                    xyzVertexWriter, 
                    getPositionOutputText(currentPosition));
        
        ProbeDataWriter.writeLineInWriter(xyVertexWriter, 
                getPositionOutputText(currentXYPosition));
        
        ProbeDataWriter.writeLineInWriter(yawPitchRollWriter, 
                getOrientationOutputString(currentYaw,currentPitch,currentRoll));
        
        vertices.add(currentPosition.clone());
    }
    
    
}
