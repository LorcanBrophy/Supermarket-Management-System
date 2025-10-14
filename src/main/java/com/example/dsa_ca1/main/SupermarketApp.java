package com.example.dsa_ca1.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SupermarketApp extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SupermarketApp.class.getResource("/com/example/dsa_ca1/setup.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/dsa_ca1/style.css")).toExternalForm());
        stage.setTitle("Supermarket Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}