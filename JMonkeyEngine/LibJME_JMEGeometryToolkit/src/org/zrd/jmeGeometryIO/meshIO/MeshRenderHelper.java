/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryIO.meshIO;

import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;

/**
 * This is class that contains methods that help
 *      with rendering meshes in JME
 *
 * @author Zach
 */
public class MeshRenderHelper {

    /**
     * This takes in a TriangleSet object from the Geometry Toolkit
     *      and makes a renderable jme mesh out of it. It is supposed to be
     *      used after the toolkit has made edits to a mesh. 
     * 
     * Details on the logic behind the code is in this page:
     * http://hub.jmonkeyengine.org/wiki/doku.php/jme3:advanced:custom_meshes
     * 
     * @param triangles     the triangleSet object
     * @param material      the material to put onto the mesh
     * @return              the spatial for the mesh
     */
    public static Spatial createMeshFromTriangles(TriangleSet triangles, Material material) {
        Mesh m = new Mesh();
        
        //gets the triangles
        ArrayList<MeshTriangle> meshTris = triangles.getTriangleList();
        
        MeshTriangle currentTriangle;
        
        //will store all the vertices of the mesh
        Vector3f[] vertices = new Vector3f[meshTris.size() * 3];
        
        //will store the mesh texture coordinates
        Vector2f[] texCoord = new Vector2f[meshTris.size() * 3];
        
        //will store the indices of the vertices of the mesh
        int[] indexes = new int[meshTris.size() * 3];
        
        /*
         * This loops through all the triangles, adding their vertices, index of 
         *      vertices, and texture coordinates to their respective arrays. 
         * 
         * NOTE: 
         * Vertices are actually repeated here. For every triangle, the
         *      vertices are added to the array and the index of the vertex
         *      is added in the order that the vertex was added. If a vertex
         *      has multiple triangles attached to it, then it will get added
         *      multiple times. 
         * This was done to ensure correctness of the mesh. It also only
         *      adds a linear factor to the time and memory requirement so in 
         *      practice it was not noticeable. 
         * In the future, this could possibly be rewritten so each vertex 
         *      is only added once. The triangleSet object keeps track
         *      of the vertices and their adjacent triangles, so those data
         *      structures could be leveraged easily. 
         */
        for (int index = 0; index < meshTris.size(); index++) {
            
            //gets the current triangle
            currentTriangle = meshTris.get(index);
            
            //puts its vertices into the vertex array
            vertices[3 * index] = currentTriangle.getVertex1().getVertex();
            vertices[3 * index + 1] = currentTriangle.getVertex2().getVertex();
            vertices[3 * index + 2] = currentTriangle.getVertex3().getVertex();
            
            //if texture coordinates exist, puts them in
            if (currentTriangle.getTextureCoords() != null) {
                texCoord[3 * index] = currentTriangle.getVertex1().getTextureCoord();
                texCoord[3 * index + 1] = currentTriangle.getVertex2().getTextureCoord();
                texCoord[3 * index + 2] = currentTriangle.getVertex3().getTextureCoord();
            }
            
            //adds the index of the vertices to the index array
            indexes[3 * index] = 3 * index;
            indexes[3 * index + 1] = 3 * index + 1;
            indexes[3 * index + 2] = 3 * index + 2;
        }
        
        //adds the arrays to the vertex buffer in the mesh
        m.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        m.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
        m.setBuffer(VertexBuffer.Type.Index, 1, BufferUtils.createIntBuffer(indexes));
        m.updateBound();
        
        //makes the jme geometry object out of the newly created mesh
        Geometry geom = new Geometry("OurMesh", m);
        geom.setMaterial(material);
        return geom;
    }
    
}
