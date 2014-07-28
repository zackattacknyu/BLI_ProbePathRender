/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.probeTrackingOnSurface;

import org.zrd.jmeGeometryInteractions.meshInteraction.PickPointOnMesh;
import org.zrd.geometryToolkit.geometryUtil.MeshPointHandler;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;
import org.zrd.probeTracking.ProbeTracker;
import org.zrd.util.dataWriting.ProbeDataWriter;
import org.zrd.util.fileHelper.FileDataHelper;

/**
 *
 * @author BLI
 */
public class PickAndRecordPoint extends GeneralKeyboardActionMethod implements MeshPointHandler{

    private boolean pickPointEnabled = false;
    private Path recordedFilePath;
    
    public PickAndRecordPoint(InputManager inputManager, Camera cam, Node shootableMesh, Path recordedFilePath){
        super(inputManager,"pickAndRecordPoint",KeyInput.KEY_P);
        new PickPointOnMesh("pickPointForRecording",inputManager,cam,this,shootableMesh);
        this.recordedFilePath = recordedFilePath;
    }
    
    @Override
    public void actionMethod() {
        pickPointEnabled = !pickPointEnabled;
        if(pickPointEnabled){
            System.out.println("Recording picked points is now enabled");
        }else{
            System.out.println("Recording picked points is now disabled");
        }
    }

    public void handleNewMeshPoint(Vector3f pointOnMesh, Triangle triangleOnMesh) {
        ArrayList<String> fileStrings = new ArrayList<String>(10);
        fileStrings.add(String.valueOf(pointOnMesh));
        fileStrings.add(String.valueOf(triangleOnMesh));
        try {
            Path outputFilePath = ProbeDataWriter.getNewDataFilePath(recordedFilePath, "meshPointPicked");
            FileDataHelper.exportLinesToFile(fileStrings, outputFilePath);
        } catch (IOException ex) {
            System.out.println("Error trying to record point: " + ex);
        }
        
    }
    
    
    
}
