/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.locationTracking;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.util.dataHelp.DataArrayToStringConversion;
import org.zrd.util.dataStreaming.ThreadedOutput;

/**
 *
 * @author BLI
 */
public interface LocationTracker {
    
    String[] getCurrentDataStrings();
    
    void setDataArrayToStringConvertor(DataArrayToStringConversion converter);
    
    float getTrackingQuality();
    String getStreamingOutput();
    void setOutputStreaming(ThreadedOutput outputStreaming);
    
    Vector3f getCurrentPosition();
    Vector3f getCurrentDisplacement();
    Vector3f getDisplacementSinceLastPoint();
    void setCurrentTriangle(MeshTriangle triangle);
    void setCurrentPosition(Vector3f position);
    void resetDisplacementSinceLastPoint();
    void updateData();
    
    String getXYZtext();
    String getYawPitchRollText();
    
    boolean isRecordingPath();
    
    Quaternion getLocalRotation();
    
    ArrayList<Vector3f> getVerticesSinceLastRead();
    float getArcLengthSinceLastRead();

    void startStopRecording();
    boolean isNewPathExists();
    ArrayList<Vector3f> getCurrentPathVertices();
    
    Vector3f getTrackerNormal();
    Vector3f getTrackerX();
    Vector3f getTrackerY();
}
