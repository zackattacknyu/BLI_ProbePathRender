/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTracking;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.nio.file.Path;
import java.util.ArrayList;
import org.zrd.geometryToolkit.geometryUtil.GeometryDataHelper;
import org.zrd.geometryToolkit.pathDataStructure.SegmentSet;
import org.zrd.geometryToolkit.pathTools.PathCompression;
import org.zrd.geometryToolkit.pathTools.PathHelper;
import org.zrd.util.dataHelp.DataArrayToStringConversion;
import org.zrd.util.dataHelp.OutputHelper;
import org.zrd.util.dataWriting.ProbeDataWriter;
import org.zrd.util.dataWriting.TimeHelper;
import org.zrd.util.fileHelper.FileDataHelper;
import org.zrd.util.fileHelper.GeneralFileHelper;

/**
 *
 * @author BLI
 */
public class PathRecorder {

    private SegmentSet pathInformation;
    private boolean pathSpecified = false;
    private ProbeDataWriter xyzVertexWriter;
    private ProbeDataWriter xyVertexWriter;
    private ProbeDataWriter yawPitchRollWriter;
    private ProbeDataWriter xyzSignalWriter;
    private ProbeDataWriter xyzSignalDataWriter;
    private Path pathRecordingFilePath;
    
    private DataArrayToStringConversion convertor;

    private float arcLengthSinceLastRead = 0;
    private Vector3f lastPosition;
    private ArrayList<Vector3f> verticesSinceLastRead;
    private String timestampSuffix;
    
    private String pathVertexFilePrefix;
    private String compressedPathFilePrefix;
    private String compressedPathInfoFilePrefix;
    
    
    //whether or not we are recording the path that is explictly following the mesh
    private boolean pathIsOnMesh = false;
    
    public PathRecorder(Vector3f startingPosition){
        pathInformation = new SegmentSet(100);
        verticesSinceLastRead = new ArrayList<Vector3f>(100);
        
        pathInformation.addToSet(startingPosition.clone());
        verticesSinceLastRead.add(startingPosition.clone());
        
        lastPosition = startingPosition.clone();
        pathSpecified = false;
    }
    
    public PathRecorder(Vector3f startingPosition,Path pathRecordingFilePath, boolean pathIsOnMesh){
        this(startingPosition);
        this.pathRecordingFilePath = pathRecordingFilePath;
        this.timestampSuffix = TimeHelper.getTimestampSuffix();
        this.pathIsOnMesh = pathIsOnMesh;
        
        setFilePrefixes();
        
        xyzVertexWriter = ProbeDataWriter.getNewWriter(
                pathRecordingFilePath, timestampSuffix,pathVertexFilePrefix);
        xyzSignalWriter = ProbeDataWriter.getNewWriter(
                pathRecordingFilePath, 
                timestampSuffix, "xyzVerticesAndSignalData");
        
        if(!pathIsOnMesh){
            xyVertexWriter = ProbeDataWriter.getNewWriter(
                pathRecordingFilePath, timestampSuffix,
                "pathXYvertices");
            yawPitchRollWriter = ProbeDataWriter.getNewWriter(
                    pathRecordingFilePath, timestampSuffix, "yawPitchRollData");
        }
        
        pathSpecified = true;
    }
    
    public PathRecorder(Vector3f startingPosition,Path pathRecordingFilePath, 
            boolean pathIsOnMesh, DataArrayToStringConversion convertor){
        this(startingPosition,pathRecordingFilePath,pathIsOnMesh);
        this.convertor = convertor;
        xyzSignalDataWriter = ProbeDataWriter.getNewWriter(
                pathRecordingFilePath, 
                timestampSuffix, "xyzVerticesAndSignalInfo");
        
    }
    
    private void setFilePrefixes(){
        pathVertexFilePrefix = pathIsOnMesh ? 
                "pathOnMeshVertices" : "pathVertices";
        compressedPathFilePrefix = pathIsOnMesh ? 
                "compressedPathOnMeshVertices" : "compressedPathVertices";
        compressedPathInfoFilePrefix = pathIsOnMesh ? 
                "compressedPathOnMeshInfo" : "compressedPathInfo";
    }
    
