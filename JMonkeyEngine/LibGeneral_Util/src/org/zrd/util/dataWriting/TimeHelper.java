/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.util.dataWriting;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This has data writing tools related to time
 * Right now, it just retrieves the current time
 *      in the specified format
 *
 * @author Zach
 */
public class TimeHelper {

    /**
     * This retrieves the current time in the format specified
     *      in the constants class in this package
     * @return      timestamp string for current time in desired format
     */
    public static String getTimestampSuffix() {
        SimpleDateFormat myFormat = new SimpleDateFormat(DataWritingConstants.TIMESTAMP_SUFFIX_FORMAT);
        Calendar rightNow = Calendar.getInstance();
        return myFormat.format(rightNow.getTime());
    }
    
}
