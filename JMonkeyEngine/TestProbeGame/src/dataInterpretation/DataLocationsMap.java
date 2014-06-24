/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataInterpretation;

import java.util.HashMap;
import java.util.Properties;

/**This contains the code that constructs the data location map
 *      using the properties file
 *
 * @author BLI
 */
public class DataLocationsMap {
    
    //these are the suffix names in the config file
    //      for the names of the properties
    public static final String TIMESTAMP_LOCATION_PROPERTY_NAME_SUFFIX = "timestamp";
    public static final String X_LOCATION_PROPERTY_NAME_SUFFIX = "x";
    public static final String Y_LOCATION_PROPERTY_NAME_SUFFIX = "y";
    public static final String YAW_LOCATION_PROPERTY_NAME_SUFFIX = "yaw";
    public static final String PITCH_LOCATION_PROPERTY_NAME_SUFFIX = "pitch";
    public static final String ROLL_LOCATION_PROPERTY_NAME_SUFFIX = "roll";
    
    //this is the prefix for all the data location property names
    //      in the config file. This should include the period
    //      or other mark used as a delimiter in the property name
    public static final String DATA_LOCATION_PROPERTIES_PREFIX = "dataLocation.";
    
    
    private HashMap<String,Integer> dataLocations;
    private Properties propsMap;

    public DataLocationsMap(Properties propsMap){
        dataLocations = new HashMap<String,Integer>(10);
        this.propsMap = propsMap;
        makeDataLocationsMap();
    }
    
    public static HashMap<String,Integer> getDataLocationMap(Properties propsMap){
        DataLocationsMap theMap = new DataLocationsMap(propsMap);      
        return theMap.getDataLocations();
    }

    public HashMap<String, Integer> getDataLocations() {
        return dataLocations;
    }
    
    private void makeDataLocationsMap(){
        
            putLoc(TIMESTAMP_LOCATION_PROPERTY_NAME_SUFFIX, 
                    getLoc(TIMESTAMP_LOCATION_PROPERTY_NAME_SUFFIX));
            putLoc(X_LOCATION_PROPERTY_NAME_SUFFIX, 
                    getLoc(X_LOCATION_PROPERTY_NAME_SUFFIX));
            putLoc(Y_LOCATION_PROPERTY_NAME_SUFFIX, 
                    getLoc(Y_LOCATION_PROPERTY_NAME_SUFFIX));
            putLoc(YAW_LOCATION_PROPERTY_NAME_SUFFIX, 
                    getLoc(YAW_LOCATION_PROPERTY_NAME_SUFFIX));
            putLoc(PITCH_LOCATION_PROPERTY_NAME_SUFFIX, 
                    getLoc(PITCH_LOCATION_PROPERTY_NAME_SUFFIX));
            putLoc(ROLL_LOCATION_PROPERTY_NAME_SUFFIX, 
                    getLoc(ROLL_LOCATION_PROPERTY_NAME_SUFFIX));
        }
    
    private int getLoc(String name){
        return Integer.parseInt(propsMap.getProperty(DATA_LOCATION_PROPERTIES_PREFIX + name));
    }
    
    private void putLoc(String name, int location){
        dataLocations.put(name, location);
    }
    
    
    
}
