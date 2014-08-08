/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.bliProbePath;

import com.jme3.math.Vector3f;
import java.nio.file.Path;
import java.util.ArrayList;
import org.zrd.geometryToolkit.geometryUtil.GeometryDataHelper;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;

/**
 *
 * @author Zach
 */
public class FixedPointIO {
    
    private ArrayList<Vector3f> fixedPoints;
    private ArrayList<MeshTriangle> correspondingTriangles;
    private ArrayList<Vector3f> outputVertices;
    
    public FixedPointIO(){
        fixedPoints = new ArrayList<Vector3f>();
        correspondingTriangles = new ArrayList<MeshTriangle>();
        outputVertices = new ArrayList<Vector3f>();
    }

    public ArrayList<Vector3f> getFixedPoints() {
        return fixedPoints;
    }

    public ArrayList<MeshTriangle> getCorrespondingTriangles() {
        return correspondingTriangles;
    }
    
    public FixedPointIO(ArrayList<Vector3f> outputVerts){
        
        fixedPoints = new ArrayList<Vector3f>();
        correspondingTriangles = new ArrayList<MeshTriangle>();
        
        int numVerts = outputVerts.size();
        
        if(numVerts % 4 == 0){
            for(int i = 0; i < numVerts/4; i++){
                
                fixedPoints.add(outputVerts.get(i*4 + 0).clone());
                correspondingTriangles.add(new MeshTriangle(
                        outputVerts.get(i*4 + 1),
                        outputVerts.get(i*4 + 2),
                        outputVerts.get(i*4 + 3)));
                
            }
        }
        
    }
    
    public void addPoint(Vector3f fixedPoint, MeshTriangle correspondingTriangle){
        fixedPoints.add(fixedPoint);
        outputVertices.add(fixedPoint);
        correspondingTriangles.add(correspondingTriangle);
        outputVertices.add(correspondingTriangle.getVertex1().getVertex());
        outputVertices.add(correspondingTriangle.getVertex2().getVertex());
        outputVertices.add(correspondingTriangle.getVertex3().getVertex());
    }
    
    public void writeInformationToFile(Path dataPath){
        System.out.println("Now putting vertices into text file");
        GeometryDataHelper.writeVerticesToDataFile(outputVertices,dataPath, "fixedPoints");
        System.out.println("Finished adding vertices to text file");
    }
    
}
