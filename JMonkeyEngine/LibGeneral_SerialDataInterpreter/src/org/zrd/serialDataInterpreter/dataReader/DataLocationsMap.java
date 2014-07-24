/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialDataInterpreter.dataReader;

import java.util.HashMap;
import java.util.Properties;

/**This contains the code that constructs the data location map
 *      using the properties file
 * 
 * The data locations map consists of strings as keys which
 *      are lower-case words representing the name of the data.
 *      The values in the hash map are the index where the field
 *      value occurs in the string coming from the serial data
 *
 * @author BLI
 */
public class DataLocationsMap {
    
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
    public static final String Y_KEY = "y";
    public static final String ROLL_KEY = "roll";
    public static final String YAW_KEY = "yaw";
    public static final String TIMESTAMP_KEY = "timestamp";
    public static final String DATAFIELD_KEY = "datafield";
    public static final String QUALITY_KEY = "quality";
    public static final String PITCH_KEY = "pitch";
    public static final String X_KEY = "x";
    
    
    private HashMap<String,Integer> dataLocations;
    private Properties propsMap;

    /**
     * Makes the data location map using the properties object
     * @param propsMap      properties object derived from the properties file
     */
    public DataLocationsMap(Properties propsMap){
        dataLocations = new HashMap<String,Integer>(10);
        this.propsMap = propsMap;
        
        //this makes the map
        putLocationIntoMap(TIMESTAMP_KEY);
        putLocationIntoMap(X_KEY);
        putLocationIntoMap(Y_KEY);
        putLocationIntoMap(YAW_KEY);
        putLocationIntoMap(PITCH_KEY);
        putLocationIntoMap(ROLL_KEY);
        putLocationIntoMap(DATAFIELD_KEY);
        putLocationIntoMap(QUALITY_KEY);
    }
    
    /**
     * this gets the data location map from the properties
     * @param propsMap
     * @return      hash map of data locations
     */
    public static HashMap<String,Integer> getDataLocationMap(Properties propsMap){
        DataLocationsMap theMap = new DataLocationsMap(propsMap);      
        return theMap.dataLocations;
    }
    
    /**
     * This retrieves the location from the properties object and puts the
     *      integer into the data locations hashmap
     * @param name      the suffix of the property name in the config file
     */
    private void putLocationIntoMap(String name){
        
        //gets the field name in the properties object
        String propertyInConfigFileName = DATA_LOCATION_PROPERTIES_PREFIX + name;
        
        //retrieves the value in the properties filed
        String propertyValue = propsMap.getProperty(propertyInConfigFileName);
        
        //if the property value was there then get the number and put it into the map
        if(propertyValue != null){
            
            //index of data
            int location = Integer.parseInt(propertyValue);
            
            //put index into map
            dataLocations.put(name, location);
        }
    }
    
    
    
}
