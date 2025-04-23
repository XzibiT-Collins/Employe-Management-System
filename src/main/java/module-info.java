module com.example.employeemanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens com.example.employeemanagementsystem to javafx.fxml;
    opens com.example.employeemanagementsystem.model to javafx.base;
    exports com.example.employeemanagementsystem.ui;
    opens com.example.employeemanagementsystem.ui to javafx.fxml;
    exports com.example.employeemanagementsystem;
}