/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryIO.meshIO;

import org.zrd.geometryToolkit.meshDataStructure.TriangleTexture;
import com.jme3.math.Vector2f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.mesh.IndexBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * These objects contain the texture data for the triangles in 
 *      a mesh. It obtains the texture coordinates for each
 *      of the triangles and then outputs them once given
 *      the index of the triangle. 
 * It does not interact with the triangleSet object at all.
 *      That is done in a different place. The triangle
 *      index is done by JME and this merely retrieves the texture
 *      coordinates which can be added to the data structure
 *      via a different class. 
 *
 * @author BLI
 */
public class MeshTextureData {
    
    private ArrayList<Vector2f> texCoordsList;
    private Mesh mesh;
    
    /**
     * Initializes the object by taking a mesh
     *      and then constructing the texture coordinate list
     * @param mesh 
     */
    public MeshTextureData(Mesh mesh){
        this.mesh = mesh;
        texCoordsList = MeshInputHelper.constructTextureCoordList(mesh);
        
    }

    /**
     * This returns the texture coordinats for each vertex in a triangle. 
     *      The data is stored in a TriangleTexture object, one of the objects
     *      from the geometry toolkit
     * @param index     index of the triangle in the mesh
     * @return          TriangleTexture showing the tex coord at each vertex
     */
    public TriangleTexture getTriangleTextCoords(int index){
        
        if(texCoordsList == null) return null;
        
        //gets the index buffer for the mesh
        IndexBuffer ib = mesh.getIndicesAsList();

        // aquire triangle's vertex indices
        int vertIndex = index * 3;
        int vert1 = ib.get(vertIndex);
        int vert2 = ib.get(vertIndex+1);
        int vert3 = ib.get(vertIndex+2);
        
        //make the triangle texture object
        return new TriangleTexture(texCoordsList.get(vert1),
                texCoordsList.get(vert2),
                texCoordsList.get(vert3));
    }
    
}
