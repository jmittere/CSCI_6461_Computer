package com.example;

import java.util.HashMap;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;


public class ComputerGUI extends Application {
    
    private Simulator sim;

    private void setupSim(Stage stage) {
        this.sim = new Simulator();    
    }
    
    @Override
    public void start(Stage primaryStage) {
        this.setupSim(primaryStage);
        primaryStage.setTitle("Computer System GUI");

        //stores references to textboxes for modification later
        HashMap<String, TextField> fieldMap = new HashMap<>();

        BorderPane root = new BorderPane();

        Label titleLabel = new Label("  Computer System GUI");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        String hintLabel = "To set a register, you must delete the existing value in the text box."
        + "\nTo load a value in memory into the MBR, set the MAR (using direct input in decimal in the MAR text box, or indirect input in the Octal or Binary text box) of the address of the value, then click Load."
        + "\nTo store a value in the MBR to memory, set the MAR (as specified above), then add a value to the MBR and click Store to store the value at the address of the MAR.";
        Label bottomLabel = new Label(hintLabel);
        bottomLabel.setStyle("-fx-font-size: 14px;");

        root.setTop(titleLabel);
        root.setBottom(bottomLabel);
        root.setPadding(new Insets(10, 10, 10, 20)); // 50px padding on the left


        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));

        grid.setHgap(10);
        grid.setVgap(10);

        //button styling
        DropShadow shadow = new DropShadow();
        shadow.setRadius(3);
        shadow.setOffsetX(2);
        shadow.setOffsetY(2);
        shadow.setColor(Color.GRAY);


        // General Purpose Registers (GPR 0-3) with Buttons
        TextField gpr0Field = new TextField();
        Button btngpr0 = new Button("Set");
        btngpr0.setEffect(shadow);
        grid.add(new Label("GPR0:"), 0, 0);
        grid.add(gpr0Field, 1, 0);
        grid.add(btngpr0, 2, 0);
        TextField gpr1Field = new TextField();
        Button btngpr1 = new Button("Set");
        btngpr1.setEffect(shadow);
        grid.add(new Label("GPR1:"), 0, 1);
        grid.add(gpr1Field, 1, 1);
        grid.add(btngpr1, 2, 1);
        TextField gpr2Field = new TextField();
        Button btngpr2 = new Button("Set");
        btngpr2.setEffect(shadow);
        grid.add(new Label("GPR2:"), 0, 2);
        grid.add(gpr2Field, 1, 2);
        grid.add(btngpr2, 2, 2);
        TextField gpr3Field = new TextField();
        Button btngpr3 = new Button("Set");
        btngpr3.setEffect(shadow);
        grid.add(new Label("GPR3:"), 0, 3);
        grid.add(gpr3Field, 1, 3);
        grid.add(btngpr3, 2, 3);

        // Index Registers (IXR 1-3) with Buttons
        TextField ixr1Field = new TextField();
        Button btnixr1 = new Button("Set");
        btnixr1.setEffect(shadow);
        grid.add(new Label("IXR1:"), 0, 5);
        grid.add(ixr1Field, 1, 5);
        grid.add(btnixr1, 2, 5);
        TextField ixr2Field = new TextField();
        Button btnixr2 = new Button("Set");
        btnixr2.setEffect(shadow);
        grid.add(new Label("IXR2:"), 0, 6);
        grid.add(ixr2Field, 1, 6);
        grid.add(btnixr2, 2, 6);
        TextField ixr3Field = new TextField();
        Button btnixr3 = new Button("Set");
        btnixr3.setEffect(shadow);
        grid.add(new Label("IXR3:"), 0, 7);
        grid.add(ixr3Field, 1, 7);
        grid.add(btnixr3, 2, 7);

        // Other Fields
        TextField PCField = new TextField();
        Button btnPC = new Button("Set");
        btnPC.setEffect(shadow);
        grid.add(new Label("PC:"), 3, 0);
        grid.add(PCField, 4, 0);
        grid.add(btnPC, 5, 0);
        TextField MARField = new TextField();
        Button btnMAR = new Button("Set");
        btnMAR.setEffect(shadow);
        grid.add(new Label("MAR:"), 3, 1);
        grid.add(MARField, 4, 1);
        grid.add(btnMAR, 5, 1);
        TextField MBRField = new TextField();
        Button btnMBR = new Button("Set");
        btnMBR.setEffect(shadow);
        grid.add(new Label("MBR:"), 3, 2);
        grid.add(MBRField, 4, 2);
        grid.add(btnMBR, 5, 2);

        // Remaining Fields Without Buttons
        String[] remainingLabels = {"MFR", "IR", "CC", "Binary", "Octal", "Program File"};

        for (int i = 0; i < remainingLabels.length; i++) {
            TextField textField = new TextField();
            fieldMap.put(remainingLabels[i], textField);
            grid.add(new Label(remainingLabels[i] + ":"), 3, i + 3);
            grid.add(textField, 4, i + 3);
        }

        // Console Input Field on Right Side
        Label consoleLabel = new Label("Console Input:");
        TextArea consoleInput = new TextArea();
        consoleInput.setPrefHeight(100);
        consoleInput.setPrefWidth(300);
        VBox consoleBox = new VBox(5, consoleLabel, consoleInput);
        // Printer Section
        Label printerLabel = new Label("Console Printer:");
        TextArea printerOutput = new TextArea();
        printerOutput.setPrefHeight(100);
        printerOutput.setPrefWidth(300);
        VBox printerBox = new VBox(5, printerLabel, printerOutput);
        // Cache Section
        Label cacheLabel = new Label("Cache:");
        TextArea cacheOutput = new TextArea();
        cacheOutput.setPrefHeight(100);
        cacheOutput.setPrefWidth(300);
        VBox cacheBox = new VBox(5, cacheLabel, cacheOutput);
        // Instruction Debug Section
        Label debugLabel = new Label("Instruction Debug:");
        TextArea debugOutput = new TextArea();
        debugOutput.setPrefHeight(100);
        debugOutput.setPrefWidth(300);
        VBox debugBox = new VBox(5, debugLabel, debugOutput);

        VBox rightSection = new VBox(15, consoleBox, printerBox, cacheBox, debugBox);
        rightSection.setPadding(new Insets(10, 20, 10, 10)); // Added padding on the right
        root.setRight(rightSection);

        // Buttons and Actions
        Button btnLoad = new Button("Load");
        btnLoad.setEffect(shadow);
        Button btnStore = new Button("Store");
        btnStore.setEffect(shadow);
        Button btnRun = new Button("Run");
        btnRun.setEffect(shadow);
        Button btnStep = new Button("Step");
        btnStep.setEffect(shadow);
        Button btnHalt = new Button("Halt");
        btnHalt.setEffect(shadow);
        Button btnIPL = new Button("IPL");
        btnIPL.setEffect(shadow);
        btnIPL.setStyle("-fx-background-color: red; -fx-text-fill: white;");

        btnLoad.setOnAction(e -> {
            if(!MARField.getText().equals("")){ //MAR text box is filled in
                int addr = Integer.parseInt(MARField.getText()); //value has already been checked to be within valid address range
            
                int memoryValue = this.sim.getFromMemory(addr);
                if(memoryValue == -1){
                    //System.out.println("Invalid Memory Address");
                    debugOutput.setText("Invalid Memory Address");
                }else{
                    MBRField.setText(String.valueOf(memoryValue));
                    this.sim.setMBR(memoryValue);
                    debugOutput.setText("Address: " + MARField.getText()  + " : " + String.valueOf(memoryValue) + " loaded into MBR");
                }
            }else{
                debugOutput.setText("Must have a value in the MAR to Load from memory");
            }
        });

        btnStore.setOnAction(e -> { 
            if(!MARField.getText().equals("") && !MBRField.getText().equals("")){ //MAR and MBR text box is filled in
                int addr = Integer.parseInt(MARField.getText()); //value has already been checked to be within valid address range
                int val = Integer.parseInt(MBRField.getText()); //value has already been checked to be within valid value range
                boolean memoryResult = this.sim.storeInMemory(addr, val);
                if(!memoryResult){
                    //System.out.println("Invalid Memory Address");
                    debugOutput.setText("Invalid Memory Address");
                }else{
                    debugOutput.setText("Stored: " + MBRField.getText() + " at Address: " + MARField.getText());
                }
            }else if(!MARField.getText().equals("") && MBRField.getText().equals("")){ //MAR filled in but MBR not filled in
                debugOutput.setText("Must have a value in the MBR to Store to memory");
            }else if(MARField.getText().equals("") && !MBRField.getText().equals("")){
                debugOutput.setText("Must have a value in the MAR to Store to memory");
            }
        });

        btnRun.setOnAction(e -> {
            HashMap<String, String> registerContents = this.sim.run(); //register contents returns a hashmap of all register contents after run()
            gpr0Field.setText(registerContents.get("GPR0"));
            gpr1Field.setText(registerContents.get("GPR1"));
            gpr2Field.setText(registerContents.get("GPR2"));
            gpr3Field.setText(registerContents.get("GPR3"));
            ixr1Field.setText(registerContents.get("IXR1"));
            ixr2Field.setText(registerContents.get("IXR2"));
            ixr3Field.setText(registerContents.get("IXR3"));
            PCField.setText(registerContents.get("PC"));
            MARField.setText(registerContents.get("MAR"));
            MBRField.setText(registerContents.get("MBR"));
            fieldMap.get("MFR").setText(registerContents.get("MFR"));
            fieldMap.get("IR").setText(registerContents.get("IR"));
            debugOutput.setText(registerContents.get("debugOutput"));
            cacheOutput.setText(registerContents.get("cache"));
            fieldMap.get("CC").setText(registerContents.get("CC"));
            printerOutput.setText(registerContents.get("consolePrinter"));
        });
        btnStep.setOnAction(e -> {
            HashMap<String, String> registerContents = this.sim.step(); //register contents returns a hashmap of all register contents after step()
            gpr0Field.setText(registerContents.get("GPR0"));
            gpr1Field.setText(registerContents.get("GPR1"));
            gpr2Field.setText(registerContents.get("GPR2"));
            gpr3Field.setText(registerContents.get("GPR3"));
            ixr1Field.setText(registerContents.get("IXR1"));
            ixr2Field.setText(registerContents.get("IXR2"));
            ixr3Field.setText(registerContents.get("IXR3"));
            PCField.setText(registerContents.get("PC"));
            MARField.setText(registerContents.get("MAR"));
            MBRField.setText(registerContents.get("MBR"));
            fieldMap.get("MFR").setText(registerContents.get("MFR"));
            fieldMap.get("IR").setText(registerContents.get("IR"));
            debugOutput.setText(registerContents.get("debugOutput"));
            cacheOutput.setText(registerContents.get("cache"));
            fieldMap.get("CC").setText(registerContents.get("CC"));
            printerOutput.setText(registerContents.get("consolePrinter"));
        });
        btnHalt.setOnAction(e -> System.out.println("Halt button clicked"));
        btnIPL.setOnAction(e -> {
            String pf = fieldMap.get("Program File").getText();
            System.out.println("Program File Path: " + pf);
            if(pf.equals("")){ //use hardcoded preloaded file
                pf = "preload.txt";
            }
            this.sim.setProgramFile(pf);
            boolean successInitialization = this.sim.initializeProgram();
            if(!successInitialization){
                debugOutput.setText("Failed to initialize with load file: " + pf);
            }else{
                debugOutput.setText("Successfully initialized with load file: " + pf);
                btnIPL.setStyle("-fx-background-color: green; -fx-text-fill: white;");
            }
        });

        //general purpose register buttons
        btngpr0.setOnAction(e -> {
            String num = "";
            if(!gpr0Field.getText().equals("")){ //gpr text box filled in
                int temp = Integer.parseInt(gpr0Field.getText());
                if(temp < -32768 || temp > 32768){
                    debugOutput.setText("Value is not valid.");
                }else{
                    this.sim.setGPR0(Integer.parseInt(gpr0Field.getText()));
                    debugOutput.setText("GPR0 set with: " + gpr0Field.getText());
                }
            }else if(!fieldMap.get("Octal").getText().equals("")){ 
                int temp = Integer.parseInt(fieldMap.get("Octal").getText(), 8);
                if(temp < -32768 || temp > 32768){
                    debugOutput.setText("Value is not valid.");
                }else{
                    num = Conversion.convertToDecimalString(fieldMap.get("Octal").getText());
                    this.sim.setGPR0(temp); //converts value in Octal text box to a decimal string
                    gpr0Field.setText(num);
                    fieldMap.get("Octal").setText("");
                    debugOutput.setText("GPR0 set with: " + num);
                }
            }else if(!fieldMap.get("Binary").getText().equals("")){
                int temp = (int) Long.parseLong(fieldMap.get("Binary").getText(), 2);
                if(temp < -32768 || temp > 32768){
                    debugOutput.setText("Value is not valid.");
                }else{
                num = Integer.toString(temp);
                this.sim.setGPR0(temp); //converts value in Binary text box to an octal string then decimal string
                gpr0Field.setText(num);
                fieldMap.get("Binary").setText("");
                debugOutput.setText("GPR0 set with: " + num);
                }
            }
        });
        btngpr1.setOnAction(e -> {
            String num = "";
            if(!gpr1Field.getText().equals("")){ //gpr text box filled in
                int temp = Integer.parseInt(gpr1Field.getText());
                if(temp < -32768 || temp > 32768){
                    debugOutput.setText("Value is not valid.");
                }else{
                    this.sim.setGPR1(Integer.parseInt(gpr1Field.getText()));
                    debugOutput.setText("GPR1 set with: " + gpr1Field.getText());
                }
            }else if(!fieldMap.get("Octal").getText().equals("")){ 
                int temp = Integer.parseInt(fieldMap.get("Octal").getText(), 8);
                if(temp < -32768 || temp > 32768){
                    debugOutput.setText("Value is not valid.");
                }else{
                    num = Conversion.convertToDecimalString(fieldMap.get("Octal").getText());
                    this.sim.setGPR1(temp); //converts value in Octal text box to a decimal string
                    gpr1Field.setText(num);
                    fieldMap.get("Octal").setText("");
                    debugOutput.setText("GPR1 set with: " + num);
                }
            }else if(!fieldMap.get("Binary").getText().equals("")){
                int temp = (int) Long.parseLong(fieldMap.get("Binary").getText(), 2);
                if(temp < -32768 || temp > 32768){
                    debugOutput.setText("Value is not valid.");
                }else{
                num = Integer.toString(temp);
                this.sim.setGPR1(temp); //converts value in Binary text box to an octal string then decimal string
                gpr1Field.setText(num);
                fieldMap.get("Binary").setText("");
                debugOutput.setText("GPR1 set with: " + num);
                }
            }
        });
        btngpr2.setOnAction(e -> {
            String num = "";
            if(!gpr2Field.getText().equals("")){ //gpr text box filled in
                int temp = Integer.parseInt(gpr2Field.getText());
                if(temp < -32768 || temp > 32768){
                    debugOutput.setText("Value is not valid.");
                }else{
                    this.sim.setGPR2(Integer.parseInt(gpr2Field.getText()));
                    debugOutput.setText("GPR2 set with: " + gpr2Field.getText());
                }
            }else if(!fieldMap.get("Octal").getText().equals("")){ 
                int temp = Integer.parseInt(fieldMap.get("Octal").getText(), 8);
                if(temp < -32768 || temp > 32768){
                    debugOutput.setText("Value is not valid.");
                }else{
                    num = Conversion.convertToDecimalString(fieldMap.get("Octal").getText());
                    this.sim.setGPR2(temp); //converts value in Octal text box to a decimal string
                    gpr2Field.setText(num);
                    fieldMap.get("Octal").setText("");
                    debugOutput.setText("GPR2 set with: " + num);
                }
            }else if(!fieldMap.get("Binary").getText().equals("")){
                int temp = (int) Long.parseLong(fieldMap.get("Binary").getText(), 2);
                if(temp < -32768 || temp > 32768){
                    debugOutput.setText("Value is not valid.");
                }else{
                num = Integer.toString(temp);
                this.sim.setGPR2(temp); //converts value in Binary text box to an octal string then decimal string
                gpr2Field.setText(num);
                fieldMap.get("Binary").setText("");
                debugOutput.setText("GPR2 set with: " + num);
                }
            }
        });
        btngpr3.setOnAction(e -> {
            String num = "";
            if(!gpr3Field.getText().equals("")){ //gpr text box filled in
                int temp = Integer.parseInt(gpr3Field.getText());
                if(temp < -32768 || temp > 32768){
                    debugOutput.setText("Value is not valid.");
                }else{
                    this.sim.setGPR3(Integer.parseInt(gpr3Field.getText()));
                    debugOutput.setText("GPR3 set with: " + gpr3Field.getText());
                }
            }else if(!fieldMap.get("Octal").getText().equals("")){ 
                int temp = Integer.parseInt(fieldMap.get("Octal").getText(), 8);
                if(temp < -32768 || temp > 32768){
                    debugOutput.setText("Value is not valid.");
                }else{
                    num = Conversion.convertToDecimalString(fieldMap.get("Octal").getText());
                    this.sim.setGPR3(temp); //converts value in Octal text box to a decimal string
                    gpr3Field.setText(num);
                    fieldMap.get("Octal").setText("");
                    debugOutput.setText("GPR3 set with: " + num);
                }
            }else if(!fieldMap.get("Binary").getText().equals("")){
                int temp = (int) Long.parseLong(fieldMap.get("Binary").getText(), 2);
                if(temp < -32768 || temp > 32768){
                    debugOutput.setText("Value is not valid.");
                }else{
                num = Integer.toString(temp);
                this.sim.setGPR3(temp); //converts value in Binary text box to an octal string then decimal string
                gpr3Field.setText(num);
                fieldMap.get("Binary").setText("");
                debugOutput.setText("GPR3 set with: " + num);
                }
            }
        });

        //index register buttons
        btnixr1.setOnAction(e -> {
            String num = "";
            if(!ixr1Field.getText().equals("")){ //gpr text box filled in
                int temp = Integer.parseInt(ixr1Field.getText());
                if(temp < -32768 || temp > 32768){
                    debugOutput.setText("Value is not valid.");
                }else{
                    this.sim.setIXR1(Integer.parseInt(ixr1Field.getText()));
                    debugOutput.setText("IXR1 set with: " + ixr1Field.getText());
                }
            }else if(!fieldMap.get("Octal").getText().equals("")){ 
                int temp = Integer.parseInt(fieldMap.get("Octal").getText(), 8);
                if(temp < -32768 || temp > 32768){
                    debugOutput.setText("Value is not valid.");
                }else{
                    num = Conversion.convertToDecimalString(fieldMap.get("Octal").getText());
                    this.sim.setIXR1(temp); //converts value in Octal text box to a decimal string
                    ixr1Field.setText(num);
                    fieldMap.get("Octal").setText("");
                    debugOutput.setText("IXR1 set with: " + num);
                }
            }else if(!fieldMap.get("Binary").getText().equals("")){
                int temp = (int) Long.parseLong(fieldMap.get("Binary").getText(), 2);
                if(temp < -32768 || temp > 32768){
                    debugOutput.setText("Value is not valid.");
                }else{
                num = Integer.toString(temp);
                this.sim.setIXR1(temp); //converts value in Binary text box to an octal string then decimal string
                ixr1Field.setText(num);
                fieldMap.get("Binary").setText("");
                debugOutput.setText("GPR3 set with: " + num);
                }
            }
        });

        btnixr2.setOnAction(e -> {
            String num = "";
            if(!ixr2Field.getText().equals("")){ //gpr text box filled in
                int temp = Integer.parseInt(ixr2Field.getText());
                if(temp < -32768 || temp > 32768){
                    debugOutput.setText("Value is not valid.");
                }else{
                    this.sim.setIXR2(Integer.parseInt(ixr2Field.getText()));
                    debugOutput.setText("IXR2 set with: " + ixr2Field.getText());
                }
            }else if(!fieldMap.get("Octal").getText().equals("")){ 
                int temp = Integer.parseInt(fieldMap.get("Octal").getText(), 8);
                if(temp < -32768 || temp > 32768){
                    debugOutput.setText("Value is not valid.");
                }else{
                    num = Conversion.convertToDecimalString(fieldMap.get("Octal").getText());
                    this.sim.setIXR2(temp); //converts value in Octal text box to a decimal string
                    ixr2Field.setText(num);
                    fieldMap.get("Octal").setText("");
                    debugOutput.setText("IXR2 set with: " + num);
                }
            }else if(!fieldMap.get("Binary").getText().equals("")){
                int temp = (int) Long.parseLong(fieldMap.get("Binary").getText(), 2);
                if(temp < -32768 || temp > 32768){
                    debugOutput.setText("Value is not valid.");
                }else{
                num = Integer.toString(temp);
                this.sim.setIXR2(temp); //converts value in Binary text box to an octal string then decimal string
                ixr2Field.setText(num);
                fieldMap.get("Binary").setText("");
                debugOutput.setText("IXR2 set with: " + num);
                }
            }
        });

        btnixr3.setOnAction(e -> {
            String num = "";
            if(!ixr3Field.getText().equals("")){ //gpr text box filled in
                int temp = Integer.parseInt(ixr3Field.getText());
                if(temp < -32768 || temp > 32768){
                    debugOutput.setText("Value is not valid.");
                }else{
                    this.sim.setIXR3(Integer.parseInt(ixr3Field.getText()));
                    debugOutput.setText("IXR3 set with: " + ixr3Field.getText());
                }
            }else if(!fieldMap.get("Octal").getText().equals("")){ 
                int temp = Integer.parseInt(fieldMap.get("Octal").getText(), 8);
                if(temp < -32768 || temp > 32768){
                    debugOutput.setText("Value is not valid.");
                }else{
                    num = Conversion.convertToDecimalString(fieldMap.get("Octal").getText());
                    this.sim.setIXR3(temp); //converts value in Octal text box to a decimal string
                    ixr3Field.setText(num);
                    fieldMap.get("Octal").setText("");
                    debugOutput.setText("IXR3 set with: " + num);
                }
            }else if(!fieldMap.get("Binary").getText().equals("")){
                int temp = (int) Long.parseLong(fieldMap.get("Binary").getText(), 2);
                if(temp < -32768 || temp > 32768){
                    debugOutput.setText("Value is not valid.");
                }else{
                num = Integer.toString(temp);
                this.sim.setIXR3(temp); //converts value in Binary text box to an octal string then decimal string
                ixr3Field.setText(num);
                fieldMap.get("Binary").setText("");
                debugOutput.setText("IXR3 set with: " + num);
                }
            }
        });

        //program counter button
        btnPC.setOnAction(e -> {
            String num = "";
            if(!PCField.getText().equals("")){ //PC text box filled in
                int temp = Integer.parseInt(PCField.getText());
                if(temp < 6 || temp > 2047){
                    debugOutput.setText("Value is not valid.");
                }else{
                    this.sim.setPC(Integer.parseInt(PCField.getText()));
                    this.sim.setMAR(Integer.parseInt(PCField.getText()));
                    MARField.setText(PCField.getText()); //set MAR to PC address
                    int addr = Integer.parseInt(MARField.getText()); //value has already been checked to be within valid address range
                    int memoryValue = this.sim.getFromMemory(addr);
                    if(memoryValue == -1){
                        debugOutput.setText("No value set at " + MARField.getText()  + " to load into MBR."
                        + "\nPC set with: " + PCField.getText());
                    }else{
                        MBRField.setText(String.valueOf(memoryValue));
                        fieldMap.get("IR").setText(String.valueOf(memoryValue));
                        this.sim.setIR(memoryValue);
                        this.sim.setMBR(memoryValue);
                        debugOutput.setText("Address: " + MARField.getText()  + " : " + String.valueOf(memoryValue) + " loaded into MBR." 
                        +"\nPC set with: " + PCField.getText());
                    }
                }
            }else if(!fieldMap.get("Octal").getText().equals("")){ 
                int temp = Integer.parseInt(fieldMap.get("Octal").getText(), 8);
                if(temp < 6 || temp > 2047){
                    debugOutput.setText("Value is not valid.");
                }else{
                    num = Conversion.convertToDecimalString(fieldMap.get("Octal").getText());
                    this.sim.setPC(temp); //converts value in Octal text box to a decimal string
                    this.sim.setMAR(temp);
                    PCField.setText(num);
                    MARField.setText(num);
                    int addr = Integer.parseInt(MARField.getText()); //value has already been checked to be within valid address range
                    int memoryValue = this.sim.getFromMemory(addr);
                    if(memoryValue == -1){
                        debugOutput.setText("No value set at " + MARField.getText()  + " to load into MBR."
                        + "\nPC set with: " + num);
                    }else{
                        MBRField.setText(String.valueOf(memoryValue));
                        fieldMap.get("IR").setText(String.valueOf(memoryValue));
                        this.sim.setIR(memoryValue);
                        this.sim.setMBR(memoryValue);
                        debugOutput.setText("Address: " + MARField.getText()  + " : " + String.valueOf(memoryValue) + " loaded into MBR." 
                        +"\nPC set with: " + num);
                    }
                    fieldMap.get("Octal").setText("");
                }
            }else if(!fieldMap.get("Binary").getText().equals("")){
                int temp = (int) Long.parseLong(fieldMap.get("Binary").getText(), 2);
                if(temp < 6 || temp > 2047){
                    debugOutput.setText("Value is not valid.");
                }else{
                    num = Integer.toString(temp);
                    this.sim.setPC(temp); //converts value in Binary text box to an octal string then decimal string
                    this.sim.setMAR(temp);
                    PCField.setText(num);
                    MARField.setText(num); //set MAR to PC address
                    int addr = Integer.parseInt(MARField.getText()); //value has already been checked to be within valid address range
                    int memoryValue = this.sim.getFromMemory(addr);
                    if(memoryValue == -1){
                        debugOutput.setText("No value set at " + MARField.getText()  + " to load into MBR."
                        + "\nPC set with: " + num);
                    }else{
                        MBRField.setText(String.valueOf(memoryValue));
                        fieldMap.get("IR").setText(String.valueOf(memoryValue));
                        this.sim.setIR(memoryValue);
                        this.sim.setMBR(memoryValue);
                        debugOutput.setText("Address: " + MARField.getText()  + " : " + String.valueOf(memoryValue) + " loaded into MBR." 
                        +"\nPC set with: " + num);
                    }

                fieldMap.get("Binary").setText("");
                }
            }
        });

        //memory register buttons
        btnMAR.setOnAction(e -> {
            String num = "";
            if(!MARField.getText().equals("")){ //gpr text box filled in
                int temp = Integer.parseInt(MARField.getText());
                if(temp < 6 || temp > 2047){
                    debugOutput.setText("Value is not valid.");
                }else{
                    this.sim.setMAR(Integer.parseInt(MARField.getText()));
                    debugOutput.setText("MAR set with: " + MARField.getText());
                }
            }else if(!fieldMap.get("Octal").getText().equals("")){ 
                int temp = Integer.parseInt(fieldMap.get("Octal").getText(), 8);
                if(temp < 6 || temp > 2047){
                    debugOutput.setText("Value is not valid.");
                }else{
                    num = Conversion.convertToDecimalString(fieldMap.get("Octal").getText());
                    this.sim.setMAR(temp); //converts value in Octal text box to a decimal string
                    MARField.setText(num);
                    fieldMap.get("Octal").setText("");
                    debugOutput.setText("MAR set with: " + num);
                }
            }else if(!fieldMap.get("Binary").getText().equals("")){
                int temp = (int) Long.parseLong(fieldMap.get("Binary").getText(), 2);
                if(temp < 6 || temp > 2047){
                    debugOutput.setText("Value is not valid.");
                }else{
                num = Integer.toString(temp);
                this.sim.setMAR(temp); //converts value in Binary text box to an octal string then decimal string
                MARField.setText(num);
                fieldMap.get("Binary").setText("");
                debugOutput.setText("MAR set with: " + num);
                }
            }
        });

        btnMBR.setOnAction(e -> {
            String num = "";
            if(!MBRField.getText().equals("")){ //gpr text box filled in
                int temp = Integer.parseInt(MBRField.getText());
                if(temp < -32768 || temp > 32768){
                    debugOutput.setText("Value is not valid.");
                }else{
                    this.sim.setMBR(Integer.parseInt(MBRField.getText()));
                    debugOutput.setText("MBR set with: " + MBRField.getText());
                }
            }else if(!fieldMap.get("Octal").getText().equals("")){ 
                int temp = Integer.parseInt(fieldMap.get("Octal").getText(), 8);
                if(temp < -32768 || temp > 32768){
                    debugOutput.setText("Value is not valid.");
                }else{
                    num = Conversion.convertToDecimalString(fieldMap.get("Octal").getText());
                    this.sim.setMBR(temp); //converts value in Octal text box to a decimal string
                    MBRField.setText(num);
                    fieldMap.get("Octal").setText("");
                    debugOutput.setText("MBR set with: " + num);
                }
            }else if(!fieldMap.get("Binary").getText().equals("")){
                int temp = (int) Long.parseLong(fieldMap.get("Binary").getText(), 2);
                if(temp < -32768 || temp > 32768){
                    debugOutput.setText("Value is not valid.");
                }else{
                num = Integer.toString(temp);
                this.sim.setMBR(temp); //converts value in Binary text box to an octal string then decimal string
                MBRField.setText(num);
                fieldMap.get("Binary").setText("");
                debugOutput.setText("MBR set with: " + num);
                }
            }
        });


        HBox buttonBox = new HBox(10, btnLoad, btnStore, btnRun, btnStep, btnHalt, btnIPL);
        VBox centerBox = new VBox(10, grid, buttonBox);
        root.setCenter(centerBox);
        
        Scene scene = new Scene(root, 1300, 650);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    
    public static void main(String[] args) {
        launch(args);
    }
}
