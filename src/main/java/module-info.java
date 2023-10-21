module els.wyrwatracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens els.wyrwatracker to javafx.fxml;
    exports els.wyrwatracker;
}