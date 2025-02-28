module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    opens com.example to javafx.base, javafx.fxml, javafx.controls;
    exports com.example;
}
