package com.example.dsa_ca1.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SupermarketApp extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        // load the initial setup screen
        FXMLLoader fxmlLoader = new FXMLLoader(SupermarketApp.class.getResource("/com/example/dsa_ca1/setup.fxml"));

        // put the loaded fxml into a scene
        Scene scene = new Scene(fxmlLoader.load());

        // attach the stylesheet
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/dsa_ca1/style.css")).toExternalForm());

        // window setup
        stage.setTitle("Supermarket Management System");
        stage.setScene(scene);
        stage.show(); // show the window
    }
}