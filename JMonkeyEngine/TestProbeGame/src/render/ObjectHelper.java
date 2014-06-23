/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;
import mesh.MeshTriangle;
import mesh.TriangleSet;
import util.ProgramConstants;

/**
 * This class is meant to take in objects from the program
 *      and convert them to objects that JMonkeyEngine
 *      will render. 
 * 
 * This code contains that helper that takes a list of vertices
 *      and makes a line as well as the code that takes a list
 *      of triangles and makes a mesh. 
 *
 * @author BLI
 */
public class ObjectHelper {

    public static Spatial createLineFromVertices(ArrayList<Vector3f> lineVertices, Material material) {
        short[] indices = new short[lineVertices.size() * 2];
        for (int index = 0; index < lineVertices.size() - 1; index++) {
            indices[2 * index] = (short) index;
            indices[2 * index + 1] = (short) (index + 1);
        }
        Vector3f[] lineVertexData = lineVertices.toArray(new Vector3f[lineVertices.size()]);
        ColorRGBA lineColor = ProgramConstants.LINE_COLOR;
        Vector4f[] lineColors = new Vector4f[lineVertices.size()];
        for (int j = 0; j < lineColors.length; j++) {
            lineColors[j] = new Vector4f(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), lineColor.getAlpha());
        }
        Mesh mesh = new Mesh();
        mesh.setMode(Mesh.Mode.Lines);
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(lineVertexData));
        mesh.setBuffer(VertexBuffer.Type.Index, 2, indices);
        mesh.setBuffer(VertexBuffer.Type.Color, 4, BufferUtils.createFloatBuffer(lineColors));
        mesh.setLineWidth(ProgramConstants.PATH_LINE_WIDTH);
        Spatial probePathLine = new Geometry("Line", mesh);
        probePathLine.setName("probeLine");
        probePathLine.setLocalScale(1);
        probePathLine.setMaterial(material);
        return probePathLine;
    }

    public static Spatial createMeshFromTriangles(TriangleSet triangles, Material material) {
        
        Mesh m = new Mesh();
        ArrayList<MeshTriangle> meshTris = triangles.getTriangleList();

        MeshTriangle currentTriangle;
        Vector3f[] vertices = new Vector3f[meshTris.size()*3];
        Vector2f[] texCoord = new Vector2f[meshTris.size()*3];
        int[] indexes = new int[meshTris.size()*3];
        for(int index = 0; index < meshTris.size(); index++){
            
            currentTriangle = meshTris.get(index);
            
            //vertex positions
            vertices[3*index] = currentTriangle.getVertex1().getVertex();
            vertices[3*index + 1] = currentTriangle.getVertex2().getVertex();
            vertices[3*index + 2] = currentTriangle.getVertex3().getVertex();
            
            //texture coordinates
            if(currentTriangle.getTextureCoords() != null){
                texCoord[3*index] = currentTriangle.getVertex1().getTextureCoord();
                texCoord[3*index + 1] = currentTriangle.getVertex2().getTextureCoord();
                texCoord[3*index + 2] = currentTriangle.getVertex3().getTextureCoord();
            }
            
            //indices
            indexes[3*index] = 3*index;
            indexes[3*index + 1] = 3*index + 1;
            indexes[3*index + 2] = 3*index + 2;
            
        }


        // Setting buffers
        m.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        m.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
        m.setBuffer(Type.Index, 1, BufferUtils.createIntBuffer(indexes));
        m.updateBound();

        Geometry geom = new Geometry("OurMesh", m);
        geom.setMaterial(material);
        return geom;
    }
    
}
