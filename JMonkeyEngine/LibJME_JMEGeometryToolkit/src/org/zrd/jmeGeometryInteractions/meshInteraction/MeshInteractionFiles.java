/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryInteractions.meshInteraction;

import java.io.File;
import java.nio.file.Path;
import org.zrd.jmeGeometryIO.meshIO.MeshDataFiles;

/**
 * 
 * This is a class that stores all the files with loading and interacting
 *      with a mesh. Right now this includes the following:
 * - OBJ and MTL file for the shape
 * - PNG file for the texture
 * - TXT file with camera coordinates for the default place to put the camera
 * - TXT file with fixed points and their triangles for calibration
 * 
 * The formats must be as follows:
 * - OBJ file has format "meshDefinition_{suffix}.obj"
 * - PNG file has format "textureDefinition_{suffix}.png"
 * - TXT file with camera coords has format "cameraCoords_{suffix}.txt"
 * - TXT file with fixed points has format "fixedPoints_{suffix}.txt"
 *
 * @author BLI
 */
public class MeshInteractionFiles {
    
    private MeshDataFiles dataFiles;
    private File cameraCoordFile;
    private File fixedPointsFile;
    private Path locationOfFiles;
    private String suffixOfFiles;

    public MeshDataFiles getDataFiles() {
        return dataFiles;
    }

    public File getCameraCoordFile() {
        return cameraCoordFile;
    }

    public File getFixedPointsFile() {
        return fixedPointsFile;
    }
    
    public MeshInteractionFiles(File meshObjFile){
        
        String meshObjFileName = meshObjFile.getName();
        
        //split into file name and extension
        String[] meshObjFileNameParts = meshObjFileName.split("\\x2E");
        
        //split the file name
        String[] meshObjNameParts = meshObjFileNameParts[0].split("_");
        suffixOfFiles = meshObjNameParts[1];
        
        locationOfFiles = meshObjFile.getParentFile().toPath();
        
        dataFiles = new MeshDataFiles(meshObjFile,getMeshInteractionFile("textureDefinition",".png"));
        
        cameraCoordFile = getMeshInteractionFile("cameraCoords",".txt");
        fixedPointsFile = getMeshInteractionFile("fixedPoints",".txt");
    }
    
    private File getMeshInteractionFile(String prefix,String fileExtension){
        return locationOfFiles.resolve(prefix + "_" + suffixOfFiles + fileExtension).toFile();
    }
    
}
