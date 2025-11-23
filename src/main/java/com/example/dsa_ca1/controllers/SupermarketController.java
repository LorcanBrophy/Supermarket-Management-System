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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

public class SupermarketController {
    @FXML private AnchorPane mapPane;
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

    private FloorArea selectedFloorArea;
    private Aisle selectedAisle;
    private Shelf selectedShelf;

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

            selectedAisle = null;
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

            selectedShelf = null;
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
                for (Product product : selectedShelf.getProducts()) {
                    products.add(product);
                }
                productTable.setItems(products);
            }
        });


    }

    @FXML
    private FloorArea findFloorAreaByDisplay(String displayText) {
        for (FloorArea floorArea : supermarket.getFloorAreas()) {
            String display = floorArea.getFloorAreaName() + " (" + floorArea.getFloorLevel() + ")";
            if (display.equals(displayText)) {
                return floorArea;
            }
        }
        return null;
    }

    @FXML
    private Aisle findAisleByDisplay(String displayText) {
        if (selectedFloorArea == null) return null;

        for (Aisle aisle : selectedFloorArea.getAisles()) {
            if (aisle.getAisleName().equals(displayText)) {
                return aisle;
            }
        }
        return null;
    }

    @FXML
    private Shelf findShelfByDisplay(String displayText) {
        if (selectedAisle == null) return null;

        for (Shelf shelf : selectedAisle.getShelves()) {
            if (("Shelf " + shelf.getShelfNum()).equals(displayText)) {
                return shelf;
            }
        }
        return null;
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

        for (FloorArea floorArea : supermarket.getFloorAreas()) {
            if (floorArea.getFloorAreaName().equals(name) && floorArea.getFloorLevel().equals(level)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Duplicate Floor Area");
                alert.setHeaderText(null);
                alert.setContentText("A Floor Area with that name and level already exists.");
                alert.showAndWait();
                return;
            }
        }

        FloorArea newFloorArea = new FloorArea(name, level);
        supermarket.getFloorAreas().add(newFloorArea);

        floorAreaList.getItems().add(name + " (" + level + ")");

        drawFloorAreasMap();

        System.out.println("Linked List:");
        for (FloorArea floorArea : supermarket.getFloorAreas()) {
            System.out.println(floorArea.toString());
        }
    }

    @FXML
    private void onRemoveFloorArea() {
        String selectedText = floorAreaList.getSelectionModel().getSelectedItem();
        if (selectedText == null) return;

        FloorArea toRemove = findFloorAreaByDisplay(selectedText);
        if (toRemove == null) return;

        selectedFloorArea = null;

        supermarket.getFloorAreas().removeValue(toRemove);
        floorAreaList.getItems().remove(selectedText);

        aisleList.getItems().clear();
        shelfList.getItems().clear();
        productTable.getItems().clear();

        drawFloorAreasMap();
    }

    @FXML
    private void onEditFloorArea() {
        String selectedText = floorAreaList.getSelectionModel().getSelectedItem();
        if (selectedText == null) return;

        FloorArea toEdit = findFloorAreaByDisplay(selectedText);
        if (toEdit == null) return;

        // name
        TextInputDialog nameDialog = new TextInputDialog(toEdit.getFloorAreaName());
        nameDialog.setTitle("Edit Floor Area");
        nameDialog.setHeaderText("Edit Floor Area Name");
        nameDialog.setContentText("Name:");
        String newName = nameDialog.showAndWait().orElse("").trim();
        if (newName.isEmpty()) return;

        // level
        TextInputDialog levelDialog = new TextInputDialog(toEdit.getFloorLevel());
        levelDialog.setTitle("Edit Floor Area");
        levelDialog.setHeaderText("Edit Floor Level");
        levelDialog.setContentText("Level:");
        String newLevel = levelDialog.showAndWait().orElse("").trim();
        if (newLevel.isEmpty()) return;

        toEdit.setFloorAreaName(newName);
        toEdit.setFloorLevel(newLevel);

        int index = floorAreaList.getSelectionModel().getSelectedIndex();
        floorAreaList.getItems().set(index, newName + " (" + newLevel + ")");

        drawFloorAreasMap();
    }

    @FXML
    public void onAddAisle() {
        if (selectedFloorArea == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Floor Area Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a floor area.");
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

        for (Aisle aisle : selectedFloorArea.getAisles()) {
            if (aisle.getAisleName().equalsIgnoreCase(name)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Duplicate Aisle");
                alert.setHeaderText(null);
                alert.setContentText("An aisle with that name already exists in this floor area.");
                alert.showAndWait();
                return;
            }
        }

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

        aisleList.getItems().add(name);

        for (FloorArea floorArea : supermarket.getFloorAreas()) {
            System.out.println(floorArea.toString());
            for (Aisle aisle : floorArea.getAisles()) {
                System.out.println(aisle.toString());
            }
        }

        drawFloorAreasMap();
    }

    @FXML
    public void onRemoveAisle() {
        String selectedText = aisleList.getSelectionModel().getSelectedItem();
        if (selectedText == null) return;

        Aisle toRemove = findAisleByDisplay(selectedText);
        if (toRemove == null) return;

        selectedAisle = null;

        selectedFloorArea.getAisles().removeValue(toRemove);
        aisleList.getItems().remove(selectedText);

        shelfList.getItems().clear();
        productTable.getItems().clear();

        drawFloorAreasMap();
    }

    @FXML
    public void onEditAisle() {
        String selectedText = aisleList.getSelectionModel().getSelectedItem();
        if (selectedText == null) return;

        Aisle toEdit = findAisleByDisplay(selectedText);
        if (toEdit == null) return;

        // name
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Add Aisle");
        nameDialog.setHeaderText("Set Aisle Name");
        nameDialog.setContentText("Name:");
        String newName = nameDialog.showAndWait().orElse("").trim();
        if (newName.isEmpty()) return;

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
        String newTemp = tempDialog.showAndWait().orElse("").trim();
        if (newTemp.isEmpty()) return;

        int newWidth, newHeight;
        try {
            newWidth = Integer.parseInt(widthStr);
            newHeight = Integer.parseInt(heightStr);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Number");
            alert.setHeaderText(null);
            alert.setContentText("Width and height must be numbers.");
            alert.showAndWait();
            return;
        }

        toEdit.setAisleName(newName);
        toEdit.setAisleWidth(newWidth);
        toEdit.setAisleHeight(newHeight);
        toEdit.setAisleTemperature(newTemp);

        int index = aisleList.getSelectionModel().getSelectedIndex();
        aisleList.getItems().set(index, newName);

        drawFloorAreasMap();
    }

    @FXML
    public void onAddShelf() {
        if (selectedAisle == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Aisle Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select an aisle.");
            alert.showAndWait();
            return;
        }

        int shelfNum = selectedAisle.getShelves().size() + 1;

        Shelf newShelf = new Shelf(shelfNum);
        selectedAisle.addShelf(newShelf);

        shelfList.getItems().add("Shelf " + shelfNum);
        drawFloorAreasMap();
    }

    @FXML
    public void onRemoveShelf() {
        String selectedText = shelfList.getSelectionModel().getSelectedItem();
        if (selectedText == null) return;

        Shelf toRemove = findShelfByDisplay(selectedText);
        if (toRemove == null) return;

        selectedShelf = null;

        selectedAisle.getShelves().removeValue(toRemove);
        shelfList.getItems().remove(selectedText);

        productTable.getItems().clear();
        drawFloorAreasMap();
    }

    @FXML
    public void onAddProduct(ActionEvent event) {
        if (selectedShelf == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Shelf Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a shelf.");
            alert.showAndWait();
            return;
        }

        // name
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Add Product");
        nameDialog.setHeaderText("Enter Product Name");
        nameDialog.setContentText("Name:");
        String name = nameDialog.showAndWait().orElse("").trim();
        if (name.isEmpty()) return;

        // price
        TextInputDialog priceDialog = new TextInputDialog();
        priceDialog.setTitle("Add Product");
        priceDialog.setHeaderText("Set Product Price");
        priceDialog.setContentText("Price:");
        String priceStr = priceDialog.showAndWait().orElse("").trim();
        if (priceStr.isEmpty()) return;

        // weight
        TextInputDialog weightDialog = new TextInputDialog();
        weightDialog.setTitle("Add Product");
        weightDialog.setHeaderText("Set Product Weight");
        weightDialog.setContentText("Weight:");
        String weightStr = weightDialog.showAndWait().orElse("").trim();
        if (weightStr.isEmpty()) return;

        // quantity
        TextInputDialog quantityDialog = new TextInputDialog();
        quantityDialog.setTitle("Add Product");
        quantityDialog.setHeaderText("Set Product Quantity");
        quantityDialog.setContentText("Quantity:");
        String quantityStr = quantityDialog.showAndWait().orElse("").trim();
        if (quantityStr.isEmpty()) return;

        // temp
        TextInputDialog tempDialog = new TextInputDialog();
        tempDialog.setTitle("Add Product");
        tempDialog.setHeaderText("Set Product Temperature");
        tempDialog.setContentText("Temp (e.g. Room, Frozen):");
        String temp = tempDialog.showAndWait().orElse("").trim();
        if (temp.isEmpty()) return;

        // photo url
        TextInputDialog photoDialog = new TextInputDialog();
        photoDialog.setTitle("Add Product");
        photoDialog.setHeaderText("Set Product Photo URL");
        photoDialog.setContentText("URL:");
        String photoUrl = photoDialog.showAndWait().orElse("").trim();
        if (photoUrl.isEmpty()) return;

        float price, weight;
        int quantity;
        try {
            price = Float.parseFloat(priceStr);
            weight = Float.parseFloat(weightStr);
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Price, weight, and quantity must be valid numbers.");
            alert.showAndWait();
            return;
        }

        Product newProduct = new Product(name, price, weight, quantity, temp, photoUrl);

        selectedShelf.addProduct(newProduct);

        productTable.getItems().add(newProduct);
    }

    @FXML
    public void onRemoveProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        selectedShelf.removeProduct(selected);
        productTable.getItems().remove(selected);
    }

    @FXML
    public void onEditProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        // name
        TextInputDialog nameDialog = new TextInputDialog(selected.getProductName());
        nameDialog.setHeaderText("Edit Product Name");
        String newName = nameDialog.showAndWait().orElse("").trim();
        if (newName.isEmpty()) return;

        // price
        TextInputDialog priceDialog = new TextInputDialog(String.valueOf(selected.getPrice()));
        priceDialog.setHeaderText("Edit Product Price");
        String priceStr = priceDialog.showAndWait().orElse("").trim();
        if (priceStr.isEmpty()) return;

        // weight
        TextInputDialog weightDialog = new TextInputDialog(String.valueOf(selected.getWeight()));
        weightDialog.setHeaderText("Edit Product Weight");
        String weightStr = weightDialog.showAndWait().orElse("").trim();
        if (weightStr.isEmpty()) return;

        // quantity
        TextInputDialog qtyDialog = new TextInputDialog(String.valueOf(selected.getQuantity()));
        qtyDialog.setHeaderText("Edit Product Quantity");
        String qtyStr = qtyDialog.showAndWait().orElse("").trim();
        if (qtyStr.isEmpty()) return;

        // temp
        TextInputDialog tempDialog = new TextInputDialog(selected.getTemperature());
        tempDialog.setHeaderText("Edit Storage Temp");
        String newTemp = tempDialog.showAndWait().orElse("").trim();
        if (newTemp.isEmpty()) return;

        // photo url
        TextInputDialog urlDialog = new TextInputDialog(selected.getPhotoURL());
        urlDialog.setHeaderText("Edit Photo URL");
        String newUrl = urlDialog.showAndWait().orElse("").trim();

        float newPrice, newWeight;
        int newQty;

        try {
            newPrice = Float.parseFloat(priceStr);
            newWeight = Float.parseFloat(weightStr);
            newQty = Integer.parseInt(qtyStr);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid Input");
            alert.setContentText("Price, weight and quantity must be numeric.");
            alert.showAndWait();
            return;
        }

        selected.setProductName(newName);
        selected.setPrice(newPrice);
        selected.setWeight(newWeight);
        selected.setQuantity(newQty);
        selected.setTemperature(newTemp);
        selected.setPhotoURL(newUrl);

        productTable.refresh();
    }

    @FXML
    private void onSearchProduct() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Product");
        dialog.setHeaderText("Enter product name:");
        dialog.setContentText("Name:");
        String name = dialog.showAndWait().orElse("").trim();
        if (name.isEmpty()) return;

        StringBuilder sb = new StringBuilder();
        boolean found = false;

        for (FloorArea floorArea : supermarket.getFloorAreas()) {
            for (Aisle aisle : floorArea.getAisles()) {
                for (Shelf shelf : aisle.getShelves()) {
                    for (Product product : shelf.getProducts()) {

                        if (product.getProductName().equalsIgnoreCase(name)) {
                            found = true;
                            sb.append("====================================\n");
                            sb.append("Product: ").append(product.getProductName()).append("\n");
                            sb.append("Floor Area: ").append(floorArea.getFloorAreaName()).append("\n");
                            sb.append("Aisle: ").append(aisle.getAisleName()).append("\n");
                            sb.append("Shelf: ").append("Shelf ").append(shelf.getShelfNum()).append("\n");
                            sb.append("Quantity: ").append(product.getQuantity()).append("\n");
                            sb.append("Price: ").append(product.getPrice()).append("\n");
                            sb.append("Weight: ").append(product.getWeight()).append("\n");
                            sb.append("====================================\n");
                            sb.append("\n");
                        }
                    }
                }
            }
        }

        if (!found) {
            sb.append("No products found with name: ").append(name);
        }

        TextArea textArea = new TextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);

        Alert resultWindow = new Alert(Alert.AlertType.INFORMATION);
        resultWindow.setTitle("Search Results");
        resultWindow.setHeaderText("Matching Products Found:");
        resultWindow.getDialogPane().setContent(textArea);

        resultWindow.showAndWait();
    }

    @FXML
    private void drawFloorAreasMap() {
        mapPane.getChildren().clear();

        int numFloorAreas = supermarket.getFloorAreas().size();
        if (numFloorAreas == 0) return;

        double mapWidth = mapPane.getWidth();
        double mapHeight = mapPane.getHeight();

        double padding = 30;
        double floorWidth = (mapWidth - (padding * (numFloorAreas + 1))) / numFloorAreas; // find usable width (width - padding) and div by total areas
        double floorHeight = mapHeight - 2 * padding;

        int index = 0;
        for (FloorArea floorArea : supermarket.getFloorAreas()) {
            double x = padding + index * (floorWidth + padding);

            Rectangle rect = new Rectangle(floorWidth, floorHeight);
            rect.setX(x);
            rect.setY(padding);
            rect.getStyleClass().add("floor-area");
            mapPane.getChildren().add(rect);

            Label label = new Label(floorArea.getFloorAreaName());
            label.setLayoutX(x + padding);
            label.setLayoutY(padding + 5);
            label.setTextFill(Color.WHITE);
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            mapPane.getChildren().add(label);

            drawAisles(floorArea, rect);
            index++;
        }
    }

    @FXML
    private void drawAisles(FloorArea floorArea, Rectangle floorRect) {
        double xStart = floorRect.getX();
        double yStart = floorRect.getY();
        double width = floorRect.getWidth();
        double height = floorRect.getHeight();

        int numAisles = floorArea.getAisles().size();
        if (numAisles == 0) return;

        double padding = 30;
        double aisleHeight = (height - (padding * (numAisles + 1))) / numAisles;
        double aisleWidth = width - 2 * padding;

        int index = 0;
        for (Aisle aisle : floorArea.getAisles()) {

            double ax = xStart + padding;
            double ay = yStart + padding + index * (aisleHeight + padding);

            Rectangle rect = new Rectangle(aisleWidth, aisleHeight);
            rect.setX(ax);
            rect.setY(ay);

            rect.getStyleClass().add("aisle");

            mapPane.getChildren().add(rect);

            Label aisleLabel = new Label(aisle.getAisleName());
            aisleLabel.setLayoutX(ax + padding);
            aisleLabel.setLayoutY(ay + 5);
            aisleLabel.setTextFill(Color.WHITE);
            aisleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12;");
            mapPane.getChildren().add(aisleLabel);

            drawShelves(aisle, rect);
            index++;
        }
    }

    private void drawShelves(Aisle aisle, Rectangle aisleRect) {
        double xStart = aisleRect.getX();
        double yStart = aisleRect.getY();
        double width = aisleRect.getWidth();
        double height = aisleRect.getHeight();

        int numShelves = aisle.getShelves().size();
        if (numShelves == 0) return;

        double padding = 30;
        double shelfHeight = (height - (padding * (numShelves + 1))) / numShelves;
        double shelfWidth = width - 2 * padding;

        int index = 0;
        for (Shelf shelf : aisle.getShelves()) {
            double sx = xStart + padding;
            double sy = yStart + padding + index * (shelfHeight + padding);

            Rectangle rect = new Rectangle(shelfWidth, shelfHeight);
            rect.setX(sx);
            rect.setY(sy);

            rect.getStyleClass().add("shelf");

            mapPane.getChildren().add(rect);

            Label shelfLabel = new Label("Shelf " + shelf.getShelfNum());
            shelfLabel.setLayoutX(sx + 5);
            shelfLabel.setLayoutY(sy + 5);
            shelfLabel.setTextFill(Color.WHITE);
            shelfLabel.setStyle("-fx-font-size: 10; -fx-font-weight: bold;");
            mapPane.getChildren().add(shelfLabel);
            index++;
        }
    }

}
