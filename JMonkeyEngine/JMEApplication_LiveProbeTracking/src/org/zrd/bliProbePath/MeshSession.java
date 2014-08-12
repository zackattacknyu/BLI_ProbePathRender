/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.bliProbePath;

import com.jme3.asset.AssetManager;
import com.jme3.renderer.Camera;
import java.nio.file.Path;
import org.zrd.jmeGeometryIO.meshIO.MeshInputHelper;
import org.zrd.jmeGeometryIO.meshIO.MeshRenderData;
import org.zrd.util.fileHelper.MeshInteractionFiles;

/**
 *
 * @author BLI
 */
public class MeshSession {
    
    private MeshInteractionFiles meshInterFiles;
    private MeshRenderData importedMesh;
    
    public MeshSession(Path meshDataPath,String defaultSuffix,AssetManager assetManager, Camera cam){
        
    }
    
}
