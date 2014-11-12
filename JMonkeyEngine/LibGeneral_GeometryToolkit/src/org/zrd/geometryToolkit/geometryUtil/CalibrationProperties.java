/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.geometryUtil;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import java.util.ArrayList;
import java.util.Properties;
import org.zrd.util.dataHelp.DisplacementHelper;
import org.zrd.util.properties.PropertiesHelper;

/**
 *
 * @author BLI
 */
public class CalibrationProperties {
    
    private Quaternion rotationCalib;
    private Float probeToRealFactor;
    private Float realToVirtualFactor;
    private Float scaleFactor;
    private Float scaleFactorX;
    private Float scaleFactorY;
    private boolean reflectX;
    private boolean reflectY;
    
    private Vector2f offsetAmountInit;
    private Vector2f offsetAmount;
    
    private Boolean flattenMesh;
    private Integer[] flattenVertexIndices;
    
    public static final String FLATTEN_MESH_NAME = "meshFlattening.flattenMesh";
    public static final String FLATTEN_MESH_VERTEX_INDICES_NAME = "meshFlattening.vertexIndicesToUse";
    
    public static final String PROBE_TO_REAL_FACTOR_NAME = "scaleFactor.probeToReal";
    public static final String REAL_TO_VIRTUAL_FACTOR_NAME = "scaleFactor.realToVirtual";
    
    public static final String QUATERNION_W_FACTOR_NAME = "initQuat.w";
    public static final String QUATERNION_X_FACTOR_NAME = "initQuat.x";
    public static final String QUATERNION_Y_FACTOR_NAME = "initQuat.y";
    public static final String QUATERNION_Z_FACTOR_NAME = "initQuat.z";
    
    public static final String REFLECT_X_FACTOR_NAME = "reflectionCalib.negateX";
    public static final String REFLECT_Y_FACTOR_NAME = "reflectionCalib.negateY";
    
    public static final String OFFSET_X_FACTOR_NAME = "offsetAmount.x";
    public static final String OFFSET_Y_FACTOR_NAME = "offsetAmount.y";
    
    public static CalibrationProperties obtainCalibrationProperties(Properties specificProps, Properties defaultProps){
        
        CalibrationProperties defaultP = new CalibrationProperties(defaultProps);
        CalibrationProperties specificP = new CalibrationProperties(specificProps);
        
        Quaternion outputRotCalib = (Quaternion)chooseSpecificOrDefault(
                specificP.rotationCalib,defaultP.rotationCalib);
        Float outputRealToProbe = (Float)chooseSpecificOrDefault(
                specificP.probeToRealFactor,defaultP.probeToRealFactor);
        Float outputVirtualToProbe = (Float)chooseSpecificOrDefault(
                specificP.realToVirtualFactor,defaultP.realToVirtualFactor);
        
        Boolean reflectX = (Boolean)chooseSpecificOrDefault(specificP.reflectX,defaultP.reflectX);
        Boolean reflectY = (Boolean)chooseSpecificOrDefault(specificP.reflectY,defaultP.reflectY);
        
        Vector2f offsetAmountInit = (Vector2f)chooseSpecificOrDefault(
                specificP.offsetAmountInit,
                defaultP.offsetAmountInit);
        
        CalibrationProperties returnProps = 
                new CalibrationProperties(outputRotCalib,outputRealToProbe,
                outputVirtualToProbe,reflectX,reflectY,offsetAmountInit);
        
        //properties that only a mesh should contain
        returnProps.flattenMesh = specificP.flattenMesh;
        returnProps.flattenVertexIndices = specificP.flattenVertexIndices;
                
        return returnProps;
    }
    
    private CalibrationProperties(Quaternion rotationCalib, Float realToProbeFactor, 
            Float virtualToRealFactor, Boolean reflectX, Boolean reflectY, Vector2f offsetAmountInit){
        this.probeToRealFactor = realToProbeFactor;
        this.rotationCalib = rotationCalib;
        this.realToVirtualFactor = virtualToRealFactor;
        this.reflectX = reflectX;
        this.reflectY = reflectY;
        
        scaleFactor = realToProbeFactor*virtualToRealFactor;
        scaleFactorX = DisplacementHelper.negativeIfTrue(scaleFactor, reflectX);
        scaleFactorY = DisplacementHelper.negativeIfTrue(scaleFactor, reflectY);
        
        //gets the scaled offset amount in probe units
        if(offsetAmountInit == null){
            this.offsetAmount = new Vector2f();
        }
        else{
            this.offsetAmount = offsetAmountInit.mult(this.probeToRealFactor);
        }
        
    }
    
