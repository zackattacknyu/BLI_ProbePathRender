/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.signalProcessRecorder;

import java.util.Properties;
import org.zrd.util.properties.PropertiesHelper;

/**
 *
 * @author BLI
 */
public class Properties_SignalProcessRecorder {

    public static final String FILE_PATH=
            "tracker.properties";
    
    public static Properties getProperties(){
           return PropertiesHelper.getProperties(FILE_PATH);
    }
    
}
