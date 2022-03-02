module AccountingApp {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;

    opens common;
    opens com.data;
}