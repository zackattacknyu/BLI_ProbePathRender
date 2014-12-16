/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.pointTools;

import com.jme3.math.Vector3f;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import org.zrd.geometryToolkit.geometryUtil.GeometryDataHelper;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.util.fileHelper.GeneralFileHelper;

/**
 * This imports and exports fixed points on the mesh to and from text files.
 * 
 * For fixed points on the mesh, we need to know the points as well
 *      as their corresponding triangles
 * 
 * The text files with fixed points consist of sets of 4 vertices:
 *      {fixed points coordinates}
 *      {corresponding triangle vertex 1 coordinates}
 *      {corresponding triangle vertex 2 coordinates}    
 *      {corresponding triangle vertex 3 coordinates}    
 *
 * @author Zach
 */
public class FixedPointIO {
    
    private ArrayList<Vector3f> outputVertices;
    private ArrayList<Vector3f> pointCoords;
    private Collection<PointData> fixedPointsOnMesh;
    
    /**
     * This initializes the object and is meant to be used when we
     *      are exporting fixed points to a text file.
     */
    public FixedPointIO(){
        outputVertices = new ArrayList<Vector3f>();
        pointCoords = new ArrayList<Vector3f>();
    }
    
    /**
     * This takes a file, reads it vertices, and then puts the
     *      vertices into a list for the constructor to parse
     *      through and get the fixed points
     *      and corresponding triangles
     * @param fileToImport      file with vertex data
     * @return                  object which contains fixed points and their triangles
     */
    public static FixedPointIO getPointsFromFile(File fileToImport){
        ArrayList<Vector3f> vertices = GeometryDataHelper.getVerticesFromFile(fileToImport);
        return new FixedPointIO(vertices);
    }

    /**
     * This takes a list of vertices and imports the fixed points
     *      and corresponding triangles from the list
     * @param outputVerts   list of vertices which have points and their triangles
     */
    public FixedPointIO(ArrayList<Vector3f> outputVerts){
        
        fixedPointsOnMesh = new ArrayList<PointData>();
        
        int numVerts = outputVerts.size();
        
        Vector3f currentFixedPt;
        MeshTriangle currentTriangle;
        
        /*
         * The vertices come in sets of 4 as described above, 
         *      so there needs to be a multiple of 4 number
         *      of vertices in the list
         */
        if(numVerts % 4 == 0){
            
            //goes through each set of 4
            for(int i = 0; i < numVerts/4; i++){
                
                //gets the fixed point in the set
                currentFixedPt = outputVerts.get(i*4).clone();
                
                //gets the corresponding triangle in the set
                currentTriangle = new MeshTriangle(outputVerts.get(i*4 + 1),outputVerts.get(i*4 + 2),outputVerts.get(i*4 + 3));
                
                //puts the data into the collection
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
    
    /**
     * This takes in a point and its triangle and adds it to the vertex output
     *      list. 
     * @param fixedPoint                the point on the mesh
     * @param correspondingTriangle     the point's triangle on the mesh
     */
    public void addPoint(Vector3f fixedPoint, MeshTriangle correspondingTriangle){
        outputVertices.add(fixedPoint);
        outputVertices.add(correspondingTriangle.getVertex1().getVertex());
        outputVertices.add(correspondingTriangle.getVertex2().getVertex());
        outputVertices.add(correspondingTriangle.getVertex3().getVertex());
        
        pointCoords.add(fixedPoint);
    }
    
    /**
     * This writes the output vertex data to a text file that can later
     *      be read in by this same class and outputs the file object
     * @param dataPath      path to put text file data
     * @return              file object where data was written to
     */
    public File writeInformationToFile(Path dataPath){
        System.out.println("Now putting vertices into text file");
        Path textFilePath = GeneralFileHelper.getNewDataFilePath(dataPath, "fixedPoints");
        GeometryDataHelper.writeVerticesToFile(outputVertices, textFilePath);
        
        Path pointCoordFilePath = GeneralFileHelper.getNewDataFilePath(dataPath, "fixedPointsCoords");
        GeometryDataHelper.writeVerticesToFile(pointCoords, pointCoordFilePath);
        System.out.println("Finished adding vertices to text file");
        return textFilePath.toFile();
    }
    
}
