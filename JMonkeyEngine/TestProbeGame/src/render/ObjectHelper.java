/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;
import mygame.Constants;

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
        ColorRGBA lineColor = Constants.LINE_COLOR;
        Vector4f[] lineColors = new Vector4f[lineVertices.size()];
        for (int j = 0; j < lineColors.length; j++) {
            lineColors[j] = new Vector4f(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), lineColor.getAlpha());
        }
        Mesh mesh = new Mesh();
        mesh.setMode(Mesh.Mode.Lines);
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(lineVertexData));
        mesh.setBuffer(VertexBuffer.Type.Index, 2, indices);
        mesh.setBuffer(VertexBuffer.Type.Color, 4, BufferUtils.createFloatBuffer(lineColors));
        mesh.setLineWidth(Constants.PATH_LINE_WIDTH);
        Spatial probePathLine = new Geometry("Line", mesh);
        probePathLine.setName("probeLine");
        probePathLine.setLocalScale(1);
        probePathLine.setMaterial(material);
        return probePathLine;
    }
    
}
