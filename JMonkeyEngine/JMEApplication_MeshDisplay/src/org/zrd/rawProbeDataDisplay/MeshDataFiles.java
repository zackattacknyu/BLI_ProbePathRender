/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.rawProbeDataDisplay;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JOptionPane;
import org.zrd.util.fileHelper.GeneralFileHelper;

/**
 *
 * @author Zach
 */
public class MeshDataFiles {
    
    private String objFileName;
    private String textureFileName;

    public String getObjFileName() {
        return objFileName;
    }

    public String getTextureFileName() {
        return textureFileName;
    }
    
    public MeshDataFiles(File initDir){
        obtainFiles(initDir);
    }
    
    public void obtainFiles(File initImportDirectory){
        Path assetPath = Paths.get("assets");
        GeneralFileHelper.createDirectoryIfNone(assetPath);
        
        Path modelAssets = assetPath.resolve("Models");
        GeneralFileHelper.createDirectoryIfNone(modelAssets);
        
        JOptionPane.showMessageDialog(null, "Please choose an OBJ File for the 3D Model");
        objFileName = GeneralFileHelper.importAndCopyFile(initImportDirectory,modelAssets);
        
        Path textureAssets = assetPath.resolve("Textures");
        GeneralFileHelper.createDirectoryIfNone(textureAssets);
        
        JOptionPane.showMessageDialog(null, "Please choose an Image file for the texture");
        textureFileName = GeneralFileHelper.importAndCopyFile(initImportDirectory,textureAssets);
    }
    
}
