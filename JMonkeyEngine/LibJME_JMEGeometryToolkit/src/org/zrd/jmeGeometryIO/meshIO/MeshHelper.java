/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryIO.meshIO;

import com.jme3.asset.AssetManager;
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
import com.jme3.util.BufferUtils;
import java.util.ArrayList;
import org.zrd.geometryToolkit.meshDataStructure.MeshTriangle;
import org.zrd.geometryToolkit.meshDataStructure.TriangleSet;

/**
 *
 * @author BLI
 */
public class MeshHelper {
    
    public static TriangleSet addMeshToTriangleSet(Mesh mesh, Matrix4f meshTransform, TriangleSet inputSet){
        Triangle currentTri;
        MeshTriangle newTri;
        MeshData meshData = new MeshData(mesh);

        //put all triangles into a hash map
        for(int index = 0; index < mesh.getTriangleCount(); index++){
            currentTri = new Triangle();
            mesh.getTriangle(index, currentTri);
            newTri = convertInputTriangleToMeshTriangle(currentTri,meshTransform);
            newTri.setTextureCoords(meshData.getTriangleTextCoords(index));
            inputSet.addTriangle(newTri);

        }
        return inputSet;
    }
    
    public static MeshTriangle convertInputTriangleToMeshTriangle(Triangle triangle, Matrix4f transform){
        Vector3f vert1 = triangle.get1();
        Vector3f vert2 = triangle.get2();
        Vector3f vert3 = triangle.get3();
        
        Vector3f vert1Trans = transform.mult(vert1);
        Vector3f vert2Trans = transform.mult(vert2);
        Vector3f vert3Trans = transform.mult(vert3);
        
        return new MeshTriangle(vert1Trans,vert2Trans,vert3Trans);
    }

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

    public static Spatial generateModel(String objFileLocation, Material ballMat, AssetManager assetManager) {
        Spatial sampleMesh = assetManager.loadModel(objFileLocation);
        sampleMesh.setMaterial(ballMat);
        return sampleMesh;
    }

    public static TriangleSet addToTriangleSet(TriangleSet meshInfo, Spatial surface, Matrix4f transform) {
        if (surface instanceof Node) {
            Node surfaceNode = (Node) surface;
            for (Spatial child : surfaceNode.getChildren()) {
                meshInfo = addToTriangleSet(meshInfo, child, transform);
            }
        } else if (surface instanceof Geometry) {
            meshInfo = addToTriangleSet(meshInfo, (Geometry) surface, transform);
        }
        return meshInfo;
    }

    public static TriangleSet addToTriangleSet(TriangleSet meshInfo, Geometry surfaceGeom, Matrix4f transform) {
        return MeshHelper.addMeshToTriangleSet(surfaceGeom.getMesh(), transform, meshInfo);
    }
    
}
