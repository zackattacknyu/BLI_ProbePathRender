/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.bliProbePath;

import java.util.Properties;
import org.zrd.util.general.PropertiesHelper;

/**
 *
 * @author BLI
 */
public class Properties_BLIProbePath {

    public static final String FILE_PATH=
            "src\\org\\zrd\\bliProbePath\\tracker.properties";
    
    public static Properties getProperties(){
           return PropertiesHelper.getProperties(FILE_PATH);
    }
    
}