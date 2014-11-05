/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.geometryUtil;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import org.zrd.geometryToolkit.pathDataStructure.SegmentSet;
import org.zrd.util.dataHelp.DataArrayToStringConversion;
import org.zrd.util.dataHelp.OutputHelper;
import org.zrd.util.dataHelp.SignalDataProcessor;
import org.zrd.util.fileHelper.FileDataHelper;
import org.zrd.util.fileHelper.GeneralFileHelper;

/**
 *
 * @author Zach
 */
public class GeometryDataHelper {
    
    public static final Vector2f getBadTexCoord(){
        return new Vector2f(Float.NaN,Float.NaN);
    }
    
    public static void writeTexCoordToDefaultOutputFile(ArrayList<Vector2f> texCoords){
        writeTexCoordToOutputFile(texCoords,"textureCoordinateOutput");
    }
    public static void writeTexCoordToOutputFile(ArrayList<Vector2f> texCoords, String outputFileName){
        writeTexCoordsToFile(texCoords,GeneralFileHelper.getNewOutputFilePath(outputFileName));
    }
    
    public static void writeTexCoordsToFile(ArrayList<Vector2f> texCoords, Path filePath){
        ArrayList<String> dataStrings = new ArrayList<String>(texCoords.size());
        String vertexPart;
        Vector2f vertex;
        for(int index = 0; index < texCoords.size(); index++){
            vertex = texCoords.get(index);
            vertexPart = OutputHelper.getPositionOutputText(vertex.getX(), vertex.getY());
            dataStrings.add(vertexPart);
        }
        FileDataHelper.exportLinesToFile(dataStrings, filePath);
    }
    
    public static void writeVerticesToFile(ArrayList<Vector3f> vertices, Path filePath){
        writeSegmentSetInfoToFile(new SegmentSet(vertices),null,filePath);
    }
    
    public static void writeSegmentSetInfoToFile(SegmentSet segments, DataArrayToStringConversion converter, Path filePath){
        ArrayList<String> dataStrings = new ArrayList<String>(segments.getSize());
        String signalInfoPart = "";
        String timePart;
        String vertexPart;
        String[] signalData;
        Vector3f vertex;
        for(int index = 0; index < segments.getSize(); index++){
            vertex = segments.getDataAtIndex(index).getVertex();
            vertexPart = OutputHelper.getPositionOutputText(vertex.getX(), vertex.getY(), vertex.getZ());
            
            if(converter != null){
                signalData = segments.getDataAtIndex(index).getData();
                signalInfoPart = converter.getTextFileStringFromData(signalData);
            }
            if(index % 20 == 0){
                System.out.println("Finished converting point " + (index+1) + " of " + segments.getSize());
            }
            timePart = String.valueOf(segments.getDataAtIndex(index).getTimestamp());
            dataStrings.add(vertexPart + "," + signalInfoPart + "," + timePart);
        }
        FileDataHelper.exportLinesToFile(dataStrings, filePath);
    }

    public static ArrayList<Vector3f> getVerticesFromFile(File dataFile){
        ArrayList<String> lines = FileDataHelper.getLinesFromFile(dataFile);
        ArrayList<Vector3f> lineVertices = new ArrayList<Vector3f>(lines.size());
        String previousLine = "";
        for(String line: lines){
            if(!line.equals(previousLine)){
                lineVertices.add(getVertexFromLine(line));
            }
            previousLine = line;
        }
        
        return lineVertices;
    }
    
    public static ArrayList<Vector2f> get2DVerticesFromFile(File dataFile){
        ArrayList<String> lines = FileDataHelper.getLinesFromFile(dataFile);
        ArrayList<Vector2f> lineVertices = new ArrayList<Vector2f>(lines.size());
        String previousLine = "";
        for(String line: lines){
            if(!line.equals(previousLine)){
                lineVertices.add(get2DVertexFromLine(line));
            }
            previousLine = line;
        }
        
        return lineVertices;
    }
    
    public static SegmentSet getSegmentSetFromFile(File dataFile){
        ArrayList<String> lines = FileDataHelper.getLinesFromFile(dataFile);
        ArrayList<Vector3f> lineVertices = new ArrayList<Vector3f>(lines.size());
        ArrayList<String[]> dataAtLines = new ArrayList<String[]>(lines.size());
        
        int numWaves = 2;
        int waveformSize = 100;
        int dataIndexStart = 3;
        SignalDataProcessor dataProcesor = new SignalDataProcessor(numWaves, waveformSize, dataIndexStart);
        
        String previousLine = "";
        for(String line: lines){
            if(!line.equals(previousLine)){
                lineVertices.add(getVertexFromLine(line));
                dataAtLines.add(dataProcesor.getDataFromRawString(line));
            }
            previousLine = line;
        }
        
        return new SegmentSet(lineVertices,dataAtLines);
    }
    
    public static Vector2f get2DVertexFromLine(String line){
        String[] parts = line.split(",");
        Float xPart = Float.valueOf(parts[0]);
        Float yPart = Float.valueOf(parts[1]);
        return new Vector2f(xPart,yPart);
    }
    
    public static Vector3f getVertexFromLine(String line){
        String[] parts = line.split(",");
        Float xPart = Float.valueOf(parts[0]);
        Float yPart = Float.valueOf(parts[1]);
        Float zPart = Float.valueOf(parts[2]);
        return new Vector3f(xPart,yPart,zPart);
    }
}
