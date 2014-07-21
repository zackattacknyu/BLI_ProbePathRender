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
        putLocationIntoMap(DataLocationConstants.TIMESTAMP_KEY);
        putLocationIntoMap(DataLocationConstants.X_KEY);
        putLocationIntoMap(DataLocationConstants.Y_KEY);
        putLocationIntoMap(DataLocationConstants.YAW_KEY);
        putLocationIntoMap(DataLocationConstants.PITCH_KEY);
        putLocationIntoMap(DataLocationConstants.ROLL_KEY);
        putLocationIntoMap(DataLocationConstants.DATAFIELD_KEY);
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
        String propertyInConfigFileName = DataLocationConstants.DATA_LOCATION_PROPERTIES_PREFIX + name;
        
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
