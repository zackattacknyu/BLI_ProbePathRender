package org.zrd.rawProbeDataDisplay;

import org.zrd.jmeGeometryInteractions.meshInteraction.ImportMesh;
import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import org.zrd.cameraTracker.cameraCoordIO.CameraCoordIO;
import org.zrd.cameraTracker.cameraMoveImpl.CameraTrackerImpl;
import org.zrd.jmeUtil.applicationHelp.ApplicationHelper;
import org.zrd.util.fileHelper.FilePathHelper;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    private ImportMesh meshImportAction;

    public static void main(String[] args) {
        ApplicationHelper.initializeApplication(new Main());
    }
    

    @Override
    public void simpleInitApp() {
        
        viewPort.setBackgroundColor(ApplicationHelper.BACKGROUND_COLOR);

        meshImportAction = new ImportMesh(inputManager,assetManager,FilePathHelper.getDefaultInputFolder().toFile());
        
        new CameraTrackerImpl(cam,flyCam,inputManager);
        new CameraCoordIO(inputManager,cam,FilePathHelper.getDefaultOutputFolder());
    }

    @Override
    public void simpleUpdate(float tpf) {
        if(meshImportAction.isNewMeshExists()){
            rootNode.attachChild(meshImportAction.getCurrentMeshImport().getSurfaceMesh());
            cam.setLocation(meshImportAction.getCurrentMeshImport().getCameraPosition());
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
