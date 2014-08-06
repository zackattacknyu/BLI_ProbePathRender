package org.zrd.rawProbeDataDisplay;

import org.zrd.jmeGeometryIO.meshIO.MeshDataFiles;
import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import java.io.File;
import java.nio.file.Paths;
import java.util.Properties;
import org.zrd.jmeGeometryIO.meshIO.MeshInputHelper;
import org.zrd.jmeUtil.applicationHelp.ApplicationHelper;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Properties appProps = Properties_RawProbeDataDisplay.getProperties();
        ApplicationHelper.initializeApplication(new Main(), appProps);
    }
    

    @Override
    public void simpleInitApp() {
        
        viewPort.setBackgroundColor(ApplicationHelper.BACKGROUND_COLOR);
        
        File initialImportDirectory = Paths.get(
                "C:\\"
                + "Users\\"
                + "BLI\\"
                + "Desktop\\"
                + "BLI_ProbePathRender\\"
                + "meshedReconstructionFiles\\"
                + "reconstructions").toFile();
        
        new CameraTrackerImpl_RawProbeDataDisplay(cam,flyCam,inputManager);
        
        new ImportMesh(inputManager,assetManager,rootNode,initialImportDirectory,cam);
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
