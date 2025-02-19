package com.example;

import java.util.HashMap;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

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

        Label titleLabel = new Label("Computer System GUI");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        root.setTop(titleLabel);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // General Purpose Registers (GPR 0-3) with Buttons
        TextField gpr0Field = new TextField();
        Button btngpr0 = new Button("Set");
        grid.add(new Label("GPR0:"), 0, 0);
        grid.add(gpr0Field, 1, 0);
        grid.add(btngpr0, 2, 0);
        TextField gpr1Field = new TextField();
        Button btngpr1 = new Button("Set");
        grid.add(new Label("GPR1:"), 0, 1);
        grid.add(gpr1Field, 1, 1);
        grid.add(btngpr1, 2, 1);
        TextField gpr2Field = new TextField();
        Button btngpr2 = new Button("Set");
        grid.add(new Label("GPR1:"), 0, 2);
        grid.add(gpr2Field, 1, 2);
        grid.add(btngpr2, 2, 2);
        TextField gpr3Field = new TextField();
        Button btngpr3 = new Button("Set");
        grid.add(new Label("GPR1:"), 0, 3);
        grid.add(gpr3Field, 1, 3);
        grid.add(btngpr3, 2, 3);

        // Index Registers (IXR 1-3) with Buttons
        TextField ixr1Field = new TextField();
        Button btnixr1 = new Button("Set");
        grid.add(new Label("IXR1:"), 0, 5);
        grid.add(ixr1Field, 1, 5);
        grid.add(btnixr1, 2, 5);
        TextField ixr2Field = new TextField();
        Button btnixr2 = new Button("Set");
        grid.add(new Label("IXR2:"), 0, 6);
        grid.add(ixr2Field, 1, 6);
        grid.add(btnixr2, 2, 6);
        TextField ixr3Field = new TextField();
        Button btnixr3 = new Button("Set");
        grid.add(new Label("IXR3:"), 0, 7);
        grid.add(ixr3Field, 1, 7);
        grid.add(btnixr3, 2, 7);

        // Other Fields
        TextField PCField = new TextField();
        Button btnPC = new Button("Set");
        grid.add(new Label("PC:"), 3, 0);
        grid.add(PCField, 4, 0);
        grid.add(btnPC, 5, 0);
        TextField MARField = new TextField();
        Button btnMAR = new Button("Set");
        grid.add(new Label("MAR:"), 3, 1);
        grid.add(MARField, 4, 1);
        grid.add(btnMAR, 5, 1);
        TextField MBRField = new TextField();
        Button btnMBR = new Button("Set");
        grid.add(new Label("MBR:"), 3, 2);
        grid.add(MBRField, 4, 2);
        grid.add(btnMBR, 5, 2);

        // Remaining Fields Without Buttons
        String[] remainingLabels = {"IR", "CC", "MFR", "Binary", "Octal", "Program File"};

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
        Label printerLabel = new Label("Printer:");
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
        Button btnStore = new Button("Store");
        Button btnRun = new Button("Run");
        Button btnStep = new Button("Step");
        Button btnHalt = new Button("Halt");
        Button btnIPL = new Button("IPL");
        btnIPL.setStyle("-fx-background-color: red; -fx-text-fill: white;");

        btnLoad.setOnAction(e -> System.out.println("Load button clicked"));
        btnStore.setOnAction(e -> System.out.println("Store button clicked"));
        btnRun.setOnAction(e -> System.out.println("Run button clicked"));
        btnStep.setOnAction(e -> System.out.println("Step button clicked"));
        btnHalt.setOnAction(e -> System.out.println("Halt button clicked"));
        btnIPL.setOnAction(e -> {
            String pf = fieldMap.get("Program File").getText();
            System.out.println("Program File Path: " + pf);
            this.sim.setProgramFile(pf);
            boolean successInitialization = this.sim.initializeProgram();
            if(!successInitialization){
                debugOutput.setText("Failed to initialize with load file: " + pf);
            }else{
                debugOutput.setText("Successfully initialized with load file: " + pf);
                btnIPL.setStyle("-fx-background-color: green; -fx-text-fill: white;");
            }
        });

        //register buttons
        btngpr0.setOnAction(e -> {
            String num = "";
            if(!gpr0Field.getText().equals("")){ //gpr text box filled in
                int temp = Integer.parseInt(gpr0Field.getText());
                if(temp < 0 || temp > 65535){
                    debugOutput.setText("Value is not valid.");
                }else{
                    this.sim.setGPR0(gpr0Field.getText());
                    debugOutput.setText("GPR0 set with: " + gpr0Field.getText());
                }
            }else if(!fieldMap.get("Octal").getText().equals("")){ 
                int temp = Integer.parseInt(fieldMap.get("Octal").getText(), 8);
                if(temp < 0 || temp > 65535){
                    debugOutput.setText("Value is not valid.");
                }else{
                    num = Conversion.convertToDecimalString(fieldMap.get("Octal").getText());
                    this.sim.setGPR0(num); //converts value in Octal text box to a decimal string
                    gpr0Field.setText(num);
                    fieldMap.get("Octal").setText("");
                    debugOutput.setText("GPR0 set with: " + num);
                }
            }else if(!fieldMap.get("Binary").getText().equals("")){
                int temp = Integer.parseInt(fieldMap.get("Binary").getText(), 2);
                if(temp < 0 || temp > 65535){
                    debugOutput.setText("Value is not valid.");
                }else{
                num = Integer.toString(Integer.parseInt(fieldMap.get("Binary").getText(), 2));
                this.sim.setGPR0(num); //converts value in Binary text box to an octal string then decimal string
                gpr0Field.setText(num);
                fieldMap.get("Binary").setText("");
                debugOutput.setText("GPR0 set with: " + num);
                }
            }
        });
        btngpr1.setOnAction(e -> System.out.println("GPR1"));
        btngpr2.setOnAction(e -> System.out.println("GPR2"));
        btngpr3.setOnAction(e -> System.out.println("GPR3"));


        HBox buttonBox = new HBox(10, btnLoad, btnStore, btnRun, btnStep, btnHalt, btnIPL);
        VBox centerBox = new VBox(10, grid, buttonBox);
        root.setCenter(centerBox);
        
        Scene scene = new Scene(root, 1100, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    
    public static void main(String[] args) {
        launch(args);
    }
}
