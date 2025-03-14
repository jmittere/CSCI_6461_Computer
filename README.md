# CSCI_6461_Computer
Computer Simulator Code (not including assembler) for CSCI6461. 

To run the main method and start simulator
You must be in the demo/ for this to work...
mvn exec:java -Dexec.mainClass="com.example.ComputerGUI"

to see all contents of jar
jar tf jarfile

to package clean uberjar
mvn clean package

to run jar
java -jar jarfile

to use other loadfiles, make sure they are in the same folder as the jar when running java -jar jarfile
