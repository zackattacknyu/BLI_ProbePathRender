package org.zrd.rawProbeDataDisplay;

import org.zrd.jmeGeometryInteractions.meshInteraction.ImportMesh;
import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.zrd.jmeUtil.applicationHelp.ApplicationHelper;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    private ImportMesh meshImportAction;

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
        
        Path logPath = Paths.get("textFiles");
        
        meshImportAction = new ImportMesh(inputManager,assetManager,initialImportDirectory);
        
        new OutputCameraCoordinates(inputManager,cam,logPath);
        new ImportCameraCoordinates(inputManager,cam,logPath);
    }

    @Override
    public void simpleUpdate(float tpf) {
        if(meshImportAction.isNewMeshExists()){
            rootNode.attachChild(meshImportAction.getCurrentMeshImport().getFinalMesh());
            cam.setLocation(meshImportAction.getCurrentMeshImport().getCameraCenter());
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
