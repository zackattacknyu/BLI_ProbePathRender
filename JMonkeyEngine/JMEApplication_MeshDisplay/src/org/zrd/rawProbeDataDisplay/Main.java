package org.zrd.rawProbeDataDisplay;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import java.io.File;
import java.nio.file.Paths;
import java.util.Properties;
import org.zrd.jmeUtil.applicationHelp.ApplicationHelper;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    private MeshImport meshImport = new MeshImport();

    public static void main(String[] args) {
        Properties appProps = Properties_RawProbeDataDisplay.getProperties();
        Main currentApp = new Main();
        
        File initialImportDirectory = Paths.get("C:\\Users\\BLI\\Desktop\\"
                + "BLI_ProbePathRender\\meshedReconstructionFiles\\"
                + "reconstructions").toFile();
        currentApp.meshImport.obtainFiles(initialImportDirectory);
        
        ApplicationHelper.initializeApplication(currentApp, appProps);
    }
    

    @Override
    public void simpleInitApp() {
        
        viewPort.setBackgroundColor(ApplicationHelper.BACKGROUND_COLOR);
        
        
        new CameraTrackerImpl_RawProbeDataDisplay(cam,flyCam,inputManager);
        
        meshImport.importMeshAndTextureChosen(assetManager);
        
        rootNode.attachChild(meshImport.getFinalMesh());
        cam.setLocation(meshImport.getCameraCenter());
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
