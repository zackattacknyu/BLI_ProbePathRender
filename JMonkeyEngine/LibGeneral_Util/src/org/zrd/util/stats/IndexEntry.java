/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.stats;

/**
 *
 * @author Zach
 */
public class IndexEntry {
    
    private int index;
    private double entry;

    public IndexEntry(int index, double entry) {
        this.index = index;
        this.entry = entry;
    }

    public int getIndex() {
        return index;
    }

    public double getEntry() {
        return entry;
    }
    
    
    
}
