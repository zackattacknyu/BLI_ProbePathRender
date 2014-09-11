/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.cmdapplication_testing;

/**
 *
 * @author BLI
 */
public class CMDApplication_Testing {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        int n = 16;
        
        FFT.testFFT(n);
        double[] blackmanWindow = WindowFunction.generateBlackmanWindow(n);
        
        for(double windowVal: blackmanWindow){
            System.out.print(windowVal + " ");
        }
        
    }

}
