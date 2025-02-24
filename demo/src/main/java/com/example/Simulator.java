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

        //hashmap storing all opCodes
        //key: string binary representation of an opCode
        //value: string representation of opCode
        private HashMap<String, String> opCodes;

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
            this.opCodes = new HashMap<>();
            this.initializeOpcodes();
        }

        //Initializing the binary value with corresponding opCode
        private void initializeOpcodes(){
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
        public void run(){
            
        }

        //steps through memory depending on where PC is set
        //should error if pc is not set
        //should also increment pc after each instruction
        public boolean step(){
            if(this.PC == -1){
                System.out.println("Cannot step without setting PC...");
                return false;
            }
            
            //get value at address of PC from memory
            int instr = this.getFromMemory(this.PC);
            if(instr == -1){
                System.out.println("No instruction at: " + this.PC);
                return false;
            }

            String binaryInstruction = Conversion.convertToBinaryString(instr, 16); //gets the binary equivalent of the instruction stored at memory location of PC
            String opCodeBinaryString = binaryInstruction.substring(0, 6);
            String opCode = this.opCodes.get(opCodeBinaryString);
            if(opCode == null){
                System.out.println("Opcode null");
                this.PC = this.getAddressOfNextInstruction();
                System.out.println("Next Instruction: " + this.PC);
                return false;
            }
            int[] contents = new int[4];
            boolean res = false;
            switch (opCode) {
                case "LDR":
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    res = this.LDR(contents[0], contents[1], contents[2], contents[3]);
                    System.out.println("LDR");
                    break;
                case "STR":
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    res = this.STR(contents[0], contents[1], contents[2], contents[3]);
                    System.out.println("STR");
                    break;
                case "LDA":
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    res = this.LDA(contents[0], contents[1], contents[2], contents[3]);
                    System.out.println("LDA");
                    break;
                case "LDX":
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    res = this.LDX(contents[0], contents[1], contents[2], contents[3]);
                    System.out.println("LDX");
                    break;
                case "STX":
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    res = this.STX(contents[0], contents[1], contents[2], contents[3]);
                    System.out.println("STX");
                    break;
                default:
                    System.out.println("Invalid command.");
            }

            this.PC = this.getAddressOfNextInstruction();
            System.out.println("Next Instruction: " + this.PC);
            return true;
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

        //Load gpr register with value from memory
        private boolean LDR(int gpr, int ixr, int indirect, int address){
            int add = this.computeEffectiveAddress(ixr, indirect, address);
            switch (gpr) {
                case 0: //load into gpr0
                    this.setGPR0(this.getFromMemory(add));
                    System.out.println("GPR0 set with: " + this.getFromMemory(add));
                    break;
                case 1: //load into gpr1
                    this.setGPR1(this.getFromMemory(add));
                    System.out.println("GPR1 set with: " + this.getFromMemory(add));
                    break;
                case 2: //load into gpr2
                    this.setGPR2(this.getFromMemory(add));
                    System.out.println("GPR2 set with: " + this.getFromMemory(add));
                    break;
                case 3: //load into gpr3
                    this.setGPR3(this.getFromMemory(add));
                    System.out.println("GPR3 set with: " + this.getFromMemory(add));
                    break;
                default:
                    System.out.println("Invalid command.");
            }
            return true;
        }

        //Store value in gpr to memory
        private boolean STR(int gpr, int ixr, int indirect, int address){
            int add = this.computeEffectiveAddress(ixr, indirect, address);
            switch (gpr) {
                case 0: //store gpr0 to memory
                    this.memory.put(add, this.GPR0);
                    System.out.println("GPR0: " + this.GPR0 + " stored at: " + add);
                    break;
                case 1: //store gpr1 to memory
                    this.memory.put(add, this.GPR1);
                    System.out.println("GPR1: " + this.GPR1 + " stored at: " + add);
                    break;
                case 2: //store gpr2 to memory
                    this.memory.put(add, this.GPR2);
                    System.out.println("GPR2: " + this.GPR2 + " stored at: " + add);
                    break;
                case 3: //store gpr3 to memory
                    this.memory.put(add, this.GPR3);
                    System.out.println("GPR3: " + this.GPR3 + " stored at: " + add);
                    break;
                default:
                    System.out.println("Invalid command.");
            }
            return true;
        }

        //Load gpr register with address
        private boolean LDA(int gpr, int ixr, int indirect, int address){
            int add = this.computeEffectiveAddress(ixr, indirect, address);
            switch (gpr) {
                case 0: //load into gpr0
                    this.setGPR0(add);
                    System.out.println("GPR0 set with: " + add);
                    break;
                case 1: //load into gpr1
                    this.setGPR1(add);
                    System.out.println("GPR1 set with: " + add);
                    break;
                case 2: //load into gpr2
                    this.setGPR2(add);
                    System.out.println("GPR2 set with: " + add);
                    break;
                case 3: //load into gpr3
                    this.setGPR3(add);
                    System.out.println("GPR3 set with: " + add);
                    break;
                default:
                    System.out.println("Invalid command.");
            }
            return true;
        }

        //Load Index register from memory
        private boolean LDX(int gpr, int ixr, int indirect, int address){
            int add = this.computeEffectiveAddress(ixr, indirect, address);
            switch (ixr) {
                case 1: //load into ixr1
                    this.setIXR1(this.getFromMemory(add));
                    System.out.println("IXR1 set with: " + this.getFromMemory(add));
                    break;
                case 2: //load into ixr2
                    this.setIXR2(this.getFromMemory(add));
                    System.out.println("IXR2 set with: " + this.getFromMemory(add));
                    break;
                case 3: //load into ixr3
                    this.setIXR3(this.getFromMemory(add));
                    System.out.println("IXR3 set with: " + this.getFromMemory(add));
                    break;
                default:
                    System.out.println("Invalid command.");
            }
            return true;
        }

        //Store Index register to memory
        private boolean STX(int gpr, int ixr, int indirect, int address){
            

            return true;
        }

        private int computeEffectiveAddress(int ixr, int indirect, int address){
            int ea = -1;
            if(ixr == 0){
                ea = address;
            }else if(ixr==1){
                ea = this.IXR1 + address;
            }else if(ixr==2){
                ea = this.IXR2 + address;
            }else if(ixr==3){
                ea = this.IXR3 + address;
            }

            if(indirect == 1){
                ea = getFromMemory(ea);
            }
            return ea;
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
