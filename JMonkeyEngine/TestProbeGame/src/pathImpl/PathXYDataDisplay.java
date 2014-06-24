/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pathImpl;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author BLI
 */
public class PathXYDataDisplay extends PathDataDisplay{

    private ArrayList<Float> xValues,yValues;
    
    private PathXYDataDisplay(File initDir){
        super(initDir);
    }
    
    public static PathXYDataDisplay obtainXYProbeData(File initDir){
        PathXYDataDisplay data = new PathXYDataDisplay(initDir);
        if(data.isNullReturn()){
            return null;
        }else{
            return data;
        }
    }
    
    protected void generateDisplayValues(){
        
        float currentX = 0.0f;
        float currentY = 0.0f;
        float constantZ = 0.0f;
        
        displayVertices = new ArrayList<Vector3f>(10000);
        for(int index = 0; index < xValues.size(); index++){
            
            currentX = currentX + (0.00002f)*xValues.get(index);
            currentY = currentY + (0.00002f)*yValues.get(index);
            
            displayVertices.add(new Vector3f(currentX, currentY, constantZ));
            
        }
        
    }

    @Override
    protected void addParts(String[] parts) {
        xValues.add(Float.parseFloat(parts[0]));
        yValues.add(Float.parseFloat(parts[1]));
    }

    @Override
    protected void initializeArrayLists() {
        xValues = new ArrayList<Float>(10000);
        yValues = new ArrayList<Float>(10000);
    }

    @Override
    public Spatial generateReferenceObject(Material mat) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
    
    
}
