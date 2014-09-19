/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryInteractions.pathInteraction;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import java.io.File;
import org.zrd.geometryToolkit.geometryUtil.GeometryDataHelper;
import org.zrd.geometryToolkit.pathDataStructure.RecordedPathSet;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;
import org.zrd.util.fileHelper.FileDataHelper;
import org.zrd.util.fileHelper.GeneralFileHelper;

/**
 * This is the class that is in charge of reading when the user hits
 *      "i" for import and then calling the methods that do the file
 *      import and render the line
 *
 * @author BLI
 */
public class PathImport extends GeneralKeyboardActionMethod{
    
    /**
     * the name for the input manager of the import action
     */
    public static final String IMPORT_ACTION_NAME = "importLine";
    
    /**
     * the keyboard key that when pressed is supposed to trigger
     *      the import action
     */
    public static final int IMPORT_KEYBOARD_KEY = KeyInput.KEY_I;
    
    private RecordedPathSet recordedPathSet;
    private File initDirectory;
    private boolean newPathExists;
    
    /**
     * Initializes the class which keeps track of when the user hits "i" 
     *      for import and then calls the appropriate methods
     * @param inputManager      the application's input manager, which handles keyboard actions
     * @param recordedPathSet   the set of recorded paths in the application
     * @param initDirectory     the initial file directory to go to for path import
     */
    public PathImport(InputManager inputManager, RecordedPathSet recordedPathSet, File initDirectory){
        super(inputManager,IMPORT_ACTION_NAME,IMPORT_KEYBOARD_KEY);
        this.initDirectory = initDirectory;
        this.recordedPathSet = recordedPathSet;
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
        File selectedFile = GeneralFileHelper.importPathUsingFileSelector(initDirectory);
        
        //whether a new path exists
        newPathExists = (selectedFile != null);
        
        //puts the new path into the recorded path set for later rendering
        if(newPathExists){
            //recordedPathSet.addPath(GeometryDataHelper.getVerticesFromFile(selectedFile));
            recordedPathSet.addPath(GeometryDataHelper.getSegmentSetFromFile(selectedFile));
        }
    }

    /**
     * Whether a new path exists or not. It is meant to return true only
     *      once for each new path, so once it returns true, it sets the variable
     *      back to false
     * @return      true if a path was just imported, false if not
     */
    public boolean isNewPathExists() {
        if(newPathExists){
            newPathExists = false;
            return true;
        }else{
            return false;
        }
    }
    
    
    
    
}
