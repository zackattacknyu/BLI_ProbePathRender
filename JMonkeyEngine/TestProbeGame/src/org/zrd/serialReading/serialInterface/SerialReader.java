/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zrd.serialReading.serialInterface;

import org.zrd.util.general.PropertiesHelper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 
import java.util.Enumeration;
import java.util.Properties;
import org.zrd.bliProbePath.Properties_BLIProbePath;

/*
 * 
 * Info on setup was found here:
 * 
 * http://playground.arduino.cc/Interfacing/Java#.UxkWA_mwLYg
 * 
 * The RXTX library was found here
 * http://mfizz.com/oss/rxtx-for-java
 * 
 * This is to be used solely for reading from the serial device.
 *      Any interpretation of the reading should be done in other classes. 
 * 
 */


public class SerialReader implements SerialPortEventListener {
	SerialPort serialPort;
        /** The port we're normally going to use. */
	/*private static final String PORT_NAMES[] = { 
                    "/dev/tty.usbserial-A9007UX1", // Mac OS X
                    "/dev/ttyUSB0", // Linux
                    "COM3", // Windows
	};*/
        private static String PORT_NAME;
	/**
	* A BufferedReader which will be fed by a InputStreamReader 
	* converting the bytes into characters 
	* making the displayed results codepage independent
	*/
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static int TIME_OUT;
	/** Default bits per second for COM port. */
	private static int DATA_RATE;
        
        private String currentArdOutput;
        
        private Properties serialProperties;

	public void initialize() {
            
            serialProperties = Properties_BLIProbePath.getProperties();
            
            TIME_OUT = Integer.parseInt(serialProperties.getProperty("serial.time_out"));
            DATA_RATE = Integer.parseInt(serialProperties.getProperty("serial.data_rate"));
            PORT_NAME = serialProperties.getProperty("serial.port_name");
            
            CommPortIdentifier portId = null;
            Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

            //First, Find an instance of serial port as set in PORT_NAMES.
            while (portEnum.hasMoreElements()) {
                CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
                if (currPortId.getName().equals(PORT_NAME)) {
                    portId = currPortId;
                    break;
                }
                /*for (String portName : PORT_NAMES) {
                    if (currPortId.getName().equals(portName)) {
                            portId = currPortId;
                            break;
                    }
                }*/
            }
            if (portId == null) {
                System.out.println("Could not find COM port.");
                return;
            }

            try {
                // open serial port, and use class name for the appName.
                serialPort = (SerialPort) portId.open(this.getClass().getName(),
                                TIME_OUT);

                // set port parameters
                serialPort.setSerialPortParams(DATA_RATE,
                                SerialPort.DATABITS_8,
                                SerialPort.STOPBITS_1,
                                SerialPort.PARITY_NONE);

                // open the streams
                input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
                output = serialPort.getOutputStream();

                // add event listeners
                serialPort.addEventListener(this);
                serialPort.notifyOnDataAvailable(true);
            } catch (Exception e) {
                System.err.println(e.toString());
            }
	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
            if (serialPort != null) {
                serialPort.removeEventListener();
                serialPort.close();
            }
	}
        
        
        

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
            if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                try {
                    currentArdOutput = input.readLine();
                } catch (Exception e) {
                    System.err.println(e.toString());
                }
            }
            // Ignore all the other eventTypes, but you should consider the other ones.
	}

        public static void executeMain(){
            SerialReader main = new SerialReader();
            main.beginExecution();
        }
        
        public void beginExecution(){
            initialize();
            Thread t=new Thread() {
                public void run() {
                        //the following line will keep this app alive for 1000 seconds,
                        //waiting for events to occur and responding to them (printing incoming messages to console).
                        try {
                            Thread.sleep(1000000);
                        } catch (InterruptedException ie) {
                        }
                }
            };
            t.start();
            System.out.println("Started");
        }

    public String getCurrentOutput() {
        return currentArdOutput;
    }
        
        
}