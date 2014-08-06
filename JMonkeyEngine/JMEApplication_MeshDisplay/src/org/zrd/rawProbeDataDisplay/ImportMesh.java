/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.rawProbeDataDisplay;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.io.File;
import org.zrd.geometryToolkit.geometryUtil.GeometryDataHelper;
import org.zrd.geometryToolkit.pathDataStructure.RecordedPathSet;
import org.zrd.jmeGeometryIO.meshIO.MeshDataFiles;
import org.zrd.jmeGeometryIO.meshIO.MeshInputHelper;
import static org.zrd.jmeGeometryInteractions.pathInteraction.PathImport.IMPORT_ACTION_NAME;
import static org.zrd.jmeGeometryInteractions.pathInteraction.PathImport.IMPORT_KEYBOARD_KEY;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;
import org.zrd.util.fileHelper.GeneralFileHelper;

/**
 *
 * @author BLI
 */
public class ImportMesh extends GeneralKeyboardActionMethod{
    
    /**
     * the name for the input manager of the import action
     */
    public static final String IMPORT_ACTION_NAME = "importMesh";
    
    /**
     * the keyboard key that when pressed is supposed to trigger
     *      the import action
     */
    public static final int IMPORT_KEYBOARD_KEY = KeyInput.KEY_I;
    
    private File initDirectory;
    private boolean newMeshExists;
    private Node rootNode;
    private AssetManager assetManager;
    private Camera cam;
    
    /**
     * Initializes the class which keeps track of when the user hits "i" 
     *      for import and then calls the appropriate methods
     * @param inputManager      the application's input manager, which handles keyboard actions
     * @param recordedPathSet   the set of recorded paths in the application
     * @param initDirectory     the initial file directory to go to for path import
     */
    public ImportMesh(InputManager inputManager, AssetManager assetManager, Node rootNode, File initDirectory, Camera cam){
        super(inputManager,IMPORT_ACTION_NAME,IMPORT_KEYBOARD_KEY);
        this.initDirectory = initDirectory;
        this.rootNode = rootNode;
        this.initDirectory = initDirectory;
        this.assetManager = assetManager;
        this.cam = cam;
    }

    /**
     * This is the method that gets called when the user selects the import key
     *      and it calls the methods that import a line and put it into the 
     *      recorded path set. It also specifies if a line was selected and if so
     *      then the loop in charge of rendering will render the new path.
     */
    @Override
    public void actionMethod() {
        
        //calls the method to get a file
        MeshDataFiles meshFiles = MeshInputHelper.obtainFiles(initDirectory);
        
        
        //whether a new mesh exists
        //newMeshExists = (selectedFile != null);
        newMeshExists = true;
        
        MeshImportData meshImport = new MeshImportData(assetManager,
                meshFiles.getObjFile(),
                meshFiles.getTextureFile());
        
        //puts the new path into the recorded path set for later rendering
        if(newMeshExists){
            rootNode.attachChild(meshImport.getFinalMesh());
            cam.setLocation(meshImport.getCameraCenter());
        }
    }

    /**
     * Whether a new path exists or not. It is meant to return true only
     *      once for each new path, so once it returns true, it sets the variable
     *      back to false
     * @return      true if a path was just imported, false if not
     */
    public boolean isNewPathExists() {
        if(newMeshExists){
            newMeshExists = false;
            return true;
        }else{
            return false;
        }
    }
    
}
