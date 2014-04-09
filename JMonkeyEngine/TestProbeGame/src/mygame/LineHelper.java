/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;

/**
 *
 * @author Zach
 */
public class LineHelper {
    
    
    public static ArrayList<Vector3f> convertPathRecordingToLineVertices(PathRecorder path){
        ArrayList<Float> xVals = path.getxCoords();
        ArrayList<Float> yVals = path.getyCoords();
        ArrayList<Vector3f> pathVertices = new ArrayList<Vector3f>(xVals.size());
        Vector3f currentVertex;
        for(int index = 0; index < xVals.size(); index++){         
            currentVertex = new Vector3f(xVals.get(index),yVals.get(index),0);
            pathVertices.add(currentVertex);
        }
        return pathVertices;
    }
    
    public static Vector2f getXYDisplacement(float deltaX, float deltaY, float yawInRadians){
        
        float cosTheta = (float) Math.cos(yawInRadians);
        float sinTheta = (float) Math.sin(yawInRadians);
        
        float newDeltaX = deltaX*cosTheta-deltaY*sinTheta;
        float newDeltaY = deltaX*sinTheta+deltaY*cosTheta;
        
        return new Vector2f(newDeltaX,newDeltaY);
        
        
    }
    
    public static Matrix3f getRotationMatrix(float yawInRadians){
        
        float cosTheta = (float) Math.cos(yawInRadians);
        float sinTheta = (float) Math.sin(yawInRadians);
        
        return new Matrix3f(cosTheta,-sinTheta,0,sinTheta,cosTheta,0,0,0,1);
        
    }
    
    public static Spatial createLineFromVertices(ArrayList<Vector3f> lineVertices, Material ballMat){
        
        short[] indices = new short[lineVertices.size()*2];
        for(int index=0; index<lineVertices.size()-1; index++){
            indices[2*index]=(short)index;
            indices[2*index+1]=(short)(index+1);
        }
        
        Vector3f[] lineVertexData = lineVertices.toArray(new Vector3f[lineVertices.size()]);
        
        ColorRGBA lineColor = ColorRGBA.Black;
        Vector4f[] lineColors = new Vector4f[lineVertices.size()];
        for(int j=0; j < lineColors.length; j++){
            lineColors[j] = new Vector4f(lineColor.getRed(),lineColor.getGreen(),
                    lineColor.getBlue(),lineColor.getAlpha());
        }
        
        Mesh mesh = new Mesh();
        mesh.setMode(Mesh.Mode.Lines);
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(lineVertexData));
        mesh.setBuffer(VertexBuffer.Type.Index, 2, indices);
        mesh.setBuffer(VertexBuffer.Type.Color, 4, BufferUtils.createFloatBuffer(lineColors));
        mesh.setLineWidth(10f);
        Spatial probePathLine = new Geometry("Line",mesh);
        probePathLine.setName("probeLine");
        probePathLine.setLocalScale(1);
        probePathLine.setMaterial(ballMat);
        
        
        return probePathLine;
    }
    
}
