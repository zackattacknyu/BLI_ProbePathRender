/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.graphicsToolsImpl.pathImpl;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;
import org.zrd.geometryToolkit.geometryUtil.ProgramConstants;
import org.zrd.geometryToolkit.pathDataStructure.SegmentSet;
import org.zrd.jmeUtil.materials.MaterialHelper;

/**
 *
 * @author BLI
 */
public class PathRenderHelper {
    
    public static Node createLineFromVerticesWithData(SegmentSet lineWithData, AssetManager assetManager){
        Node outputNode = new Node();
        Spatial currentSeg;
        Material currentMaterial;
        
        ArrayList<Vector3f> currentPath = new ArrayList<Vector3f>(2);
        ArrayList<Vector3f> pathVertices = lineWithData.getPathVertices();
        ArrayList<Float> dataAtVertices = lineWithData.getDataAtVertices();
        
        currentPath.add(pathVertices.get(0).clone());
        for(int index = 1; index < pathVertices.size()-1;index++){
            currentPath.add(pathVertices.get(index));
            currentMaterial = MaterialHelper.getGrayscaleMaterial(dataAtVertices.get(index-1), assetManager);
            currentSeg = createLineFromVertices(currentPath, currentMaterial);
            outputNode.attachChild(currentSeg);
            currentPath.remove(0);
        }
        
        return outputNode;
        
    }

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
    
}
