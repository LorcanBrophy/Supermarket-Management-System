module com.example.dsa_ca1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.dsa_ca1 to javafx.fxml;

    exports com.example.dsa_ca1.main;

    exports com.example.dsa_ca1.controllers;
    opens com.example.dsa_ca1.controllers to javafx.fxml;

    exports com.example.dsa_ca1.models;
}