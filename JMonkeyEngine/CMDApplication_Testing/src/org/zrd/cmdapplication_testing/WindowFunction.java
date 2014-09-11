/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.cmdapplication_testing;

/*
 * This code comes directly from Google Code. This is the web page:
 * 
 * https://code.google.com/p/musicg/source/browse/src/com/musicg/dsp/WindowFunction.java?r=7a0c77284b0d9263ab705821e333f36a38bced96
 */
public class WindowFunction {

    public static final int RECTANGULAR = 0;
    public static final int BARTLETT = 1;
    public static final int HANNING = 2;
    public static final int HAMMING = 3;
    public static final int BLACKMAN = 4;

    int windowType = 0; // defaults to rectangular window

    public WindowFunction() {
    }

    public void setWindowType(int wt) {
        windowType = wt;
    }

    public void setWindowType(String w) {
        if (w.toUpperCase().equals("RECTANGULAR"))
                windowType = RECTANGULAR;
        if (w.toUpperCase().equals("BARTLETT"))
                windowType = BARTLETT;
        if (w.toUpperCase().equals("HANNING"))
                windowType = HANNING;
        if (w.toUpperCase().equals("HAMMING"))
                windowType = HAMMING;
        if (w.toUpperCase().equals("BLACKMAN"))
                windowType = BLACKMAN;
    }

    public int getWindowType() {
        return windowType;
    }
    
    public static double[] generateBlackmanWindow(int nSamples){
        WindowFunction blackman = new WindowFunction();
        blackman.setWindowType(WindowFunction.BLACKMAN);
        return blackman.generate(nSamples);
    }

    public double[] generate(int nSamples) {
        // generate nSamples window function values
        // for index values 0 .. nSamples - 1
        int m = nSamples / 2;
        double r;
        double pi = Math.PI;
        double[] w = new double[nSamples];
        switch (windowType) {
            
            case BARTLETT: // Bartlett (triangular) window
                for (int n = 0; n < nSamples; n++)
                        w[n] = 1.0f - Math.abs(n - m) / m;
                break;
            case HANNING: // Hanning window
                r = pi / (m + 1);
                for (int n = -m; n < m; n++)
                        w[m + n] = 0.5f + 0.5f * Math.cos(n * r);
                break;
            case HAMMING: // Hamming window
                r = pi / m;
                for (int n = -m; n < m; n++)
                        w[m + n] = 0.54f + 0.46f * Math.cos(n * r);
                break;
            case BLACKMAN: // Blackman window
                r = pi / m;
                for (int n = -m; n < m; n++)
                        w[m + n] = 0.42f + 0.5f * Math.cos(n * r) + 0.08f
                                        * Math.cos(2 * n * r);
                break;
            default: // Rectangular window function
                for (int n = 0; n < nSamples; n++)
                        w[n] = 1.0f;
        }
        return w;
    }
}
