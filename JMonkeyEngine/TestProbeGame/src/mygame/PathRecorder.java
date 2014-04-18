/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 *
 * @author BLI
 */
public class PathRecorder {

    private ArrayList<Float> xCoords = new ArrayList<Float>(100);
    private ArrayList<Float> yCoords = new ArrayList<Float>(100);
    private ArrayList<Float> zCoords = new ArrayList<Float>(100);
    
    private float lastX, lastY,lastZ;
    private float firstX, firstY,firstZ;
    
    public PathRecorder(float xStart, float yStart){
        lastX = xStart;
        lastY = yStart;
        firstX = xStart;
        firstY = yStart;
        lastZ = 0;
        firstZ = 0;
    }
    
    public PathRecorder(float xStart, float yStart, float zStart){
        lastX = xStart;
        lastY = yStart;
        firstX = xStart;
        firstY = yStart;
        lastZ = zStart;
        firstZ = zStart;
    }

    public float getFirstX() {
        return firstX;
    }

    public float getFirstY() {
        return firstY;
    }

    public float getLastZ() {
        return lastZ;
    }

    public float getFirstZ() {
        return firstZ;
    }
    
    

    public void addToPath(float deltaX, float deltaY, float deltaZ){
        float currentX = lastX + deltaX;
        float currentY = lastY + deltaY;
        float currentZ = lastZ + deltaZ;
        xCoords.add(currentX);
        yCoords.add(currentY);
        zCoords.add(currentZ);
        lastX = currentX;
        lastY = currentY;
        lastZ = currentZ;
    }
    
    public void addToPath(Vector3f xyzDisplacement){
        addToPath(xyzDisplacement.getX(),xyzDisplacement.getY(),xyzDisplacement.getZ());
    }

    public ArrayList<Float> getxCoords() {
        return xCoords;
    }

    public ArrayList<Float> getyCoords() {
        return yCoords;
    }

    public float getLastX() {
        return lastX;
    }

    public float getLastY() {
        return lastY;
    }

    public ArrayList<Float> getzCoords() {
        return zCoords;
    }
    
    
    
}
