/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.rawProbeDataDisplay;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import java.io.File;
import org.zrd.jmeGeometryIO.meshIO.MeshDataFiles;
import org.zrd.jmeGeometryIO.meshIO.MeshInputHelper;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;

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
    private AssetManager assetManager;
    private MeshImportData currentMeshImport;
    
    /**
     * Initializes the class which keeps track of when the user hits "i" 
     *      for import and then calls the appropriate methods
     * @param inputManager      the application's input manager, which handles keyboard actions
     * @param recordedPathSet   the set of recorded paths in the application
     * @param initDirectory     the initial file directory to go to for path import
     */
    public ImportMesh(InputManager inputManager, AssetManager assetManager, File initDirectory){
        super(inputManager,IMPORT_ACTION_NAME,IMPORT_KEYBOARD_KEY);
        this.initDirectory = initDirectory;
        this.initDirectory = initDirectory;
        this.assetManager = assetManager;
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
        
        currentMeshImport = new MeshImportData(assetManager,
                meshFiles.getObjFile(),
                meshFiles.getTextureFile());
    }

    public MeshImportData getCurrentMeshImport() {
        return currentMeshImport;
    }

    /**
     * Whether a new path exists or not. It is meant to return true only
     *      once for each new path, so once it returns true, it sets the variable
     *      back to false
     * @return      true if a path was just imported, false if not
     */
    public boolean isNewMeshExists() {
        if(newMeshExists){
            newMeshExists = false;
            return true;
        }else{
            return false;
        }
    }
    
}
