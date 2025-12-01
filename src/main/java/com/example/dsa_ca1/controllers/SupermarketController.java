package com.example.dsa_ca1.controllers;

import com.example.dsa_ca1.models.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

public class SupermarketController {
    // reference to the supermarket currently being viewed
    private Supermarket supermarket;

    @FXML
    private ListView<String> floorAreaListView;
    @FXML
    private ListView<String> aisleListView;
    @FXML
    private ListView<String> shelfListView;

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
    public Pane floorMapPane;
    @FXML
    private ComboBox<String> floorSelectComboBox;

    // keeps track of what the user has selected at each level
    private FloorArea selectedFloorArea;
    private Aisle selectedAisle;
    private Shelf selectedShelf;

    @FXML
    public void initialiseSupermarket(Supermarket supermarket) {
        // store the supermarket instance so everything else can use it
        this.supermarket = supermarket;

        // fill the floor area list with the names of each floor area
        for (FloorArea floorArea : supermarket.getFloorAreas()) {
            floorAreaListView.getItems().add(floorArea.getFloorAreaName());
        }

        // adds a listener to the floorArea ListView, loads the relevant aisles for the selected floorArea
        floorAreaListView.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> {
            // ignore if nothing was selected
            if (newVal == null) return;

            // clear previous aisle, shelf, and product data
            aisleListView.getItems().clear();
            shelfListView.getItems().clear();
            productTable.getItems().clear();

            // reset the selected floor area before searching
            selectedFloorArea = null;

            // find the actual FloorArea object that matches the clicked name
            for (FloorArea floorArea : supermarket.getFloorAreas()) {
                String display = floorArea.getFloorAreaName();
                if (display.equals(newVal)) {
                    selectedFloorArea = floorArea;
                    break;
                }
            }

            // if floor area is found, load its aisles
            if (selectedFloorArea != null) {
                for (Aisle aisle : selectedFloorArea.getAisles()) {
                    aisleListView.getItems().add(aisle.getAisleName());
                }
            }

            System.out.println(selectedFloorArea);
        });

