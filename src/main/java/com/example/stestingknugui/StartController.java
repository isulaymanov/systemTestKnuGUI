package com.example.stestingknugui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartController {
    @FXML
    private void handleStudentButtonClick() throws Exception {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Авторизация студента");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleTeacherButtonClick() throws Exception {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GroupListView.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Список групп");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleRegisterButtonClick() throws Exception {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("registration.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Регистрация");
        stage.setScene(scene);
        stage.show();
    }


}
