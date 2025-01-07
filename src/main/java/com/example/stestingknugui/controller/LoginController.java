package com.example.stestingknugui.controller;

import com.example.stestingknugui.service.LoginService;
import com.example.stestingknugui.util.WindowCenterer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    public void initialize() {
        loginButton.setOnAction(event -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Все поля должны быть заполнены");
            alert.showAndWait();
            return;
        }

        LoginService loginRequest = new LoginService(username, password);
        loginRequest.setOnSucceeded(event -> {
            String jwtToken = loginRequest.getValue();
            openJwtWindow(jwtToken);
        });

        loginRequest.setOnFailed(event -> {

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не удалось выполнить вход");
            alert.setContentText("Проверьте логин и пароль");
            alert.showAndWait();
        });

        new Thread(loginRequest).start();
    }

    private void openJwtWindow(String jwtToken) {

        //System.out.println(getClass().getResource("/com/example/stestingknugui/dashboard.fxml"));

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/stestingknugui/dashboard.fxml"));
            Parent root = loader.load();
            PanelController controller = loader.getController();
            controller.setJwtToken(jwtToken);
            Stage stage = new Stage();
            stage.setTitle("Панель");
            stage.setScene(new Scene(root));
            // Центрируем окно
            WindowCenterer.centerOnScreen(stage);
            stage.show();

            ((Stage) loginButton.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не удалось открыть панель");
            alert.showAndWait();
        }
    }

}
