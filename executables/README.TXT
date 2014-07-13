Before running these programs, there are two things that must be done:
- Arduino must be installed
- Java 7 or later 64-bit must be installed properly
Details are below on these steps

Arduino Installation steps:
1. Install a 64-bit version of the Arduino IDE
2. Copy the file rxtxSerial.dll file from Arduino into C:\Windows\SysWOW64
3. Take the directory that originally contained rxtxSerial.dll and append it to the end of the PATH environment variable

Info on arduino setup was found here:
http://playground.arduino.cc/Interfacing/Java#.UxkWA_mwLYg
 
The RXTX library for Arduino was found here
http://mfizz.com/oss/rxtx-for-java

Java Installation Steps:
1. Go to the Oracle Java SE website:
http://www.oracle.com/technetwork/java/javase/downloads/index.html
2. Click on the Download button next to the Java icon. Alternatively, this is the link:
http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
3. Click on the "Windows x64" version to download the 64-bit Windows version.
Currently 64-bit Windows is the only platform supported for these programs
4. Run the setup and make sure the Java JRE is installed
5. Once installed, 

Setup the program:

1. Unzip this folder
2. Go to tracker.properties and make sure properties at the top
		are set to the correct values
3. Make sure the Arduino software is installed


Run the program:

To run the project from the command line, go to the unzipped folder and
type the following:

java -jar SerialDataRecorder.jar

