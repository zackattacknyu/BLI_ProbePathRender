/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeUtil;

import com.jme3.math.ColorRGBA;
import java.awt.Color;

/**
 *
 * @author Zach
 */
public class ColorHelper {
    
    public static Color convertJMEcolorToJavaColor(ColorRGBA jmeColor){
        float[] rgbValues = jmeColor.getColorArray();
        return new Color(rgbValues[0],rgbValues[1],rgbValues[2],rgbValues[3]);
    }
}
