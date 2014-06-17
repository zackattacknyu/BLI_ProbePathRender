/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
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
        ArrayList<Float> zVals = path.getzCoords();
        ArrayList<Vector3f> pathVertices = new ArrayList<Vector3f>(xVals.size());
        Vector3f currentVertex;
        for(int index = 0; index < xVals.size(); index++){         
            currentVertex = new Vector3f(xVals.get(index),yVals.get(index),zVals.get(index));
            pathVertices.add(currentVertex);
        }
        return pathVertices;
    }
    
}
