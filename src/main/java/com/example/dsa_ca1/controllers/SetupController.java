package com.example.dsa_ca1.controllers;

import com.example.dsa_ca1.models.Supermarket;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class SetupController {
    @FXML private TextField supermarketName;
    @FXML private Spinner<Integer> numFloors;
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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/dsa_ca1/supermarket.fxml"));
        Scene scene = new Scene(loader.load(), 600, 400);

        SupermarketController controller = loader.getController();
        controller.initialiseSupermarket(new Supermarket(name), floors);

        Stage stage = (Stage) supermarketName.getScene().getWindow();
        stage.setTitle(name + " Supermarket");
        stage.setScene(scene);

    }
}