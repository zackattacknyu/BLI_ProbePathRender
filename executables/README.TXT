------------------------------
General Installation
------------------------------

Before running these programs, there are two things that must be done:
- Arduino must be installed
- Java 7 or later 64-bit must be installed properly
Details are below on these steps

Arduino Installation steps:
1. Install a 64-bit version of the Arduino IDE
2. Copy the file rxtxSerial.dll file from Arduino into C:\Windows\SysWOW64
3. Take the directory that originally contained rxtxSerial.dll and append it to the end of the PATH environment variable
- Below are environment variable steps if you are unfamiliar

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
5. Once installed, go to the folder where it was installed and locate the "bin" folder for the JRE
- It should either be "<Java Installation Folder>\jre\bin" or "<java installation>\bin"
- Make sure one of the files in the folder is java.exe
6. Append the folder from step 5 to the PATH environment variable
- Below are environment variable steps if you are unfamiliar

Environment variable steps:
In order to append to an environment variable, follow these steps:
1. Right-click on "My Computer" and click "Properties"
2. Click on "Advanced System Settings"
3. Click the "Advanced" tab
4. Click the "Environment Variables" button
5. Under System Variables, find the desired variable and click "Edit"
6. In the "Variable value:" field, append the desired value to the existing value in the field
7. Click "OK" a few times until you have exited the windows


------------------------------
Specific Installation
------------------------------
1. Go to the folder of the desired program
2. Edit the .properties file and put in the desired values


-----------------------------
Running the programs
-----------------------------
1. Double click on Run.bat
2. Press enter when prompted and it will begin receiving data from the probe
3. Press enter to being recording
4. Press enter to end the current recording
5. Repeat steps 3-4 until you are done recording
6. Press Ctrl-C and then y to end the process