    public PathRecorder(Vector3f startingPosition,Path pathRecordingFilePath){
        this(startingPosition,pathRecordingFilePath,false);
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
        return pathInformation.getPathVertices();
    }
    
    public void closeRecording(){
        ProbeDataWriter.closeWriter(xyzVertexWriter);
        ProbeDataWriter.closeWriter(xyzSignalWriter);
        ProbeDataWriter.closeWriter(xyzSignalDataWriter);
        if(!pathIsOnMesh){
            ProbeDataWriter.closeWriter(xyVertexWriter);
            ProbeDataWriter.closeWriter(yawPitchRollWriter);
        }
        pathInformation.finalizeSegment();
        
        //write the compressed path
        ArrayList<Vector3f> compressedVertices = PathCompression.
            getCompressedPath(pathInformation.getPathVertices(),PathHelper.MIN_SEGMENT_LENGTH);
        Path compressedPathFile = GeneralFileHelper.getNewDataFilePath(
                pathRecordingFilePath,timestampSuffix, compressedPathFilePrefix);
        GeometryDataHelper.writeVerticesToFile(compressedVertices, compressedPathFile);
        
        //write the path arc length
        SegmentSet recordedPath = new SegmentSet(compressedVertices);
        Path recordedPathStats = GeneralFileHelper.getNewDataFilePath(
                pathRecordingFilePath,timestampSuffix, compressedPathInfoFilePrefix);
        FileDataHelper.exportLinesToFile(recordedPath.getResultStrings(), recordedPathStats);
        OutputHelper.printStringCollection(recordedPath.getResultStrings());
    }
    
    public ArrayList<Vector3f> getMostRecentVertices(){
        arcLengthSinceLastRead = 0;
        ArrayList<Vector3f> returnVerts = PathHelper.getCopyOfPath(verticesSinceLastRead);
        verticesSinceLastRead.clear();
        return returnVerts;
    }

    public float getArcLengthSinceLastRead() {
        return arcLengthSinceLastRead;
    }
    
    void addToPath(Vector3f currentPosition){
        
        if(pathSpecified){
            
            ProbeDataWriter.writeLineInWriter(
                    xyzVertexWriter, 
                    getPositionOutputText(currentPosition));
            
        }

        float segLength = currentPosition.distance(lastPosition);
        arcLengthSinceLastRead += segLength;
        lastPosition = currentPosition.clone();
        
        pathInformation.addToSet(currentPosition.clone());
        verticesSinceLastRead.add(currentPosition.clone());
    }
    
    void addToPath(String[] signalData, Vector3f currentPosition, 
            Vector2f currentXYPosition, 
            float currentYaw, float currentPitch, float currentRoll){
        addToPath(currentPosition,currentXYPosition,currentYaw,currentPitch,currentRoll);
        
        String vertexPart = getPositionOutputText(currentPosition);
        StringBuilder signalPart = new StringBuilder(signalData.length*5);
        for(String entry: signalData){
            signalPart.append(",");
            signalPart.append(entry);
        }
        
        if(convertor != null){
            //String signalInfoPart = convertor.getTextFileStringFromData(signalData);
            //ProbeDataWriter.writeLineInWriter(xyzSignalDataWriter, vertexPart + "," + signalInfoPart);
        }
        
        ProbeDataWriter.writeLineInWriter(xyzSignalWriter, vertexPart + signalPart);
        
    }

    void addToPath(Vector3f currentPosition, 
            Vector2f currentXYPosition, 
            float currentYaw, float currentPitch, float currentRoll) {
        
        addToPath(currentPosition);
        
        if(pathSpecified){
        
            if(!pathIsOnMesh){
                
                ProbeDataWriter.writeLineInWriter(xyVertexWriter, 
                    getPositionOutputText(currentXYPosition));

                ProbeDataWriter.writeLineInWriter(yawPitchRollWriter, 
                        getOrientationOutputString(currentYaw,currentPitch,currentRoll));
            }
            
        }
    }
    
    
}
