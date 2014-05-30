/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meshTraversal;

import com.jme3.math.Quaternion;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;

/**
 *
 * @author Zach
 */
public class MeshHelper {
    
    
    public static String getTriangleInfo(Triangle triangle){
        return triangle.get(0).toString() + "," 
                + triangle.get(1).toString() + 
                "," + triangle.get(2).toString();
    }
    
    /**
     * Given an actual direction and desired direction, this method
     *      computes the rotation matrix that will rotate the actual
     *      vector to the desired vector.
     * 
     * @param actualDir
     * @param desiredDir
     * @return 
     */
    public static Quaternion getRotationFromVectors(Vector3f actualDir, Vector3f desiredDir){
        float cosTheta = desiredDir.dot(actualDir);
        float rotAngle = (float)Math.acos(cosTheta);
        
        Vector3f rotAxis = desiredDir.cross(actualDir);
        rotAxis = rotAxis.normalize();
        
        Quaternion rotQuaternion = new Quaternion();
        
        //the -1 is necessary to ensure the actual vector rotates to the expected vector
        rotQuaternion.fromAngleAxis(-1*rotAngle, rotAxis);
        
        return rotQuaternion;
        
    }
    
}
