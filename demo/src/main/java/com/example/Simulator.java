package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.List;

public class Simulator {
    
        // General Purpose Registers
        private String GPR0;
        private String GPR1;
        private String GPR2;
        private String GPR3;
        // Index Registers
        private String IXR1;
        private String IRX2;
        private String IXR3;
        //program counter
        private String PC;
        //memory registers, memory address reg and memory buffer register
        private String MAR;
        private String MBR;

        //load file location and loadFile array
        private String programFile;
        private List<String> loadFile;

        // Constructor
        public Simulator() {
            this.GPR0 = "";
            this.GPR1 = "";
            this.GPR2 = "";
            this.GPR3 = "";
            this.IXR1 = "";
            this.IRX2 = "";
            this.IXR3 = "";
            this.PC = "";
            this.MAR = "";
            this.MBR = "";
            this.programFile = "";
            this.loadFile = new ArrayList<>();
        }
    
        // Method to display information
        public void displayInfo() {
        }

        //reads each line in the loadfile and puts it into loadFile array
        private boolean readLoadFile(){
            System.out.println("Program File: " + this.programFile);
            try (Scanner scanner = new Scanner(new File(this.programFile))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    this.loadFile.add(line);
                    System.out.println(line);

                }
            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + e.getMessage());
                return false;
            }
            if(this.loadFile.size() > 0){
                return true;
            }else{
                return false;
            }
        }
    
        //initalizes program with program File specified 
        //returns false if failed to initalize with loadFile
        public boolean initializeProgram(){
            boolean res;
            if(programFile == ""){
                return false;
            }else{
                res = this.readLoadFile();
            }
            return res;
        }



        public String getGPR0() {
            return GPR0;
        }

        public void setGPR0(String gPR0) {
            GPR0 = gPR0;
        }

        public String getGPR1() {
            return GPR1;
        }

        public void setGPR1(String gPR1) {
            GPR1 = gPR1;
        }

        public String getGPR2() {
            return GPR2;
        }

        public void setGPR2(String gPR2) {
            GPR2 = gPR2;
        }

        public String getGPR3() {
            return GPR3;
        }

        public void setGPR3(String gPR3) {
            GPR3 = gPR3;
        }

        public String getIXR1() {
            return IXR1;
        }

        public void setIXR1(String iXR1) {
            IXR1 = iXR1;
        }

        public String getIRX2() {
            return IRX2;
        }

        public void setIRX2(String iRX2) {
            IRX2 = iRX2;
        }

        public String getIXR3() {
            return IXR3;
        }

        public void setIXR3(String iXR3) {
            IXR3 = iXR3;
        }

        public String getPC() {
            return PC;
        }

        public void setPC(String pC) {
            PC = pC;
        }

        public String getMAR() {
            return MAR;
        }

        public void setMAR(String mAR) {
            MAR = mAR;
        }

        public String getMBR() {
            return MBR;
        }

        public void setMBR(String mBR) {
            MBR = mBR;
        }

        public String getProgramFile() {
            return programFile;
        }

        public void setProgramFile(String programFile) {
            this.programFile = programFile;
        }

        
       
}
