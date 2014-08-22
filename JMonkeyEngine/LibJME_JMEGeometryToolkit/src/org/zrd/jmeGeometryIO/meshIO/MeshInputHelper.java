/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryIO.meshIO;

import org.zrd.util.fileHelper.MeshDataFiles;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.material.Material;
import com.jme3.math.Matrix4f;
import com.jme3.math.Triangle;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import java.io.File;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;
import org.zrd.util.fileHelper.MeshInteractionFiles;
import org.zrd.jmeUtil.materials.MaterialHelper;
import org.zrd.util.fileHelper.GeneralFileHelper;

/**
 * This contains code that helps with taking a mesh from an OBJ file
 *      or other source and add it to the TriangleSet data structure
 *      from the Geometry toolkit
 *
 * @author BLI
 */
public class MeshInputHelper {
    
    /**
     * This takes a data buffer from the mesh and outputs the corresponding
     *      FloatBuffer object
     * @param mesh      the JME mesh
     * @param type      the type of buffer
     * @return          the FloatBuffer for that mesh corresponding to the type
     */
    public static FloatBuffer obtainMeshBuffer(Mesh mesh, VertexBuffer.Type type){
        VertexBuffer pb = mesh.getBuffer(type);
        if(pb == null) return null;
        FloatBuffer fpb = (FloatBuffer) pb.getData();
        return fpb;
    }
    
    /**
     * This takes in a mesh and returns the texture coordinate list
     *      where the index in the list is the same as the index
     *      of the vertex in the mesh
     * @param mesh      the Mesh object
     * @return          the corresponding texture coordinate list
     */
    public static ArrayList<Vector2f> constructTextureCoordList(Mesh mesh){
        
        //gets the texture coordinate buffer
        FloatBuffer fb = obtainMeshBuffer(mesh,VertexBuffer.Type.TexCoord);
        if(fb == null){
            return null;
        }
        
        ArrayList<Vector2f> textureCoordinates = 
                new ArrayList<Vector2f>(mesh.getVertexCount());
        fb.clear();
        
        //assembles the tex coord list using the buffer
        while(fb.remaining() > 0){
            float x = fb.get();
            float y = fb.get();
            textureCoordinates.add(new Vector2f(x,y));
        }
        return textureCoordinates;
    }
    
    /**
     * This adds a JME mesh to a TriangleSet object form the 
     *      geometry toolkit package
     * @param mesh                  the JME mesh
     * @param meshTransform         a transform to apply to the vertices if necessary
     * @param inputSet              triangleSet to add to
     * @return 
     */
    public static TriangleSet addMeshToTriangleSet(Mesh mesh, 
            Matrix4f meshTransform, TriangleSet inputSet){
        
        Triangle currentTri;
        MeshTriangle newTri;
        
        //gets the texture information to add
        MeshTextureData meshData = new MeshTextureData(mesh);

        for(int index = 0; index < mesh.getTriangleCount(); index++){
            
            //retrieves the jme triangle from the jme mesh
            currentTri = new Triangle();
            mesh.getTriangle(index, currentTri);
            
            //converts the triangle to a mesh triangle
            newTri = convertInputTriangleToMeshTriangle(currentTri,meshTransform);
            
            //adds the texture coordinates
            newTri.setTextureCoords(meshData.getTriangleTextCoords(index));
            
            //adds the triangle to the triangleSet
            inputSet.addTriangle(newTri);

        }
        return inputSet;
    }
    
    /**
     * This converts a JME triangle to a MeshTriangle from the Geometry toolkit.
     *      It will also apply a transform to the vertices in case that's 
     *      necessary. 
     * @param triangle      an input JME triangle
     * @param transform     a transform to apply to the vertices
     * @return              the transformed triangle in MeshTriangle form
     */
    public static MeshTriangle convertInputTriangleToMeshTriangle(
            Triangle triangle, Matrix4f transform){
        Vector3f vert1 = triangle.get1();
        Vector3f vert2 = triangle.get2();
        Vector3f vert3 = triangle.get3();
        
        Vector3f vert1Trans = transform.mult(vert1);
        Vector3f vert2Trans = transform.mult(vert2);
        Vector3f vert3Trans = transform.mult(vert3);
        
        return new MeshTriangle(vert1Trans,vert2Trans,vert3Trans);
    }

