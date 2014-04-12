/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector2f;
import java.util.ArrayList;

/**
 *
 * @author BLI
 */
public class PathRecorder {

    private ArrayList<Float> xCoords = new ArrayList<Float>(100);
    private ArrayList<Float> yCoords = new ArrayList<Float>(100);
    
    private float lastX, lastY;
    private float firstX, firstY;
    
    public PathRecorder(float xStart, float yStart){
        lastX = xStart;
        lastY = yStart;
        firstX = xStart;
        firstY = yStart;
    }

    public float getFirstX() {
        return firstX;
    }

    public float getFirstY() {
        return firstY;
    }
    
    public void addToPath(float deltaX, float deltaY){
        float currentX = lastX + deltaX;
        float currentY = lastY + deltaY;
        xCoords.add(currentX);
        yCoords.add(currentY);
        lastX = currentX;
        lastY = currentY;
    }
    
    public void addToPath(Vector2f xyDisplacement){
        addToPath(xyDisplacement.getX(),xyDisplacement.getY());
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
    
    
    
}
