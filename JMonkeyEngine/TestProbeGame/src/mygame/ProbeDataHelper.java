/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.cinematic.MotionPath;
import com.jme3.math.Vector3f;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Zach
 */
public class ProbeDataHelper {
    
    public static ArrayList<Vector3f> getVerticesFromFile(String fileName){
        ArrayList<String> lines = getLinesFromFile(fileName);
        ArrayList<Vector3f> lineVertices = new ArrayList<Vector3f>(lines.size());
        for(String line: lines){
            lineVertices.add(lineToWayPoint(line));
        }
        
        return lineVertices;
    }
    
    public static MotionPath getMotionPathFromVertices(ArrayList<Vector3f> lineVertices){
        MotionPath path = new MotionPath();
        for(Vector3f vertex: lineVertices){
            path.addWayPoint(vertex);
        }
        return path;
    }
    
    public static ArrayList<String> getLinesFromFile(String fileName){
        Path sampleDataFile = Paths.get("textFiles/sampleData.txt");
        ArrayList<String> lines = new ArrayList<String>();
        try {
            lines = (ArrayList<String>) Files.readAllLines(sampleDataFile, 
                    StandardCharsets.UTF_8);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }
    
    /**
     * This converts the line of probe data into a way point
     * @param line
     * @return 
     */
    public static Vector3f lineToWayPoint(String line){
        String[] parts = line.split(",");
        Float xPart = Float.valueOf(parts[5]);
        Float yPart = Float.valueOf(parts[6]);
        return new Vector3f(xPart,yPart,22.8080f);
    }
}
