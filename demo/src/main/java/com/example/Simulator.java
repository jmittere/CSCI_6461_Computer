package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
        private int[] conditionCode;
        //debug output to be passed to the frontend debugOutput text area 
        //contains error code if failed instruction or symbolic instruction
        private String debugOutput;
        //content that is being outputted to console printer
        private String consolePrinter;

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

        //represents L1 cache 
        public Cache cache;

        // Constructor
        public Simulator() {
            this.GPR0 = -32769;
            this.GPR1 = -32769;
            this.GPR2 = -32769;
            this.GPR3 = -32769;
            this.IR = -32769;
            this.conditionCode = new int[4]; //[OVERFLOW, UNDERFLOW, DIVZERO, EQUALNOT] 
            for (int i=0; i<conditionCode.length; i++){
                this.conditionCode[i] = -1;
            }
            this.IXR1 = -32769;
            this.IXR2 = -32769;
            this.IXR3 = -32769;
            this.PC = -32769;
            this.MAR = -32769;
            this.MBR = -32769;
            this.MFR = -32769;
            this.debugOutput = "";
            this.programFile = "";
            this.loadFile = new ArrayList<>();
            this.memory = new HashMap<>();
            this.opCodes = new HashMap<>();
            this.registers = new HashMap<>();
            this.initializeOpcodes();
            this.cache = new Cache();
            this.consolePrinter = "";
            this.memory.put(1,6); //put address of HALT instruction at address 1 
            this.memory.put(6,0); //put HALT instruction at address 6 for Machine Faults 
        }

        //updates hashmap that has contents of each register
        //must run this function before returning values to the frontend
        public void updateRegisters(){
            if(this.GPR0 == -32769){
                this.registers.put("GPR0", "");
            }else{
                this.registers.put("GPR0", String.valueOf(this.GPR0));
            }
            if(this.GPR1 == -32769){
                this.registers.put("GPR1", "");
            }else{
                this.registers.put("GPR1", String.valueOf(this.GPR1));
            }
            if(this.GPR2 == -32769){
                this.registers.put("GPR2", "");
            }else{
                this.registers.put("GPR2", String.valueOf(this.GPR2));
            }
            if(this.GPR3 == -32769){
                this.registers.put("GPR3", "");
            }else{
                this.registers.put("GPR3", String.valueOf(this.GPR3));
            }
            if(this.IXR1 == -32769){
                this.registers.put("IXR1", "");
            }else{
                this.registers.put("IXR1", String.valueOf(this.IXR1));
            }
            if(this.IXR2 == -32769){
                this.registers.put("IXR2", "");
            }else{
                this.registers.put("IXR2", String.valueOf(this.IXR2));
            }
            if(this.IXR3 == -32769){
                this.registers.put("IXR3", "");
            }else{
                this.registers.put("IXR3", String.valueOf(this.IXR3));
            }
            if(this.PC == -32769){
                this.registers.put("PC", "");
            }else{
                this.registers.put("PC", String.valueOf(this.PC));
            }
            if(this.MAR == -32769){
                this.registers.put("MAR", "");
            }else{
                this.registers.put("MAR", String.valueOf(this.MAR));
            }
            if(this.MBR == -32769){
                this.registers.put("MBR", "");
            }else{
                this.registers.put("MBR", String.valueOf(this.MBR));
            }
            if(this.MFR == -32769){
                this.registers.put("MFR", "");
            }else{
                this.registers.put("MFR", String.valueOf(this.MFR));
            }
            if(this.IR == -32769){
                this.registers.put("IR", "");
            }else{
                this.registers.put("IR", String.valueOf(this.IR));
            }
            if(this.conditionCode[0] == 1){
                this.registers.put("CC", "OVERFLOW");
            }else if(this.conditionCode[1] == 1){
                this.registers.put("CC", "UNDERFLOW");
            }else if(this.conditionCode[2] == 1){
                this.registers.put("CC", "DIVZERO");
            }else if(this.conditionCode[3] == 1){
                this.registers.put("CC", "EQUAL");
            }else if(this.conditionCode[3] == 0){
                this.registers.put("CC", "NOTEQUAL");
            }else{
                this.registers.put("CC", "");
            }  
            this.registers.put("debugOutput", this.debugOutput);
            this.registers.put("cache", this.cache.displayCacheBlocks());
            this.registers.put("consolePrinter", this.consolePrinter);
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
        
        //runs the entire program in the LoadFile when the run button is pressed
        public HashMap<String, String> run(){
            HashMap<String, String> registerContents = null;
            if(this.programFile.equals("")){
                this.debugOutput = "No Program File to Run";
                this.updateRegisters();
                return this.registers;
            }

            int count = 1;
            registerContents = this.step();
            if((registerContents.get("debugOutput")).contains("waiting for input")){
                return registerContents;
            }
            while(!(registerContents.get("debugOutput")).contains("ERROR: No instruction")){ //while there are still valid instructions to be executed
                if(registerContents.get("debugOutput").contains("waiting for input")){
                    return registerContents;
                }
                registerContents = this.step();
                count++;
                if(count > 10000000){ //failsafe to prevent infinite loops
                    System.exit(0);
                }
            }
            return registerContents;
        }

        //steps through memory depending on where PC is set
        //should error if pc is not set
        //should also increment pc after each instruction
        public HashMap<String, String> step(){
            if(this.PC == -32769){
                //System.out.println("Cannot step without setting PC...");
                this.debugOutput = "ERROR: Cannot step without setting PC";
                this.updateRegisters();
                return this.registers;
            } else if(this.PC > 2047){
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

            int bitWidth = 16; //16, Mask to extract only the lower `bitWidth` bits
            
            //String binaryInstruction = Conversion.convertToBinaryString(instr, 16); //gets the binary equivalent of the instruction stored at memory location of PC
            String binaryInstruction = String.format("%" + bitWidth + "s", Integer.toBinaryString(instr & ((1 << bitWidth) - 1))).replace(' ', '0');
            String opCodeBinaryString = binaryInstruction.substring(0, 6);
            String opCode = this.opCodes.get(opCodeBinaryString);
            if(opCode == null){ //Illegal Operation Code machine fault
                this.debugOutput = this.machineFault(2);
                this.updateRegisters();
                return this.registers;
            }
            //HALT THE PROGRAM
            if(opCodeBinaryString.equals("000000") && binaryInstruction.substring(10).equals("000000")){ //Halt conditions
                this.PC = this.getAddressOfNextInstruction();
                this.updateMemoryRegisters();
                this.debugOutput = "HALT: Stopping the Program" + "\nNext Instruction: " + this.PC;
                this.updateRegisters();
                return this.registers;
            }
            int[] contents = new int[4];
            switch (opCode) {
                case "LDR":
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.LDR(contents[0], contents[1], contents[2], contents[3]);
                    break;
                case "STR":
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.STR(contents[0], contents[1], contents[2], contents[3]);
                    if(this.debugOutput.contains("Illegal")){//trap or machine fault occured
                        this.debugOutput = this.debugOutput + "\nNext Instruction: " + this.PC;
                        this.updateMemoryRegisters();
                        this.updateRegisters();
                        return this.registers;
                    }
                    break;
                case "LDA":
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.LDA(contents[0], contents[1], contents[2], contents[3]);
                    break;
                case "LDX":
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.LDX(contents[0], contents[1], contents[2], contents[3]);
                    break;
                case "STX":
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.STX(contents[0], contents[1], contents[2], contents[3]);
                    if(this.debugOutput.contains("Illegal")){//trap or machine fault occured
                        this.debugOutput = this.debugOutput + "\nNext Instruction: " + this.PC;
                        this.updateMemoryRegisters();
                        this.updateRegisters();
                        return this.registers;
                    }
                    break;
                case "AMR":    
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.AMR(contents[0], contents[1], contents[2], contents[3]);
                    break;
                case "SMR":    
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.SMR(contents[0], contents[1], contents[2], contents[3]);
                    break;
                case "AIR":    
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.AIR(contents[0], contents[3]);
                    break;
                case "SIR":    
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.SIR(contents[0], contents[3]);
                    break;
                case "MLT":    
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.MLT(contents[0], contents[1], contents[2], contents[3]);
                    break;
                case "DVD":    
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.DVD(contents[0], contents[1], contents[2], contents[3]);
                    break;
                case "TRR":    
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.TRR(contents[0], contents[1], contents[2], contents[3]);
                    break;
                case "AND":    
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.AND(contents[0], contents[1], contents[2], contents[3]);
                    break;
                case "ORR":    
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.ORR(contents[0], contents[1], contents[2], contents[3]);
                    break;
                case "NOT":    
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.NOT(contents[0], contents[1], contents[2], contents[3]);
                    break;
                case "JZ":    
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.JZ(contents[0], contents[1], contents[2], contents[3]);
                    if(debugOutput.contains("PC set with")){//pc already set so return from step here
                        this.updateMemoryRegisters();
                        this.debugOutput = this.debugOutput + "\nNext Instruction: " + this.PC;
                        this.updateRegisters();
                        return this.registers;
                    }
                    break;
                case "JNE":    
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.JNE(contents[0], contents[1], contents[2], contents[3]);
                    if(debugOutput.contains("PC set with")){//pc already set so return from step here
                        this.updateMemoryRegisters();
                        this.debugOutput = this.debugOutput + "\nNext Instruction: " + this.PC;
                        this.updateRegisters();
                        return this.registers;
                    }
                    break;
                case "JCC":    
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.JCC(contents[0], contents[1], contents[2], contents[3]);
                    if(debugOutput.contains("PC set with")){//pc already set so return from step here
                        this.updateMemoryRegisters();
                        this.debugOutput = this.debugOutput + "\nNext Instruction: " + this.PC;
                        this.updateRegisters();
                        return this.registers;
                    }
                    break;  
                case "JMA":    
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.JMA(contents[0], contents[1], contents[2], contents[3]);
                    this.updateMemoryRegisters();
                    this.debugOutput = this.debugOutput + "\nNext Instruction: " + this.PC;
                    this.updateRegisters();
                    return this.registers;
                case "JSR":    
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.JSR(contents[0], contents[1], contents[2], contents[3]);
                    this.updateMemoryRegisters();
                    this.debugOutput = this.debugOutput + "\nNext Instruction: " + this.PC;
                    this.updateRegisters();
                    return this.registers;
                case "RFS":    
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.RFS(contents[0], contents[1], contents[2], contents[3]);
                    this.updateMemoryRegisters();
                    this.debugOutput = this.debugOutput + "\nNext Instruction: " + this.PC;
                    this.updateRegisters();
                    return this.registers;
                case "SOB":    
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.SOB(contents[0], contents[1], contents[2], contents[3]);
                    if(debugOutput.contains("PC set with")){//pc already set so return from step here
                        this.updateMemoryRegisters();
                        this.debugOutput = this.debugOutput + "\nNext Instruction: " + this.PC;
                        this.updateRegisters();
                        return this.registers;
                    }
                    break;  
                case "JGE":    
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.JGE(contents[0], contents[1], contents[2], contents[3]);
                    if(debugOutput.contains("PC set with")){//pc already set so return from step here
                        this.updateMemoryRegisters();
                        this.debugOutput = this.debugOutput + "\nNext Instruction: " + this.PC;
                        this.updateRegisters();
                        return this.registers;
                    }
                    break; 
                case "OUT":    
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.OUT(contents[0], contents[1], contents[2], contents[3]);
                    break;
                case "CHK":    
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.CHK(contents[0], contents[1], contents[2], contents[3]);
                    break;
                case "IN":
                    contents = this.parseLoadStoreInst(binaryInstruction);
                    this.debugOutput = this.IN(contents[0], contents[1], contents[2], contents[3]);
                    this.updateMemoryRegisters();
                    this.debugOutput = this.debugOutput + "\nNext Instruction: " + this.PC;
                    this.updateRegisters();
                    return this.registers;
                case "SRC": 
                    contents = this.parseShiftRotate(binaryInstruction);
                    this.debugOutput = this.SRC(contents[0], contents[1], contents[2], contents[3]);
                    break;
                case "RRC": 
                    contents = this.parseShiftRotate(binaryInstruction);
                    this.debugOutput = this.RRC(contents[0], contents[1], contents[2], contents[3]);
                    break;
                case "TRAP":
                    contents = this.parseShiftRotate(binaryInstruction);
                    this.debugOutput = this.TRAP(contents[0],contents[1], contents[2], contents[3]);
                    this.updateMemoryRegisters();
                    this.updateRegisters();
                    return this.registers;
                default:
                    this.debugOutput = "ERROR: Invalid Instruction";
                    //System.out.println("Invalid Command.");
            }

            this.PC = this.getAddressOfNextInstruction();
            this.updateMemoryRegisters();
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

        //advances program after user input has been used to set a register
        public HashMap<String, String> advanceAfterUserInput(int gpr, int val){
            if(val < -32768 || val > 32767){
                this.debugOutput = "ERROR: value must be between -32768 and 32767";
                this.updateRegisters();
                return this.registers; 
            }
            String debugOut = "";
            if(gpr == 0){
                this.GPR0 = val;
                debugOut = "IN: GPR0 set with: " + this.GPR0;
            }else if(gpr == 1){
                this.GPR1 = val;
                debugOut = "IN: GPR1 set with: " + this.GPR1;
            }else if(gpr == 2){
                this.GPR2 = val;
                debugOut = "IN: GPR2 set with: " + this.GPR2;
            }else if(gpr == 3){
                this.GPR3 = val;
                debugOut = "IN: GPR3 set with: " + this.GPR3;
            }else{
                debugOut = "ERROR: GPR must be between 0-3";
            }
            this.PC = this.getAddressOfNextInstruction();
            this.updateMemoryRegisters();
            this.debugOutput = debugOut + "\nNext Instruction: " + this.PC;
            this.updateRegisters();
            return this.registers;
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

        //returns a int array where each portion of a shiftRotate instruction is parsed out
        //returned contents are in decimal
        //[0] == gpr, [1] == A/L, [2] == L/R, [3] == Count
        public int[] parseShiftRotate(String binaryString){
            assert binaryString.length() == 16: "Instructions must be 16 bits";
            int[] contents = new int[4]; // Creates an array of size 4
            contents[0] = Integer.parseInt(binaryString.substring(6,8), 2); //gpr
            contents[1] = Integer.parseInt(binaryString.substring(8,9), 2); //A/L
            contents[2] = Integer.parseInt(binaryString.substring(9,10), 2); //L/R
            contents[3] = Integer.parseInt(binaryString.substring(12), 2); //count
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
                System.out.println("Finished Loading Load File");
            } catch (FileNotFoundException err) {
                try (InputStream input = getClass().getClassLoader().getResourceAsStream(this.programFile)) {
                if (input == null) {
                    System.out.println("File not found!");
                    return false;
                }

                Scanner scanner = new Scanner(input);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    this.loadFile.add(line);
                }
                scanner.close(); 
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
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
                if((Conversion.convertToDecimal(address) != 0 && Conversion.convertToDecimal(address) < 6) || Conversion.convertToDecimal(address) > 2047){
                    System.out.println("ERROR: Cannot store in memory over 2047");
                    return false;
                }
                String instruction = words[1];
                int num = Integer.parseInt(words[1], 8);
                String binaryInst = Conversion.convertToBinaryString(num, 16);
                if (binaryInst.charAt(0) == '1') {
                    num -= (1 << 16); // Subtract 2^bitWidth to get the correct negative value
                }
                if(num < -32768 || num > 32767){
                    System.out.println("ERROR: Cannot store a value over 32767 or less than -32768");
                    return false;
                }else{
                    this.memory.put(Conversion.convertToDecimal(address), num);
                }
            }
            this.printMemory();
            return true;
        }

        private void printMemory(){
            this.memory.forEach((key, value) -> System.out.println(key + " -> " + value));
        }

        //retrieves a value from an address in memory using an int decimal address
        //adds address and memory to cache if not already loaded 
        public int getFromMemory(int address){
            Cacheblock c = this.cache.getCacheBlock(address);
            if (c==null) { //address not loaded into cache
                //System.out.println(address + " not in cache");
            }else{
                //System.out.println(c.getValue() + " from Cache");
                return c.getValue();
            }

            if(!memory.containsKey(address)){
                return -1;
            }else if(address < 0 || address > 2047){
                //TODO: Throw exception for trying to access memory out of bounds exception
                return -1;
            }else{
                int val = this.memory.get(address); //value from memory
                Cacheblock deadBlock = this.cache.setCacheBlock(address, val); //add address and value to cache before returning memory
                if(deadBlock != null){ //need to do a write back
                    int addr = deadBlock.getAddress();
                    int oldVal = deadBlock.getValue();        
                    if((addr != 0 && addr < 6) || addr > 2047){
                        System.out.println("Error storing during write back...");
                    }else if(oldVal < -32768 || oldVal > 32767){
                        System.out.println("Error storing during write back...");
                    }else{
                        this.memory.put(addr, oldVal); //write back
                        //System.out.println("Write Back: Stored value back in memory for cache block...");
                    }
                }
                //System.out.println("Val returned from memory: " + val);
                return val;
            }
        }

        public String storeInMemory(int address, int value){
            if(this.cache.containsAddress(address)){ //cache has address, so write to cache rather than memory
                this.cache.getCacheBlock(address).setValue(value);
                this.cache.getCacheBlock(address).setDirtyBit(1); //modified value for memory in cache
                return "";
            }

            if(address !=0 && address < 6){
                return this.machineFault(0);
            }else if(address > 2047){
                return this.machineFault(3);
            }else if(value < -32768 || value > 32767){
                return "Illegal Store value over alloted bit amount";
            }else{
                this.memory.put(address, value);
                return "";
            }
        }

        private String machineFault(int id){
            if(id == 0){//Illegal Memory Address to Reserved 1-6 Locations
                this.MFR = 1; //set MFR to binary 0001
                this.PC = 6; //set PC to address 6 which contains the HALT instruction
                System.out.println("Illegal Memory Address to Reserved Locations");
                return "Illegal Memory Address to Reserved Locations";
            }else if(id == 1){ //Illegal Trap Code
                this.MFR = 2; //set MFR to binary 0010
                this.PC = 6; //set PC to address 6 which contains the HALT instruction
                System.out.println("Illegal TRAP code");
                return "Illegal TRAP code";
            }else if(id == 2){ //Illegal Opcode
                this.MFR = 4; //set MFR to binary 0100
                this.PC = 6; //set PC to address 6 which contains the HALT instruction
                System.out.println("Illegal Operation Code");
                return "Illegal Operation Code";
            }else if(id == 3){ //Illegal Memory Address beyond 2048
                this.MFR = 8; //set MFR to binary 1000
                this.PC = 6; //set PC to address 6 which contains the HALT instruction
                System.out.println("Illegal Memory Address beyond 2048");
                return "Illegal Memory Address beyond 2048";
            }else{
                System.out.println("Illegal machine fault ID");
                return "Illegal machine fault ID";
            }
        }

        //initalizes program with program File specified 
        //returns false if failed to initalize with loadFile
        public boolean initializeProgram(){
            boolean res;
            if(this.programFile.equals("")){
                return false;
            }else{
                res = this.readLoadFile();
            }
            return res;
        }

        //TRAP code
        private String TRAP(int gpr, int AL, int LR, int trapCode){
            assert trapCode < 16: "TrapCode cannot be greater than 15";
            int nextPC = this.PC  + 1;
            this.memory.put(2, nextPC);
            int addressOfTrap = this.getFromMemory(0); 
            if(addressOfTrap == -1){
                return this.machineFault(1);
            }
            
            addressOfTrap = addressOfTrap + trapCode; //mem location 0 contains address of trap table + trapCode index
            addressOfTrap = this.getFromMemory(addressOfTrap);
            if(addressOfTrap == -1){
                return "ERROR: No Trap instructions stored at " + addressOfTrap;
            }
            this.PC = addressOfTrap;
            int count = 0;
            HashMap<String, String> registerContents = null;
            while(count < 1000){ //execute Trap subroutine while counter is less than 1000, no subroutines greater than that amount of inst
                //get value at address of PC from memory
                int instr = this.getFromMemory(this.PC);
                if(instr == -1){
                //System.out.println("No instruction at: " + this.PC);
                return "ERROR: No instruction at " + this.PC;
                }
 
                int bitWidth = 16; //16, Mask to extract only the lower `bitWidth` bits 
                //String binaryInstruction = Conversion.convertToBinaryString(instr, 16); //gets the binary equivalent of the instruction stored at memory location of PC
                String binaryInstruction = String.format("%" + bitWidth + "s", Integer.toBinaryString(instr & ((1 << bitWidth) - 1))).replace(' ', '0');
                String opCodeBinaryString = binaryInstruction.substring(0, 6);
                String opCode = this.opCodes.get(opCodeBinaryString);
                if(opCode == null){ //Illegal Operation Code machine fault
                    return this.machineFault(2);
                }else if(opCode.equals("RFS")){ //return from subroutine of TRAP
                    //restore PC and break from loop
                    registerContents = this.step();
                    this.PC = this.memory.get(2); //get return address after TRAP
                    return "Returning from TRAP" + "\nNext Instruction: " + this.PC;
                }else{
                    registerContents = this.step();
                }

                count ++;
            }
            return "ERROR: No RFS instruction at the end of TRAP";
        }

        //Load gpr register with value from memory
        private String LDR(int gpr, int ixr, int indirect, int address){
            int add = this.computeEffectiveAddress(ixr, indirect, address, false);
            int val = 0;
            switch (gpr) {
                case 0: //load into gpr0
                    val = this.getFromMemory(add);
                    this.setGPR0(val);
                    return "GPR0 set with: " + this.GPR0;
                case 1: //load into gpr1
                    val = this.getFromMemory(add);
                    this.setGPR1(val);
                    return "GPR1 set with: " + this.GPR1;
                case 2: //load into gpr2
                    val = this.getFromMemory(add);
                    this.setGPR2(val);
                    return "GPR2 set with: " + this.GPR2;
                case 3: //load into gpr3
                    val = this.getFromMemory(add);
                    this.setGPR3(val);
                    return "GPR3 set with: " + this.GPR3;
                default:
                    return "Invalid command";
            }
        }

        //Store value in gpr to memory
        private String STR(int gpr, int ixr, int indirect, int address){
            int add = this.computeEffectiveAddress(ixr, indirect, address, false);
            String res;
            switch (gpr) {
                case 0: //store gpr0 to memory
                    res = this.storeInMemory(add, this.GPR0);
                    if (res.equals("")){
                        return "GPR0: " + this.GPR0 + " stored at: " + add;
                    }else{
                        return res;
                    }
                    
                case 1: //store gpr1 to memory
                    res = this.storeInMemory(add, this.GPR1);
                    if(res.equals("")){
                        return "GPR1: " + this.GPR1 + " stored at: " + add;
                    }else{
                        return res;
                    }
                case 2: //store gpr2 to memory
                    res = this.storeInMemory(add, this.GPR2);
                    if(res.equals("")){
                        return "GPR2: " + this.GPR2 + " stored at: " + add;
                    }else{
                        return res;
                    }
                case 3: //store gpr3 to memory
                    res = this.storeInMemory(add, this.GPR3);
                    if(res.equals("")){
                        return "GPR3: " + this.GPR3 + " stored at: " + add;
                    }else{
                        return res;
                    }
                default:
                    return "Invalid command";
            }
        }

        //Load gpr register with address
        private String LDA(int gpr, int ixr, int indirect, int address){
            int add = this.computeEffectiveAddress(ixr, indirect, address, false);
            switch (gpr) {
                case 0: //load into gpr0
                    this.setGPR0(add);
                    return "GPR0 set with: " + add;
                case 1: //load into gpr1
                    this.setGPR1(add);
                    return "GPR1 set with: " + add;
                case 2: //load into gpr2
                    this.setGPR2(add);
                    return "GPR2 set with: " + add;
                case 3: //load into gpr3
                    this.setGPR3(add);
                    return "GPR3 set with: " + add;
                default:
                    return "Invalid command";
            }
        }

        //Load Index register from memory
        private String LDX(int gpr, int ixr, int indirect, int address){
            int add = this.computeEffectiveAddress(ixr, indirect, address, true);
            int val = 0;
            switch (ixr) {
                case 1: //load into ixr1
                    val = this.getFromMemory(add);
                    this.setIXR1(val);
                    return "IXR1 set with: " + this.IXR1;
                case 2: //load into ixr2
                    val = this.getFromMemory(add);
                    this.setIXR2(val);
                    return "IXR2 set with: " + this.IXR2;
                case 3: //load into ixr3
                    val = this.getFromMemory(add);
                    this.setIXR3(val);
                    return "IXR3 set with: " + this.IXR3;
                default:
                    return "Invalid command";
            }
        }

        //Store Index register to memory
        private String STX(int gpr, int ixr, int indirect, int address){
            int add = this.computeEffectiveAddress(ixr, indirect, address, true);
            String res;
            switch (ixr) {
                case 1: //store ixr1 to memory
                    res = this.storeInMemory(add,this.IXR1);
                    if(res.equals("")){
                        return "IXR1: " + this.IXR1 + " stored at: " + add;
                    }else{
                        return res;
                    }
                case 2: //store ixr2 to memory
                    res = this.storeInMemory(add,this.IXR2);
                    if(res.equals("")){
                        return "IXR2: " + this.IXR2 + " stored at: " + add;
                    }else{
                        return res;
                    }
                case 3: //store ixr3 to memory
                    res = this.storeInMemory(add,this.IXR3);
                    if(res.equals("")){
                        return "IXR3: " + this.IXR3 + " stored at: " + add;
                    }else{
                        return res;
                    }
                default:
                    //System.out.println("Invalid command");
                    return "Invalid command";
            }
        }

        //Add memory to register
        private String AMR(int gpr, int ixr, int indirect, int address){
            int add = this.computeEffectiveAddress(ixr, indirect, address, false);
            int valFromMemory = this.getFromMemory(add);
            assert valFromMemory < 32: "Immediates cannot be greater than 32";
            switch (gpr) {
                case 0: //add contents of memory to contents of Gpr0
                    this.GPR0 = this.GPR0+valFromMemory;
                    return valFromMemory + " added to GPR0 which is now: " + this.GPR0 + "\n" + this.detectOverUnder(this.GPR0);
                case 1: //add contents of memory to contents of Gpr1
                    this.GPR1 = this.GPR1+valFromMemory;
                    return valFromMemory + " added to GPR1 which is now: " + this.GPR1 + "\n" + this.detectOverUnder(this.GPR1);
                case 2: //add contents of memory to contents of Gpr2
                    this.GPR2 = this.GPR2+valFromMemory;
                    return valFromMemory + " added to GPR2 which is now: " + this.GPR2 + "\n" + this.detectOverUnder(this.GPR2);
                case 3: //add contents of memory to contents of Gpr3
                    this.GPR3 = this.GPR3+valFromMemory;
                    return valFromMemory + " added to GPR3 which is now: " + this.GPR3 + "\n" + this.detectOverUnder(this.GPR3);
                default:
                    //System.out.println("Invalid command");
                    return "Invalid command";
            }
        }

        //Subtract memory from register
        private String SMR(int gpr, int ixr, int indirect, int address){
            int add = this.computeEffectiveAddress(ixr, indirect, address, false);
            int valFromMemory = this.getFromMemory(add);
            assert valFromMemory < 32: "Immediates cannot be greater than 32";
            switch (gpr) {
                case 0: //subtracts contents of memory from contents of Gpr0
                    this.GPR0 = this.GPR0-valFromMemory;
                    return valFromMemory + " subtracted from GPR0 which is now: " + this.GPR0 + "\n" + this.detectOverUnder(this.GPR0);
                case 1: //subtracts contents of memory from contents of Gpr1
                    this.GPR1 = this.GPR1-valFromMemory;
                    return valFromMemory + " subtracted from GPR1 which is now: " + this.GPR1 + "\n" + this.detectOverUnder(this.GPR1);
                case 2: //subtracts contents of memory from contents of Gpr2
                    this.GPR2 = this.GPR2-valFromMemory;
                    return valFromMemory + " subtracted from GPR2 which is now: " + this.GPR2 + "\n" + this.detectOverUnder(this.GPR2);
                case 3: //subtracts contents of memory from contents of Gpr3
                    this.GPR3 = this.GPR3-valFromMemory;
                    return valFromMemory + " subtracted from GPR3 which is now: " + this.GPR3 + "\n" + this.detectOverUnder(this.GPR3);
                default:
                    //System.out.println("Invalid command");
                    return "Invalid command";
            }
        }

        //Add immediate to register
        private String AIR(int gpr, int immediate){            
            switch (gpr) {
                case 0: //add immediate to contents of Gpr0
                    if (this.GPR0==-32769){//uninitialized gpr
                        this.GPR0 = immediate;
                    }else{
                        this.GPR0 = this.GPR0 + immediate;
                    }
                    return immediate + " added to GPR0 which is now: " + this.GPR0 + "\n" + this.detectOverUnder(this.GPR0);
                case 1: //add immediate to contents of Gpr0
                    if (this.GPR1==-32769){
                        this.GPR1 = immediate;
                    }else{
                        this.GPR1 = this.GPR1 + immediate;
                    }
                    return immediate + " added to GPR1 which is now: " + this.GPR1 + "\n" + this.detectOverUnder(this.GPR1);
                case 2: //add immediate to contents of Gpr0
                    if (this.GPR2==-32769){
                        this.GPR2 = immediate;
                    }else{
                        this.GPR2 = this.GPR2 + immediate;
                    }
                    return immediate + " added to GPR2 which is now: " + this.GPR2 + "\n" + this.detectOverUnder(this.GPR2);
                case 3: //add immediate to contents of Gpr0
                    if (this.GPR3==-32769){
                        this.GPR3 = immediate;
                    }else{
                        this.GPR3 = this.GPR3 + immediate;
                    }
                    return immediate + " added to GPR3 which is now: " + this.GPR3 + "\n" + this.detectOverUnder(this.GPR3);
                default:
                    //System.out.println("Invalid command");
                    return "Invalid command";
            }
        }

        private String SIR(int gpr, int immediate){
            switch (gpr) {
                case 0: //subtract immediate from contents of Gpr0
                    if (this.GPR0==-32769){
                        this.GPR0 = immediate*-1;
                    }else{
                        this.GPR0 = this.GPR0 - immediate;
                    }
                    return immediate + " subtracted from GPR0 which is now: " + this.GPR0 + "\n" + this.detectOverUnder(this.GPR0);
                case 1: //subtract immediate from contents of Gpr1
                    if (this.GPR1==-32769){
                        this.GPR1 = immediate*-1;
                    }else{
                        this.GPR1 = this.GPR1 - immediate;
                    }
                    return immediate + " subtracted from GPR1 which is now: " + this.GPR1 + "\n" + this.detectOverUnder(this.GPR1);
                case 2: //subtract immediate from contents of Gpr2
                    if (this.GPR2==-32769){
                        this.GPR2 = immediate*-1;
                    }else{
                        this.GPR2 = this.GPR2 - immediate;
                    }
                    return immediate + " subtracted from GPR2 which is now: " + this.GPR2 + "\n" + this.detectOverUnder(this.GPR2);
                case 3: //subtract immediate from contents of Gpr3
                    if (this.GPR3==-32769){
                        this.GPR3 = immediate*-1;
                    }else{
                        this.GPR3 = this.GPR3 - immediate;
                    }
                    return immediate + " subtracted from GPR3 which is now: " + this.GPR3 + "\n" + this.detectOverUnder(this.GPR3);
                default:
                    //System.out.println("Invalid command");
                    return "Invalid command";
            }
        }

        //MULTIPLY AND DIVIDE INSTRUCTIONS//
        //Multiply values in two gprs
        private String MLT(int gprx, int gpry, int indirect, int address){
            assert (gprx == 0 || gprx == 2) && (gpry == 0 || gpry == 2): "GPRX and GPRY must be 0 or 2"; 
            long num = 0;
            long limit = 4294967295L;
            switch (gprx) {
                case 0: 
                    if(gpry == 0){
                        num = this.GPR0*this.GPR0;
                    }else{
                        num = this.GPR0*this.GPR2;
                    }
                    if(num> limit){
                        conditionCode[0] = 1;
                        return "MLT: OVERFLOW, GPR0 not set"; 
                    }else{
                        //split up top 16 bits in GPR0 and lower order bits in GPR1
                        this.GPR0 = (int) ((num >> 16) & 0xFFFF);  // Right shift by 16 to bring high bits to the low end
                        this.GPR1 = (int) (num & 0xFFFF);  // Use a mask to extract the low 16 bits
                        return "MLT: GPR0: " + this.GPR0 + " GPR1: " + this.GPR1;
                    }
                case 2: 
                    if(gpry == 0){
                        num = this.GPR2*this.GPR0;
                    }else{
                        num = this.GPR2*this.GPR2;
                    }
                    if(num> limit){
                        conditionCode[0] = 1;
                        return "MLT: OVERFLOW, GPR2 not set"; 
                    }else{
                        //split up top 16 bits in GPR2 and lower order bits in GPR3
                        this.GPR2 = (int) ((num >> 16) & 0xFFFF);  // Right shift by 16 to bring high bits to the low end
                        this.GPR3 = (int) (num & 0xFFFF);  // Use a mask to extract the low 16 bits
                        return "MLT: GPR2: " + this.GPR2 + " GPR3: " + this.GPR3;
                    }
                default:
                    //System.out.println("Invalid command");
                    return "Invalid command";
            }
        }

        //Divide values in two gprs
        private String DVD(int gprx, int gpry, int indirect, int address){
            assert (gprx == 0 || gprx == 2) && (gpry == 0 || gpry == 2): "GPRX and GPRY must be 0 or 2"; 
            int quotient = -1;
            int remainder = -1;
            switch (gprx) {
                case 0:
                    if(gpry == 0){
                        if(this.GPR0 == 0 || this.GPR0 == -1){
                            conditionCode[2] = 1;
                            return "DVD: DIVZERO, GPR0 not set"; 
                        }else{
                            quotient = this.GPR0 / this.GPR0;
                            remainder = this.GPR0 % this.GPR0;
                        }
                    }else{
                        if(this.GPR2 == 0 || this.GPR2 == -1){
                            conditionCode[2] = 1;
                            return "DVD: DIVZERO, GPR2 not set";
                        }else{
                            quotient = this.GPR0 / this.GPR2;
                            remainder = this.GPR0 % this.GPR2;
                        } 
                    } 
                    this.GPR0 = quotient;
                    this.GPR1 = remainder;
                    return "DVD: quotient in GPR0: " + quotient + " , remainder in GPR1: " + remainder;
                case 2:
                    if(gpry == 0){
                        if(this.GPR0 == 0 || this.GPR0 == -1){
                            conditionCode[2] = 1;
                            return "DVD: DIVZERO, GPR0 not set"; 
                        }else{
                            quotient = this.GPR2 / this.GPR0;
                            remainder = this.GPR2 % this.GPR0;
                        }
                    }else{
                        if(this.GPR2 == 0 || this.GPR2 == -1){
                            conditionCode[2] = 1;
                            return "DVD: DIVZERO, GPR2 not set";
                        }else{
                            quotient = this.GPR2 / this.GPR2;
                            remainder = this.GPR2 % this.GPR2;
                        } 
                    } 
                    this.GPR2 = quotient;
                    this.GPR3 = remainder;
                    return "DVD: quotient in GPR2: " + quotient + " , remainder in GPR3: " + remainder;
                default:
                    //System.out.println("Invalid command");
                    return "Invalid command";
            }
        }

        //Test register to register equality
        private String TRR(int gprx, int gpry, int indirect, int address){
            if(gprx == gpry){
                this.conditionCode[3] = 1; //gpr == gpr
                return "TRR: GPR " + gprx + "== GPR" + gpry;
            }
            if((gprx == 0 && gpry == 1) || (gprx == 1 && gpry == 0)){
                if(this.GPR0 == this.GPR1){
                    this.conditionCode[3] = 1;
                    return "TRR: GPR0 equals GPR1";
                }else{
                    this.conditionCode[3] = 0;
                    return "TRR: GPR0 not equal GPR1";
                }
            }else if((gprx == 0 && gpry == 2) || (gprx == 2 && gpry == 0)){
                if(this.GPR0 == this.GPR2){
                    this.conditionCode[3] = 1;
                    return "TRR: GPR0 equals GPR2";
                }else{
                    this.conditionCode[3] = 0;
                    return "TRR: GPR0 not equal GPR2";
                }
            }else if((gprx == 0 && gpry == 3) || (gprx == 3 && gpry == 0)){
                if(this.GPR0 == this.GPR3){
                    this.conditionCode[3] = 1;
                    return "TRR: GPR0 equals GPR3";
                }else{
                    this.conditionCode[3] = 0;
                    return "TRR: GPR0 not equal GPR3";
                }
            }else if((gprx == 1 && gpry == 2) || (gprx == 2 && gpry == 1)){
                if(this.GPR1 == this.GPR2){
                    this.conditionCode[3] = 1;
                    return "TRR: GPR1 equals GPR2";
                }else{
                    this.conditionCode[3] = 0;
                    return "TRR: GPR1 not equal GPR2";
                }
            }else if((gprx == 1 && gpry == 3) || (gprx == 3 && gpry == 1)){
                if(this.GPR1 == this.GPR3){
                    this.conditionCode[3] = 1;
                    return "TRR: GPR1 equals GPR3";
                }else{
                    this.conditionCode[3] = 0;
                    return "TRR: GPR1 not equal GPR3";
                }
            }else if((gprx == 2 && gpry == 3) || (gprx == 3 && gpry == 2)){
                if(this.GPR2 == this.GPR3){
                    this.conditionCode[3] = 1;
                    return "TRR: GPR2 equals GPR3";
                }else{
                    this.conditionCode[3] = 0;
                    return "TRR: GPR2 not equal GPR3";
                }
            }else{
                return "Invalid command";
            }
        }

        //Bitwise AND two gprs
        private String AND(int gprx, int gpry, int indirect, int address){
            switch (gprx) {
                case 0:
                    if(gpry == 1){
                        this.GPR0 = this.GPR0 & this.GPR1;
                    }else if(gpry == 2){
                        this.GPR0 = this.GPR0 & this.GPR2;
                    }else if(gpry == 3){
                        this.GPR0 = this.GPR0 & this.GPR3;
                    }
                    return "AND: GPR0 set with: " + this.GPR0;
                case 1: 
                    if(gpry == 0){
                        this.GPR1 = this.GPR1 & this.GPR0;
                    }else if(gpry == 2){
                        this.GPR1 = this.GPR1 & this.GPR2;
                    }else if(gpry == 3){
                        this.GPR1 = this.GPR1 & this.GPR3;
                    }
                    return "AND: GPR1 set with: " + this.GPR1;
                case 2: 
                    if(gpry == 0){
                        this.GPR2 = this.GPR2 & this.GPR0;
                    }else if(gpry == 1){
                        this.GPR2 = this.GPR2 & this.GPR1;
                    }else if(gpry == 3){
                        this.GPR2 = this.GPR2 & this.GPR3;
                    }
                    return "AND: GPR2 set with: " + this.GPR2; 
                case 3: 
                    if(gpry == 0){
                        this.GPR3 = this.GPR3 & this.GPR0;
                    }else if(gpry == 1){
                        this.GPR3 = this.GPR3 & this.GPR1;
                    }else if(gpry == 2){
                        this.GPR3 = this.GPR3 & this.GPR2;
                    }
                    return "AND: GPR3 set with: " + this.GPR3; 
                default:
                    //System.out.println("Invalid command");
                    return "Invalid command";
            }
        }

        //Bitwise OR two gprs
        private String ORR(int gprx, int gpry, int indirect, int address){
            switch (gprx) {
                case 0:
                    if(gpry == 1){
                        this.GPR0 = this.GPR0 | this.GPR1;
                    }else if(gpry == 2){
                        this.GPR0 = this.GPR0 | this.GPR2;
                    }else if(gpry == 3){
                        this.GPR0 = this.GPR0 | this.GPR3;
                    }
                    return "ORR: GPR0 set with: " + this.GPR0;
                case 1: 
                    if(gpry == 0){
                        this.GPR1 = this.GPR1 | this.GPR0;
                    }else if(gpry == 2){
                        this.GPR1 = this.GPR1 | this.GPR2;
                    }else if(gpry == 3){
                        this.GPR1 = this.GPR1 | this.GPR3;
                    }
                    return "ORR: GPR1 set with: " + this.GPR1;
                case 2: 
                    if(gpry == 0){
                        this.GPR2 = this.GPR2 | this.GPR0;
                    }else if(gpry == 1){
                        this.GPR2 = this.GPR2 | this.GPR1;
                    }else if(gpry == 3){
                        this.GPR2 = this.GPR2 | this.GPR3;
                    }
                    return "ORR: GPR2 set with: " + this.GPR2; 
                case 3: 
                    if(gpry == 0){
                        this.GPR3 = this.GPR3 | this.GPR0;
                    }else if(gpry == 1){
                        this.GPR3 = this.GPR3 | this.GPR1;
                    }else if(gpry == 2){
                        this.GPR3 = this.GPR3 | this.GPR2;
                    }
                    return "ORR: GPR3 set with: " + this.GPR3; 
                default:
                    //System.out.println("Invalid command");
                    return "Invalid command";
            }
        }

        //Bitwise NOT one gpr
        private String NOT(int gprx, int gpry, int indirect, int address){
            switch (gprx) {
                case 0:
                    if(this.GPR0!= -32769){
                        this.GPR0 = ~this.GPR0;
                    }
                    return "NOT: GPR0: " + this.GPR0;
                case 1: 
                    if(this.GPR1!= -32769){
                        this.GPR1 = ~this.GPR1;
                    }
                    return "NOT: GPR1: " + this.GPR1;
                case 2: 
                    if(this.GPR2!= -32769){
                        this.GPR2 = ~this.GPR2;
                    }
                    return "NOT: GPR2: " + this.GPR2;
                case 3: 
                    if(this.GPR3!= -32769){
                        this.GPR3 = ~this.GPR3;
                    }
                    return "NOT: GPR3: " + this.GPR3;
                default:
                    //System.out.println("Invalid command");
                    return "Invalid command";
            }
        }

        //Transfer Instructions//
        //Jump if register equals zero
         private String JZ(int gpr, int ixr, int indirect, int address){
            int add = this.computeEffectiveAddress(ixr, indirect, address, false);
            switch (gpr) {
                case 0: 
                    if(this.GPR0 == 0){
                        this.PC = add;
                        return "JZ: PC set with: " + this.PC;
                    }else{
                        return "JZ: GPR0" + this.GPR0 + " Not equal to zero";
                    }
                case 1: 
                    if(this.GPR1 == 0){
                        this.PC = add;
                        return "JZ: PC set with: " + this.PC;
                    }else{
                        return "JZ: GPR1" + this.GPR1 + " Not equal to zero";
                    }
                case 2: 
                    if(this.GPR2 == 0){
                        this.PC = add;
                        return "JZ: PC set with: " + this.PC;
                    }else{
                        return "JZ: GPR2" + this.GPR2 + " Not equal to zero";
                    }
                case 3: 
                    if(this.GPR3 == 0){
                        this.PC = add;
                        return "JZ: PC set with: " + this.PC;
                    }else{
                        return "JZ: GPR3" + this.GPR3 + " Not equal to zero";
                    }
                default:
                    //System.out.println("Invalid command");
                    return "Invalid command";
            }
        }

        //Jump if register not equal to zero or initialized
        private String JNE(int gpr, int ixr, int indirect, int address){
            int add = this.computeEffectiveAddress(ixr, indirect, address, false);
            switch (gpr) {
                case 0: 
                    if(this.GPR0 != 0){
                        this.PC = add;
                        return "JNE: PC set with: " + this.PC;
                    }else{
                        return "JNE: GPR0" + this.GPR0 + " equals zero";
                    }
                case 1: 
                    if(this.GPR1 != 0){
                        this.PC = add;
                        return "JNE: PC set with: " + this.PC;
                    }else{
                        return "JNE: GPR1" + this.GPR1 + " equals zero";
                    }
                case 2: 
                    if(this.GPR2 != 0){
                        this.PC = add;
                        return "JNE: PC set with: " + this.PC;
                    }else{
                        return "JNE: GPR2" + this.GPR2 + " equals zero";
                    }
                case 3: 
                    if(this.GPR3 != 0){
                        this.PC = add;
                        return "JNE: PC set with: " + this.PC;
                    }else{
                        return "JNE: GPR3" + this.GPR3 + " equals zero";
                    }
                default:
                    //System.out.println("Invalid command");
                    return "Invalid command";
            }
        }

        //Jump if CC bit equals bit specified
        private String JCC(int cc, int ixr, int indirect, int address){
            int add = this.computeEffectiveAddress(ixr, indirect, address, false);
            if(this.conditionCode[cc] == 1){ //cc should be 0,1,2,3
                this.PC = add;
                return "JCC: PC set with: " + this.PC;
            }else{
                return "JCC: PC not set in JCC";
            }
        }

        //Unconditional Jump to address
        private String JMA(int gpr, int ixr, int indirect, int address){
            int add = this.computeEffectiveAddress(ixr, indirect, address, false);
            this.PC = add;
            return "JMA: PC set with: " + this.PC;
        }

        //Jump and save next address in R3, R0 should contain pointer to args\
        //TODO: R0 should contain pointer to arguments Argument list should end with –1 (all 1s) value??
        private String JSR(int gpr, int ixr, int indirect, int address){
            int add = this.computeEffectiveAddress(ixr, indirect, address, false);
            this.GPR3 = this.PC + 1;
            this.PC = add;
            return "JSR: PC set with: " + this.PC + " and GPR3 set with" + this.GPR3;
        }

        //Return from Subroutine
        private String RFS(int gpr, int ixr, int indirect, int immed){
            this.GPR0 = immed;
            return "RFS: GPR0 set with" + this.GPR0;
        }

        //Subtract one from register then branch
        private String SOB(int gpr, int ixr, int indirect, int address){
            int add = this.computeEffectiveAddress(ixr, indirect, address, false);
            if(gpr == 0){
                this.GPR0 = this.GPR0 - 1;
                if(this.GPR0 > 0){
                    this.PC = add;
                    return "SOB: PC set with: " + this.PC;
                }
            }else if(gpr == 1){
                this.GPR1 = this.GPR1 - 1;
                if(this.GPR1 > 0){
                    this.PC = add;
                    return "SOB: PC set with: " + this.PC;
                }
            }else if(gpr == 2){
                this.GPR2 = this.GPR2 - 1;
                if(this.GPR2 > 0){
                    this.PC = add;
                    return "SOB: PC set with: " + this.PC;
                }
            }else if(gpr == 3){
                this.GPR3 = this.GPR3 - 1;
                if(this.GPR3 > 0){
                    this.PC = add;
                    return "SOB: PC set with: " + this.PC;
                }
            }else{
                return "Invalid command";
            }
            return "SOB: no branch";
        }

        //Jump if register greater than or equal to zero
        private String JGE(int gpr, int ixr, int indirect, int address){
            int add = this.computeEffectiveAddress(ixr, indirect, address, false);
            if(gpr == 0){
                if(this.GPR0 >= 0){
                    this.PC = add;
                    return "JGE: PC set with: " + this.PC;
                }
            }else if(gpr == 1){
                if(this.GPR1 >= 0){
                    this.PC = add;
                    return "JGE: PC set with: " + this.PC;
                }
            }else if(gpr == 2){
                if(this.GPR2 >= 0){
                    this.PC = add;
                    return "JGE: PC set with: " + this.PC;
                }
            }else if(gpr == 3){
                if(this.GPR3 >= 0){
                    this.PC = add;
                    return "JGE: PC set with: " + this.PC;
                }
            }else{
                return "Invalid command";
            }
            return "JGE: no jump";
        }

        //I/O Operations
        private String OUT(int gpr, int ixr, int indirect, int devid){
            if(devid!=1){
                System.out.println("ERROR: Can only print to Console Printer");
                return "ERROR: Can only print to Console Printer";
            }
            if(gpr==0){ //print GPR0 to console printer
                this.consolePrinter = Integer.toString(this.GPR0);
                return "OUT: " + this.GPR0 + " printed to the console";
            }else if(gpr==1){
                this.consolePrinter = Integer.toString(this.GPR1);
                return "OUT: " + this.GPR1 + " printed to the console";
            }else if(gpr==2){
                this.consolePrinter = Integer.toString(this.GPR2);
                return "OUT: " + this.GPR2 + " printed to the console";
            }else if(gpr==3){
                this.consolePrinter = Integer.toString(this.GPR3);
                return "OUT: " + this.GPR3 + " printed to the console";
            }
            return "ERROR in OUT: GPR must be 0-3";
        }

        //Check device status from Register
        private String CHK(int gpr, int ixr, int indirect, int devid){
            if(devid!=1){
                System.out.println("ERROR: Can only use CHK to Console Printer");
                return "ERROR: Can only CHK to Console Printer";
            }
            if(gpr==0){ //print GPR0 to console printer
                this.GPR0 = 1; //device not busy
                return "CHK: " + this.GPR0 + " set to 1, device not busy";
            }else if(gpr==1){
                this.GPR1 = 1; //device not busy
                return "CHK: " + this.GPR1 + " set to 1, device not busy";
            }else if(gpr==2){
                this.GPR2 = 1;
                return "CHK: " + this.GPR2 + " set to 1, device not busy";
            }else if(gpr==3){
                this.GPR3 = 1;
                return "CHK: " + this.GPR3 + " set to 1, device not busy";
            }
            return "ERROR in CHK: GPR must be 0-3";
        }

        //Shift and Rotate Operations
        //Shift register by count
        private String SRC(int gpr, int AL, int LR, int count){
            if(count>16){
                System.out.println("ERROR: count can not be greater than 16");
            }
            if(gpr == 0){
                if(LR == 1){//Shift Left
                    if(AL == 1){ //Logical Shift, ignores sign bit
                        //shift left, logical and arithmetic are identical in java
                        this.GPR0 = this.GPR0 << count;
                        return "SRC: GPR0 left shifted to: " + this.GPR0;
                    }
                }else{//Shift Right
                    if(AL == 1){  //Logical Shift, ignores sign bit
                        //shift right, logical
                        this.GPR0 = this.GPR0 >>> count;
                        return "SRC: GPR0 right logical shifted to: " + this.GPR0;
                    }else{
                        //shift right, arithmetic
                        this.GPR0 = this.GPR0 >> count;
                        return "SRC: GPR0 right arithmetic shifted to: " + this.GPR0;
                    }
                }
            }else if(gpr == 1){
                if(LR == 1){//Shift Left
                    if(AL == 1){ //Logical Shift, ignores sign bit
                        //shift left, logical and arithmetic are identical in java
                        this.GPR1 = this.GPR1 << count;
                        return "SRC: GPR1 left shifted to: " + this.GPR1;
                    }
                }else{//Shift Right
                    if(AL == 1){  //Logical Shift, ignores sign bit
                        //shift right, logical
                        this.GPR1 = this.GPR1 >>> count;
                        return "SRC: GPR1 right logical shifted to: " + this.GPR1;
                    }else{
                        //shift right, arithmetic
                        this.GPR1 = this.GPR1 >> count;
                        return "SRC: GPR1 right arithmetic shifted to: " + this.GPR1;
                    }
                }
            }else if(gpr == 2){
                if(LR == 1){//Shift Left
                    if(AL == 1){ //Logical Shift, ignores sign bit
                        //shift left, logical and arithmetic are identical in java
                        this.GPR2 = this.GPR2 << count;
                        return "SRC: GPR2 left shifted to: " + this.GPR2;
                    }
                }else{//Shift Right
                    if(AL == 1){  //Logical Shift, ignores sign bit
                        //shift right, logical
                        this.GPR2 = this.GPR2 >>> count;
                        return "SRC: GPR2 right logical shifted to: " + this.GPR2;
                    }else{
                        //shift right, arithmetic
                        this.GPR2 = this.GPR2 >> count;
                        return "SRC: GPR2 right arithmetic shifted to: " + this.GPR2;
                    }
                }

            }else if(gpr == 3){
                if(LR == 1){//Shift Left
                    if(AL == 1){ //Logical Shift, ignores sign bit
                        //shift left, logical and arithmetic are identical in java
                        this.GPR3 = this.GPR3 << count;
                        return "SRC: GPR0 left shifted to: " + this.GPR3;
                    }
                }else{//Shift Right
                    if(AL == 1){  //Logical Shift, ignores sign bit
                        //shift right, logical
                        this.GPR3 = this.GPR3 >>> count;
                        return "SRC: GPR3 right logical shifted to: " + this.GPR3;
                    }else{
                        //shift right, arithmetic
                        this.GPR3 = this.GPR3 >> count;
                        return "SRC: GPR3 right arithmetic shifted to: " + this.GPR3;
                    }
                }
            }else{
                return "ERROR: GPR must be between 0-3";
            }
            return "ERROR IN SRC";
        }

        //Rotate register by count
        private String RRC(int gpr, int AL, int LR, int count){
            if(count>16){
                System.out.println("ERROR: count can not be greater than 16");
            }
            if(gpr == 0){
                if(LR == 1){//Rotate Left
                    if(AL == 1){
                        this.GPR0 = leftRotate(this.GPR0, count, 16);
                        return "RRC: GPR0 left rotated to: " + this.GPR0;
                    }
                }else{//Rotate Right
                    if(AL == 1){ 
                        this.GPR0 = rightRotate(this.GPR0, count, 16);
                        return "RRC: GPR0 right rotated to: " + this.GPR0;
                    }
                }
            }else if(gpr == 1){
                if(LR == 1){//Rotate Left
                    if(AL == 1){
                        this.GPR1 = leftRotate(this.GPR1, count, 16);
                        return "RRC: GPR1 left rotated to: " + this.GPR1;
                    }
                }else{//Rotate Right
                    if(AL == 1){ 
                        this.GPR1 = rightRotate(this.GPR1, count, 16);
                        return "RRC: GPR1 right rotated to: " + this.GPR1;
                    }
                }
            }else if(gpr == 2){
                if(LR == 1){//Rotate Left
                    if(AL == 1){
                        this.GPR2 = leftRotate(this.GPR2, count, 16);
                        return "RRC: GPR2 left rotated to: " + this.GPR2;
                    }
                }else{//Rotate Right
                    if(AL == 1){ 
                        this.GPR2 = rightRotate(this.GPR2, count, 16);
                        return "RRC: GPR2 right rotated to: " + this.GPR2;
                    }
                }
            }else if(gpr == 3){
                if(LR == 1){//Rotate Left
                    if(AL == 1){
                        this.GPR3 = leftRotate(this.GPR3, count, 16);
                        return "RRC: GPR3 left rotated to: " + this.GPR3;
                    }
                }else{//Rotate Right
                    if(AL == 1){ 
                        this.GPR3 = rightRotate(this.GPR3, count, 16);
                        return "RRC: GPR3 right rotated to: " + this.GPR3;
                    }
                }
            }else{
                return "ERROR: GPR must be between 0-3";
            }
            return "ERROR in RRC";
        }

        private String IN(int gpr, int ixr, int indirect, int devid){
            if(gpr==0){
                this.consolePrinter = "IN: Enter a number in the Console Input text box.\nPress the Enter button to store it in GPR0.";
                return "IN: GPR0 waiting for input";
            }else if(gpr == 1){
                this.consolePrinter = "IN: Enter a number in the Console Input text box.\nPress the Enter button to store it in GPR1.";
                return "IN: GPR1 waiting for input";
            }else if(gpr == 2){
                this.consolePrinter = "IN: Enter a number in the Console Input text box.\nPress the Enter button to store it in GPR2.";
                return "IN: GPR2 waiting for input";
            }else if(gpr == 3){
                this.consolePrinter = "IN: Enter a number in the Console Input text box.\nPress the Enter button to store it in GPR3.";
                return "IN: GPR3 waiting for input";
            }else{
                return "ERROR IN IN: GPR must be between 0-3";
            }
        }

        private int rightRotate(int num, int shift, int bitWidth) {
            return (num >>> shift) | (num << (bitWidth - shift));
        }

        private int leftRotate(int num, int shift, int bitWidth) {
            return (num << shift) | (num >>> (bitWidth - shift));
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
    
        //helper function to detect over or under flow
        private String detectOverUnder(int register){
            if(register > 32767){//overflow
                this.conditionCode[0] = 1;
                return "OVERFLOW";
            }else if(register < -32768){ //underflow
                this.conditionCode[1] = 1;
                return "UNDERFLOW";
            }else{
                return "";
            }
        }

        public int[] getCC(){
            return this.conditionCode;
        }

        public void setCC(int cc, int index){
            this.conditionCode[index] = cc;
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
