package com.example.dsa_ca1.controllers;

import com.example.dsa_ca1.models.Supermarket;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/dsa_ca1/supermarket.fxml"));
        var newRoot = loader.load();

        SupermarketController controller = loader.getController();
        controller.initialiseSupermarket(new Supermarket(name));

        Stage stage = (Stage) supermarketName.getScene().getWindow();
        Scene scene = stage.getScene();

        scene.setRoot((Parent) newRoot);

        stage.setTitle(name + " Supermarket");
    }
}