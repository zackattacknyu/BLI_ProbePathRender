/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.dataStreaming;

/**
 *
 * @author BLI
 */
public interface StreamQualityTracker{
    
    float getCurrentQuality();
    void updateData();
}
