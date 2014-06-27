/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.general;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author BLI
 */
public class PropertiesHelper {
    
    public static final String FILE_PATH = 
            "C:\\Users\\BLI\\Desktop\\BLI_ProbePathRender\\JMonkeyEngine"
            + "\\TestProbeGame\\src\\org\\zrd\\bliProbePath\\tracker.properties";
    
    public static final String FILE_PATH_2=
            "src\\org\\zrd\\bliProbePath\\tracker.properties";
    
    
    public static Properties getProperties(String fileName){
        
        FileReader reader;
        Properties trackerProps = new Properties();
        
        try {
            reader = new FileReader(fileName);
            trackerProps.load(reader);
        } catch (FileNotFoundException exc) {
            System.err.println("Properties File Not Found: " + exc);
        } catch (IOException ex) {
            System.err.println("Error Reading Properties File " + ex);
        }
        
        return trackerProps;
        
    }
    
    /**
     * TODO: Make the path better
     * @return 
     */
    public static Properties getProperties(){
        return getProperties(FILE_PATH_2);
    }
    
}
