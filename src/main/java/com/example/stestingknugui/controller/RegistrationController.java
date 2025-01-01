package com.example.stestingknugui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Alert;
public class RegistrationController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField middleNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleRegisterButtonClick() {
        try {
            if (nameField.getText().isEmpty() || lastNameField.getText().isEmpty() || middleNameField.getText().isEmpty() ||
                    usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Ошибка", "Все поля должны быть заполнены!");
                return;
            }

            if (isUsernameTaken(usernameField.getText())) {
                showAlert(Alert.AlertType.ERROR, "Ошибка", "Логин уже существует!");
                return;
            }

            String apiUrl = "http://localhost:8087/api/createuser";


            String name = nameField.getText();
            String lastName = lastNameField.getText();
            String middleName = middleNameField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            String role = "ADMIN";

            String requestBody = String.format(
                    "{\"name\":\"%s\",\"lastName\":\"%s\",\"middleName\":\"%s\",\"username\":\"%s\",\"password\":\"%s\",\"role\":\"%s\"}",
                    name, lastName, middleName, username, password, role
            );

            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(requestBody.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {

                openLoginWindow();

                Stage stage = (Stage) nameField.getScene().getWindow();
                stage.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Ошибка регистрации", "Ошибка регистрации: " + responseCode);
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Произошла ошибка при регистрации: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isUsernameTaken(String username) throws Exception {

        String apiUrl = "http://localhost:8087/api/user";
        HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");


        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String jsonResponse = response.toString();
        List<String> usernames = new ArrayList<>();
        String[] users = jsonResponse.substring(1, jsonResponse.length() - 1).split("},");
        for (String user : users) {
            if (user.contains("\"username\":\"" + username + "\"")) {
                return true;
            }
        }

        return false;
    }

    private void openLoginWindow() throws Exception {
        Stage loginStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/stestingknugui/login.fxml"));
        Scene scene = new Scene(loader.load());
        loginStage.setTitle("Вход");
        loginStage.setScene(scene);
        loginStage.show();
    }

    // Метод для отображения предупреждения
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

