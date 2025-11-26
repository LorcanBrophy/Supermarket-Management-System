package com.example.dsa_ca1.controllers;

import com.example.dsa_ca1.models.Supermarket;
import com.example.dsa_ca1.models.FloorArea;
import com.example.dsa_ca1.models.Aisle;
import com.example.dsa_ca1.models.Shelf;
import com.example.dsa_ca1.models.Product;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class SupermarketController {
    @FXML
    public Pane floorMapPane;
    @FXML
    private ComboBox<String> floorSelectComboBox;
    @FXML
    private AnchorPane mapPane;
    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, String> productNameCol;
    @FXML
    private TableColumn<Product, Float> productPriceCol;
    @FXML
    private TableColumn<Product, Float> productWeightCol;
    @FXML
    private TableColumn<Product, Integer> productQuantityCol;
    @FXML
    private TableColumn<Product, String> productTempCol;
    @FXML
    private TableColumn<Product, String> photoUrl;


    @FXML
    private ListView<String> floorAreaListView;
    @FXML
    private ListView<String> aisleListView;
    @FXML
    private ListView<String> shelfListView;

    private FloorArea selectedFloorArea;
    private Aisle selectedAisle;
    private Shelf selectedShelf;

    private Supermarket supermarket;

    @FXML
    public void initialiseSupermarket(Supermarket supermarket) {
        this.supermarket = supermarket;

        for (FloorArea floorArea : supermarket.getFloorAreas()) {
            floorAreaListView.getItems().add(floorArea.getFloorAreaName());
        }

        floorAreaListView.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> {
            // ignore if nothing was selected
            if (newVal == null) return;

            // clear anything previously shown for another floor area
            aisleListView.getItems().clear();
            shelfListView.getItems().clear();
            productTable.getItems().clear();

            // reset the selected floor area reference
            selectedFloorArea = null;

            // find the actual floorArea object that matches the clicked name
            for (FloorArea floorArea : supermarket.getFloorAreas()) {
                String display = floorArea.getFloorAreaName();
                if (display.equals(newVal)) {
                    selectedFloorArea = floorArea;
                    break;
                }
            }

            // if a floor area was found, populate its aisles in the list
            if (selectedFloorArea != null) {
                for (Aisle aisle : selectedFloorArea.getAisles()) {
                    aisleListView.getItems().add(aisle.getAisleName());
                }
            }
        });

        aisleListView.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> {
            if (newVal == null) return;

            shelfListView.getItems().clear();
            productTable.getItems().clear();

            selectedAisle = null;
            for (Aisle aisle : selectedFloorArea.getAisles()) {
                if (aisle.getAisleName().equals(newVal)) {
                    selectedAisle = aisle;
                    break;
                }
            }

            if (selectedAisle != null) {
                for (Shelf shelf : selectedAisle.getShelves()) {
                    shelfListView.getItems().add(selectedAisle.getAisleName() + " | Shelf " + shelf.getShelfNum());
                }
            }
        });

        shelfListView.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> {
            if (newVal == null) return;

            productTable.getItems().clear();

            selectedShelf = null;
            for (Shelf shelf : selectedAisle.getShelves()) {
                if ((selectedAisle.getAisleName() + " | Shelf " + shelf.getShelfNum()).equals(newVal)) {
                    selectedShelf = shelf;
                    break;
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

        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        productWeightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
        productQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        productTempCol.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        photoUrl.setCellValueFactory(new PropertyValueFactory<>("photoURL"));

        Platform.runLater(this::drawFloorAreasMap);

        floorSelectComboBox.getItems().clear();
        for (int i = 0; i < supermarket.getNumFloors(); i++) {
            floorSelectComboBox.getItems().add("Floor " + (i + 1));
        }

        if (!floorSelectComboBox.getItems().isEmpty()) {
            floorSelectComboBox.getSelectionModel().selectFirst();
        }

        floorSelectComboBox.setOnAction(_ -> drawFloorAreasMap());

    }

    @FXML
    private FloorArea findFloorAreaByDisplay(String displayText) {
        for (FloorArea floorArea : supermarket.getFloorAreas()) {
            String display = floorArea.getFloorAreaName();
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
            if ((selectedAisle.getAisleName() + " | Shelf " + shelf.getShelfNum()).equals(displayText)) {
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
        String[] levels = new String[supermarket.getNumFloors()];
        for (int i = 0; i < supermarket.getNumFloors(); i++) {
            levels[i] = "Floor " + (i + 1);
        }

        ChoiceDialog<String> levelDialog = new ChoiceDialog<>(levels[0], levels);
        levelDialog.setTitle("Add Floor Area");
        levelDialog.setHeaderText("Select Floor Level");
        levelDialog.setContentText("Choose:");

        String level = levelDialog.showAndWait().orElse(null);
        if (level == null) return;

        for (FloorArea floorArea : supermarket.getFloorAreas()) {
            if (floorArea.getFloorAreaName().equals(name)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Duplicate Floor Area");
                alert.setHeaderText(null);
                alert.setContentText("A Floor Area with that name already exists.");
                alert.showAndWait();
                return;
            }
        }

        FloorArea newFloorArea = new FloorArea(name, level);
        supermarket.getFloorAreas().add(newFloorArea);

        floorAreaListView.getItems().add(name);

        printLinkedList();
        drawFloorAreasMap();
    }

    @FXML
    private void onRemoveFloorArea() {
        String selectedText = floorAreaListView.getSelectionModel().getSelectedItem();
        if (selectedText == null) return;

        FloorArea toRemove = findFloorAreaByDisplay(selectedText);
        if (toRemove == null) return;

        selectedFloorArea = null;

        supermarket.getFloorAreas().removeValue(toRemove);
        floorAreaListView.getItems().remove(selectedText);

        aisleListView.getItems().clear();
        shelfListView.getItems().clear();
        productTable.getItems().clear();

        drawFloorAreasMap();
    }

    @FXML
    private void onEditFloorArea() {
        String selectedText = floorAreaListView.getSelectionModel().getSelectedItem();
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

        int index = floorAreaListView.getSelectionModel().getSelectedIndex();
        floorAreaListView.getItems().set(index, newName + " (" + newLevel + ")");

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
        widthDialog.setContentText("Width (m):");
        String widthStr = widthDialog.showAndWait().orElse("").trim();
        if (widthStr.isEmpty()) return;

        // height
        TextInputDialog heightDialog = new TextInputDialog();
        heightDialog.setTitle("Add Aisle");
        heightDialog.setHeaderText("Set Aisle Height");
        heightDialog.setContentText("Height (m):");
        String heightStr = heightDialog.showAndWait().orElse("").trim();
        if (heightStr.isEmpty()) return;

        // temp
        String[] temps = {"Room", "Refrigerated", "Frozen"};
        ChoiceDialog<String> tempDialog = new ChoiceDialog<>(temps[0], temps);
        tempDialog.setTitle("Add Aisle");
        tempDialog.setHeaderText("Set Aisle Temperature");
        tempDialog.setContentText("Temp:");

        String temp;
        temp = tempDialog.showAndWait().orElse(null);
        if (temp == null || temp.isEmpty()) return;

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

        for (Aisle aisle : selectedFloorArea.getAisles()) {
            System.out.println(aisle.toString());
        }

        aisleListView.getItems().add(name);

        drawFloorAreasMap();
    }

    @FXML
    public void onRemoveAisle() {
        String selectedText = aisleListView.getSelectionModel().getSelectedItem();
        if (selectedText == null) return;

        Aisle toRemove = findAisleByDisplay(selectedText);
        if (toRemove == null) return;

        selectedAisle = null;

        selectedFloorArea.getAisles().removeValue(toRemove);
        aisleListView.getItems().remove(selectedText);

        shelfListView.getItems().clear();
        productTable.getItems().clear();

        drawFloorAreasMap();
    }

    @FXML
    public void onEditAisle() {
        String selectedText = aisleListView.getSelectionModel().getSelectedItem();
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
        widthDialog.setContentText("Width (m):");
        String widthStr = widthDialog.showAndWait().orElse("").trim();
        if (widthStr.isEmpty()) return;

        // height
        TextInputDialog heightDialog = new TextInputDialog();
        heightDialog.setTitle("Add Aisle");
        heightDialog.setHeaderText("Set Aisle Height");
        heightDialog.setContentText("Height (m):");
        String heightStr = heightDialog.showAndWait().orElse("").trim();
        if (heightStr.isEmpty()) return;

        // temp
        String[] temps = {"Room", "Refrigerated", "Frozen"};
        ChoiceDialog<String> tempDialog = new ChoiceDialog<>(temps[0], temps);
        tempDialog.setTitle("Add Aisle");
        tempDialog.setHeaderText("Set Aisle Temperature");
        tempDialog.setContentText("Temp:");

        String newTemp;
        newTemp = tempDialog.showAndWait().orElse(null);
        if (newTemp == null || newTemp.isEmpty()) return;

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

        int index = aisleListView.getSelectionModel().getSelectedIndex();
        aisleListView.getItems().set(index, newName);

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

        shelfListView.getItems().add(selectedAisle.getAisleName() + " | Shelf " + shelfNum);
        drawFloorAreasMap();
    }

    @FXML
    public void onRemoveShelf() {
        String selectedText = shelfListView.getSelectionModel().getSelectedItem();
        if (selectedText == null) return;

        Shelf toRemove = findShelfByDisplay(selectedText);
        if (toRemove == null) return;

        selectedShelf = null;

        selectedAisle.getShelves().removeValue(toRemove);
        shelfListView.getItems().remove(selectedText);

        productTable.getItems().clear();
        drawFloorAreasMap();
    }

    @FXML
    public void onAddProduct() {
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
        priceDialog.setContentText("Price (€):");
        String priceStr = priceDialog.showAndWait().orElse("").trim();
        if (priceStr.isEmpty()) return;

        // weight
        TextInputDialog weightDialog = new TextInputDialog();
        weightDialog.setTitle("Add Product");
        weightDialog.setHeaderText("Set Product Weight");
        weightDialog.setContentText("Weight (g):");
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
        String temp = selectedAisle.getAisleTemperature();

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
            alert.setContentText("Price, weight, and quantity must be valid  numbers.");
            alert.showAndWait();
            return;
        }

        Product matchingProduct = null;
        for (Product product : selectedShelf.getProducts()) {
            if (product.getProductName().equals(name) && product.getWeight() == weight) {
                matchingProduct = product;
                break;
            }
        }

        if (matchingProduct != null) {
            matchingProduct.setQuantity(matchingProduct.getQuantity() + quantity);

            matchingProduct.setPrice(price);
            matchingProduct.setPhotoURL(photoUrl);

            productTable.refresh();
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

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Remove Quantity");
        dialog.setHeaderText("Removing from: " + selected.getProductName());
        dialog.setContentText("Enter quantity to remove (Total " + selected.getQuantity() + "):");

        String result = dialog.showAndWait().orElse(null);
        if (result == null) return;

        int amount;
        try {
            amount = Integer.parseInt(result);
            if (amount < 1) return;
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Invalid input for removal.");
            alert.showAndWait();
            return;
        }

        if (amount >= selected.getQuantity()) {
            selectedShelf.removeProduct(selected);
            productTable.getItems().remove(selected);
        } else {
            selected.setQuantity(selected.getQuantity() - amount);
        }

        productTable.refresh();
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
        priceDialog.setHeaderText("Edit Product Price (€)");
        String priceStr = priceDialog.showAndWait().orElse("").trim();
        if (priceStr.isEmpty()) return;

        // weight
        TextInputDialog weightDialog = new TextInputDialog(String.valueOf(selected.getWeight()));
        weightDialog.setHeaderText("Edit Product Weight (g)");
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
                            sb.append("Shelf: ").append(selectedAisle.getAisleName()).append(" | Shelf ").append(shelf.getShelfNum()).append("\n");
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
        floorMapPane.getChildren().clear();

        String selectedFloor = floorSelectComboBox.getValue();
        if (selectedFloor == null) return;

        List<FloorArea> filteredFloorAreas = new ArrayList<>();
        for (FloorArea floorArea : supermarket.getFloorAreas()) {
            if (floorArea.getFloorLevel().equals(selectedFloor)) {
                filteredFloorAreas.add(floorArea);
            }
        }

        int numFloorAreas = filteredFloorAreas.size();
        if (numFloorAreas == 0) return;

        double mapWidth = floorMapPane.getWidth();
        double mapHeight = floorMapPane.getHeight();
        double padding = 30;
        double floorWidth = (mapWidth - (padding * (numFloorAreas + 1))) / numFloorAreas;
        double floorHeight = mapHeight - 2 * padding;

        int index = 0;
        for (FloorArea floorArea : filteredFloorAreas) {
            double x = padding + index * (floorWidth + padding);

            Rectangle rect = new Rectangle(floorWidth, floorHeight);
            rect.setX(x);
            rect.setY(padding);
            rect.getStyleClass().add("floor-area");
            floorMapPane.getChildren().add(rect);

            Label label = new Label(floorArea.getFloorAreaName());
            label.setLayoutX(x + 5);
            label.setLayoutY(padding + 5);
            label.setTextFill(Color.WHITE);
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            floorMapPane.getChildren().add(label);

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

            floorMapPane.getChildren().add(rect);

            Label aisleLabel = new Label(aisle.getAisleName());
            aisleLabel.setLayoutX(ax + padding);
            aisleLabel.setLayoutY(ay + 5);
            aisleLabel.setTextFill(Color.WHITE);
            aisleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12;");
            floorMapPane.getChildren().add(aisleLabel);

            drawShelves(aisle, rect);
            index++;
        }
    }

    @FXML
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

            floorMapPane.getChildren().add(rect);

            Label shelfLabel = new Label(aisle.getAisleName() + " | Shelf " + shelf.getShelfNum());
            shelfLabel.setLayoutX(sx + 5);
            shelfLabel.setLayoutY(sy + 5);
            shelfLabel.setTextFill(Color.WHITE);
            shelfLabel.setStyle("-fx-font-size: 10; -fx-font-weight: bold;");
            floorMapPane.getChildren().add(shelfLabel);
            index++;
        }
    }

    @FXML
    public void onSave() {
        try {
            supermarket.save(supermarket.getName());
        } catch (Exception e) {
            System.err.println("Error writing to file: " + e);
        }
    }

    @FXML
    public void onReset() {
        supermarket.getFloorAreas().clear();

        floorAreaListView.getItems().clear();
        aisleListView.getItems().clear();
        shelfListView.getItems().clear();
        productTable.getItems().clear();

        selectedFloorArea = null;
        selectedAisle = null;
        selectedShelf = null;

        drawFloorAreasMap();
    }

    private String printLinkedList() {
        StringBuilder sb = new StringBuilder();

        sb.append("======== SUPERMARKET========\n");

        sb.append("\n").append(supermarket.toString()).append("\n");
        sb.append(String.format("Total Value: €%.2f\n", supermarket.totalValue()));

        for (FloorArea floorArea : supermarket.getFloorAreas()) {

            sb.append("\n========================================\n");
            sb.append(floorArea).append("\n");
            sb.append(String.format("Total Value: €%.2f\n", floorArea.totalValue()));
            sb.append("========================================\n");

            for (Aisle aisle : floorArea.getAisles()) {

                sb.append("\n--------------------------------------\n");
                sb.append(aisle).append("\n");
                sb.append(String.format("Total Value: €%.2f\n", aisle.totalValue()));
                sb.append("--------------------------------------\n");

                for (Shelf shelf : aisle.getShelves()) {
                    sb.append(shelf).append("\n");
                    sb.append(String.format("Total Value: €%.2f\n", shelf.totalValue()));

                    sb.append("\n").append(shelf.getProducts().display()).append("\n");
                }
            }
        }

        sb.append("\n============================\n");

        return sb.toString();
    }

    @FXML
    public void onViewAll() {
        TextArea textArea = new TextArea(printLinkedList());
        textArea.setWrapText(false);

        Alert resultWindow = new Alert(Alert.AlertType.INFORMATION);
        resultWindow.setTitle("All Stock");
        resultWindow.setHeaderText("Overview of All Stock");


        resultWindow.getDialogPane().setMinWidth(900);
        resultWindow.getDialogPane().setMinHeight(600);

        resultWindow.getDialogPane().setContent(textArea);

        resultWindow.showAndWait();
    }
}
