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
    @FXML public Spinner<Integer> numFloors;
    @FXML private TextField supermarketName;
    @FXML private Label welcomeText;

    @FXML
    public void initialize() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        numFloors.setValueFactory(valueFactory);
    }

    @FXML
    protected void onCreateSupermarketButtonPressed() throws IOException {
        String name = supermarketName.getText();
        if (name.isEmpty()) {
            welcomeText.setText("Please enter a supermarket name!");
            return;
        }

        int floors = numFloors.getValue();
        Supermarket supermarket;

        File file = new File(name + ".xml");

        if (file.exists()) {
            try {
                supermarket = Supermarket.load(name);
            } catch (Exception e) {
                return;
            }
        } else {
            supermarket = new Supermarket(name, floors);
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