/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryInteractions.meshInteraction;

import org.zrd.geometryToolkit.geometryUtil.MeshPointHandler;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.math.Matrix4f;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.jmeGeometryIO.meshIO.MeshInputHelper;
import org.zrd.jmeUtil.mouseKeyboard.GeneralKeyboardActionMethod;
import org.zrd.util.dataWriting.ProbeDataWriter;
import org.zrd.util.fileHelper.FileDataHelper;

/**
 *
 * @author BLI
 */
public class PickAndRecordPoint extends GeneralKeyboardActionMethod implements MeshPointHandler{

    private boolean pickPointEnabled = false;
    private Path recordedFilePath;
    private Matrix4f triangleTransform;
    
    public PickAndRecordPoint(InputManager inputManager, Camera cam, Node shootableMesh, Path recordedFilePath, Matrix4f triangleTransform){
        super(inputManager,"pickAndRecordPoint",KeyInput.KEY_P);
        new PickPointOnMesh("pickPointForRecording",inputManager,cam,this,shootableMesh);
        this.recordedFilePath = recordedFilePath;
        this.triangleTransform = triangleTransform;
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

    @Override
    public void handleNewMeshPoint(Vector3f pointOnMesh, Triangle triangleOnMesh) {
        ArrayList<String> fileStrings = new ArrayList<String>(10);
        ArrayList<String> pointStrings = new ArrayList<String>(10);
        ArrayList<String> currentTriStrings = new ArrayList<String>(10);
        ArrayList<String> origTriStrings = new ArrayList<String>(10);
        
        pointStrings.add("Point On Mesh: ");
        pointStrings.add(String.valueOf(pointOnMesh));
        
        /*
         * TODO: Fix why this block is not giving the original triangle
         * 
        origTriStrings.add(" ");
        origTriStrings.add("Triangle On Original Mesh:");
        origTriStrings.add(String.valueOf(triangleOnMesh.get1()));
        origTriStrings.add(String.valueOf(triangleOnMesh.get2()));
        origTriStrings.add(String.valueOf(triangleOnMesh.get3()));
        */

        MeshTriangle meshTri = MeshInputHelper.convertInputTriangleToMeshTriangle(triangleOnMesh, triangleTransform);
        currentTriStrings.add(" ");
        currentTriStrings.add("Triangle On Current Mesh: ");
        currentTriStrings.add(String.valueOf(meshTri.getVertex1()));
        currentTriStrings.add(String.valueOf(meshTri.getVertex2()));
        currentTriStrings.add(String.valueOf(meshTri.getVertex3()));
        
        //decide the order in which to put the strings
        fileStrings.addAll(pointStrings);
        fileStrings.addAll(currentTriStrings);
        fileStrings.addAll(origTriStrings);
        
        
        try {
            Path outputFilePath = ProbeDataWriter.getNewDataFilePath(recordedFilePath, "meshPointPicked");
            FileDataHelper.exportLinesToFile(fileStrings, outputFilePath);
        } catch (IOException ex) {
            System.out.println("Error trying to record point: " + ex);
        }
        
    }
    
    
    
}
