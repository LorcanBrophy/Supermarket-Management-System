package com.example.dsa_ca1.controllers;

import com.example.dsa_ca1.models.Supermarket;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class SetupController {
    @FXML private TextField supermarketName;
    @FXML private Label welcomeText;

    @FXML
    protected void onCreateSupermarketButtonPressed() throws IOException {
        String name = supermarketName.getText();
        if (name.isEmpty()) {
            welcomeText.setText("Please enter a supermarket name!");
            return;
        }

        Supermarket supermarket;

        File file = new File(name + ".xml");

        if (file.exists()) {
            try {
                supermarket = Supermarket.load(name);
                welcomeText.setText("Loaded existing supermarket: " + name);
            } catch (Exception e) {
                welcomeText.setText("Failed to load existing file!");
                e.printStackTrace();
                return;
            }
        } else {
            // Create new
            supermarket = new Supermarket(name);
            welcomeText.setText("Created new supermarket: " + name);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/dsa_ca1/supermarket.fxml"));
        Parent newRoot = loader.load();

        SupermarketController controller = loader.getController();
        controller.initialiseSupermarket(supermarket);

        Stage stage = (Stage) supermarketName.getScene().getWindow();
        Scene scene = stage.getScene();

        scene.setRoot(newRoot);

        stage.setTitle(name + " Supermarket");
    }
}