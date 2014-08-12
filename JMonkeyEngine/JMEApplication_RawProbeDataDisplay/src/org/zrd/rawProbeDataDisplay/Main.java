package org.zrd.rawProbeDataDisplay;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import java.io.File;
import java.nio.file.Paths;
import org.zrd.jmeUtil.applicationHelp.ApplicationHelper;
import org.zrd.rawProbeDataDisplay.rawDataRendering.RawXYDataImport;
import org.zrd.rawProbeDataDisplay.rawDataRendering.RawYawPitchRollDataImport;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        ApplicationHelper.initializeApplication(new Main());
    }
    private File initialImportDirectory;

    @Override
    public void simpleInitApp() {
        
        viewPort.setBackgroundColor(ApplicationHelper.BACKGROUND_COLOR);
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
