module com.daytour.ui.daytour {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.daytour.ui to javafx.fxml;

    exports com.daytour.ui;
    exports com.daytour.processing;
}