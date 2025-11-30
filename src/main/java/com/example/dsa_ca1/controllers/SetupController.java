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
        // set up spinner so the user can pick between 1 and 10 floors, defaulting to 1
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        numFloors.setValueFactory(valueFactory);
    }

    @FXML
    protected void onCreateSupermarketButtonPressed() throws IOException {
        // get the supermarket name typed by the user
        String name = supermarketName.getText();

        // validation in case blank
        if (name.isEmpty()) {
            welcomeText.setText("Please enter a supermarket name!");
            return;
        }

        // read how many floors the user wants
        int floors = numFloors.getValue();
        Supermarket supermarket;

        // check if xml file for this supermarket already exists
        File file = new File(name + ".xml");

        if (file.exists()) {
            try {
                // if it exists, load the supermarket data from xml
                supermarket = Supermarket.load(name);
            } catch (Exception e) {
                // if something went wrong loading, return
                return;
            }
        } else {
            // if no file exists, create a new supermarket object
            // user can choose to save this supermarket to xml later
            supermarket = new Supermarket(name, floors);
        }

        // load the supermarket ui
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/dsa_ca1/supermarket.fxml"));
        Parent newRoot = loader.load();

        // loads supermarketController
        SupermarketController controller = loader.getController();
        // passes the new supermarket object as argument
        controller.initialiseSupermarket(supermarket);

        // switch the setup window to the supermarket screen
        Stage stage = (Stage) supermarketName.getScene().getWindow();
        Scene scene = stage.getScene();

        // update the scene's root to the new layout
        scene.setRoot(newRoot);

        // prevent resizing
        stage.setResizable(false);

        // update the title to show the supermarket's name
        stage.setTitle(name + " Supermarket");
    }
}