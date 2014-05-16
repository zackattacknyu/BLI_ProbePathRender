/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.cinematic.MotionPath;
import com.jme3.math.Vector3f;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Zach
 */
public class ProbeDataHelper {

    public static ArrayList<Vector3f> getVerticesFromFile(File dataFile){
        ArrayList<String> lines = getLinesFromFile(dataFile);
        ArrayList<Vector3f> lineVertices = new ArrayList<Vector3f>(lines.size());
        String previousLine = "";
        for(String line: lines){
            if(!line.equals(previousLine)){
                lineVertices.add(getVertexFromLine(line));
            }
            line = previousLine;
        }
        
        return lineVertices;
    }
    
    public static ArrayList<String> getLinesFromFile(File dataFile){
        ArrayList<String> lines = new ArrayList<String>();
        try {
            lines = (ArrayList<String>) 
                    Files.readAllLines(dataFile.toPath(),StandardCharsets.UTF_8);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return lines;
    }
    
    public static Vector3f getVertexFromLine(String line){
        String[] parts = line.split(",");
        Float xPart = Float.valueOf(parts[0]);
        Float yPart = Float.valueOf(parts[1]);
        Float zPart = Float.valueOf(parts[2]);
        return new Vector3f(xPart,yPart,zPart);
    }

    public static String getTimestampSuffix() {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy_MM_dd__kk_mm_ss");
        Calendar rightNow = Calendar.getInstance();
        return myFormat.format(rightNow.getTime());
    }
}
