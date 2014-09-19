/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialInterface.arduinoReading;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Properties;


public class FileDataReader implements DataReading {
	
        /**
	* A BufferedReader which will be fed by a InputStreamReader 
	* converting the bytes into characters 
	* making the displayed results codepage independent
	*/
	private BufferedReader input;
        
        /**
         * This string is the current output from the arduino
         */
        private String currentArdOutput;
        
        /**
         * These are the properties to be used to instantiate the arduino reading
         */
        private Properties serialProperties;
        
        private File simulationFile;
        
        private long lastLineReadTime;
        
        /*
         * Time between line reads for simulation (in milliseconds)
         */
        private static final long TIME_BETWEEN_LINE_READS = 20;
        
        public FileDataReader(Properties props){
            this.serialProperties = props;
            
            String fileName = serialProperties.getProperty("serialSimulation.fileName");
            simulationFile = Paths.get(fileName).toFile();
            try {
                // open the streams
                input = new BufferedReader(new FileReader(simulationFile));
                
                lastLineReadTime = getTimeNow();
                readCurrentLine();
                
            } catch (FileNotFoundException ex) {
                System.out.println("NO SUCH SIMULATION FILE: " + ex);
            }
        }
        
        private long getTimeNow(){
            return Calendar.getInstance().getTimeInMillis();
        }
        
        private void readCurrentLine(){
            try {
                currentArdOutput = input.readLine();
            } catch (IOException ex) {
                System.out.println("Error reading line in file: " + ex);
            }
        }
        
        /**
         * This class resets the reading of the probe. When the probe is
         *      sent the 'r' command, it resets. This simply sends
         *      that command to the probe through its output stream.
         */
        @Override
        public void reset(){
            try {
                input.close();
                input = new BufferedReader(new FileReader(simulationFile));
            } catch (IOException ex) {
                System.out.println("Error resetting: " + ex);
            }
        }

        /**
         * This returns the current string being read from Arduino
         * @return      current string from arduino
         */
        @Override
        public String getCurrentOutput() {
            long timeNow = getTimeNow();
            if(timeNow-lastLineReadTime >= TIME_BETWEEN_LINE_READS){
                readCurrentLine();
            }
            return currentArdOutput;
        }
        
        
}