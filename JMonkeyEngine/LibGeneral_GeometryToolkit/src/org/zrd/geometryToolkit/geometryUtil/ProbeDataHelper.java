/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.geometryUtil;

import com.jme3.math.Vector3f;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import org.zrd.util.dataHelp.OutputHelper;
import org.zrd.util.fileHelper.FileDataHelper;

/**
 *
 * @author Zach
 */
public class ProbeDataHelper {
    
    public static void writeVerticesToFile(ArrayList<Vector3f> vertices, Path filePath){
        ArrayList<String> vertexStrings = new ArrayList<String>(vertices.size());
        for(Vector3f vertex: vertices){
            vertexStrings.add(OutputHelper.getPositionOutputText(
                    vertex.getX(), vertex.getY(), vertex.getZ()));
        }
        FileDataHelper.exportLinesToFile(vertexStrings, filePath);
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
    
    public static Vector3f getVertexFromLine(String line){
        String[] parts = line.split(",");
        Float xPart = Float.valueOf(parts[0]);
        Float yPart = Float.valueOf(parts[1]);
        Float zPart = Float.valueOf(parts[2]);
        return new Vector3f(xPart,yPart,zPart);
    }
}
