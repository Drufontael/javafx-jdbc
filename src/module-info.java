module workshop.javafx.jdbc {
    requires javafx.controls;
    requires javafx.fxml;

    opens application;
    opens gui;
    opens model.entities;
}