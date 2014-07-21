/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.dataReader;

/**
 *
 * @author BLI
 */
public class DataLocationConstants {

    /**this is the prefix for all the data location property names
    *      in the config file. This should include the period
    *      or other mark used as a delimiter in the property name
    */
    public static final String DATA_LOCATION_PROPERTIES_PREFIX = "dataLocation.";
    
    /*
     * These strings are used to denote the property name in the config file.
     *      They are the suffix as the property names will all start with
     *          dataLocations.
     * 
     * They are also used as the string keys in the data locations
     *      hashmap
     */
    public static final String YAW_KEY = "yaw";
    public static final String X_KEY = "x";
    public static final String PITCH_KEY = "pitch";
    public static final String Y_KEY = "y";
    public static final String ROLL_KEY = "roll";
    public static final String TIMESTAMP_KEY = "timestamp";
    public static final String DATAFIELD_KEY = "datafield";
    
    
}
