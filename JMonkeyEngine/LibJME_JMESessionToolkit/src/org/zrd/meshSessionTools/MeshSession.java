/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.meshSessionTools;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.nio.file.Path;
import java.util.Properties;
import org.zrd.cameraTracker.cameraCoordIO.CameraCoordProperties;
import org.zrd.geometryToolkit.geometryUtil.CalibrationProperties;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.geometryToolkit.pointTools.FixedPointIO;
import org.zrd.geometryToolkit.pointTools.FixedPointPicker;
import org.zrd.jmeGeometryIO.meshIO.MeshInputHelper;
import org.zrd.jmeGeometryIO.meshIO.MeshRenderData;
import org.zrd.jmeGeometryIO.renderedObjects.FixedPointRender;
import org.zrd.jmeUtil.materials.MaterialHelper;
import org.zrd.util.fileHelper.FilePathHelper;
import org.zrd.util.fileHelper.MeshInteractionFiles;
import org.zrd.util.properties.PropertiesHelper;

/**
 *
 * @author BLI
 */
public class MeshSession {
    
    private MeshInteractionFiles meshInterFiles;
    private TriangleSet meshInfo;
    private Node shootableMesh;
    private Node fixedPointNode = new Node();
    private FixedPointPicker fixedPtsToPick;
    private MeshRenderData importedMesh;
    private Material normalLineMaterial;
    private Properties calibPropsFromFile;
    private CalibrationProperties allCalibrationProperties;
    
    public MeshSession(AssetManager assetManager,Camera cam){
        this(FilePathHelper.getDefaultInputFolder(),PropertiesHelper.getDefaultProperties(),assetManager,cam);
    }

    public CalibrationProperties getAllCalibrationProperties() {
        return allCalibrationProperties;
    }
    
    public MeshSession(Path meshDataPath,Properties props,AssetManager assetManager, Camera cam){
        
        String defaultSuffix = props.getProperty("defaultMesh");
        
        Material fixedPtMaterial = MaterialHelper.getColorMaterial(1.0f, 0.0f, 0.0f, assetManager);
        normalLineMaterial = MaterialHelper.getColorMaterial(assetManager, ColorRGBA.Black);
        
        meshInterFiles = MeshInputHelper.obtainAllFiles(meshDataPath.toFile(),defaultSuffix);
        importedMesh = MeshInputHelper.generateRenderData(
                meshInterFiles.getDataFiles(),assetManager);
        if(meshInterFiles.getCameraCoordFile().exists()){
            CameraCoordProperties.setCameraCoordinatesUsingFile(cam, meshInterFiles.getCameraCoordFile());
        }
        if(meshInterFiles.getFixedPointsFile().exists()){
            FixedPointIO fixedPtsImported = FixedPointIO.getPointsFromFile(meshInterFiles.getFixedPointsFile());
            fixedPtsToPick = fixedPtsImported.getFixedPtPicker();
            fixedPointNode = FixedPointRender.displayFixedPoints(fixedPtsImported,fixedPtMaterial);
        }
        if(meshInterFiles.getCalibrationProperties().exists()){
            calibPropsFromFile = PropertiesHelper.getProperties(meshInterFiles.getCalibrationProperties());
        }
        
        allCalibrationProperties = CalibrationProperties.obtainCalibrationProperties(calibPropsFromFile,props);

        meshInfo = importedMesh.getActiveMeshInfo();
        
        Spatial surface = importedMesh.getSurfaceMesh();
        shootableMesh = new Node("shootables");
        shootableMesh.attachChild(surface);
        
    }
    
    /**
     * This gets the lines for all the triangle normals which originate
     *      from the triangle center points. Note that this should only be used
     *      once for model verification purposes
     * @return      Node for all the normal lines
     */
    public Node getTriangleNormalDisplay(){
        return importedMesh.generateNormalLines(normalLineMaterial);
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

    public Properties getCalibrationProperties() {
        return calibPropsFromFile;
    }
    
    
    
}
