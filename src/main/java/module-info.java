module managesystem.user {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.desktop;

    opens managesystem.user to javafx.fxml;
    exports managesystem.user;
}