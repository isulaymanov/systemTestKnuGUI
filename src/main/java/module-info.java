module com.example.stestingknugui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.json;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires org.apache.poi.ooxml;

    opens com.example.stestingknugui to javafx.fxml;
    exports com.example.stestingknugui;
    exports com.example.stestingknugui.service;
    opens com.example.stestingknugui.service to javafx.fxml;
    exports com.example.stestingknugui.model;
    opens com.example.stestingknugui.model to javafx.fxml;
    exports com.example.stestingknugui.util;
    opens com.example.stestingknugui.util to javafx.fxml;
    exports com.example.stestingknugui.controller;
    opens com.example.stestingknugui.controller to javafx.fxml;


}