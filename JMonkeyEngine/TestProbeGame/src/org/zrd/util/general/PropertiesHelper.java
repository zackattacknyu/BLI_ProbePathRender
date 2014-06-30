/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.general;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author BLI
 */
public class PropertiesHelper {
    
    
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
    
}
