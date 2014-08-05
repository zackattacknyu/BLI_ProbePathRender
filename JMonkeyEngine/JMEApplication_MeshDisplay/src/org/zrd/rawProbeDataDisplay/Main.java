package org.zrd.rawProbeDataDisplay;

import org.zrd.jmeGeometryIO.meshIO.MeshDataFiles;
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
    private MeshDataFiles meshFiles;

    public static void main(String[] args) {
        File initialImportDirectory = Paths.get("C:\\Users\\BLI\\Desktop\\"
                + "BLI_ProbePathRender\\meshedReconstructionFiles\\"
                + "reconstructions").toFile();

        MeshDataFiles meshFiles = new MeshDataFiles(initialImportDirectory);
        
        Properties appProps = Properties_RawProbeDataDisplay.getProperties();
        Main currentApp = new Main();
        
        currentApp.meshFiles = meshFiles;
        ApplicationHelper.initializeApplication(currentApp, appProps);
    }
    

    @Override
    public void simpleInitApp() {
        
        viewPort.setBackgroundColor(ApplicationHelper.BACKGROUND_COLOR);
        
        
        new CameraTrackerImpl_RawProbeDataDisplay(cam,flyCam,inputManager);
        
        meshImport.importMeshAndTextureChosen(assetManager,
                meshFiles.getObjFile(),
                meshFiles.getTextureFile());
        
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
