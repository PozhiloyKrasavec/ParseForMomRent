module com.example.mycourse {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.jsoup;
    requires org.json;
    requires commons.csv;

    opens com.example.mycourse to javafx.fxml;
    exports com.example.mycourse;
    exports com.example.mycourse.Modal;
    opens com.example.mycourse.Modal to javafx.fxml;
    exports com.example.mycourse.Modal.threads.ForBaby;
    opens com.example.mycourse.Modal.threads.ForBaby to javafx.fxml;
}