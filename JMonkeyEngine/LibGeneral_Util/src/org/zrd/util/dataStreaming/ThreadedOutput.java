/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.dataStreaming;

/**
 *
 * @author BLI
 */
public interface ThreadedOutput extends Runnable{
    
    String getCurrentOutput();
    void setData(String[] data);
    
}