    private CalibrationProperties(Properties props){
        
        if(props == null) return;
        
        if(props.containsKey(FLATTEN_MESH_NAME)){
            flattenMesh = PropertiesHelper.getBooleanValueProperty(props, FLATTEN_MESH_NAME);
        }
        
        if(props.containsKey(FLATTEN_MESH_VERTEX_INDICES_NAME)){
            flattenVertexIndices = PropertiesHelper.getIntegerArrayValueProperty(props, FLATTEN_MESH_VERTEX_INDICES_NAME);
        }
        
        if(props.containsKey(PROBE_TO_REAL_FACTOR_NAME)){
            probeToRealFactor = PropertiesHelper.getFloatValueProperty(props, PROBE_TO_REAL_FACTOR_NAME);
        }
        if(props.containsKey(REAL_TO_VIRTUAL_FACTOR_NAME)){
            realToVirtualFactor = PropertiesHelper.getFloatValueProperty(props, REAL_TO_VIRTUAL_FACTOR_NAME);
        }
        
        if(props.containsKey(REFLECT_X_FACTOR_NAME)){
            reflectX = PropertiesHelper.getBooleanValueProperty(props, REFLECT_X_FACTOR_NAME);
        }
        if(props.containsKey(REFLECT_Y_FACTOR_NAME)){
            reflectY = PropertiesHelper.getBooleanValueProperty(props, REFLECT_Y_FACTOR_NAME);
        }
        
        if(props.containsKey(QUATERNION_W_FACTOR_NAME) && 
                props.containsKey(QUATERNION_X_FACTOR_NAME) && 
                props.containsKey(QUATERNION_Y_FACTOR_NAME) && 
                props.containsKey(QUATERNION_Z_FACTOR_NAME)){

            rotationCalib = new Quaternion(
                    PropertiesHelper.getFloatValueProperty(props, QUATERNION_X_FACTOR_NAME),
                    PropertiesHelper.getFloatValueProperty(props, QUATERNION_Y_FACTOR_NAME),
                    PropertiesHelper.getFloatValueProperty(props, QUATERNION_Z_FACTOR_NAME),
                    PropertiesHelper.getFloatValueProperty(props, QUATERNION_W_FACTOR_NAME));
        }
        
        if(props.containsKey(OFFSET_X_FACTOR_NAME) &&
                props.containsKey(OFFSET_Y_FACTOR_NAME)){
            
            offsetAmountInit = new Vector2f(
                    PropertiesHelper.getFloatValueProperty(props, OFFSET_X_FACTOR_NAME),
                    PropertiesHelper.getFloatValueProperty(props, OFFSET_Y_FACTOR_NAME));
            
        }
        
        
        
    }
    
    public static Object chooseSpecificOrDefault(Object specificObj, Object defaultObj){
        if(specificObj != null){
            return specificObj;
        }else{
            return defaultObj;
        }
    }
    
    public static ArrayList<String> getCalibrationPropertiesStrings(Quaternion calib){
        ArrayList<String> returnString = new ArrayList<String>(4);
        returnString.add(getPropertiesString(QUATERNION_W_FACTOR_NAME,calib.getW()));
        returnString.add(getPropertiesString(QUATERNION_X_FACTOR_NAME,calib.getX()));
        returnString.add(getPropertiesString(QUATERNION_Y_FACTOR_NAME,calib.getY()));
        returnString.add(getPropertiesString(QUATERNION_Z_FACTOR_NAME,calib.getZ()));
        return returnString;
    }
    private static String getPropertiesString(String name, float value){
        return name + "=" + String.valueOf(value);
    }

    public Quaternion getRotationCalib() {
        return rotationCalib;
    }
    
    public Float getScaleFactor(){
        return scaleFactor;
    }

    public boolean isFlattenMesh() {
        if(flattenMesh == null) return false;
        
        return flattenMesh;
    }

    public Integer[] getFlattenVertexIndices() {
        return flattenVertexIndices;
    }
    
    public Float getScaleFactorX(){
        return scaleFactorX;
    }

    public Vector2f getOffsetAmount() {
        return offsetAmount;
    }
    
    public Float getScaleFactorY(){
        return scaleFactorY;
    }
}
