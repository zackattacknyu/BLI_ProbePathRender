/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.util.ArrayList;

/**
 *
 * @author BLI
 */
public class PathRecorder {

    private ArrayList<Float> xCoords = new ArrayList<Float>(100);
    private ArrayList<Float> yCoords = new ArrayList<Float>(100);
    
    private float lastX, lastY;
    
    public PathRecorder(float xStart, float yStart){
        lastX = xStart;
        lastY = yStart;
    }
    
    public void addToPath(float deltaX, float deltaY){
        float currentX = lastX + deltaX;
        float currentY = lastY + deltaY;
        xCoords.add(currentX);
        yCoords.add(currentY);
        lastX = currentX;
        lastY = currentY;
    }

    public ArrayList<Float> getxCoords() {
        return xCoords;
    }

    public ArrayList<Float> getyCoords() {
        return yCoords;
    }
    
    
    
}
