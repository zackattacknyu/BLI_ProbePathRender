/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.dataHelp;

import java.util.Collection;

/**
 * These are simple methods that take in float values and string
 *      them together to make a string for output purposes
 *
 * @author BLI
 */
public class OutputHelper {
    
    
    public static int getCorrectMod(int number, int mod){
        mod = Math.abs(mod);
        while(number<0){
            number += mod;
        }
        return number%mod;
    }
    
    public static String printStringArray(String[] array){
        StringBuilder sb = new StringBuilder(array.length);
        for(String entry: array){
            sb.append(entry);
            sb.append(";");
        }
        return sb.toString();
    }
    
    
    /**
     * In each line of the output, this is the character
     *      that separates each value. Change this variable
     *      if you want a semicolon or other separator instead
     *      of the current one.
     */
    public static final String DATA_IN_OUTPUT_SEPARATOR = ",";
    public static final String EMPTY_LINE_STRING = " ";
    
    /**
     * This takes in a name of a field and its value and makes a string
     *      in the form "{name}={value}  "
     * There are two spaces afterwards
     * 
     * @param name      name of field
     * @param value     its value
     * @return          string in form "{name}={value}  "
     */
    public static String makeNameValueDisplay(String name, float value){
        return name + "=" + value + "  ";
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
    public static String getOutputText(float x, float y, float yaw, float pitch, float roll) {
        return yaw + OutputHelper.DATA_IN_OUTPUT_SEPARATOR + pitch + OutputHelper.DATA_IN_OUTPUT_SEPARATOR + roll + OutputHelper.DATA_IN_OUTPUT_SEPARATOR + x + OutputHelper.DATA_IN_OUTPUT_SEPARATOR + y;
    }

    /**
     * Given three position values, writes the string for output
     * @param x     the x value
     * @param y     the y value
     * @param z     the z value
     * @return      the string showing the values, in the format {x},{y},{z}
     */
    public static String getPositionOutputText(float x, float y, float z) {
        return x + OutputHelper.DATA_IN_OUTPUT_SEPARATOR + y + OutputHelper.DATA_IN_OUTPUT_SEPARATOR + z;
    }

    /**
     * Given two position values, writes the string for output
     * @param x     the x value
     * @param y     the y value
     * @return      the string showing the values, in the format {x},{y}
     */
    public static String getPositionOutputText(float x, float y) {
        return x + OutputHelper.DATA_IN_OUTPUT_SEPARATOR + y;
    }

    /**
     * When writing orientation data into a text file, this gets the string
     *      in the format {yaw},{pitch},{roll}
     * @param yaw           yaw value
     * @param pitch         pitch value
     * @param roll          roll value
     * @return          string in format {yaw},{pitch},{roll}
     */
    public static String getOrientationOutputText(float yaw, float pitch, float roll) {
        return yaw + OutputHelper.DATA_IN_OUTPUT_SEPARATOR + pitch + OutputHelper.DATA_IN_OUTPUT_SEPARATOR + roll;
    }
    
    public static void printStringCollection(Collection<String> stringCol){
        for(String line: stringCol){
            System.out.println(line);
        }
    }
    
    
}
