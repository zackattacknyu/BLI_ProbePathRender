/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.dataWriting;

/**
 *
 * @author BLI
 */
public class DataWriterHelper {

    
    public static String getPositionOutputText(float x, float y){
        return x + "," + y;
    }
    
    public static String getPositionOutputText(float x, float y, float z){
        return x + "," + y + "," + z;
    }
    
    public static String getOrientationOutputText(float yaw, float pitch, float roll){
        return yaw + "," + pitch + "," + roll;
    }
    
    public static String getOutputText(float x, float y, float yaw, float pitch, float roll){
        return yaw + "," + pitch + "," + roll + "," + x + "," + y;
    }
    
}
