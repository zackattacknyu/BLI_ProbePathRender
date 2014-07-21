/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.dataWriting;

/**
 *
 * This is meant to take in data and return the appropriate string
 *      to send to the writer of text files
 * 
 * @author BLI
 */
public class DataWriterHelper {

    
    /**
     * Given two position values, writes the string for the text file
     * @param x     the x value
     * @param y     the y value
     * @return      the string showing the values, in the format {x},{y}
     */
    public static String getPositionOutputText(float x, float y){
        return x + DataWritingConstants.DATA_IN_TEXT_FILE_SEPARATOR + y;
    }
    
    /**
     * Given three position values, writes the string for the text file
     * @param x     the x value
     * @param y     the y value
     * @param z     the z value
     * @return      the string showing the values, in the format {x},{y},{z}
     */
    public static String getPositionOutputText(float x, float y, float z){
        return x + DataWritingConstants.DATA_IN_TEXT_FILE_SEPARATOR + y 
                + DataWritingConstants.DATA_IN_TEXT_FILE_SEPARATOR + z;
    }
    
    /**
     * When writing orientation data into a text file, this gets the string
     *      in the format {yaw},{pitch},{roll}
     * @param yaw           yaw value
     * @param pitch         pitch value
     * @param roll          roll value  
     * @return          string in format {yaw},{pitch},{roll}
     */
    public static String getOrientationOutputText(float yaw, float pitch, float roll){
        return yaw + DataWritingConstants.DATA_IN_TEXT_FILE_SEPARATOR + 
                pitch + DataWritingConstants.DATA_IN_TEXT_FILE_SEPARATOR + roll;
    }
    
    /**
     * When writing position and orientation data, this takes in the data
     *      and returns a string in the form {yaw},{pitch},{roll},{x},{y}
     * @param x         the x value
     * @param y         the y value
     * @param yaw       the yaw value
     * @param pitch     the pitch value
     * @param roll      the roll value
     * @return          the string in form {yaw},{pitch},{roll},{x},{y}
     */
    public static String getOutputText(float x, float y, float yaw, float pitch, float roll){
        return yaw + DataWritingConstants.DATA_IN_TEXT_FILE_SEPARATOR + 
                pitch + DataWritingConstants.DATA_IN_TEXT_FILE_SEPARATOR + 
                roll + DataWritingConstants.DATA_IN_TEXT_FILE_SEPARATOR + 
                x + DataWritingConstants.DATA_IN_TEXT_FILE_SEPARATOR + y;
    }
    
}
