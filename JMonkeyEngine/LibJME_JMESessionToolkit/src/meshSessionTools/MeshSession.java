/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meshSessionTools;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.nio.file.Path;
import java.util.Properties;
import org.zrd.cameraTracker.cameraCoordIO.CameraCoordProperties;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.pointTools.FixedPointIO;
import org.zrd.geometryToolkit.pointTools.FixedPointPicker;
import org.zrd.jmeGeometryIO.meshIO.MeshInputHelper;
import org.zrd.jmeGeometryIO.meshIO.MeshRenderData;
import org.zrd.jmeGeometryIO.renderedObjects.FixedPointRender;
import org.zrd.jmeUtil.materials.MaterialHelper;
import org.zrd.util.fileHelper.MeshInteractionFiles;

/**
 *
 * @author BLI
 */
public class MeshSession {
    
    private MeshInteractionFiles meshInterFiles;
    private TriangleSet meshInfo;
    private Node shootableMesh;
    private Node fixedPointNode;
    private FixedPointPicker fixedPtsToPick;
    
    public MeshSession(Path meshDataPath,Properties props,AssetManager assetManager, Camera cam){
        
        String defaultSuffix = props.getProperty("defaultMesh");
        
        Material fixedPtMaterial = MaterialHelper.getColorMaterial(1.0f, 0.0f, 0.0f, assetManager);
        meshInterFiles = MeshInputHelper.obtainAllFiles(meshDataPath.toFile(),defaultSuffix);
        MeshRenderData importedMesh = MeshInputHelper.generateRenderData(
                meshInterFiles.getDataFiles(),assetManager);
        if(meshInterFiles.getCameraCoordFile().exists()){
            CameraCoordProperties.setCameraCoordinatesUsingFile(cam, meshInterFiles.getCameraCoordFile());
        }
        if(meshInterFiles.getFixedPointsFile().exists()){
            FixedPointIO fixedPtsImported = FixedPointIO.getPointsFromFile(meshInterFiles.getFixedPointsFile());
            fixedPtsToPick = fixedPtsImported.getFixedPtPicker();
            fixedPointNode = FixedPointRender.displayFixedPoints(fixedPtsImported,fixedPtMaterial);
        }

        meshInfo = importedMesh.getActiveMeshInfo();
        
        Spatial surface = importedMesh.getSurfaceMesh();
        shootableMesh = new Node("shootables");
        shootableMesh.attachChild(surface);
        
    }

    public MeshInteractionFiles getMeshInterFiles() {
        return meshInterFiles;
    }

    public TriangleSet getMeshInfo() {
        return meshInfo;
    }

    public Node getShootableMesh() {
        return shootableMesh;
    }

    public Node getFixedPointNode() {
        return fixedPointNode;
    }

    public FixedPointPicker getFixedPtsToPick() {
        return fixedPtsToPick;
    }
    
    
    
}
