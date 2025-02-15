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
        for (int i = 0; i < 4; i++) {
            TextField textField = new TextField();
            Button button = new Button("Set");
            fieldMap.put("GPR" + i, textField);
            grid.add(new Label("GPR" + i + ":"), 0, i);
            grid.add(textField, 1, i);
            grid.add(button, 2, i);
        }

        // Index Registers (IXR 1-3) with Buttons
        for (int i = 1; i <= 3; i++) {
            TextField textField = new TextField();
            Button button = new Button("Set");
            fieldMap.put("IXR" + i, textField);
            grid.add(new Label("IXR" + i + ":"), 0, i + 4);
            grid.add(textField, 1, i + 4);
            grid.add(button, 2, i + 4);
        }

        // Other Fields
        String[] labels = {"PC", "MAR", "MBR"};
        for (int i = 0; i < labels.length; i++) {
            TextField textField = new TextField();
            Button button = new Button("Set");
            fieldMap.put(labels[i], textField);
            grid.add(new Label(labels[i] + ":"), 3, i);
            grid.add(textField, 4, i);
            grid.add(button, 5, i);
        }

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

        // Buttons
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
