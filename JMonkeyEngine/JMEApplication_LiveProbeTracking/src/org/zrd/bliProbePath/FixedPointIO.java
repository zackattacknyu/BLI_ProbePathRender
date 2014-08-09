/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.bliProbePath;

import com.jme3.math.Vector3f;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import org.zrd.geometryToolkit.geometryUtil.GeometryDataHelper;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.pointTools.FixedPointPicker;
import org.zrd.geometryToolkit.pointTools.PointData;
import org.zrd.geometryToolkit.pointTools.PointOnMeshData;

/**
 *
 * @author Zach
 */
public class FixedPointIO {
    
    private ArrayList<Vector3f> outputVertices;
    private Collection<PointData> fixedPointsOnMesh;
    
    public FixedPointIO(){
        outputVertices = new ArrayList<Vector3f>();
    }

    public FixedPointIO(ArrayList<Vector3f> outputVerts){
        
        fixedPointsOnMesh = new ArrayList<PointData>();
        
        int numVerts = outputVerts.size();
        
        Vector3f currentFixedPt;
        MeshTriangle currentTriangle;
        
        if(numVerts % 4 == 0){
            for(int i = 0; i < numVerts/4; i++){
                
                currentFixedPt = outputVerts.get(i*4).clone();
                currentTriangle = new MeshTriangle(outputVerts.get(i*4 + 1),outputVerts.get(i*4 + 2),outputVerts.get(i*4 + 3));
                
                fixedPointsOnMesh.add(new PointOnMeshData(currentFixedPt,currentTriangle));
                
            }
        }
        
    }

    public Collection<PointData> getFixedPointsOnMesh() {
        return fixedPointsOnMesh;
    }
    
    public FixedPointPicker getFixedPtPicker(){
        return new FixedPointPicker(fixedPointsOnMesh);
    }
    
    public void addPoint(Vector3f fixedPoint, MeshTriangle correspondingTriangle){
        outputVertices.add(fixedPoint);
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
