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
     * @param triangles     the triangleSet object
     * @param material      the material to put onto the mesh
     * @return              the spatial for the mesh
     */
    public static Spatial createMeshFromTriangles(TriangleSet triangles, Material material) {
        Mesh m = new Mesh();
        ArrayList<MeshTriangle> meshTris = triangles.getTriangleList();
        MeshTriangle currentTriangle;
        Vector3f[] vertices = new Vector3f[meshTris.size() * 3];
        Vector2f[] texCoord = new Vector2f[meshTris.size() * 3];
        int[] indexes = new int[meshTris.size() * 3];
        for (int index = 0; index < meshTris.size(); index++) {
            currentTriangle = meshTris.get(index);
            vertices[3 * index] = currentTriangle.getVertex1().getVertex();
            vertices[3 * index + 1] = currentTriangle.getVertex2().getVertex();
            vertices[3 * index + 2] = currentTriangle.getVertex3().getVertex();
            if (currentTriangle.getTextureCoords() != null) {
                texCoord[3 * index] = currentTriangle.getVertex1().getTextureCoord();
                texCoord[3 * index + 1] = currentTriangle.getVertex2().getTextureCoord();
                texCoord[3 * index + 2] = currentTriangle.getVertex3().getTextureCoord();
            }
            indexes[3 * index] = 3 * index;
            indexes[3 * index + 1] = 3 * index + 1;
            indexes[3 * index + 2] = 3 * index + 2;
        }
        m.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        m.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
        m.setBuffer(VertexBuffer.Type.Index, 1, BufferUtils.createIntBuffer(indexes));
        m.updateBound();
        Geometry geom = new Geometry("OurMesh", m);
        geom.setMaterial(material);
        return geom;
    }
    
}
