/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.rawProbeDataDisplay;

import java.io.File;
import javax.swing.JOptionPane;
import org.zrd.util.fileHelper.GeneralFileHelper;

/**
 *
 * @author Zach
 */
public class MeshDataFiles {
    
    private File objFileName;
    private File textureFileName;

    public File getObjFileName() {
        return objFileName;
    }

    public File getTextureFileName() {
        return textureFileName;
    }
    
    public MeshDataFiles(File initDir){
        obtainFiles(initDir);
    }
    
    public void obtainFiles(File initImportDirectory){
        
        JOptionPane.showMessageDialog(null, "Please choose an OBJ File for the 3D Model");
        objFileName = GeneralFileHelper.importPathUsingFileSelector(initImportDirectory);
        
        JOptionPane.showMessageDialog(null, "Please choose an Image file for the texture");
        textureFileName = GeneralFileHelper.importPathUsingFileSelector(initImportDirectory);
    }
    
    
}
