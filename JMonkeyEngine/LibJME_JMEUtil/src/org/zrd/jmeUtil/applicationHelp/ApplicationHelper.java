/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeUtil.applicationHelp;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import java.util.Properties;

/**
 * This helper code helps initialize the JME Applications.
 *      Specifically, it sets up the default settings
 *      and calls the methods to get the other default settings
 *      from the .properties file
*  Refer to these two web pages to find out about start settings:
*      http://hub.jmonkeyengine.org/wiki/doku.php/jme3:intermediate:appsettings
* 
*      http://hub.jmonkeyengine.org/wiki/doku.php/jme3:intermediate:simpleapplication
* 
 *
 * @author BLI
 */
public class ApplicationHelper {

    public static AppSettings getAppSettings(Properties appProps){
        AppSettings settings = new AppSettings(true);
        
        //adjusts title shown on window
        settings.setTitle(appProps.getProperty("settings.title"));
        
        //adjusts resolution
        int xRes = Integer.parseInt(appProps.getProperty("settings.xResolution"));
        int yRes = Integer.parseInt(appProps.getProperty("settings.yResolution"));
        settings.setResolution(xRes, yRes);
        
        return settings;
    }
    
    public static void initializeApplication(SimpleApplication app, AppSettings settings){
        
        app.setSettings(settings); //disables the setting screen at start-up
        app.setShowSettings(false); //shows the above settings
        app.setDisplayFps(false); //makes sure the fps text is not displayed
        app.setDisplayStatView(false); //makes sure the stat view is not displayed
        app.start();
        
    }
    
    public static void initializeApplication(SimpleApplication app, Properties appProps){
        
        initializeApplication(app,getAppSettings(appProps));
        
    }
    
}
