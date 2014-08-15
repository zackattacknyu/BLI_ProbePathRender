package org.zrd.meshDisplay;

import org.zrd.jmeGeometryInteractions.meshInteraction.ImportMesh;
import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import org.zrd.cameraTracker.cameraTrackingIO.CameraTrackingIO;
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
        
        ApplicationHelper.setBackgroundColor(viewPort);

        meshImportAction = new ImportMesh(inputManager,assetManager,FilePathHelper.getDefaultInputFolder().toFile());
        
        CameraTrackingIO.initializeCameraTrackingIO(inputManager, cam, flyCam);
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
