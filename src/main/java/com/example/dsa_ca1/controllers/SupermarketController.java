package com.example.dsa_ca1.controllers;

import com.example.dsa_ca1.models.Supermarket;
import com.example.dsa_ca1.models.FloorArea;
import com.example.dsa_ca1.models.Aisle;
import com.example.dsa_ca1.models.Shelf;
import com.example.dsa_ca1.models.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class SupermarketController {

    @FXML private TableView<Product> productTable;

    @FXML private TableColumn<Product, String> productNameCol;
    @FXML private TableColumn<Product, Float> productPriceCol;
    @FXML private TableColumn<Product, Float> productWeightCol;
    @FXML private TableColumn<Product, Integer> productQuantityCol;
    @FXML private TableColumn<Product, String> productTempCol;
    @FXML private TableColumn<Product, String> photoUrl;


    @FXML private ListView<String> floorAreaList;
    @FXML private ListView<String> aisleList;
    @FXML private ListView<String> shelfList;

    @FXML private FloorArea selectedFloorArea;

    private Supermarket supermarket;

    @FXML
    public void initialiseSupermarket(Supermarket supermarket) {
        this.supermarket = supermarket;
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        productWeightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
        productQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        productTempCol.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        photoUrl.setCellValueFactory(new PropertyValueFactory<>("photoURL"));

        floorAreaList.getItems().clear();
        for (FloorArea floorArea : supermarket.getFloorAreas()) {
            floorAreaList.getItems().add(floorArea.getFloorAreaName());
        }

        floorAreaList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) return;

            aisleList.getItems().clear();
            shelfList.getItems().clear();
            productTable.getItems().clear();

            selectedFloorArea = null;
            for (FloorArea floorArea : supermarket.getFloorAreas()) {
                String display = floorArea.getFloorAreaName() + " (" + floorArea.getFloorLevel() + ")";
                if (display.equals(newVal)) {
                    selectedFloorArea = floorArea;
                    break;
                }
            }

            if (selectedFloorArea != null) {
                for (Aisle aisle : selectedFloorArea.getAisles()) {
                    aisleList.getItems().add(aisle.getAisleName());
                }
            }
        });

        aisleList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) return;

            shelfList.getItems().clear();
            productTable.getItems().clear();

            Aisle selectedAisle = null;
            outerAisle:
            for (FloorArea floorArea : supermarket.getFloorAreas()) {
                for (Aisle aisle : floorArea.getAisles()) {
                    if (aisle.getAisleName().equals(newVal)) {
                        selectedAisle = aisle;
                        break outerAisle;
                    }
                }
            }

            if (selectedAisle != null) {
                for (Shelf shelf : selectedAisle.getShelves()) {
                    shelfList.getItems().add("Shelf " + shelf.getShelfNum());
                }
            }
        });

        shelfList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            productTable.getItems().clear();
            if (newVal == null) return;

            Shelf selectedShelf = null;
            outerShelf:
            for (FloorArea floorArea : supermarket.getFloorAreas()) {
                for (Aisle aisle : floorArea.getAisles()) {
                    for (Shelf shelf : aisle.getShelves()) {
                        if (("Shelf " + shelf.getShelfNum()).equals(newVal)) {
                            selectedShelf = shelf;
                            break outerShelf;
                        }
                    }
                }
            }

            if (selectedShelf != null) {
                ObservableList<Product> products = FXCollections.observableArrayList();
                for (Product p : selectedShelf.getProducts()) {
                    products.add(p);
                }
                productTable.setItems(products);
            }
        });


    }

    @FXML
    private void onAddFloorArea() {
        // name
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Add Floor Area");
        nameDialog.setHeaderText("Set Floor Area Name");
        nameDialog.setContentText("Name (e.g. Dairy):");
        String name = nameDialog.showAndWait().orElse("").trim();
        if (name.isEmpty()) return;

        // level
        TextInputDialog levelDialog = new TextInputDialog();
        levelDialog.setTitle("Add Floor Area");
        levelDialog.setHeaderText("Set Floor Level");
        levelDialog.setContentText("Level (e.g. Ground, First):");
        String level = levelDialog.showAndWait().orElse("").trim();
        if (level.isEmpty()) return;

        FloorArea newFloorArea = new FloorArea(name, level);
        supermarket.getFloorAreas().add(newFloorArea);

        floorAreaList.getItems().add(name + " (" + level + ")");
    }

    @FXML
    private void onRemoveFloorArea() {
        String selectedText = floorAreaList.getSelectionModel().getSelectedItem();
        if (selectedText == null) return;

        FloorArea toRemove = null;
        for (FloorArea fa : supermarket.getFloorAreas()) {
            String display = fa.getFloorAreaName() + " (" + fa.getFloorLevel() + ")";
            if (display.equals(selectedText)) {
                toRemove = fa;
                break;
            }
        }

        if (toRemove != null) {
            supermarket.getFloorAreas().removeValue(toRemove);
            floorAreaList.getItems().remove(selectedText);

            aisleList.getItems().clear();
            shelfList.getItems().clear();
            productTable.getItems().clear();
        }
    }

    @FXML
    private void onEditFloorArea() {
        String selectedText = floorAreaList.getSelectionModel().getSelectedItem();
        if (selectedText == null) return;

        FloorArea toEdit = null;
        for (FloorArea fa : supermarket.getFloorAreas()) {
            String display = fa.getFloorAreaName() + " (" + fa.getFloorLevel() + ")";
            if (display.equals(selectedText)) {
                toEdit = fa;
                break;
            }
        }
        if (toEdit == null) return;

        TextInputDialog nameDialog = new TextInputDialog(toEdit.getFloorAreaName());
        nameDialog.setTitle("Edit Floor Area");
        nameDialog.setHeaderText("Edit Floor Area Name");
        nameDialog.setContentText("Name:");
        String newName = nameDialog.showAndWait().orElse("").trim();
        if (newName.isEmpty()) return;

        TextInputDialog levelDialog = new TextInputDialog(toEdit.getFloorLevel());
        levelDialog.setTitle("Edit Floor Area");
        levelDialog.setHeaderText("Edit Floor Level");
        levelDialog.setContentText("Level:");
        String newLevel = levelDialog.showAndWait().orElse("").trim();
        if (newLevel.isEmpty()) return;

        toEdit.setFloorAreaName(newName);
        toEdit.setFloorLevel(newLevel);

        int selectedIndex = floorAreaList.getSelectionModel().getSelectedIndex();
        floorAreaList.getItems().set(selectedIndex, newName + " (" + newLevel + ")");
    }

    @FXML
    public void onAddAisle(ActionEvent event) {
        if (selectedFloorArea == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Floor Area Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a floor area before adding an aisle.");
            alert.showAndWait();
            return;
        }

        // name
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Add Aisle");
        nameDialog.setHeaderText("Set Aisle Name");
        nameDialog.setContentText("Name:");
        String name = nameDialog.showAndWait().orElse("").trim();
        if (name.isEmpty()) return;

        // width
        TextInputDialog widthDialog = new TextInputDialog();
        widthDialog.setTitle("Add Aisle");
        widthDialog.setHeaderText("Set Aisle Width");
        widthDialog.setContentText("Width (number):");
        String widthStr = widthDialog.showAndWait().orElse("").trim();
        if (widthStr.isEmpty()) return;

        // height
        TextInputDialog heightDialog = new TextInputDialog();
        heightDialog.setTitle("Add Aisle");
        heightDialog.setHeaderText("Set Aisle Height");
        heightDialog.setContentText("Height (number):");
        String heightStr = heightDialog.showAndWait().orElse("").trim();
        if (heightStr.isEmpty()) return;

        // temp
        TextInputDialog tempDialog = new TextInputDialog();
        tempDialog.setTitle("Add Aisle");
        tempDialog.setHeaderText("Set Aisle Temperature");
        tempDialog.setContentText("Temp (e.g. Room, Frozen):");
        String temp = tempDialog.showAndWait().orElse("").trim();
        if (temp.isEmpty()) return;

        int width, height;
        try {
            width = Integer.parseInt(widthStr);
            height = Integer.parseInt(heightStr);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Number");
            alert.setHeaderText(null);
            alert.setContentText("Width and height must be numbers.");
            alert.showAndWait();
            return;
        }

        Aisle newAisle = new Aisle(name, width, height, temp);
        selectedFloorArea.addAisle(newAisle);
    }

    @FXML
    public void onRemoveAisle(ActionEvent event) {
    }

    @FXML
    public void onEditAisle(ActionEvent event) {
    }

    @FXML
    public void onAddShelf(ActionEvent event) {
    }

    @FXML
    public void onRemoveShelf(ActionEvent event) {
    }

    @FXML
    public void onEditShelf(ActionEvent event) {
    }
}