        // adds a listener to the aisle ListView, loads the relevant shelves for the selected aisle
        aisleListView.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> {
            if (newVal == null) return;

            // clear previous shelves and products
            shelfListView.getItems().clear();
            productTable.getItems().clear();

            selectedAisle = null;

            // find the selected aisle based on the clicked name
            for (Aisle aisle : selectedFloorArea.getAisles()) {
                if (aisle.getAisleName().equals(newVal)) {
                    selectedAisle = aisle;
                    break;
                }
            }

            // if we found an aisle, add its shelves to the list
            if (selectedAisle != null) {
                for (Shelf shelf : selectedAisle.getShelves()) {
                    shelfListView.getItems().add(selectedAisle.getAisleName() + " | Shelf " + shelf.getShelfNum());
                }
            }

            System.out.println(selectedAisle);
        });

        // adds a listener to the shelf ListView, loads the relevant products for the selected aisle
        shelfListView.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> {
            if (newVal == null) return;

            // clear product table before adding new items
            productTable.getItems().clear();

            selectedShelf = null;

            // find the selected shelf based on the clicked name
            for (Shelf shelf : selectedAisle.getShelves()) {
                if ((selectedAisle.getAisleName() + " | Shelf " + shelf.getShelfNum()).equals(newVal)) {
                    selectedShelf = shelf;
                    break;
                }
            }

            // load all products into the table
            if (selectedShelf != null) {
                ObservableList<Product> products = FXCollections.observableArrayList();
                for (Product product : selectedShelf.getProducts()) {
                    products.add(product);
                }
                productTable.setItems(products);
            }

            System.out.println(selectedShelf);
        });

        // set up product table columns to pull their values from Product fields
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        productWeightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
        productQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        productTempCol.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        photoUrl.setCellValueFactory(new PropertyValueFactory<>("photoURL"));

        // draw the map after the ui fully loads, or else it will be blank if loading xml
        Platform.runLater(this::drawFloorAreasMap);

        // populate the floor selection combo box
        floorSelectComboBox.getItems().clear();
        for (int i = 0; i < supermarket.getNumFloors(); i++) {
            floorSelectComboBox.getItems().add("Floor " + (i + 1));
        }

        // automatically select the first floor
        if (!floorSelectComboBox.getItems().isEmpty()) {
            floorSelectComboBox.getSelectionModel().selectFirst();
        }

        // whenever the selected floor changes, redraw the map for that floor
        floorSelectComboBox.setOnAction(_ -> drawFloorAreasMap());
    }

    // Searches through all floorAreas objects and returns the one whose display name matches the text selected in the ListView
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

    // Searches through all aisle objects in the current floorArea and returns the one whose display name matches the text selected in the ListView
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

    // Searches through all shelf objects in the current floorArea and returns the one whose display name matches the text selected in the ListView
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

    // helper method that applies style sheet to dialog boxes
    private void styleDialog(Dialog<?> dialog) {
        dialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/dsa_ca1/style.css")).toExternalForm());
    }

    // helper method that displays the dialogs to add/edit a floorArea
    @FXML
    private FloorArea floorAreaDialog(FloorArea existing) {
        String prefillName = (existing != null) ? existing.getFloorAreaName() : "";

        String dialogTitle = (existing == null) ? "Add Floor Area" : "Edit Floor Area";

        // name dialog
        TextInputDialog nameDialog = new TextInputDialog(prefillName);
        nameDialog.setTitle(dialogTitle);
        nameDialog.setHeaderText("Enter Floor Area Name");
        nameDialog.setContentText("Name:");
        nameDialog.setGraphic(null);
        styleDialog(nameDialog);

        String name = nameDialog.showAndWait().orElse("").trim();
        if (name.isEmpty()) return null;

        // levels
        String[] levels = new String[supermarket.getNumFloors()];
        for (int i = 0; i < levels.length; i++) {
            levels[i] = "Floor " + (i + 1);
        }

        // level dialog
        ChoiceDialog<String> levelDialog = new ChoiceDialog<>(levels[0], levels);
        levelDialog.setTitle(dialogTitle);
        levelDialog.setHeaderText("Select Floor Level");
        levelDialog.setContentText("Choose:");
        levelDialog.setGraphic(null);
        styleDialog(levelDialog);

        String level = levelDialog.showAndWait().orElse("").trim();
        if (level.isEmpty()) return null;

        if (existing == null) return new FloorArea(name, level);

        existing.setFloorAreaName(name);
        existing.setFloorLevel(level);

        return existing;
    }

    // adds a floorArea when Add button is pressed
    @FXML
    private void onAddFloorArea() {
        FloorArea newFloorArea = floorAreaDialog(null);
        if (newFloorArea == null) return;

        supermarket.getFloorAreas().linkedListAdd(newFloorArea);
        floorAreaListView.getItems().add(newFloorArea.getFloorAreaName());

        drawFloorAreasMap();
    }

    // deletes a selected floorArea when Remove button is pressed
    @FXML
    private void onRemoveFloorArea() {
        // get floorArea name from UI
        String selectedText = floorAreaListView.getSelectionModel().getSelectedItem();
        if (selectedText == null) return;

        // find the actual object linked to that display text
        FloorArea toRemove = findFloorAreaByDisplay(selectedText);
        if (toRemove == null) return;

        // clear the selectedFloorArea since its is being removed
        selectedFloorArea = null;

        // remove from linked list and UI ListView
        supermarket.getFloorAreas().linkedListRemove(toRemove);
        floorAreaListView.getItems().remove(selectedText);

        // wipe any aisles/shelves/products that were showing
        aisleListView.getItems().clear();
        shelfListView.getItems().clear();
        productTable.getItems().clear();

        drawFloorAreasMap();
    }

    // edits a selected floorArea when Edit button is pressed
    @FXML
    private void onEditFloorArea() {
        if (selectedFloorArea == null) return;

        FloorArea toEdit = floorAreaDialog(selectedFloorArea);
        if (toEdit == null) return;

        int index = floorAreaListView.getSelectionModel().getSelectedIndex();
        floorAreaListView.getItems().set(index, toEdit.getFloorAreaName());

        drawFloorAreasMap();
    }

    // helper method that displays the dialogs to add/edit an aisle
    @FXML
    public Aisle aisleDialog(Aisle existing) {
        String prefillName = (existing != null) ? existing.getAisleName() : "";
        String prefillWidth = (existing != null) ? Float.toString(existing.getAisleWidth()) : "";
        String prefillHeight = (existing != null) ? Float.toString(existing.getAisleHeight()) : "";

        String dialogTitle = (existing == null) ? "Add Aisle" : "Edit Aisle";

        // stops user from adding aisle when no floorArea is selected
        if (selectedFloorArea == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Floor Area Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a floor area.");
            alert.showAndWait();
            return null;
        }

        // name
        TextInputDialog nameDialog = new TextInputDialog(prefillName);
        nameDialog.setTitle(dialogTitle);
        nameDialog.setHeaderText("Set Aisle Name");
        nameDialog.setContentText("Name:");
        nameDialog.setGraphic(null);
        styleDialog(nameDialog);

        String name = nameDialog.showAndWait().orElse("").trim();
        if (name.isEmpty()) return null;

        // check if an aisle with this name already exists in the floor area
        if (existing == null || !existing.getAisleName().equalsIgnoreCase(name)) {
            for (Aisle aisle : selectedFloorArea.getAisles()) {
                if (aisle.getAisleName().equalsIgnoreCase(name)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Duplicate Aisle");
                    alert.setHeaderText(null);
                    alert.setContentText("An aisle with that name already exists in this floor area.");
                    alert.showAndWait();
                    return null;
                }
            }
        }

        // width
        TextInputDialog widthDialog = new TextInputDialog(prefillWidth);
        widthDialog.setTitle(dialogTitle);
        widthDialog.setHeaderText("Set Aisle Width");
        widthDialog.setContentText("Width (m):");
        widthDialog.setGraphic(null);
        styleDialog(widthDialog);

        String widthStr = widthDialog.showAndWait().orElse("").trim();
        if (widthStr.isEmpty()) return null;

        // height
        TextInputDialog heightDialog = new TextInputDialog(prefillHeight);
        heightDialog.setTitle(dialogTitle);
        heightDialog.setHeaderText("Set Aisle Height");
        heightDialog.setContentText("Height (m):");
        heightDialog.setGraphic(null);
        styleDialog(heightDialog);

        String heightStr = heightDialog.showAndWait().orElse("").trim();
        if (heightStr.isEmpty()) return null;

        // temp
        // makes array for each option, which is used in a choiceDialog
        String[] temps = {"Room", "Refrigerated", "Frozen"};

        ChoiceDialog<String> tempDialog = new ChoiceDialog<>(temps[0], temps);
        tempDialog.setTitle(dialogTitle);
        tempDialog.setHeaderText("Set Aisle Temperature");
        tempDialog.setContentText("Temp:");
        tempDialog.setGraphic(null);
        styleDialog(tempDialog);

        String temp;
        temp = tempDialog.showAndWait().orElse("").trim();
        if (temp.isEmpty()) return null;

        // parse width/height, gives error if not valid input
        float width, height;
        try {
            width = Float.parseFloat(widthStr);
            height = Float.parseFloat(heightStr);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Number");
            alert.setHeaderText(null);
            alert.setContentText("Width and height must be numbers.");
            alert.showAndWait();
            return null;
        }

        if (existing == null) return new Aisle(name, width, height, temp);

        existing.setAisleName(name);
        existing.setAisleWidth(width);
        existing.setAisleHeight(height);
        existing.setAisleTemperature(temp);

        // updates products temp
        for (Shelf shelf : existing.getShelves()) {
            for (Product product : shelf.getProducts()) {
                product.setTemperature(temp);
            }
        }

        return existing;
    }

    // adds an aisle when Add button is pressed
    @FXML
    public void onAddAisle() {
        Aisle newAisle = aisleDialog(null);
        if (newAisle == null) return;

        selectedFloorArea.addAisle(newAisle);
        aisleListView.getItems().add(newAisle.getAisleName());

        drawFloorAreasMap();
    }

    // deletes a selected aisle when Remove button is pressed
    @FXML
    public void onRemoveAisle() {
        // get aisle name from UI
        String selectedText = aisleListView.getSelectionModel().getSelectedItem();
        if (selectedText == null) return;

        // find the matching aisle object
        Aisle toRemove = findAisleByDisplay(selectedText);
        if (toRemove == null) return;

        // clear reference
        selectedAisle = null;

        // remove from the linked list + ui
        selectedFloorArea.getAisles().linkedListRemove(toRemove);
        aisleListView.getItems().remove(selectedText);

        // clear shelf and product table
        shelfListView.getItems().clear();
        productTable.getItems().clear();

        drawFloorAreasMap();
    }

    // edits a selected aisle when Edit button is pressed
    @FXML
    public void onEditAisle() {
        if (selectedAisle == null) return;

        Aisle toEdit = aisleDialog(selectedAisle);
        if (toEdit == null) return;

        int index = aisleListView.getSelectionModel().getSelectedIndex();
        aisleListView.getItems().set(index, toEdit.getAisleName());

        drawFloorAreasMap();
    }

    // adds a shelf when Add button is pressed
    @FXML
    public void onAddShelf() {
        // if no aisle is selected, warn the user and stop
        if (selectedAisle == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Aisle Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select an aisle.");
            alert.showAndWait();
            return;
        }

        // adds 1 to total shelf size, since linkedList uses 0 based indexing
        int shelfNum = selectedAisle.getShelves().size() + 1;

        // create the new shelf and add it to the aisle
        Shelf newShelf = new Shelf(shelfNum);
        selectedAisle.addShelf(newShelf);

        // display it in the shelf list
        shelfListView.getItems().add(selectedAisle.getAisleName() + " | Shelf " + shelfNum);
        drawFloorAreasMap();
    }

    // deletes a selected shelf when Remove button is pressed
    @FXML
    public void onRemoveShelf() {
        // get whatever shelf the user clicked
        String selectedText = shelfListView.getSelectionModel().getSelectedItem();
        if (selectedText == null) return;

        // find the actual shelf object that matches the list entry
        Shelf toRemove = findShelfByDisplay(selectedText);
        if (toRemove == null) return;

        // clear any selected shelf
        selectedShelf = null;

        // remove from the linked list + ui
        selectedAisle.getShelves().linkedListRemove(toRemove);
        shelfListView.getItems().remove(selectedText);

        // clear the table since the shelf is gone
        productTable.getItems().clear();

        drawFloorAreasMap();
    }

    // helper method that displays the dialogs to add/edit a product
    @FXML
    private Product productDialog(Product existing) {
        String prefillName = (existing != null) ? existing.getProductName() : "";
        String prefillPrice = (existing != null) ? String.valueOf(existing.getPrice()) : "";
        String prefillWeight = (existing != null) ? String.valueOf(existing.getWeight()) : "";
        String prefillQuantity = (existing != null) ? String.valueOf(existing.getQuantity()) : "";
        String prefillPhotoUrl = (existing != null) ? existing.getPhotoURL() : "";

        String dialogTitle = (existing == null) ? "Add Product" : "Edit Product";


        // validation for no selected shelf
        if (selectedShelf == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Shelf Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a shelf.");
            alert.showAndWait();
            return null;
        }

        // name
        TextInputDialog nameDialog = new TextInputDialog(prefillName);
        nameDialog.setTitle(dialogTitle);
        nameDialog.setHeaderText("Set Product Name");
        nameDialog.setContentText("Name:");
        nameDialog.setGraphic(null);
        styleDialog(nameDialog);

        String name = nameDialog.showAndWait().orElse("").trim();
        if (name.isEmpty()) return null; // if I didn't have .orElse() i would need to check (name == null)

        // price
        TextInputDialog priceDialog = new TextInputDialog(prefillPrice);
        priceDialog.setTitle(dialogTitle);
        priceDialog.setHeaderText("Set Product Price");
        priceDialog.setContentText("Price (€):");
        priceDialog.setGraphic(null);
        styleDialog(priceDialog);

        String priceStr = priceDialog.showAndWait().orElse("").trim();
        if (priceStr.isEmpty()) return null;

        // weight
        TextInputDialog weightDialog = new TextInputDialog(prefillWeight);
        weightDialog.setTitle(dialogTitle);
        weightDialog.setHeaderText("Set Product Weight");
        weightDialog.setContentText("Weight (g):");
        weightDialog.setGraphic(null);
        styleDialog(weightDialog);

        String weightStr = weightDialog.showAndWait().orElse("").trim();
        if (weightStr.isEmpty()) return null;

        // quantity
        TextInputDialog quantityDialog = new TextInputDialog(prefillQuantity);
        quantityDialog.setTitle(dialogTitle);
        quantityDialog.setHeaderText("Set Product Quantity");
        quantityDialog.setContentText("Quantity:");
        quantityDialog.setGraphic(null);
        styleDialog(quantityDialog);

        String quantityStr = quantityDialog.showAndWait().orElse("").trim();
        if (quantityStr.isEmpty()) return null;

        // temp
        // get temp from the aisle (products inherit aisle temperature)
        String temp = selectedAisle.getAisleTemperature();

        // photo url
        TextInputDialog photoDialog = new TextInputDialog(prefillPhotoUrl);
        photoDialog.setTitle(dialogTitle);
        photoDialog.setHeaderText("Set Product Photo URL");
        photoDialog.setContentText("URL:");
        photoDialog.setGraphic(null);
        styleDialog(photoDialog);

        String photoUrl = photoDialog.showAndWait().orElse("").trim();
        if (photoUrl.isEmpty()) return null;


        // parse price, weight, quantity to numbers
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
            return null;
        }

        if (existing == null) return new Product(name, price, weight, quantity, temp, photoUrl);


        // update all product fields with the new values
        existing.setProductName(name);
        existing.setPrice(price);
        existing.setWeight(weight);
        existing.setQuantity(quantity);
        existing.setPhotoURL(photoUrl);

        return existing;
    }

    // adds a product when Add button is pressed
    @FXML
    public void onAddProduct() {
        Product newProduct = productDialog(null);
        if (newProduct == null) return;

        // check if a product with the same name + weight already exists on this shelf
        for (Product product : selectedShelf.getProducts()) {
            if (product.getProductName().equalsIgnoreCase(newProduct.getProductName()) && product.getWeight() == newProduct.getWeight()) {
                product.updateQuantity(newProduct.getQuantity());
                product.setPrice(newProduct.getPrice());
                product.setPhotoURL(newProduct.getPhotoURL());

                productTable.refresh();
                return;
            }
        }

        selectedShelf.addProduct(newProduct); // adds to the shelf object
        productTable.getItems().add(newProduct); // adds to the tableView
    }

    // deletes a selected product when Remove button is pressed
    @FXML
    public void onRemoveProduct() {
        // check if a product is actually selected, otherwise stop
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        // asks how much quantity to remove
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Remove Quantity");
        dialog.setHeaderText("Removing from: " + selected.getProductName());
        dialog.setContentText("Enter quantity to remove (Total " + selected.getQuantity() + "):");
        dialog.setGraphic(null);
        styleDialog(dialog);

        String result = dialog.showAndWait().orElse("").trim();
        if (result.isEmpty()) return;

        // parse result to int
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

        // if removing all/more than total, delete the product from the shelf completely
        if (amount >= selected.getQuantity()) {
            selectedShelf.removeProduct(selected);
            productTable.getItems().remove(selected);
        } else {
            // else just decrease the quantity
            selected.updateQuantity(-amount);
        }

        // refresh the table so the UI shows the updated values
        productTable.refresh();
    }

    // edits a selected product when Edit button is pressed
    @FXML
    public void onEditProduct() {
        Product toEdit = productTable.getSelectionModel().getSelectedItem();
        if (toEdit == null) return;

        Product updated = productDialog(toEdit);
        if (updated == null) return;

        productTable.refresh();
    }

    // searches for a product by name across the entire supermarke
    @FXML
    private void onSearchProduct() {
        // asks user for product name to search, if multiple are found displays all
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Product");
        dialog.setHeaderText("Enter product name:");
        dialog.setContentText("Name:");
        dialog.setGraphic(null);
        styleDialog(dialog);

        String name = dialog.showAndWait().orElse("").trim();
        if (name.isEmpty()) return;

        StringBuilder sb = new StringBuilder();
        boolean found = false;

        // search through the entire supermarket
        // floorAreas
        for (FloorArea floorArea : supermarket.getFloorAreas()) {
            // aisles
            for (Aisle aisle : floorArea.getAisles()) {
                // shelves
                for (Shelf shelf : aisle.getShelves()) {
                    // products
                    for (Product product : shelf.getProducts()) {

                        if (product.getProductName().equalsIgnoreCase(name)) {
                            found = true;
                            sb.append("====================================\n");
                            sb.append("Product: ").append(product.getProductName()).append("\n");
                            sb.append("Floor Area: ").append(floorArea.getFloorAreaName()).append("\n");
                            sb.append("Aisle: ").append(aisle.getAisleName()).append("\n");
                            sb.append("Shelf: ").append(aisle.getAisleName()).append(" | Shelf ").append(shelf.getShelfNum()).append("\n");
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

        // if nothing matched, tell the user
        if (!found) {
            sb.append("No products found with name: ").append(name);
        }

        // display the results inside TextArea
        TextArea textArea = new TextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);

        // displays TextArea in Alert popup
        Alert resultWindow = new Alert(Alert.AlertType.INFORMATION);
        resultWindow.setTitle("Search Results");
        resultWindow.setHeaderText("Matching Products Found:");
        resultWindow.getDialogPane().setContent(textArea);
        resultWindow.setGraphic(null);
        styleDialog(resultWindow);

        resultWindow.showAndWait();
    }

    // draws floorArea to map based on floor level
    @FXML
    private void drawFloorAreasMap() {
        // clear anything that was previously drawn on the map
        floorMapPane.getChildren().clear();

        // get the currently selected floor level from the combo box
        String selectedFloor = floorSelectComboBox.getValue();

        // loops through floor areas and only displays those that are on correct floor
        CustomLinkedList<FloorArea> filteredFloorAreas = new CustomLinkedList<>();
        for (FloorArea floorArea : supermarket.getFloorAreas()) {
            if (floorArea.getFloorLevel().equals(selectedFloor)) {
                filteredFloorAreas.linkedListAdd(floorArea);
            }
        }

        // make size var based on size
        int numFloorAreas = filteredFloorAreas.size();

        // if none match, exit
        if (numFloorAreas == 0) return;

        // calculate available space in the pane
        double mapWidth = floorMapPane.getWidth();
        double mapHeight = floorMapPane.getHeight();

        // padding around each floorArea
        double padding = 30;

        // calculate width and height for each floorArea

        // (width - total padding) / num floorAreas
        double floorWidth = (mapWidth - (padding * (numFloorAreas + 1))) / numFloorAreas;
        // height - total padding
        double floorHeight = mapHeight - 2 * padding;

        // represents position in map
        int index = 0;

        // draw each floorArea as a rectangle with a label
        for (FloorArea floorArea : filteredFloorAreas) {

            // calculates x position based on index (stacks horizontally)
            double x = padding + (index * (floorWidth + padding));

            // create the rectangle representing the floor area
            Rectangle rect = new Rectangle(floorWidth, floorHeight);
            rect.setX(x);
            rect.setY(padding);
            rect.getStyleClass().add("floor-area");
            floorMapPane.getChildren().add(rect);

            // add a label for the floor area name
            Label label = new Label(floorArea.getFloorAreaName());
            label.setLayoutX(x + 5);
            label.setLayoutY(padding + 5);
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            floorMapPane.getChildren().add(label);

            // draw all aisles inside this floor area
            drawAisles(floorArea, rect);

            index++;
        }
    }

    // draws aisles inside selected floorArea
    @FXML
    private void drawAisles(FloorArea floorArea, Rectangle floorAreaRect) {
        // x and y coordinates for the floorArea rectangle
        double xStart = floorAreaRect.getX();
        double yStart = floorAreaRect.getY();

        // width and height of the floorArea rect
        double width = floorAreaRect.getWidth();
        double height = floorAreaRect.getHeight();

        // since filtering is already done, just use amount of aisles in selected floorArea
        int numAisles = floorArea.getAisles().size();

        // if 0, don't draw
        if (numAisles == 0) return;

        // padding around each aisle
        double padding = 30;

        // calculate width and height for each aisle

        // width - total padding
        double aisleWidth = width - 2 * padding;
        // (height - total padding) / num aisle
        double aisleHeight = (height - (padding * (numAisles + 1))) / numAisles;

        // represents position in floorArea
        int index = 0;

        // draw each aisle as a rectangle with a label
        for (Aisle aisle : floorArea.getAisles()) {

            // calculates x and y position based on index (stacks vertically)
            double aisleX = xStart + padding;
            double aisleY = yStart + padding + index * (aisleHeight + padding);

            // draw the aisle in the floorArea
            Rectangle rect = new Rectangle(aisleWidth, aisleHeight);
            rect.setX(aisleX);
            rect.setY(aisleY);
            rect.getStyleClass().add("aisle");
            floorMapPane.getChildren().add(rect);

            // draw label
            Label aisleLabel = new Label(aisle.getAisleName());
            aisleLabel.setLayoutX(aisleX + padding);
            aisleLabel.setLayoutY(aisleY + 5);
            aisleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12;");
            floorMapPane.getChildren().add(aisleLabel);

            // draw shelves inside this aisle
            drawShelves(aisle, rect);

            index++;
        }
    }

    // draws shelves inside selected aisle
    @FXML
    private void drawShelves(Aisle aisle, Rectangle aisleRect) {
        // x and y coordinates for the aisle rectangle
        double xStart = aisleRect.getX();
        double yStart = aisleRect.getY();

        // width and height of the aisle rect
        double width = aisleRect.getWidth();
        double height = aisleRect.getHeight();

        // gets total amount of shelves
        int numShelves = aisle.getShelves().size();

        // if 0, don't draw
        if (numShelves == 0) return;

        // padding around each shelf
        double padding = 30;

        // calculate width and height for each aisle

        // width - total padding
        double shelfWidth = width - 2 * padding;
        // (height - total padding) / num aisle
        double shelfHeight = (height - (padding * (numShelves + 1))) / numShelves;

        // represents position in floorArea
        int index = 0;

        // draw each aisle as a rectangle with a label
        for (Shelf shelf : aisle.getShelves()) {
            // calculates x and y position based on index (stacks vertically)
            double shelfX = xStart + padding;
            double shelfY = yStart + padding + index * (shelfHeight + padding);

            // draw the shelf
            Rectangle rect = new Rectangle(shelfWidth, shelfHeight);
            rect.setX(shelfX);
            rect.setY(shelfY);
            rect.getStyleClass().add("shelf");
            floorMapPane.getChildren().add(rect);

            // draw label
            Label shelfLabel = new Label(aisle.getAisleName() + " | Shelf " + shelf.getShelfNum());
            shelfLabel.setLayoutX(shelfX + 5);
            shelfLabel.setLayoutY(shelfY + 5);
            shelfLabel.setStyle("-fx-font-size: 10; -fx-font-weight: bold;");
            floorMapPane.getChildren().add(shelfLabel);

            index++;
        }
    }

    // when Save is clicked, save current linked list
    @FXML
    public void onSave() {
        try {
            // try save the supermarket using its name as the filename
            supermarket.save(supermarket.getName());
        } catch (Exception e) {
            // prints out issue name
            System.err.println("Error writing to file: " + e);
        }
    }

    // when Reset is clicked, resets the entire supermarket and ui lists
    @FXML
    public void onReset() {
        // clear all floor areas from the supermarket object
        supermarket.getFloorAreas().clear();

        // clear all list views so nothing is shown
        floorAreaListView.getItems().clear();
        aisleListView.getItems().clear();
        shelfListView.getItems().clear();
        productTable.getItems().clear();

        // reset any selected items
        selectedFloorArea = null;
        selectedAisle = null;
        selectedShelf = null;

        drawFloorAreasMap();
    }

    // loops through the supermarket and adds everything to a string
    private String printLinkedList() {
        // build a string representation of the supermarket and all its contents
        StringBuilder sb = new StringBuilder();

        sb.append("======== SUPERMARKET========\n");

        // print the main supermarket info and its total value
        sb.append("\n").append(supermarket.toString()).append("\n");
        sb.append(String.format("Total Value: €%.2f\n", supermarket.totalValue()));

        // loop through each floor area in the supermarket
        for (FloorArea floorArea : supermarket.getFloorAreas()) {

            sb.append("\n========================================\n");
            sb.append(floorArea).append("\n");
            sb.append(String.format("Total Value: €%.2f\n", floorArea.totalValue()));
            sb.append("========================================\n");

            // loop through each aisle in the floor area
            for (Aisle aisle : floorArea.getAisles()) {

                sb.append("\n--------------------------------------\n");
                sb.append(aisle).append("\n");
                sb.append(String.format("Total Value: €%.2f\n", aisle.totalValue()));
                sb.append("--------------------------------------\n");

                // loop through each shelf in the aisle
                for (Shelf shelf : aisle.getShelves()) {
                    sb.append(shelf).append("\n");
                    sb.append(String.format("Total Value: €%.2f\n", shelf.totalValue()));

                    // print all products on this shelf
                    sb.append("\n").append(shelf.getProducts().display()).append("\n");
                }
            }
        }

        sb.append("\n============================\n");

        return sb.toString();
    }

    // displays the string of everything in a textArea in an alert
    @FXML
    public void onViewAll() {
        // create a textArea and fill it with the full supermarket printout
        TextArea textArea = new TextArea(printLinkedList());
        textArea.setWrapText(false);

        // create an alert to display the textArea
        Alert resultWindow = new Alert(Alert.AlertType.INFORMATION);
        resultWindow.setTitle("All Stock");
        resultWindow.setHeaderText("Overview of All Stock");
        resultWindow.setGraphic(null);
        styleDialog(resultWindow);

        // make the dialog match scene size
        resultWindow.getDialogPane().setMinWidth(900);
        resultWindow.getDialogPane().setMinHeight(600);

        // set the textArea as the content of the alert
        resultWindow.getDialogPane().setContent(textArea);

        // show the alert and wait for the user to close it
        resultWindow.showAndWait();
    }

    // automatically adds a product to the best Shelf
    @FXML
    public void onSmartAdd() {
        // name
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Smart Add Product");
        nameDialog.setHeaderText("Enter Product Name");
        nameDialog.setContentText("Name:");
        nameDialog.setGraphic(null);
        styleDialog(nameDialog);

        String name = nameDialog.showAndWait().orElse("").trim();
        if (name.isEmpty()) return; // if I didn't have .orElse() i would need to check (name == null)

        // price
        TextInputDialog priceDialog = new TextInputDialog();
        priceDialog.setTitle("Smart Add Product");
        priceDialog.setHeaderText("Set Product Price");
        priceDialog.setContentText("Price (€):");
        priceDialog.setGraphic(null);
        styleDialog(priceDialog);

        String priceStr = priceDialog.showAndWait().orElse("").trim();
        if (priceStr.isEmpty()) return;

        // weight
        TextInputDialog weightDialog = new TextInputDialog();
        weightDialog.setTitle("Smart Add Product");
        weightDialog.setHeaderText("Set Product Weight");
        weightDialog.setContentText("Weight (g):");
        weightDialog.setGraphic(null);
        styleDialog(weightDialog);

        String weightStr = weightDialog.showAndWait().orElse("").trim();
        if (weightStr.isEmpty()) return;

        // quantity
        TextInputDialog quantityDialog = new TextInputDialog();
        quantityDialog.setTitle("Smart Add Product");
        quantityDialog.setHeaderText("Set Product Quantity");
        quantityDialog.setContentText("Quantity:");
        quantityDialog.setGraphic(null);
        styleDialog(quantityDialog);

        String quantityStr = quantityDialog.showAndWait().orElse("").trim();
        if (quantityStr.isEmpty()) return;

        // temp
        // makes array for each option, which is used in a choiceDialog
        String[] temps = {"Room", "Refrigerated", "Frozen"};
        ChoiceDialog<String> tempDialog = new ChoiceDialog<>(temps[0], temps);
        tempDialog.setTitle("Smart Add Product");
        tempDialog.setHeaderText("Set Product Temperature");
        tempDialog.setContentText("Temp:");
        tempDialog.setGraphic(null);
        styleDialog(tempDialog);

        String temp;
        temp = tempDialog.showAndWait().orElse("").trim();
        if (temp.isEmpty()) return;

        // photo url
        TextInputDialog photoDialog = new TextInputDialog();
        photoDialog.setTitle("Smart Add Product");
        photoDialog.setHeaderText("Set Product Photo URL");
        photoDialog.setContentText("URL:");
        photoDialog.setGraphic(null);
        styleDialog(photoDialog);

        String photoUrl = photoDialog.showAndWait().orElse("").trim();
        if (photoUrl.isEmpty()) return;

        // parse price, weight, quantity to numbers
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

        Product newProduct = new Product(name, price, weight, quantity, temp, photoUrl);

        smartAdd(newProduct);
        productTable.refresh();
    }

    // logic for onSmartAdd()
    public void smartAdd(Product smartProd) {

        // 1. check if any identical product exists, if so add quantity
        Product match = findIdenticalProduct(smartProd);
        if (match != null) {
            match.updateQuantity(smartProd.getQuantity());
            return;
        }

        // 2. find aisle/shelf with most similar name to smartAdd product
        Aisle aisle = findBestAisle(smartProd);
        Shelf shelf = findBestShelf(aisle, smartProd);

        shelf.addProduct(smartProd); // add object
        productTable.getItems().add(smartProd); // add to ui
    }

    // helper methods for smartAdd

    // finds an identical product when adding
    private Product findIdenticalProduct(Product target) {
        for (FloorArea floorArea : supermarket.getFloorAreas()) {
            for (Aisle aisle : floorArea.getAisles()) {
                for (Shelf shelf : aisle.getShelves()) {
                    for (Product product : shelf.getProducts()) {
                        if (
                                product.getProductName().equalsIgnoreCase(target.getProductName())
                                        && product.getWeight() == target.getWeight()
                                        && product.getPrice() == target.getPrice()
                                        && product.getTemperature().equalsIgnoreCase(target.getTemperature())
                                        && product.getPhotoURL().equalsIgnoreCase(target.getPhotoURL())
                        ) {
                            return product;
                        }
                    }
                }
            }
        }
        return null;
    }

    // tokenises
    private String[] tokenise(String name) {
        if (name == null) return new String[0];

        // replace everything not a-z, 0-9, or space
        // i.e. "Hello ____ WORLD!!!" => "hello world"
        name = name.toLowerCase().replaceAll("[^a-z0-9 ]", "");

        // if one or more whitespace, make new work.
        // i.e. "hello world" => ["hello", "world"]
        return name.split("\\s+");
    }

    // compares the name of an aisle/product to the smart product to determine how similar the names are
    public float similarityScore(String a, String b) {
        String[] tokensA = tokenise(a);
        String[] tokensB = tokenise(b);

        if (tokensA.length == 0 || tokensB.length == 0) return 0;

        // increments var everytime the same word appears
        int matches = 0;
        for (String tokenA : tokensA) {
            for (String tokenB : tokensB) {
                if (tokenA.equals(tokenB)) {
                    matches++;
                    break;
                }
            }
        }

        // chooses the longer name
        // int maxLength = (tokensA.length > tokensB.length) ? tokensA.length : tokensB.length;
        int maxLength = Math.max(tokensA.length, tokensB.length);
        return (float) matches / maxLength;
    }

    // see how similar one products name is to another in an aisle
    private double productSimilarityInAisle(Aisle aisle, Product newProd) {
        double best = 0;

        // loops through aisle and compares existing product name vs new product
        for (Shelf shelf : aisle.getShelves()) {
            for (Product oldProd : shelf.getProducts()) {
                double score = similarityScore(oldProd.getProductName(), newProd.getProductName());
                if (score > best) best = score;
            }
        }
        return best;
    }

    // find the best aisle for smart add product based on name/temp
    private Aisle findBestAisle(Product newProd) {
        Aisle bestAisle = null;
        double highestScore;
        double threshold = 0.5;

        // threshold lowers every iteration if similarity is not high enough
        while (threshold > 0) {

            highestScore = 0;
            // loops through all floorAreas
            for (FloorArea floorArea : supermarket.getFloorAreas()) {
                // loops through all aisles in floorArea
                for (Aisle aisle : floorArea.getAisles()) {
                    // if temp is not the same, skip
                    if (!aisle.getAisleTemperature().equals(newProd.getTemperature())) continue;

                    double score = similarityScore(aisle.getAisleName(), newProd.getProductName());

                    if (score >= threshold && score > highestScore) {
                        highestScore = score;
                        bestAisle = aisle;
                    }
                }
            }

            if (bestAisle != null) return bestAisle;

            threshold -= 0.05; // lowers threshold
        }

        // 2. IF NONE SIMILAR, CHECK PRODUCT VS PRODUCT NAME

        highestScore = 0;
        threshold = 0.5;

        while (threshold > 0) {
            for (FloorArea floorArea : supermarket.getFloorAreas()) {
                for (Aisle aisle : floorArea.getAisles()) {

                    // if aisle is not the same temp as product, move to next aisle
                    if (!aisle.getAisleTemperature().equals(newProd.getTemperature())) continue;

                    double score = productSimilarityInAisle(aisle, newProd);
                    if (score >= threshold && score > highestScore) {
                        highestScore = score;
                        bestAisle = aisle;
                    }
                }
            }
            threshold -= 0.05;
        }
        return bestAisle;
    }

    // find the best shelf for smart add product
    private Shelf findBestShelf(Aisle bestAisle, Product newProd) {
        Shelf bestShelf = null;
        double highestScore = 0;
        double threshold = 0.5;

        while (threshold > 0) {
            // Search every shelf and every product on it
            for (Shelf shelf : bestAisle.getShelves()) {
                for (Product oldProd : shelf.getProducts()) {
                    double score = similarityScore(oldProd.getProductName(), newProd.getProductName());

                    if (score >= threshold && score > highestScore) {
                        highestScore = score;
                        bestShelf = shelf;
                    }
                }
            }
            if (bestShelf != null) return bestShelf;

            threshold -= 0.05;
        }

        // choose empty shelf
        for (Shelf shelf : bestAisle.getShelves()) {
            if (shelf.getProducts().size() == 0) {
                return shelf;
            }
        }

        // if no shelf on aisle, make new one
        Shelf newShelf = new Shelf(bestAisle.getShelves().size() + 1);
        bestAisle.addShelf(newShelf);

        return newShelf;
    }
}
