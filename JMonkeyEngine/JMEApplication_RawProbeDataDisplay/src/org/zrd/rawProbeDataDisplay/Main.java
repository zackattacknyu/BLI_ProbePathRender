package org.zrd.rawProbeDataDisplay;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import java.io.File;
import java.nio.file.Path;
import org.zrd.cameraTracker.cameraCoordIO.CameraCoordIO;
import org.zrd.cameraTracker.cameraCoordIO.CameraCoordProperties;
import org.zrd.cameraTracker.cameraMoveImpl.CameraTrackerImpl;
import org.zrd.cameraTracker.cameraTrackingIO.CameraTrackingIO;
import org.zrd.jmeUtil.applicationHelp.ApplicationHelper;
import org.zrd.rawProbeDataDisplay.rawDataRendering.RawXYDataImport;
import org.zrd.rawProbeDataDisplay.rawDataRendering.RawYawPitchRollDataImport;
import org.zrd.util.fileHelper.FilePathHelper;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        ApplicationHelper.initializeApplication(new Main());
    }

    @Override
    public void simpleInitApp() {
        
        ApplicationHelper.setBackgroundColor(viewPort);
        Path inputFolder = FilePathHelper.getDefaultInputFolder();
        
        CameraTrackingIO.initializeCameraTrackingIO(inputManager, cam, flyCam);
        
        File defaultCameraCoordsFile = inputFolder.resolve("defaultCameraCoords.txt").toFile();
        if(defaultCameraCoordsFile.exists()){
            CameraCoordProperties.setCameraCoordinatesUsingFile(cam, defaultCameraCoordsFile);
        }
        
        
        new RawXYDataImport(inputManager,assetManager,rootNode,inputFolder.toFile());
        new RawYawPitchRollDataImport(inputManager,assetManager,rootNode,inputFolder.toFile());
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
