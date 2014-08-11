/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.fileHelper;

import java.io.File;

/**
 *
 * @author Zach
 */
public class MeshDataFiles {
    
    private File objFile;
    private File textureFile;

    public File getObjFile() {
        return objFile;
    }

    public File getTextureFile() {
        return textureFile;
    }
    
    public MeshDataFiles(File objFile, File textureFile){
        this.objFile = objFile;
        this.textureFile = textureFile;
    }
    
    
}
