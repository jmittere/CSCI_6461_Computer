package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ComputerGUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Computer System GUI");

        BorderPane root = new BorderPane();

        Label titleLabel = new Label("Computer System GUI");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        root.setTop(titleLabel);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // General Purpose Registers (GPR 0-3)
        for (int i = 0; i < 4; i++) {
            grid.add(new Label("GPR " + i + ":"), 0, i);
            grid.add(new TextField(), 1, i);
        }

        // Index Registers (IXR 1-3)
        for (int i = 1; i <= 3; i++) {
            grid.add(new Label("IXR " + i + ":"), 0, i + 4);
            grid.add(new TextField(), 1, i + 4);
        }

        // Other Fields
        String[] labels = {"PC", "MAR", "MBR", "IR", "CC", "MFR", "Binary", "Octal", "Program File"};
        for (int i = 0; i < labels.length; i++) {
            grid.add(new Label(labels[i] + ":"), 2, i);
            grid.add(new TextField(), 3, i);
        }

        // Console Input Field on Right Side
        Label consoleLabel = new Label("Console Input:");
        TextArea consoleInput = new TextArea();
        consoleInput.setPrefHeight(400);
        consoleInput.setPrefWidth(300);

        VBox consoleBox = new VBox(5, consoleLabel, consoleInput);
        root.setRight(consoleBox);

        // Buttons
        Button btnLoad = new Button("Load");
        Button btnLoadMem = new Button("Load in Memory");
        Button btnStore = new Button("Store");
        Button btnStoreMem = new Button("Store in Memory");
        Button btnRun = new Button("Run");
        Button btnStep = new Button("Step");
        Button btnHalt = new Button("Halt");

        btnLoad.setOnAction(e -> System.out.println("Load button clicked"));
        btnLoadMem.setOnAction(e -> System.out.println("Load in Memory button clicked"));
        btnStore.setOnAction(e -> System.out.println("Store button clicked"));
        btnStoreMem.setOnAction(e -> System.out.println("Store in Memory button clicked"));
        btnRun.setOnAction(e -> System.out.println("Run button clicked"));
        btnStep.setOnAction(e -> System.out.println("Step button clicked"));
        btnHalt.setOnAction(e -> System.out.println("Halt button clicked"));

        HBox buttonBox = new HBox(10, btnLoad, btnLoadMem, btnStore, btnStoreMem, btnRun, btnStep, btnHalt);
        VBox centerBox = new VBox(10, grid, buttonBox);
        root.setCenter(centerBox);
        
        Scene scene = new Scene(root, 900, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}