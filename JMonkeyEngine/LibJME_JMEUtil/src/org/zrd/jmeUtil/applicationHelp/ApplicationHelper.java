/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeUtil.applicationHelp;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.ViewPort;
import com.jme3.system.AppSettings;
import java.util.Properties;
import org.zrd.util.properties.PropertiesHelper;

/**
 * This helper code helps initialize the JME Applications.
 *      Specifically, it sets up the default settings
 *      and calls the methods to get the other default settings
 *      from the .properties file
 * 
 * Code here will affect all the JME applications. If a change
 *      should be made to all of them, then it should be
 *      done in this class.
 * 
*  Refer to these two web pages to find out about start settings:
*      http://hub.jmonkeyengine.org/wiki/doku.php/jme3:intermediate:appsettings
* 
*      http://hub.jmonkeyengine.org/wiki/doku.php/jme3:intermediate:simpleapplication
* 
 *
 * @author BLI
 */
public class ApplicationHelper {
    
    /*
     * These constants will affect all the JME applications
     */
    
    //whether or not to dispaly the app settings in the window
    public static final boolean SHOW_SETTINGS_IN_WINDOW = false;
    
    //whether or not to display the fps (frame per second) text
    public static final boolean DISPLAY_FPS_IN_WINDOW = false;
    
    //whether or not to display the stat view in the window
    public static final boolean DISPLAY_STAT_VIEW_IN_WINDOW = false;
    
    /*
     * These constants are used for reading the properties file
     *      with the settings
     */
    public static final String TITLE_PROPERTY_NAME = "settings.title";
    public static final String X_RES_PROPERTY_NAME = "settings.xResolution";
    public static final String Y_RES_PROPERTY_NAME = "settings.yResolution";
    public static final ColorRGBA BACKGROUND_COLOR = 
            new ColorRGBA(205.0F / 256.0F, 204.0F / 256.0F, 207.0F / 256.0F, 1.0F);
    
    /**
     * This initializes an application using the application settings
     * @param app           the application to start
     * @param settings      the appsettings to use for it
     */
    public static void initializeApplication(SimpleApplication app, AppSettings settings){
        
        /*
         * This disables the setting screen in the beginning
         *      from appearing
         */
        app.setSettings(settings); 
        
        //says whether or not to display certain stats
        app.setShowSettings(SHOW_SETTINGS_IN_WINDOW); 
        app.setDisplayFps(DISPLAY_FPS_IN_WINDOW); 
        app.setDisplayStatView(DISPLAY_STAT_VIEW_IN_WINDOW); 
        
        //starts the application
        app.start();
        
    }
    
    /**
     * This intializes an application using the properties file that contain
     *      the important settings
     * @param app           the application to start
     * @param appProps      the proerties object containing the important info
     */
    public static void initializeApplication(SimpleApplication app, Properties appProps){
        
        initializeApplication(app,getAppSettings(appProps));
        
    }
    
    /**
     * This intializes an application using the properties file that contain
     *      the important settings
     * @param app           the application to start
     * @param appProps      the proerties object containing the important info
     */
    public static void initializeApplication(SimpleApplication app){
        
        initializeApplication(app,PropertiesHelper.getDefaultProperties());
        
    }

    /**
     * This gets the application settings, which in this case
     *      come from the .properties file
     * @param appProps      properties object derived from properties file
     * @return              AppSettings object for application settings derived from file
     */
    public static AppSettings getAppSettings(Properties appProps){
        
        AppSettings settings = new AppSettings(true);
        
        //adjusts title shown on window
        settings.setTitle(appProps.getProperty(TITLE_PROPERTY_NAME));
        
        //adjusts resolution
        int xRes = Integer.parseInt(appProps.getProperty(X_RES_PROPERTY_NAME));
        int yRes = Integer.parseInt(appProps.getProperty(Y_RES_PROPERTY_NAME));
        settings.setResolution(xRes, yRes);
        
        return settings;
    }
    
    
    public static void setBackgroundColor(ViewPort viewPort){
        viewPort.setBackgroundColor(ApplicationHelper.BACKGROUND_COLOR);
    }
}
