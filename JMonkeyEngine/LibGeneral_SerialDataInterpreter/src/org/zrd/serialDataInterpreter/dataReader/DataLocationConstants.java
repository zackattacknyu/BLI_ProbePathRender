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
    
    //says the delimiter used to separate parts of the serial data string
    public static final String SERIAL_DATA_STRING_DELIMITER = ",";
    
    
    //this is the prefix for all the data location property names
    //      in the config file. This should include the period
    //      or other mark used as a delimiter in the property name
    public static final String DATA_LOCATION_PROPERTIES_PREFIX = "dataLocation.";
    
    //these are used to denote the property name suffix in the config file
    public static final String YAW_LOCATION_PROPERTY_NAME_SUFFIX = "yaw";
    public static final String X_LOCATION_PROPERTY_NAME_SUFFIX = "x";
    public static final String Y_LOCATION_PROPERTY_NAME_SUFFIX = "y";
    public static final String TIMESTAMP_LOCATION_PROPERTY_NAME_SUFFIX = "timestamp";
    public static final String PITCH_LOCATION_PROPERTY_NAME_SUFFIX = "pitch";
    public static final String ROLL_LOCATION_PROPERTY_NAME_SUFFIX = "roll";
    
    //this denotes the map key used in the data location hashmap
    public static final String YAW_KEY = "yaw";
    public static final String X_KEY = "x";
    public static final String PITCH_KEY = "pitch";
    public static final String Y_KEY = "y";
    public static final String ROLL_KEY = "roll";
    public static final String TIMESTAMP_KEY = "timestamp";
    
    
}