    /**
     * This is used to import a file
     * @param objFileLocation       the file location
     * @param ballMat               the material to put onto the mesh
     * @param assetManager          the application's asset manager
     * @return                      the jme spatial for the mesh
     */
    public static MeshRenderData generateModel(String objFileLocation, 
            Material ballMat, AssetManager assetManager) {
        Spatial sampleMesh = assetManager.loadModel(objFileLocation);
        sampleMesh.setMaterial(ballMat);
        return new MeshRenderData(sampleMesh,ballMat);
    }

    /**
     * This is used to import a file
     * @param objFile               the model file
     * @param ballMat               the material to put onto the mesh
     * @param assetManager          the application's asset manager
     * @return                      the jme spatial for the mesh
     */
    public static MeshRenderData generateModel(File objFile, 
            Material ballMat, AssetManager assetManager) {
        assetManager.registerLocator(objFile.getParentFile().getAbsolutePath(), FileLocator.class);
        return generateModel(objFile.getName(),ballMat,assetManager);
    }
    
    /**
     * This is used to import a file
     * @param objFile               the model file
     * @param textureFile           the texture to overlay onto the mesh
     * @param assetManager          the application's asset manager
     * @return                      the jme spatial for the mesh
     */
    public static MeshRenderData generateModel(File objFile, 
            File textureFile, AssetManager assetManager) {
        Material objectMaterial = MaterialHelper.getTextureMaterial(assetManager, textureFile);
        //if(wireframeOn) objectMaterial.getAdditionalRenderState().setWireframe(true);
        
        return generateModel(objFile, objectMaterial, assetManager);
    }
    
    public static MeshRenderData generateRenderData(MeshDataFiles meshFiles, AssetManager assetManager){
        return generateModel(meshFiles.getObjFile(),
                meshFiles.getTextureFile(),assetManager);
    }
    
    public static MeshRenderData selectFilesAndGenerateRenderData(File initImport, AssetManager assetManager){
        MeshInteractionFiles meshFiles = MeshInputHelper.obtainAllFiles(initImport);
        if(meshFiles == null){
            return null;
        }else{
            return generateRenderData(meshFiles.getDataFiles(),assetManager);
        }
    }

    /**
     * This adds a general spatial to a triangleSet. It is meant to be used
     *      after an OBJ file has been loaded and become a spatial. Most 
     *      importantly, this is a recursive routine that will make sure to add
     *      the leaves in case the Spatial is a tree structure. 
     * 
     * @param meshInfo      the triangleSet to add 
     * @param surface       the spatial to add to the set
     * @param transform     the transform to apply to the spatial if necessary
     * @return              the TriangleSet with the geometries in the Spatial added
     */
    public static TriangleSet addToTriangleSet(TriangleSet meshInfo, 
            Spatial surface, Matrix4f transform) {
        
        //if the spatial is a node, then it recurses into its children
        if (surface instanceof Node) {
            Node surfaceNode = (Node) surface;
            
            for (Spatial child : surfaceNode.getChildren()) {
                
                //recurse into the child
                meshInfo = addToTriangleSet(meshInfo, child, transform);
            }
        } else if (surface instanceof Geometry) {
            
            //we are at a leaf, so add the mesh object to the triangleSet
            meshInfo = addMeshToTriangleSet( ((Geometry) surface).getMesh(), 
                    transform, meshInfo);
        }
        return meshInfo;
    }

    public static MeshDataFiles obtainFiles(File initImportDirectory) {
        
        JOptionPane.showMessageDialog(null, "Please choose an OBJ File for the 3D Model");
        File objFile = GeneralFileHelper.importPathUsingFileSelector(initImportDirectory);
        
        if(objFile == null) return null;
        
        JOptionPane.showMessageDialog(null, "Please choose an Image file for the texture");
        File textureFile = GeneralFileHelper.importPathUsingFileSelector(initImportDirectory);
        
        if(textureFile == null) return null;
        
        return new MeshDataFiles(objFile, textureFile);
    }
    
    public static MeshInteractionFiles obtainAllFiles(File initImportDirectory, String defaultFileSuffix) {
        
        File objFile = GeneralFileHelper.importPathUsingFileSelector(initImportDirectory,"obj");
        
        if(objFile == null){
            if(defaultFileSuffix == null){
                return null;
            }else{
                return new MeshInteractionFiles(initImportDirectory.toPath(),defaultFileSuffix);
            }
            
        }
        
        return new MeshInteractionFiles(objFile);
    }
    
    public static MeshInteractionFiles obtainAllFiles(File initImportDirectory) {
        return obtainAllFiles(initImportDirectory,null);
    }
    
}
