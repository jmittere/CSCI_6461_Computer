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
        private int IR;

        //debug output to be passed to the frontend debugOutput text area 
        //contains error code if failed instruction or symbolic instruction
        private String debugOutput;

        //load file location and loadFile array
        private String programFile;
        private List<String> loadFile;

        //stores addresses as the key and values
        //can access memory by using the address as the key
        //key: address, decimal int 
        //value: instruction, decimal int
        private HashMap<Integer,Integer> memory;

        //hashmap storing all opCodes
        //key: string binary representation of an opCode
        //value: string representation of opCode
        private HashMap<String, String> opCodes;

        //represents the values of each register, PC, and output message for debugging
        //Important: key is the register, value is the contents of the register in STRING form
        public HashMap<String, String> registers;

        // Constructor
        public Simulator() {
            this.GPR0 = -1;
            this.GPR1 = -1;
            this.GPR2 = -1;
            this.GPR3 = -1;
            this.IR = -1;
            this.IXR1 = -1;
            this.IXR2 = -1;
            this.IXR3 = -1;
            this.PC = -1;
            this.MAR = -1;
            this.MBR = -1;
            this.MFR = -1;
            this.debugOutput = "";
            this.programFile = "";
            this.loadFile = new ArrayList<>();
            this.memory = new HashMap<>();
            this.opCodes = new HashMap<>();
            this.registers = new HashMap<>();
            this.initializeOpcodes();
        }

        //updates hashmap that has contents of each register
        //must run this function before returning values to the frontend
        private void updateRegisters(){
            if(this.GPR0 == -1){
                this.registers.put("GPR0", "");
            }else{
                this.registers.put("GPR0", String.valueOf(this.GPR0));
            }
            if(this.GPR1 == -1){
                this.registers.put("GPR1", "");
            }else{
                this.registers.put("GPR1", String.valueOf(this.GPR1));
            }
            if(this.GPR2 == -1){
                this.registers.put("GPR2", "");
            }else{
                this.registers.put("GPR2", String.valueOf(this.GPR2));
            }
            if(this.GPR3 == -1){
                this.registers.put("GPR3", "");
            }else{
                this.registers.put("GPR3", String.valueOf(this.GPR3));
            }
            if(this.IXR1 == -1){
                this.registers.put("IXR1", "");
            }else{
                this.registers.put("IXR1", String.valueOf(this.IXR1));
            }
            if(this.IXR2 == -1){
                this.registers.put("IXR2", "");
            }else{
                this.registers.put("IXR2", String.valueOf(this.IXR2));
            }
            if(this.IXR3 == -1){
                this.registers.put("IXR3", "");
            }else{
                this.registers.put("IXR3", String.valueOf(this.IXR3));
            }
            if(this.PC == -1){
                this.registers.put("PC", "");
            }else{
                this.registers.put("PC", String.valueOf(this.PC));
            }
            if(this.MAR == -1){
                this.registers.put("MAR", "");
            }else{
                this.registers.put("MAR", String.valueOf(this.MAR));
            }
            if(this.MBR == -1){
                this.registers.put("MBR", "");
            }else{
                this.registers.put("MBR", String.valueOf(this.MBR));
            }
            if(this.MFR == -1){
                this.registers.put("MFR", "");
            }else{
                this.registers.put("MFR", String.valueOf(this.MFR));
            }
            if(this.IR == -1){
                this.registers.put("IR", "");
            }else{
                this.registers.put("IR", String.valueOf(this.IR));
            }
            this.registers.put("debugOutput", this.debugOutput);
        }


        //Initializing the binary value with corresponding opCode
        private void initializeOpcodes(){
            opCodes.put("000000", "HLT");
            opCodes.put("000001", "LDR");
            opCodes.put("011000", "TRAP");
            opCodes.put("000010", "STR");
            opCodes.put("000011", "LDA");
            opCodes.put("100001", "LDX");
            opCodes.put("100010", "STX");
            opCodes.put("001000", "JZ");
            opCodes.put("001001", "JNE");
            opCodes.put("001010", "JCC");
            opCodes.put("001011", "JMA");
            opCodes.put("001100", "JSR");
            opCodes.put("001101", "RFS");
            opCodes.put("001110", "SOB");
            opCodes.put("001111", "JGE");
            opCodes.put("000100", "AMR");
            opCodes.put("000101", "SMR");
            opCodes.put("000110", "AIR");
            opCodes.put("000111", "SIR");
            opCodes.put("111000", "MLT");
            opCodes.put("111001", "DVD");
            opCodes.put("111010", "TRR");
            opCodes.put("111011", "AND");
            opCodes.put("111100", "ORR");
            opCodes.put("111101", "NOT");
            opCodes.put("011001", "SRC");
            opCodes.put("011010", "RRC");
            opCodes.put("110001", "IN");
            opCodes.put("110010", "OUT");
            opCodes.put("110011", "CHK");
            opCodes.put("011011", "FADD");
            opCodes.put("011100", "FSUB");
            opCodes.put("011101", "VADD");
            opCodes.put("011110", "VSUB");
            opCodes.put("011111", "CNVRT");
            opCodes.put("101000", "LDFR");
            opCodes.put("101001", "STFR");
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
        public HashMap<String, String> run(){
            if(this.programFile.equals("")){
                this.debugOutput = "No Program File to Run";
                this.updateRegisters();
                return this.registers;
            }
    
            for (int i = 0; i < this.loadFile.size(); i++) { //runs through each line of the load file and steps for each line
                //System.out.println(i);
                this.step();
                /*if(this.debugOutput.contains("ERROR: Cannot step without setting PC")){
                    break;
                } */

            }
            this.updateMemoryRegisters();
            this.updateRegisters();
            return this.registers;
        }

        //steps through memory depending on where PC is set
        //should error if pc is not set
        //should also increment pc after each instruction
        public HashMap<String, String> step(){
            if(this.PC == -1){
                //System.out.println("Cannot step without setting PC...");
                this.debugOutput = "ERROR: Cannot step without setting PC";
                this.updateRegisters();
                return this.registers;
            } else if(this.PC > 4095){
                //System.out.println("PC: Memory out of bounds");
                this.debugOutput = "ERROR: PC Memory out of bounds";
                this.updateRegisters();
                return this.registers;
            }
            
            //get value at address of PC from memory
            int instr = this.getFromMemory(this.PC);
            if(instr == -1){
                //System.out.println("No instruction at: " + this.PC);
                this.debugOutput = "ERROR: No instruction at " + this.PC;
                this.updateRegisters();
                return this.registers;
            }

            String binaryInstruction = Conversion.convertToBinaryString(instr, 16); //gets the binary equivalent of the instruction stored at memory location of PC
            String opCodeBinaryString = binaryInstruction.substring(0, 6);
            String opCode = this.opCodes.get(opCodeBinaryString);
            if(opCode == null){
                //System.out.println("Opcode null");
                this.PC = this.getAddressOfNextInstruction();
                this.debugOutput = "ERROR: OpCode null";
                this.updateRegisters();
                //System.out.println("Next Instruction: " + this.PC);
                return this.registers;
            }
            int[] contents = new int[4];
            switch (opCode) {
                case "LDR":
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.LDR(contents[0], contents[1], contents[2], contents[3]);
                    //System.out.println("LDR");
                    break;
                case "STR":
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.STR(contents[0], contents[1], contents[2], contents[3]);
                    //System.out.println("STR");
                    break;
                case "LDA":
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.LDA(contents[0], contents[1], contents[2], contents[3]);
                    //System.out.println("LDA");
                    break;
                case "LDX":
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.LDX(contents[0], contents[1], contents[2], contents[3]);
                    //System.out.println("LDX");
                    break;
                case "STX":
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.STX(contents[0], contents[1], contents[2], contents[3]);
                    //System.out.println("STX");
                    break;
                default:
                    this.debugOutput = "ERROR: Invalid Instruction";
                    //System.out.println("Invalid Command.");
            }

            this.PC = this.getAddressOfNextInstruction();
            this.updateMemoryRegisters();
            //System.out.println("\nNext Instruction: " + this.PC);
            this.debugOutput = this.debugOutput + "\nNext Instruction: " + this.PC;
            this.updateRegisters();
            return this.registers;
        }

        //using the PC, gets the address of the next instruction in the loadFile
        public int getAddressOfNextInstruction(){
            for (int i = 0; i < this.loadFile.size(); i++) {
                String[] words = this.loadFile.get(i).split("\\s+"); //splits by any number of spaces
                int address = Conversion.convertToDecimal(words[0]); //converts octal address in loadFile to decimal 
                if(this.PC == address && i != this.loadFile.size()-1){ //if PC equals address and it isn't last line in loadfile
                    String[] nextWords = this.loadFile.get(i+1).split("\\s+"); //splits by any number of spaces
                    return Conversion.convertToDecimal(nextWords[0]); //returns next address in loadFile
                }
            }
            //end of LoadFile so PC + 1
            return this.PC+1;
        }

        //returns a int array where each portion of a loadStore instruction is parsed out
        //returned contents are in decimal
        //[0] == gpr, [1] == ix, [2] == indirect, [3] == address
        public int[] parseLoadStoreInst(String binaryString){
            assert binaryString.length() == 16: "Instructions must be 16 bits";
            int[] contents = new int[4]; // Creates an array of size 4
            contents[0] = Integer.parseInt(binaryString.substring(6,8), 2); //gpr
            contents[1] = Integer.parseInt(binaryString.substring(8,10), 2); //ixr
            contents[2] = Integer.parseInt(binaryString.substring(10,11), 2); //indirect bit
            contents[3] = Integer.parseInt(binaryString.substring(11,16), 2); //address
            return contents;
        }

        //reads each line in the loadfile and puts it into loadFile array
        private boolean readLoadFile(){
            //System.out.println("Program File: " + this.programFile);
            try (Scanner scanner = new Scanner(new File(this.programFile))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    this.loadFile.add(line);
                }
            } catch (FileNotFoundException e) {
                //System.err.println("File not found: " + e.getMessage());
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
                    //System.out.println("Incorrect Load File");
                    return false;
                }

                String address = words[0];
                if(Conversion.convertToDecimal(address) < 6 || Conversion.convertToDecimal(address) > 2047){
                    //System.out.println("ERROR: Cannot store in memory over 2047");
                    return false;
                }
                String instruction = words[1];
                int num = Conversion.convertToDecimal(instruction);
                if(num<0 || num>65535){
                    //System.out.println("ERROR: Cannot store a value over 65535 or less than 0");
                    return false;
                }else{
                    this.memory.put(Conversion.convertToDecimal(address), Conversion.convertToDecimal(instruction));
                }
            }
            this.printMemory();
            return true;
        }

        private void printMemory(){
            this.memory.forEach((key, value) -> System.out.println(key + " -> " + value));
        }

        public boolean storeInMemory(int address, int value){
            if(address < 6 || address > 2047){
                return false;
            }else if(value < 0 || value > 65535){
                return false;
            }else{
                this.memory.put(address, value);
                return true;
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

        //Load gpr register with value from memory
        private String LDR(int gpr, int ixr, int indirect, int address){
            int add = this.computeEffectiveAddress(ixr, indirect, address, false);
            switch (gpr) {
                case 0: //load into gpr0
                    this.setGPR0(this.getFromMemory(add));
                    //System.out.println("GPR0 set with: " + this.getFromMemory(add));
                    return "GPR0 set with: " + this.getFromMemory(add);
                case 1: //load into gpr1
                    this.setGPR1(this.getFromMemory(add));
                    //System.out.println("GPR1 set with: " + this.getFromMemory(add));
                    return "GPR1 set with: " + this.getFromMemory(add);
                case 2: //load into gpr2
                    this.setGPR2(this.getFromMemory(add));
                    //System.out.println("GPR2 set with: " + this.getFromMemory(add));
                    return "GPR2 set with: " + this.getFromMemory(add);
                case 3: //load into gpr3
                    this.setGPR3(this.getFromMemory(add));
                    //System.out.println("GPR3 set with: " + this.getFromMemory(add));
                    return "GPR3 set with: " + this.getFromMemory(add);
                default:
                    //System.out.println("Invalid command");
                    return "Invalid command";
            }
        }

        //Store value in gpr to memory
        private String STR(int gpr, int ixr, int indirect, int address){
            int add = this.computeEffectiveAddress(ixr, indirect, address, false);
            switch (gpr) {
                case 0: //store gpr0 to memory
                    this.memory.put(add, this.GPR0);
                    //System.out.println("GPR0: " + this.GPR0 + " stored at: " + add);
                    return "GPR0: " + this.GPR0 + " stored at: " + add;
                case 1: //store gpr1 to memory
                    this.memory.put(add, this.GPR1);
                    //System.out.println("GPR1: " + this.GPR1 + " stored at: " + add);
                    return "GPR1: " + this.GPR1 + " stored at: " + add;
                case 2: //store gpr2 to memory
                    this.memory.put(add, this.GPR2);
                    //System.out.println("GPR2: " + this.GPR2 + " stored at: " + add);
                    return "GPR2: " + this.GPR2 + " stored at: " + add;
                case 3: //store gpr3 to memory
                    this.memory.put(add, this.GPR3);
                    //System.out.println("GPR3: " + this.GPR3 + " stored at: " + add);
                    return "GPR3: " + this.GPR3 + " stored at: " + add;
                default:
                    //System.out.println("Invalid command");
                    return "Invalid command";
            }
        }

        //Load gpr register with address
        private String LDA(int gpr, int ixr, int indirect, int address){
            int add = this.computeEffectiveAddress(ixr, indirect, address, false);
            switch (gpr) {
                case 0: //load into gpr0
                    this.setGPR0(add);
                    //System.out.println("GPR0 set with: " + add);
                    return "GPR0 set with: " + add;
                case 1: //load into gpr1
                    this.setGPR1(add);
                    //System.out.println("GPR1 set with: " + add);
                    return "GPR1 set with: " + add;
                case 2: //load into gpr2
                    this.setGPR2(add);
                    //System.out.println("GPR2 set with: " + add);
                    return "GPR2 set with: " + add;
                case 3: //load into gpr3
                    this.setGPR3(add);
                    //System.out.println("GPR3 set with: " + add);
                    return "GPR3 set with: " + add;
                default:
                    //System.out.println("Invalid command");
                    return "Invalid command";
            }
        }

        //Load Index register from memory
        private String LDX(int gpr, int ixr, int indirect, int address){
            int add = this.computeEffectiveAddress(ixr, indirect, address, true);
            switch (ixr) {
                case 1: //load into ixr1
                    this.setIXR1(this.getFromMemory(add));
                    //System.out.println("IXR1 set with: " + this.getFromMemory(add));
                    return "IXR1 set with: " + this.getFromMemory(add);
                case 2: //load into ixr2
                    this.setIXR2(this.getFromMemory(add));
                    //System.out.println("IXR2 set with: " + this.getFromMemory(add));
                    return "IXR2 set with: " + this.getFromMemory(add);
                case 3: //load into ixr3
                    this.setIXR3(this.getFromMemory(add));
                    //System.out.println("IXR3 set with: " + this.getFromMemory(add));
                    return "IXR3 set with: " + this.getFromMemory(add);
                default:
                    //System.out.println("Invalid command");
                    return "Invalid command";
            }
        }

        //Store Index register to memory
        private String STX(int gpr, int ixr, int indirect, int address){
            int add = this.computeEffectiveAddress(ixr, indirect, address, true);
            switch (ixr) {
                case 1: //store ixr1 to memory
                    this.memory.put(add, this.IXR1);
                    //System.out.println("IXR1: " + this.IXR1 + " stored at: " + add);
                    return "IXR1: " + this.IXR1 + " stored at: " + add;
                case 2: //store ixr2 to memory
                    this.memory.put(add, this.IXR2);
                    //System.out.println("IXR2: " + this.IXR2 + " stored at: " + add);
                    return "IXR2: " + this.IXR2 + " stored at: " + add;
                case 3: //store ixr3 to memory
                    this.memory.put(add, this.IXR3);
                    //System.out.println("IXR3: " + this.IXR3 + " stored at: " + add);
                    return "IXR3: " + this.IXR3 + " stored at: " + add;
                default:
                    //System.out.println("Invalid command");
                    return "Invalid command";
            }
        }

        private int computeEffectiveAddress(int ixr, int indirect, int address, boolean ignoreIXR){
            int ea = -1;
            if(ignoreIXR){ //LDX or STX instruction
                ea = address;
            }else{
                if(ixr == 0){
                    ea = address;
                }else if(ixr==1){
                    if(this.IXR1 == -1){ //ixr1 not set yet, so use 0 as the value in ixr
                        ea = address;
                    }else{
                        ea = this.IXR1 + address;
                    }
                }else if(ixr==2){
                    if(this.IXR2 == -1){
                        ea = address;
                    }else{
                        ea = this.IXR2 + address;
                    }
                }else if(ixr==3){
                    if(this.IXR3 == -1){
                        ea = address;
                    }else{
                        ea = this.IXR3 + address;
                    }
                }else{
                    System.out.println("Invalid IXR");
                }

            }
            

            if(indirect == 1){
                ea = getFromMemory(ea);
            }
            return ea;
        }

        //updates MAR, MBR when the PC changes
        private void updateMemoryRegisters(){
            if(this.PC!=-1){
                this.MAR = this.PC;
                this.MBR = this.getFromMemory(this.MAR);
                this.IR = this.getFromMemory(PC);
            }
        }
    
        public int getIR(){
            return this.IR;
        }

        public void setIR(int ir) {
            this.IR = ir;
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
            return this.MFR;
        }

        public void setMFR(int MFR) {
            this.MFR = MFR;
        }

        public String getProgramFile() {
            return programFile;
        }

        public void setProgramFile(String programFile) {
            this.programFile = programFile;
        }  
}
