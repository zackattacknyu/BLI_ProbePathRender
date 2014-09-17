/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.jmeGeometryIO.pathIO;

import com.jme3.math.ColorRGBA;

/**
 *
 * @author Zach
 */
public class FloatToRedBlueGradient implements StringToColorConversion{

    @Override
    public ColorRGBA convertStringToColor(String data) {
        float brightness = Float.parseFloat(data);
        return new ColorRGBA(1-brightness,0f,brightness,1.0f);
    }
    
}
