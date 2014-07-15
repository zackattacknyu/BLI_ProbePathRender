package org.zrd.rawProbeDataDisplay;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import java.io.File;
import java.nio.file.Paths;
import java.util.Properties;
import org.zrd.geometryToolkit.geometryUtil.ProgramConstants;
import org.zrd.rawProbeDataDisplay.rawDataRendering.RawXYDataImport;
import org.zrd.rawProbeDataDisplay.rawDataRendering.RawYawPitchRollDataImport;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        /*
         * Refer to these two web pages to find out about start settings:
         *      http://hub.jmonkeyengine.org/wiki/doku.php/jme3:intermediate:appsettings
         * 
         *      http://hub.jmonkeyengine.org/wiki/doku.php/jme3:intermediate:simpleapplication
         * 
         * 
         */
        Properties appProps = Properties_RawProbeDataDisplay.getProperties();
        AppSettings settings = new AppSettings(true);
        
        //adjusts title shown on window
        settings.setTitle(appProps.getProperty("settings.title"));
        
        //adjusts resolution
        int xRes = Integer.parseInt(appProps.getProperty("settings.xResolution"));
        int yRes = Integer.parseInt(appProps.getProperty("settings.yResolution"));
        settings.setResolution(xRes, yRes);
        
        Main app = new Main();
        app.setSettings(settings); //disables the setting screen at start-up
        app.setShowSettings(false); //shows the above settings
        app.setDisplayFps(false); //makes sure the fps text is not displayed
        app.setDisplayStatView(false); //makes sure the stat view is not displayed
        app.start();
    }
    private File initialImportDirectory;

    @Override
    public void simpleInitApp() {
        
        viewPort.setBackgroundColor(ProgramConstants.BACKGROUND_COLOR);
        initialImportDirectory = Paths.get("C:\\Users\\BLI\\Desktop\\BLI_ProbePathRender\\sampleTextFiles").toFile();
        
        new CameraTrackerImpl_RawProbeDataDisplay(cam,flyCam,inputManager);
        new RawXYDataImport(inputManager,assetManager,rootNode,initialImportDirectory);
        new RawYawPitchRollDataImport(inputManager,assetManager,rootNode,initialImportDirectory);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
