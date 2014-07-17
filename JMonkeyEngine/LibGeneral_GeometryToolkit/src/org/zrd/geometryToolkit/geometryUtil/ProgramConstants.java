/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.geometryToolkit.geometryUtil;

import com.jme3.math.ColorRGBA;

/**
 *
 * @author BLI
 */
public class ProgramConstants {

    //min lenght of individual segment
    public static final float MIN_SEGMENT_LENGTH = 0.001f;
    
    
    public static final float PATH_LINE_WIDTH = 30f;
    public static final ColorRGBA LINE_COLOR = ColorRGBA.Black;
    public static final float EPSILON = (float)Math.pow(10, -9);
    
    public static final ColorRGBA BACKGROUND_COLOR = 
            new ColorRGBA(205.0f/256.0f,204.0f/256.0f,207.0f/256.0f,1.0f);
}
