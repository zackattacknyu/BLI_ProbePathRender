/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.properties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;
import org.zrd.util.dataWriting.ProbeDataWriter;

/**
 * This contains methods related to getting properties from a
 *      .properties file
 *
 * @author BLI
 */
public class PropertiesHelper {
    
    public static float getFloatValueProperty(Properties props,String propertyName){
        return Float.valueOf(props.getProperty(propertyName));
    }
    
    public static boolean getBooleanValueProperty(Properties props, String propertyName){
        return Boolean.valueOf(props.getProperty(propertyName));
    }
    
    public static void writePropertiesFile(Properties propsFile, Path dataPath, String prefix, String comments){
        ProbeDataWriter dataWriting = ProbeDataWriter.getNewWriter(dataPath, prefix);
        try {
            propsFile.store(dataWriting.getOutputFileWriter(), comments);
        } catch (IOException ex) {
            System.out.println("Error writing properties file: " + ex);
        }
        ProbeDataWriter.closeWriter(dataWriting);
        
    }
    
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
