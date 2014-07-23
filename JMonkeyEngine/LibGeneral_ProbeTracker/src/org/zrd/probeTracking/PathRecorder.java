/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTracking;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import org.zrd.geometryToolkit.geometryUtil.MiscGeometryHelper;
import org.zrd.geometryToolkit.geometryUtil.ProbeDataHelper;
import org.zrd.geometryToolkit.geometryUtil.ProgramConstants;
import org.zrd.geometryToolkit.pathTools.PathCompression;
import org.zrd.util.dataHelp.OutputHelper;
import org.zrd.util.dataWriting.ProbeDataWriter;

/**
 *
 * @author BLI
 */
public class PathRecorder {

    private ArrayList<Vector3f> vertices;
    private boolean pathSpecified = false;
    private ProbeDataWriter xyzVertexWriter;
    private ProbeDataWriter xyVertexWriter;
    private ProbeDataWriter yawPitchRollWriter;
    private Path pathRecordingFilePath;

    private float arcLengthSinceLastRead = 0;
    private Vector3f lastPosition;
    private ArrayList<Vector3f> verticesSinceLastRead;
    
    public PathRecorder(Vector3f startingPosition){
        vertices = new ArrayList<Vector3f>(100);
        verticesSinceLastRead = new ArrayList<Vector3f>(100);
        
        vertices.add(startingPosition.clone());
        verticesSinceLastRead.add(startingPosition.clone());
        
        lastPosition = startingPosition.clone();
        pathSpecified = false;
    }
    
    public PathRecorder(Vector3f startingPosition,Path pathRecordingFilePath){
        this(startingPosition);
        this.pathRecordingFilePath = pathRecordingFilePath;
        xyzVertexWriter = ProbeDataWriter.getNewWriter(
                pathRecordingFilePath, "pathVertices");
        xyVertexWriter = ProbeDataWriter.getNewWriter(
                pathRecordingFilePath, 
                "pathXYvertices");
        yawPitchRollWriter = ProbeDataWriter.getNewWriter(
                pathRecordingFilePath, "yawPitchRollData");
        pathSpecified = true;
    }

    public static String getPositionOutputText(Vector3f position){
        return OutputHelper.getPositionOutputText(
                position.getX(), 
                position.getY(), 
                position.getZ());
    }
    public static String getPositionOutputText(Vector2f position){
        return OutputHelper.getPositionOutputText(
                position.getX(), 
                position.getY());
    }
    public static String getOrientationOutputString(float yaw, float pitch, float roll){
        return OutputHelper.getOrientationOutputText(
                yaw, pitch, roll);
    }
    public ArrayList<Vector3f> getVertices() {
        return vertices;
    }
    
    public void closeRecording(){
        ProbeDataWriter.closeWriter(xyzVertexWriter);
        ProbeDataWriter.closeWriter(xyVertexWriter);
        ProbeDataWriter.closeWriter(yawPitchRollWriter);
        
        //write the compressed path
        try {
            ArrayList<Vector3f> compressedVertices = PathCompression.
                getCompressedPath(vertices,ProgramConstants.MIN_SEGMENT_LENGTH);
            Path compressedPathFile = ProbeDataWriter.getNewDataFilePath(pathRecordingFilePath, "pathVerticesCompressed");
            ProbeDataHelper.writeVerticesToFile(compressedVertices, compressedPathFile);
        } catch (IOException ex) {
            System.out.println("Exception thrown trying to write compressed file: " + ex);
        }
    }
    
    public ArrayList<Vector3f> getMostRecentVertices(){
        arcLengthSinceLastRead = 0;
        ArrayList<Vector3f> returnVerts = MiscGeometryHelper.getCopyOfPath(verticesSinceLastRead);
        verticesSinceLastRead.clear();
        return returnVerts;
    }

    public float getArcLengthSinceLastRead() {
        return arcLengthSinceLastRead;
    }

    void addToPath(Vector3f currentPosition, 
            Vector2f currentXYPosition, 
            float currentYaw, float currentPitch, float currentRoll) {
        
        if(pathSpecified){
            
            ProbeDataWriter.writeLineInWriter(
                    xyzVertexWriter, 
                    getPositionOutputText(currentPosition));
        
            ProbeDataWriter.writeLineInWriter(xyVertexWriter, 
                    getPositionOutputText(currentXYPosition));

            ProbeDataWriter.writeLineInWriter(yawPitchRollWriter, 
                    getOrientationOutputString(currentYaw,currentPitch,currentRoll));
        }

        float segLength = currentPosition.distance(lastPosition);
        arcLengthSinceLastRead += segLength;
        lastPosition = currentPosition.clone();
        
        vertices.add(currentPosition.clone());
        verticesSinceLastRead.add(currentPosition.clone());
    }
    
    
}
