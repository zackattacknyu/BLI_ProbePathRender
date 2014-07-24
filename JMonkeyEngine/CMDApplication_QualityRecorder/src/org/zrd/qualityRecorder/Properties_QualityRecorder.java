/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.qualityRecorder;

import java.util.Properties;
import org.zrd.util.properties.PropertiesHelper;

/**
 *
 * @author BLI
 */
public class Properties_QualityRecorder {

    public static final String FILE_PATH=
            "tracker.properties";
    
    public static Properties getProperties(){
           return PropertiesHelper.getProperties(FILE_PATH);
    }
    
}
