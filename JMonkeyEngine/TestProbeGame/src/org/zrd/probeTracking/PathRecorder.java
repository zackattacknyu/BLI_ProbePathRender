/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTracking;

import com.jme3.math.Vector3f;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import static org.zrd.probeTracking.ProbeTracker.getPositionOutputText;
import org.zrd.util.dataWriting.ProbeDataWriter;

/**
 *
 * @author BLI
 */
public class PathRecorder {

    private ArrayList<Vector3f> vertices;
    private ProbeDataWriter xyzVertexWriter;
    
    public PathRecorder(Vector3f startingPosition){
        vertices = new ArrayList<Vector3f>(100);
        Path pathRecordingFilePath = Paths.get("textFiles").
                resolve("logs").resolve("paths");
        xyzVertexWriter = ProbeDataWriter.getNewWriter(
                pathRecordingFilePath, "pathVertices");
        vertices.add(startingPosition.clone());
    }
    
    public void addToPath(Vector3f vertex){
        ProbeDataWriter.writeLineInWriter(
                    xyzVertexWriter, 
                    getPositionOutputText(vertex));
        vertices.add(vertex.clone());
    }
    
    public ArrayList<Vector3f> getVertices() {
        return vertices;
    }
    
    public void closeRecording(){
        ProbeDataWriter.closeWriter(xyzVertexWriter);
    }
    
    
}
