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
        makeDataLocationsMap();
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
    
    private void makeDataLocationsMap(){
        
            putLoc(DataLocationConstants.TIMESTAMP_KEY, 
                    getLoc(DataLocationConstants.TIMESTAMP_LOCATION_PROPERTY_NAME_SUFFIX));
            putLoc(DataLocationConstants.X_KEY, 
                    getLoc(DataLocationConstants.X_LOCATION_PROPERTY_NAME_SUFFIX));
            putLoc(DataLocationConstants.Y_KEY, 
                    getLoc(DataLocationConstants.Y_LOCATION_PROPERTY_NAME_SUFFIX));
            putLoc(DataLocationConstants.YAW_KEY, 
                    getLoc(DataLocationConstants.YAW_LOCATION_PROPERTY_NAME_SUFFIX));
            putLoc(DataLocationConstants.PITCH_KEY, 
                    getLoc(DataLocationConstants.PITCH_LOCATION_PROPERTY_NAME_SUFFIX));
            putLoc(DataLocationConstants.ROLL_KEY, 
                    getLoc(DataLocationConstants.ROLL_LOCATION_PROPERTY_NAME_SUFFIX));
        }
    
    private int getLoc(String name){
        return Integer.parseInt(propsMap.getProperty(DataLocationConstants.DATA_LOCATION_PROPERTIES_PREFIX + name));
    }
    
    private void putLoc(String name, int location){
        dataLocations.put(name, location);
    }
    
    
    
}
