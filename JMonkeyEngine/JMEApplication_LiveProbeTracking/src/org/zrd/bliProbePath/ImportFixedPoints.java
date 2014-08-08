/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.bliProbePath;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.math.Vector3f;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import org.zrd.geometryToolkit.geometryUtil.GeometryDataHelper;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;
import org.zrd.util.fileHelper.GeneralFileHelper;

/**
 *
 * @author BLI
 */
public class ImportFixedPoints extends GeneralKeyboardActionMethod{

    private Path initDirectory;
    private boolean newPointsImported = false;
    private FixedPointIO importedPoints;
    
    public ImportFixedPoints(InputManager inputManager, Path initDirectory){
        super(inputManager,"importFixedPoints",KeyInput.KEY_Y);
        this.initDirectory = initDirectory;
    }
    
    @Override
    public void actionMethod() {
        File fileToImport = GeneralFileHelper.importPathUsingFileSelector(initDirectory.toFile());
        ArrayList<Vector3f> vertices = GeometryDataHelper.getVerticesFromFile(fileToImport);
        importedPoints = new FixedPointIO(vertices);
        if(importedPoints.getFixedPoints().size() > 0){
            newPointsImported = true;
        }
    }

    public boolean isNewPointsImported() {
        if(newPointsImported){
            newPointsImported = false;
            return true;
        }else{
            return false;
        }
    }

    public FixedPointIO getImportedPoints() {
        return importedPoints;
    }
    
    
    
    
    
}
