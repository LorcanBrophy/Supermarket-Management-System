package com.example.dsa_ca1.controllers;

import com.example.dsa_ca1.models.Supermarket;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class SupermarketController {

    @FXML private Label supermarketTitle;
    @FXML private TabPane floorTabs;

    public void initialiseSupermarket(Supermarket supermarket, int numFloors) {
        supermarketTitle.setText("Supermarket: " + supermarket.getName());

        floorTabs.getTabs().clear();
        for (int i = 1; i <= numFloors; i++) {
            Tab tab = new Tab("Floor " + i);
            tab.setContent(new Label("Manage floor " + i + " here."));
            floorTabs.getTabs().add(tab);
        }
    }
}
