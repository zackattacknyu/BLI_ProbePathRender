/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.properties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * This contains methods related to getting properties from a
 *      .properties file
 *
 * @author BLI
 */
public class PropertiesHelper {
    
    /**
     * This takes in a properties file name and gets the properties
     *      object for the properties in that file
     * @param fileName      file name of .properties file
     * @return              properties object for its properties
     */
    public static Properties getProperties(String fileName){
        
        return getProperties(new File(fileName));
        
    }
    
    /**
     * This takes in a properties file and gets the properties
     *      object for the properties in that file
     * @param file          the .properties file object
     * @return              properties object for its properties
     */
    public static Properties getProperties(File file){
        FileReader reader;
        Properties trackerProps = new Properties();
        
        try {
            reader = new FileReader(file);
            trackerProps.load(reader);
        } catch (FileNotFoundException exc) {
            System.err.println("Properties File Not Found: " + exc);
        } catch (IOException ex) {
            System.err.println("Error Reading Properties File " + ex);
        }
        
        return trackerProps;
    }
    
}
