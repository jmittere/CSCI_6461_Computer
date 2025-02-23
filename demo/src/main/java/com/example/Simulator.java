package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;


public class Simulator {
    
        //all register store string decimal values for what is in the register
        // General Purpose Registers
        private int GPR0;
        private int GPR1;
        private int GPR2;
        private int GPR3;
        // Index Registers
        private int IXR1;
        private int IXR2;
        private int IXR3;
        //program counter
        private int PC;
        //memory registers, memory address reg and memory buffer register
        private int MAR;
        private int MBR;
        private int MFR;

        //load file location and loadFile array
        private String programFile;
        private List<String> loadFile;

        //stores addresses as the key and values
        //can access memory by using the address as the key
        //key: address, decimal int 
        //value: instruction, decimal int
        private HashMap<Integer,Integer> memory;
        // Constructor
        public Simulator() {
            this.GPR0 = -1;
            this.GPR1 = -1;
            this.GPR2 = -1;
            this.GPR3 = -1;
            this.IXR1 = -1;
            this.IXR2 = -1;
            this.IXR3 = -1;
            this.PC = -1;
            this.MAR = -1;
            this.MBR = -1;
            this.MFR = -1;
            this.programFile = "";
            this.loadFile = new ArrayList<>();
            this.memory = new HashMap<>();
        }
        
        //retrieves a value from an address in memory using a String decimal address
        public int getFromMemory(int address){
            if(!memory.containsKey(address)){
                return -1;
            }else if(address < 0 || address > 2047){
                //TODO: Throw exception for trying to access memory out of bounds exception
                return -1;
            }else{
                return this.memory.get(address);
            }

        }

        //runs the entire program in the LoadFile when the run button is pressed
        public void run(){
            
        }

        //steps through the LoadFile one line at a time
        //TODO: Figure out what all needs to be passed into this instruction
        public void step(){

        }

        //reads each line in the loadfile and puts it into loadFile array
        private boolean readLoadFile(){
            System.out.println("Program File: " + this.programFile);
            try (Scanner scanner = new Scanner(new File(this.programFile))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    this.loadFile.add(line);
                }
            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + e.getMessage());
                return false;
            }
            if(this.loadFile.size() > 0){
                boolean loadedMemory = this.initializeMemory();
                if(loadedMemory){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }
    
        //takes the loadFile array list and adds each instruction to its memory location
        private boolean initializeMemory(){
            for (String line : this.loadFile) {
                String[] words = line.split("\\s+"); //splits by any number of spaces
                if (words.length>2){
                    System.out.println("Incorrect Load File");
                    return false;
                }

                String address = words[0];
                String instruction = words[1];
                this.memory.put(Conversion.convertToDecimal(address), Conversion.convertToDecimal(instruction));
            }
            this.printMemory();
            return true;
        }

        private void printMemory(){
            this.memory.forEach((key, value) -> System.out.println(key + " -> " + value));
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

        public int getGPR0() {
            return GPR0;
        }

        public void setGPR0(int gPR0) {
            GPR0 = gPR0;
        }

        public int getGPR1() {
            return GPR1;
        }

        public void setGPR1(int gPR1) {
            GPR1 = gPR1;
        }

        public int getGPR2() {
            return GPR2;
        }

        public void setGPR2(int gPR2) {
            GPR2 = gPR2;
        }

        public int getGPR3() {
            return GPR3;
        }

        public void setGPR3(int gPR3) {
            GPR3 = gPR3;
        }

        public int getIXR1() {
            return IXR1;
        }

        public void setIXR1(int iXR1) {
            IXR1 = iXR1;
        }

        public int getIXR2() {
            return IXR2;
        }

        public void setIXR2(int iXR2) {
            IXR2 = iXR2;
        }

        public int getIXR3() {
            return IXR3;
        }

        public void setIXR3(int iXR3) {
            IXR3 = iXR3;
        }

        public int getPC() {
            return PC;
        }

        public void setPC(int PC) {
            this.PC = PC;
        }

        public int getMAR() {
            return MAR;
        }

        public void setMAR(int mAR) {
            MAR = mAR;
        }

        public int getMBR() {
            return MBR;
        }

        public void setMBR(int mBR) {
            MBR = mBR;
        }
        
        public int getMFR() {
            return MBR;
        }

        public void setMFR(int mBR) {
            MBR = mBR;
        }

        public String getProgramFile() {
            return programFile;
        }

        public void setProgramFile(String programFile) {
            this.programFile = programFile;
        }  
}
