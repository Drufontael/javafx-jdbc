module workshop.javafx.jdbc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens application;
    opens gui;
    opens model.entities;
}