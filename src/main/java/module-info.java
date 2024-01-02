module els.wyrwatracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires gen_alg;


    opens els.wyrwatracker to javafx.fxml;
    exports els.wyrwatracker;
    exports els.sqliteIO;
    opens els.sqliteIO to javafx.fxml;
}