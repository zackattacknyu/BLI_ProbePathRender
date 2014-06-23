/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import util.ProgramConstants;

/**
 *
 * @author BLI
 */
public class ProbePathDisplay {

    
    private FloatBuffer position;
    private FloatBuffer color;
    private ShortBuffer index;
    
    private static final int initNumEntries = 500000;
    private static final ColorRGBA lineColor = ProgramConstants.LINE_COLOR;
    private static final float lineColorRed = lineColor.getRed();
    private static final float lineColorGreen = lineColor.getGreen();
    private static final float lineColorBlue = lineColor.getBlue();
    private static final float lineColorAlpha = lineColor.getAlpha();
    
    private Mesh mesh;
    private Spatial probePathLine;
    private short currentIndex = 0;
    
    public ProbePathDisplay(Material material){
        
        index = BufferUtils.createShortBuffer(initNumEntries);
        position = BufferUtils.createFloatBuffer(initNumEntries);
        color = BufferUtils.createFloatBuffer(initNumEntries);
        
        mesh = new Mesh();
        mesh.setMode(Mesh.Mode.Lines);
        mesh.setBuffer(VertexBuffer.Type.Position, 3, position);
        mesh.setBuffer(VertexBuffer.Type.Index, 2, index);
        mesh.setBuffer(VertexBuffer.Type.Color, 4, color);
        mesh.setLineWidth(ProgramConstants.PATH_LINE_WIDTH);
        probePathLine = new Geometry("Line",mesh);
        probePathLine.setName("probeLine");
        probePathLine.setLocalScale(1);
        probePathLine.setMaterial(material);
            
    }
    
    public void addVertex(Vector3f vertex){
        addVertex(vertex.getX(),vertex.getY(),vertex.getZ());
    }
    
    public void addVertex(float x, float y, float z){
        
        position.put(x);
        position.put(y);
        position.put(z);
        
        color.put(lineColorRed);
        color.put(lineColorGreen);
        color.put(lineColorBlue);
        color.put(lineColorAlpha);
        
        index.put(currentIndex);
        index.put((short)(currentIndex+1));
        currentIndex = (short)(currentIndex + 2);
        
    }

    public Spatial getProbePathLine() {
        return probePathLine;
    }
    
    
    
}
