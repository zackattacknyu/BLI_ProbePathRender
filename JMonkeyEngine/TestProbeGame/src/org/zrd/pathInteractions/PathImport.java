/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.pathInteractions;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import java.io.File;
import org.zrd.geometryToolkit.geometryUtil.ProbeDataHelper;
import org.zrd.geometryToolkit.pathDataStructure.RecordedPathSet;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;
import org.zrd.util.fileHelper.FileDataHelper;

/**
 *
 * @author BLI
 */
public class PathImport extends GeneralKeyboardActionMethod{
    
    private RecordedPathSet recordedPathSet;
    private File initDirectory;
    private boolean newPathExists;
    
    public PathImport(InputManager inputManager, RecordedPathSet recordedPathSet, File initDirectory){
        super(inputManager,"importLine",KeyInput.KEY_I);
        this.initDirectory = initDirectory;
        this.recordedPathSet = recordedPathSet;
    }

    @Override
    public void actionMethod() {
        File selectedFile = FileDataHelper.importPathUsingFileSelector(initDirectory);
        newPathExists = (selectedFile != null);
        if(newPathExists){
            recordedPathSet.addPath(ProbeDataHelper.getVerticesFromFile(selectedFile));
        }
    }

    public boolean isNewPathExists() {
        if(newPathExists){
            newPathExists = false;
            return true;
        }else{
            return false;
        }
    }
    
    
    
    
}
