/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.timeTools;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Zach
 */
public class TimeHelper {

    public static String getTimestampSuffix() {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy_MM_dd__kk_mm_ss");
        Calendar rightNow = Calendar.getInstance();
        return myFormat.format(rightNow.getTime());
    }
    
}
