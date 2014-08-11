/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.bliProbePath;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import javax.swing.JOptionPane;
import org.zrd.jmeGeometryInteractions.meshInteraction.MeshInteractionFiles;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;

/**
 *
 * @author BLI
 */
public class RecordMeshSessionInfo extends GeneralKeyboardActionMethod{
    
    private MeshInteractionFiles meshSessionFiles;
    
    public RecordMeshSessionInfo(InputManager manager,MeshInteractionFiles meshSessionFiles){
        super(manager,"recordMeshSessionInfo",KeyInput.KEY_V);
        this.meshSessionFiles = meshSessionFiles;
    }

    @Override
    public void actionMethod() {
        int option = JOptionPane.showConfirmDialog(
                null,
                "Do you want to copy the last saved camera coordinates "
                + "and the last saved fixed points\n"
                + "to the folder containing the mesh and texture?\n"
                + "This will overwrite previous files there.",
                "Save Session Info?", JOptionPane.YES_NO_OPTION);
        
        if(option == JOptionPane.YES_OPTION){
            System.out.println("Yes Option Chosen");
            meshSessionFiles.copyFixedPointsFile();
        }
    }
    
}
